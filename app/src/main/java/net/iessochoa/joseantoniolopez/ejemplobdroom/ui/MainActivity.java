package net.iessochoa.joseantoniolopez.ejemplobdroom.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;

/**
 * Este ejemplo nos permite ver como podemos implementar el acceso a la base de datos sqlite
 * mediante el patrón de diseño MVVM con el conjunto de librerías de Jetpack
 * Tenemos una única base de datos de una agenda simple, vamos a ver diferentes opciones de acceso a los
 * datos mediante Room y ViewModel
 *
 * AABasico: Acceso básico a la base de datos
 * BBCondicionBusqueda: Patrón para  realizar una búsqueda de datos por parte del usuario en el que la condición puede ir cambiando
 * CCVariasCondiciones: Patrón para manejar dos condiciones de búsqueda en la sentencia SQL
 * DDOdenar: Patrón para manejar el cambien en  OrderBy en la sentencia SQL.
 *          Patrón para manejar llamadas a la base de datos con rxJava
 * EESaveState: Patrón para guardar el estado del ViewModel ante destrucción total
 */
public class MainActivity extends AppCompatActivity {

    private Button btnBasico;
    private Button btnCondicionBusqueda;
    private Button btnVariasCondicionesBusqueda;
    private Button btnOrdenadoPor;
    private Button btnSavedState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBasico=findViewById(R.id.btnBasico);
        btnCondicionBusqueda=findViewById(R.id.btnBusqueda);
        btnVariasCondicionesBusqueda=findViewById(R.id.btnVariasCondiciones);
        btnOrdenadoPor=findViewById(R.id.btnOdenadoPor);
        btnSavedState=findViewById(R.id.btnSaveState);

        View.OnClickListener onClickListener=new View.OnClickListener() {
            private View view;

            @Override
            public void onClick(View view) {
                this.view = view;
                Intent intent=null ;

                switch (view.getId()){
                    case R.id.btnBasico:
                        intent  =new Intent(MainActivity.this, AABasicoViewModelActivity.class);
                        break;
                    case R.id.btnBusqueda:
                        intent  =new Intent(MainActivity.this, BBCondicionBusquedaViewModelActivity.class);
                        break;
                    case R.id.btnVariasCondiciones:
                        intent  =new Intent(MainActivity.this, CCVariasCondicionesViewModelActivity.class);
                        break;
                    case  R.id.btnOdenadoPor:
                        intent  =new Intent(MainActivity.this, DDOrdenarPorActivity.class);
                        break;
                    case  R.id.btnSaveState:
                        intent  =new Intent(MainActivity.this, EESaveStateHandleActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };
         btnBasico.setOnClickListener(onClickListener);
         btnCondicionBusqueda.setOnClickListener(onClickListener);
         btnVariasCondicionesBusqueda.setOnClickListener(onClickListener);
         btnOrdenadoPor.setOnClickListener(onClickListener);
        btnSavedState.setOnClickListener(onClickListener);


    }
}
