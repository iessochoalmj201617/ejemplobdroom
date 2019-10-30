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
import android.widget.Button;
import android.widget.EditText;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.ContactoViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int NUEVO_CONTACTO_REQUEST_CODE = 1;

    private ContactoViewModel contactoViewModel;
    private Button btnNuevo;
    private EditText etBuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etBuscar=findViewById(R.id.etBuscar);
        RecyclerView recyclerView = findViewById(R.id.reciclerView);
        //creamos el adaptador
        final ContactoListAdapter adapter = new ContactoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        contactoViewModel= ViewModelProviders.of(this).get(ContactoViewModel.class);
        //Este livedata nos permite ver todos los contactos y en caso de que haya un cambio en la
        //base de datos, se mostrará automáticamente
        contactoViewModel.getAllContactos().observe(this, new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
            //    adapter.setContactos(contactos);
            }
        });
        //Creamos un nuevo contacto mediante otra actividad. Al insestar el nuevo elemento,el observer anterior
        // nos mostrará el resultado automáticamente
        //
        btnNuevo=findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NuevoContactoActivity.class);
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
        Prueba de como actualizar el livedata cuando cambian las condiciones. En nuestro caso vamos
        a probar como al modificar el texto de busqueda por nombre, se modifica el Livedata de la lista
        Para ello, fijaros en la clase ContactoViewModel, la clase Transformation
         */
        //Asignamos el observador al string condicionBusqueda
        contactoViewModel.getByNombre().observe(this, new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
                adapter.setContactos(contactos);
            }
        });
        //Cuando cambie el campo de búsqueda modificamos el LiveData condiciondBusqueda que
        //cambiará el liveData listaContactos restringida por la condición de busqueda
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                /*contactoViewModel.getByNombre(charSequence.toString());
                contactoViewModel.getAllContactos().observe(MainActivity.this, new Observer<List<Contacto>>() {
                    @Override
                    public void onChanged(List<Contacto> contactos) {
                        adapter.setContactos(contactos);
                    }
                });*/
            }

            @Override
            public void afterTextChanged(Editable editable) {
                contactoViewModel.setCondicionBusqueda(editable.toString());
            }
        });

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
