package net.iessochoa.joseantoniolopez.ejemplobdroom.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels.VariasCondicionesViewModel;

import java.util.List;

public class VariasCondicionesViewModelActivity extends AppCompatActivity {
    public static final int NUEVO_CONTACTO_REQUEST_CODE = 1;

    private VariasCondicionesViewModel contactoViewModel;
    private Button btnNuevo;
    private EditText etBuscar;
    final String[] ordenadoPor =
            new String[]{Contacto.NOMBRE,Contacto.APELLIDO,Contacto.FECHA_NACIMIENTO};
//    final String[] ordenadoPor =
//            new String[]{"1","2","3"};
 //   Spinner spnOrdenadoPor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varias_condiciones_view_model);

        etBuscar=findViewById(R.id.etBuscar);

        //************SPINNER******************
       /* spnOrdenadoPor=findViewById(R.id.spnOrdenadoPor);
        final ArrayAdapter<String> adaptador =new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ordenadoPor);
        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spnOrdenadoPor.setAdapter(adaptador);*/
        //************RECYCLERVIEW******************
        RecyclerView recyclerView = findViewById(R.id.rvListaContactos);
        //creamos el adaptador
        final ContactoListAdapter adapter = new ContactoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //*********VIEWMODEL******************
        contactoViewModel= ViewModelProviders.of(this).get(VariasCondicionesViewModel.class);


        //************NUEVO Y BORRADO DE CONTACTO
        // Creamos un nuevo contacto mediante otra actividad. Al insertar el nuevo elemento,el observer anterior
        // nos mostrará el resultado automáticamente
        //
        btnNuevo=findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VariasCondicionesViewModelActivity.this,NuevoContactoActivity.class);
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
        a probar como al modificar el texto de busqueda por nombre, se modifica el Livedata de la lista
        Para ello, fijaros en la clase CondicioBusquedaViewModel, la clase Transformation
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
       /* spnOrdenadoPor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                contactoViewModel.setOrdenarPor((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                contactoViewModel.setOrdenarPor(ordenadoPor[0]);
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==NUEVO_CONTACTO_REQUEST_CODE && resultCode==RESULT_OK){
            Contacto contacto=(Contacto) data.getSerializableExtra(NuevoContactoActivity.EXTRA_CONTACTO);
            contactoViewModel.insert(contacto);
        }

    }
}
