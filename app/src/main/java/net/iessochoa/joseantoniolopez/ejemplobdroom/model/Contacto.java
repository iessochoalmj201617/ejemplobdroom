package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


//para ver los usos de las anotaciones de Entity
// https://developer.android.com/training/data-storage/room/defining-data.html#java
//https://codelabs.developers.google.com/codelabs/android-room-with-a-view
@Entity(tableName = "contacto")
public class Contacto implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "nombre")
    @NonNull
    private  String mNombre;
    @ColumnInfo(name="apellido")
    private String mApellido;
    @ColumnInfo(name = "numerotelefono")
    @NonNull
    private String mNumTelefono;

    @NonNull
    public Date getMfechaNacimiento() {
        return mfechaNacimiento;
    }

    @ColumnInfo(name = "fechanacimiento")
    @NonNull
    private Date mfechaNacimiento;

    public Contacto(@NonNull String mNombre, String mApellido, @NonNull String mNumTelefono, @NonNull Date mfechaNacimiento) {
        this.mNombre = mNombre;
        this.mApellido = mApellido;
        this.mNumTelefono = mNumTelefono;
        this.mfechaNacimiento = mfechaNacimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNumTelefono() {
        return mNumTelefono;
    }

    public void setNumTelefono(@NonNull String mNumTelefono) {
        this.mNumTelefono = mNumTelefono;
    }

    @NonNull
    public String getNombre() {
        return mNombre;
    }

    public void setNombre(@NonNull String mNombre) {
        this.mNombre = mNombre;
    }

    public String getApellido() {
        return mApellido;
    }

    public void setApellido(String mApellido) {
        this.mApellido = mApellido;
    }

    @NonNull
    public Date getFechaNacimiento() {
        return mfechaNacimiento;
    }

    public void setFechaNacimiento(@NonNull Date mfechaNacimiento) {
        this.mfechaNacimiento = mfechaNacimiento;
    }

    @Override
    public String toString() {
        //para mostrar la fecha en formato del idioma del dispositivo
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return  mNombre + " " + mApellido + " "+ mNumTelefono + ' ' + df.format(mfechaNacimiento) ;
    }
}
