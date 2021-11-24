package net.iessochoa.joseantoniolopez.ejemplobdroom.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels.BBCondicionBusquedaViewModel;
import net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels.CCVariasCondicionesViewModel;

import java.util.Calendar;
import java.util.List;
/**    HashMap
 * En este ejemplo haremos algo parecido que en el B pero con dos condiciones diferentes
Para conseguirlo, utilizaremos un livedata de tipo HashMap con dos valores, uno para
cada condición. Si cambia uno de ellos, actualizará la sentencia SQL mediante la clase
Transformation
 */
public class CCVariasCondicionesViewModelActivity extends AppCompatActivity {
    public static final int NUEVO_CONTACTO_REQUEST_CODE = 1;

    private CCVariasCondicionesViewModel contactoViewModel;
    private Button btnNuevo;
    private EditText etBuscar;
    private TextView tvFechaNacimiento;
    Calendar calendar = Calendar.getInstance();
    //final String[] ordenadoPor =
    //        new String[]{Contacto.NOMBRE,Contacto.APELLIDO,Contacto.FECHA_NACIMIENTO};
//    final String[] ordenadoPor =
//            new String[]{"1","2","3"};
 //   Spinner spnOrdenadoPor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccvarias_condiciones_view_model);

        etBuscar=findViewById(R.id.etBuscar);
        tvFechaNacimiento=findViewById(R.id.tvFechaNacimiento);


        //************RECYCLERVIEW******************
        RecyclerView recyclerView = findViewById(R.id.rvListaContactos);
        //creamos el adaptador
        final ContactoListAdapter adapter = new ContactoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //*********VIEWMODEL******************
        contactoViewModel= new ViewModelProvider(this).get(CCVariasCondicionesViewModel.class);


        //************NUEVO Y BORRADO DE CONTACTO
        // Creamos un nuevo contacto mediante otra actividad. Al insertar el nuevo elemento,el observer anterior
        // nos mostrará el resultado automáticamente
        //
        btnNuevo=findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CCVariasCondicionesViewModelActivity.this,NuevoContactoActivity.class);
                startActivityForResult(intent,NUEVO_CONTACTO_REQUEST_CODE);
            }
        });
        //asignamos la acción de borrado de elemento al recycler view. Fijaros como hemos creado
        //un nuevo objeto que implementa nuestra interface. Al borrar el elemento se muestra automaticamente
        //gracias al observer anterior
        //
        adapter.setOnClickListener(new ContactoListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Contacto contacto) {
                contactoViewModel.delete(contacto);
            }
        });
        /***********OBSERVERS*****************************
         el livedata cuando cambian las condiciones de busqueda actualiza la query
         . En nuestro caso vamos
        a probar como al modificar el texto de busqueda por nombre, cambiamos el LiveData des HashMap que mediante swichMap modifica el Livedata de la lista
        Para ello, fijaros en la clase CondicioBusquedaViewModelpe
         */
        //Asignamos el observador a la busqueda hecha. Si hay cambios actualizamos el adaptador
        contactoViewModel.getListContactosLiveData().observe(this, new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
                adapter.setContactos(contactos);
            }
        });
        //Cuando cambie el campo de búsqueda,Transformation.swichtMap cambiara la condicion de busqueda del livedata
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //actualizamos el Livedata que a su vez cambiara las condiciones de busqueda
                contactoViewModel.setNombre(editable.toString());
            }
        });

    }
/*NUEVO CONTACTO*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==NUEVO_CONTACTO_REQUEST_CODE && resultCode==RESULT_OK){
            Contacto contacto=(Contacto) data.getParcelableExtra(NuevoContactoActivity.EXTRA_CONTACTO);
            //La insercción causa una modificación en el LiveData y el observador modificará el RecyclerView
            contactoViewModel.insert(contacto);
        }

    }
    /**
     * nos permite abrir un calendario para seleccionar la fecha y modificar la condición de
     * busqueda
     */
    public void onClickFecha(View view) {
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog dialogo = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                tvFechaNacimiento.setText(dayOfMonth+"/"+ monthOfYear+"/"+year);
                //al igual que al cambiar el nombre, modificamos el LiveData de condición de busqueda
                //que provocará la modificación del LiveData de la Query Sql
                contactoViewModel.setFechaMenorQue(calendar.getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dialogo.show();
    }
}
