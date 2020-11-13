package net.iessochoa.joseantoniolopez.ejemplobdroom.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels.AABasicoViewModel;
import net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels.CCVariasCondicionesViewModel;
import net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels.DDOrdenarPorViewModel;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DDOrdenarPorActivity extends AppCompatActivity {
    public static final int NUEVO_CONTACTO_REQUEST_CODE = 1;

    private DDOrdenarPorViewModel contactoViewModel;
    private Button btnNuevo;
    private RadioGroup rgOrden;
    private RadioGroup rgAscDesc;
    private Button btnTotalContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddordenar_por);

        rgOrden=findViewById(R.id.rgOrden);
        rgAscDesc=findViewById(R.id.rgAscDes);
        btnTotalContactos=findViewById(R.id.btnTotalContactos);

        //RECYCLER_VIEW
        RecyclerView rvListaContactos = findViewById(R.id.rvListaContactos);
        //creamos el adaptador
        final ContactoListAdapter adapter = new ContactoListAdapter(this);
        rvListaContactos.setAdapter(adapter);
        rvListaContactos.setLayoutManager(new LinearLayoutManager(this));
        //VIEW_MODEL
        //Recuperamos el ViewModel
        contactoViewModel= new ViewModelProvider(this).get(DDOrdenarPorViewModel.class);
        //Este livedata nos permite ver todos los contactos y en caso de que haya un cambio en la
        //base de datos, se mostrará automáticamente
        contactoViewModel.getAllContactos().observe(this, new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
                adapter.setContactos(contactos);
            }
        });

        //CAMBIO DE ORDEN DE LA LISTA por fecha y nombre
        rgOrden.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbtPorFecha:
                        contactoViewModel.setOrderBy(DDOrdenarPorViewModel.OrderBy.FECHA);
                        break;
                    case R.id.rbtPorNombre:
                        contactoViewModel.setOrderBy(DDOrdenarPorViewModel.OrderBy.NOMBRE);
                }

            }
        });
        //cambia el orden ASC y DESC
        rgAscDesc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbAsc:
                        contactoViewModel.setOrder(DDOrdenarPorViewModel.Order.ASC);
                        break;
                    case R.id.rbDesc:
                        contactoViewModel.setOrder(DDOrdenarPorViewModel.Order.DESC);
                }

            }
        });
        rgOrden.check(R.id.rbtPorNombre);

        btnTotalContactos.setOnClickListener(view -> mostrarTotalContactos());
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

    /**
     * Mediante rxJava vamos a lanzar en un hilo la busqueda y declaramos un observador para que cuando termine podamos
     * mostrar el resultado
     */
    private void mostrarTotalContactos() {
        contactoViewModel.geTotalContactos()//obtenemos objeto reactivo de un solo uso 'Single' para que haga la consulta en un hilo
                .subscribeOn(Schedulers.io())//el observable(la consulta sql) se ejecuta en uno diferente
                .observeOn(AndroidSchedulers.mainThread())//indicamos que el observador es el hilo principal  de Android
                .subscribe(new SingleObserver<Integer>() { //creamos el observador
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override//cuando termine  la consulta de la base de datos recibimos el valor
                    public void onSuccess(@NonNull Integer contactosTotales) {
                       Toast.makeText(DDOrdenarPorActivity.this,"Tenemos "+contactosTotales.toString()+" contactos",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

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
            Contacto contacto=(Contacto) data.getParcelableExtra(NuevoContactoActivity.EXTRA_CONTACTO);
            //La insercción causa una modificación en el LiveData y el observador modificará el RecyclerView
            contactoViewModel.insert(contacto);
        }

    }

}

