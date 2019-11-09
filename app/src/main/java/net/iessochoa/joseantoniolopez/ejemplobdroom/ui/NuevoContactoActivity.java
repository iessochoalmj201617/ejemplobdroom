package net.iessochoa.joseantoniolopez.ejemplobdroom.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;

import java.util.Calendar;
import java.util.Date;
/*
Crea un nuevo contacto y lo devuelve a la actividad
 */
public class NuevoContactoActivity extends AppCompatActivity {
    public static final String EXTRA_CONTACTO="net.iessochoa.joseantoniolopez.ejemplobdroom.ui.contacto";
    private EditText etNombre;
    private EditText etApellido;
    private EditText etTelefono;
    private Button btnAceptar;
    private Button btnCancelar;
    private TextView tvFechaNacimiento;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_contacto);
        etNombre=findViewById(R.id.etNombre);
        etApellido=findViewById(R.id.etApellido);
        etTelefono=findViewById(R.id.etTelefono);
        btnAceptar=findViewById(R.id.btnAceptar);
        btnCancelar=findViewById(R.id.btnCancelar);
        tvFechaNacimiento=findViewById(R.id.tvFechaNacimiento);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contacto contacto=new Contacto(etNombre.getText().toString(),etApellido.getText().toString(),etTelefono.getText().toString(),calendar.getTime());
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

    /**
     * nos permite abrir un calendario para seleccionar la fecha
     */
    public void onClickFecha(View view) {
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog dialogo = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(year, monthOfYear, dayOfMonth);
                tvFechaNacimiento.setText(dayOfMonth+"/"+ monthOfYear+"/"+year);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dialogo.show();
    }
}
