package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;
import java.util.List;

@Dao
public interface ContactoDao {
    //nuevo contacto sustituyendo si ya existe
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contacto contacto);
//borrado de los contactos
    @Query("DELETE FROM "+Contacto.TABLE_NAME)
    void deleteAll();
    //borrado del contacto pasado por parámetro.
    @Delete
    void deleteByContacto(Contacto contacto);

//    @Query("DELETE FROM contacto WHERE id = :id")
//    void deleteByContactoId(int id);

    @Update
    void update(Contacto contacto);
//https://github.com/ajaysaini-sgvu/room-persistence-sample/blob/master/app/src/main/java/com/nagarro/persistence/dao/UserDao.java
    //https://github.com/android/architecture-components-samples/tree/master/BasicSample/app/src/main/java/com/example/android/persistence
    //todos los contactos
    @Query("SELECT * FROM "+Contacto.TABLE_NAME+" ORDER BY apellido")
    LiveData<List<Contacto>> getAllContactos();
    //NO FUNCIONA-->Todos los contactos ordenado por nombre o apellido o fecha pasado por parámetro
    @Query("SELECT * FROM contacto ORDER BY :ordenadoPor ASC")
    LiveData<List<Contacto>> getContactosOrdenadoPor(String ordenadoPor);
    //Aquellos contactos que coincide con la condición
    @Query("SELECT * FROM contacto where nombre LIKE  :nombre OR apellido LIKE :nombre")
    LiveData<List<Contacto>> findByNombre(String nombre);
    //número de contáctos
    @Query("SELECT COUNT(*) from contacto")
    int countContactos();
    //aquellos contactos menores que la fecha pasada por parámetro
    @Query("SELECT * FROM contacto WHERE fechanacimiento <= :fechaNacimiento")
    LiveData<List<Contacto>> contactosMenoresQue(Date fechaNacimiento);


    //lista por nombre y fecha de nacimiento menor que
    @Query("SELECT * FROM contacto where (nombre LIKE  :nombre OR apellido LIKE :nombre)" +
            " AND (fechanacimiento <= :menorQue)")
    LiveData<List<Contacto>> findByNombreFecha(String nombre,Date menorQue);
   //LISTAS ORDENADO POR
   @Query("SELECT * FROM "+Contacto.TABLE_NAME+" ORDER BY nombre,apellido")
   LiveData<List<Contacto>> getContactosOrderByNombre();
    @Query("SELECT * FROM "+Contacto.TABLE_NAME+" ORDER BY fechanacimiento")
    LiveData<List<Contacto>> getContactosOrderByFecha();



}
