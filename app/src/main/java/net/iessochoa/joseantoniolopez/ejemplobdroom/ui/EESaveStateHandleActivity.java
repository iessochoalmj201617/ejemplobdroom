package net.iessochoa.joseantoniolopez.ejemplobdroom.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels.BBCondicionBusquedaViewModel;
import net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels.EESaveStateViewModel;

import java.util.List;

public class EESaveStateHandleActivity extends AppCompatActivity {
    public static final int NUEVO_CONTACTO_REQUEST_CODE = 1;

    private EESaveStateViewModel contactoViewModel;
    private Button btnNuevo;
    private EditText etBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcondicion_busqueda_view_model);


        etBuscar=findViewById(R.id.etBuscar);
        RecyclerView recyclerView = findViewById(R.id.rvListaContactos);
        //creamos el adaptador
        final ContactoListAdapter adapter = new ContactoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Recuperamos el ViewModel
        contactoViewModel= new ViewModelProvider(this).get(EESaveStateViewModel.class);


        //Creamos un nuevo contacto mediante otra actividad. Al insestar el nuevo elemento,el observer anterior
        // nos mostrará el resultado automáticamente
        //
        btnNuevo=findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EESaveStateHandleActivity.this,NuevoContactoActivity.class);
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
        /*
         el livedata cuando cambian las condiciones de busqueda actualiza la query
         . En nuestro caso vamos
        a probar como al modificar el texto de busqueda por nombre, se modifica el Livedata de la lista
        Para ello, fijaros en la clase CondicioBusquedaViewModel, la clase Transformation
         */
        //Asignamos el observador a la busqueda hecha. Si hay cambios actualizamos el adaptador
        contactoViewModel.getByNombre().observe(this, new Observer<List<Contacto>>() {
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
                contactoViewModel.setCondicionBusqueda(editable.toString());
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
}