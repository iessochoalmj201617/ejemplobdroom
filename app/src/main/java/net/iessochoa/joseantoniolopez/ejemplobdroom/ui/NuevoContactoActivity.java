package net.iessochoa.joseantoniolopez.ejemplobdroom.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;

public class NuevoContactoActivity extends AppCompatActivity {
    public static final String EXTRA_CONTACTO="net.iessochoa.joseantoniolopez.ejemplobdroom.ui.contacto";
    private EditText etNombre;
    private EditText etApellido;
    private EditText etTelefono;
    private Button btnAceptar;
    private Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_contacto);
        etNombre=findViewById(R.id.etNombre);
        etApellido=findViewById(R.id.etApellido);
        etTelefono=findViewById(R.id.etTelefono);
        btnAceptar=findViewById(R.id.btnAceptar);
        btnCancelar=findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contacto contacto=new Contacto(etNombre.getText().toString(),etApellido.getText().toString(),etTelefono.getText().toString());
                Intent intent=new Intent();
                intent.putExtra(EXTRA_CONTACTO,contacto);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}
