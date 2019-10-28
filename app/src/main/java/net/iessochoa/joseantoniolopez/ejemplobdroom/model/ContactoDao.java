package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contacto contacto);

    @Query("DELETE FROM contacto")
    void deleteAll();

    @Query("SELECT * FROM contacto ORDER BY nombre ASC")
    LiveData<List<Contacto>> getAllContactos();

    @Query("DELETE FROM contacto WHERE id = :contactoId")
    abstract void deleteByUserId(int contactoId);

    @Update
    void update(Contacto contacto);
}
