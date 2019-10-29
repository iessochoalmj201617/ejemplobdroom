package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface ContactoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contacto contacto);

    @Query("DELETE FROM contacto")
    void deleteAll();
    @Delete
    void deleteByContacto(Contacto contacto);
//    @Query("DELETE FROM contacto WHERE id = :id")
//    void deleteByContactoId(int id);

    @Update
    void update(Contacto contacto);
//https://github.com/ajaysaini-sgvu/room-persistence-sample/blob/master/app/src/main/java/com/nagarro/persistence/dao/UserDao.java
    //https://github.com/android/architecture-components-samples/tree/master/BasicSample/app/src/main/java/com/example/android/persistence
    @Query("SELECT * FROM contacto ORDER BY nombre ASC")
    LiveData<List<Contacto>> getAllContactos();
    @Query("SELECT * FROM contacto ORDER BY nombre ASC")
    LiveData<List<Contacto>> getContacto();
    @Query("SELECT * FROM contacto where nombre LIKE  :nombre OR apellido LIKE :nombre")
    LiveData<List<Contacto>> findByNombre(String nombre);
    @Query("SELECT COUNT(*) from contacto")
    int countContactos();
    @Query("SELECT * FROM contacto WHERE fechaNacimiento <= :fechaNacimiento")
    LiveData<List<Contacto>> contactosMenoresQue(Date fechaNacimiento);




}
