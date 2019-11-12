package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

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
@Entity(tableName = Contacto.TABLE_NAME)
public class Contacto implements Parcelable {
    public static final String TABLE_NAME="contacto";
    public static final String ID= BaseColumns._ID;
    public static final String NOMBRE="nombre";
    public static final String APELLIDO="apellido";
    public static final String TELEFONO="numerotelefono";
    public static final String FECHA_NACIMIENTO="fechanacimiento";


    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = ID)
    private int id;

    @ColumnInfo(name = NOMBRE)
    @NonNull
    private  String mNombre;

    @ColumnInfo(name=APELLIDO)
    private String mApellido;

    @ColumnInfo(name = TELEFONO)
    @NonNull
    private String mNumTelefono;

    @ColumnInfo(name = FECHA_NACIMIENTO)
    @NonNull
    private Date mFechaNacimiento;

    public Contacto(@NonNull String mNombre, String mApellido, @NonNull String mNumTelefono, @NonNull Date mFechaNacimiento) {
        this.mNombre = mNombre;
        this.mApellido = mApellido;
        this.mNumTelefono = mNumTelefono;
        this.mFechaNacimiento = mFechaNacimiento;
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
        return mFechaNacimiento;
    }
    /*@NonNull
    public Date getmFechaNacimiento() {
        return mFechaNacimiento;
    }*/
    public void setFechaNacimiento(@NonNull Date mFechaNacimiento) {
        this.mFechaNacimiento = mFechaNacimiento;
    }

    @Override
    public String toString() {
        //para mostrar la fecha en formato del idioma del dispositivo
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return  mNombre + " " + mApellido + " "+ mNumTelefono + ' ' + df.format(mFechaNacimiento) ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.mNombre);
        dest.writeString(this.mApellido);
        dest.writeString(this.mNumTelefono);
        dest.writeLong(this.mFechaNacimiento != null ? this.mFechaNacimiento.getTime() : -1);
    }

    protected Contacto(Parcel in) {
        this.id = in.readInt();
        this.mNombre = in.readString();
        this.mApellido = in.readString();
        this.mNumTelefono = in.readString();
        long tmpMFechaNacimiento = in.readLong();
        this.mFechaNacimiento = tmpMFechaNacimiento == -1 ? null : new Date(tmpMFechaNacimiento);
    }

    public static final Parcelable.Creator<Contacto> CREATOR = new Parcelable.Creator<Contacto>() {
        @Override
        public Contacto createFromParcel(Parcel source) {
            return new Contacto(source);
        }

        @Override
        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }
    };
}
