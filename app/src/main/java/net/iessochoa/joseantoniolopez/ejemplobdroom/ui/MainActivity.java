package net.iessochoa.joseantoniolopez.ejemplobdroom.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;

public class MainActivity extends AppCompatActivity {

    private Button btnBasico;
    private Button btnCondicionBusqueda;
    private Button btnVariasCondicionesBusqueda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBasico=findViewById(R.id.btnBasico);
        btnCondicionBusqueda=findViewById(R.id.btnBusqueda);
        btnVariasCondicionesBusqueda=findViewById(R.id.btnVariasCondiciones);

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
                }
                startActivity(intent);
            }
        };
         btnBasico.setOnClickListener(onClickListener);
         btnCondicionBusqueda.setOnClickListener(onClickListener);
         btnVariasCondicionesBusqueda.setOnClickListener(onClickListener);

    }
}
