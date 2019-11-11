package net.iessochoa.joseantoniolopez.ejemplobdroom.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels.AABasicoViewModel;
import net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels.DDOrdenarPorViewModel;

import java.util.List;

public class DDOrdenarPorActivity extends AppCompatActivity {
    public static final int NUEVO_CONTACTO_REQUEST_CODE = 1;

    private DDOrdenarPorViewModel contactoViewModel;
    private Button btnNuevo;
    private RadioGroup rgOrden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddordenar_por);

        rgOrden=findViewById(R.id.rgOrden);

        //RECYCLER_VIEW
        RecyclerView rvListaContactos = findViewById(R.id.rvListaContactos);
        //creamos el adaptador
        final ContactoListAdapter adapter = new ContactoListAdapter(this);
        rvListaContactos.setAdapter(adapter);
        rvListaContactos.setLayoutManager(new LinearLayoutManager(this));
        //VIEW_MODEL
        //Recuperamos el ViewModel
        contactoViewModel= ViewModelProviders.of(this).get(DDOrdenarPorViewModel.class);
        //Este livedata nos permite ver todos los contactos y en caso de que haya un cambio en la
        //base de datos, se mostrará automáticamente
        contactoViewModel.getAllContactos().observe(this, new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
                adapter.setContactos(contactos);
            }
        });

        //CAMBIO DE ORDEN DE LA LISTA
        rgOrden.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbtPorFecha:
                        contactoViewModel.setOrdenadoPor("fecha");
                        break;
                    case R.id.rbtPorNombre:
                        contactoViewModel.setOrdenadoPor("nombre");
                }

            }
        });

        rgOrden.check(R.id.rbtPorNombre);

        //NUEVO_CONTACTO
        //Creamos un nuevo contacto mediante otra actividad. Al insestar el nuevo elemento,el observer anterior
        // nos mostrará el resultado automáticamente
        //
        btnNuevo=findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DDOrdenarPorActivity.this,NuevoContactoActivity.class);
                startActivityForResult(intent,NUEVO_CONTACTO_REQUEST_CODE);
            }
        });
        //ACCION DE BORRADO
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

    }
    /*
    NUEVO CONTACTO
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==NUEVO_CONTACTO_REQUEST_CODE && resultCode==RESULT_OK){
            Contacto contacto=(Contacto) data.getSerializableExtra(NuevoContactoActivity.EXTRA_CONTACTO);
            //La insercción causa una modificación en el LiveData y el observador modificará el RecyclerView
            contactoViewModel.insert(contacto);
        }

    }

}

