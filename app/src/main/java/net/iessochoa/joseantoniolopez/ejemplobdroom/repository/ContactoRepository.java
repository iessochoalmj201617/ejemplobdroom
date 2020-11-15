package net.iessochoa.joseantoniolopez.ejemplobdroom.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;


import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.ContactoDao;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.ContactoDatabase;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;

public class ContactoRepository {
    //implementamos Singleton
    private static volatile ContactoRepository INSTANCE;

    private ContactoDao mContactoDao;
    private LiveData<List<Contacto>> mAllContactos;
//singleton
    public static ContactoRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ContactoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE=new ContactoRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    private ContactoRepository(Application application){
        //creamos la base de datos
        ContactoDatabase db=ContactoDatabase.getDatabase(application);
        //Recuperamos el DAO necesario para el CRUD de la base de datos
        mContactoDao =db.contactoDao();
        //Recuperamos la lista como un LiveData
        mAllContactos=mContactoDao.getAllContactos();
    }
    public LiveData<List<Contacto>> getAllContactos(){
        return mAllContactos;
    }
    public LiveData<List<Contacto>> getByNombre(String nombre){
        mAllContactos=mContactoDao.findByNombre(nombre);
        return mAllContactos;
    }


    public LiveData<List<Contacto>> getByNombreFecha(String nombre, Date menorQue){
        mAllContactos=mContactoDao.findByNombreFecha(nombre,menorQue);
        return mAllContactos;
    }
    //lista ordenado por columnas diferentes
    public LiveData<List<Contacto>> getContactosOrderByNombre(){
        mAllContactos=mContactoDao.getContactosOrderByNombre();
        return mAllContactos;
    }
    public LiveData<List<Contacto>> getContactosOrderByFecha(){
        mAllContactos=mContactoDao.getContactosOrderByFecha();
        return mAllContactos;
    }
    public LiveData<List<Contacto>> getContactosOrderBy(String order_by, String order){
        mAllContactos=mContactoDao.getContactosOrderBy(order_by, order);
        return mAllContactos;
    }
    public Single<Integer> geTotalContactos(){
        return mContactoDao.geTotalContactos();
    }
    /*
    Insertar: nos obliga a crear tarea en segundo plano
     */
    public void insert(Contacto contacto){
      //administramos el hilo con el Executor
        ContactoDatabase.databaseWriteExecutor.execute(()->{
            mContactoDao.insert(contacto);
        });


    }

/*
Borrar: nos obliga a crear tarea en segundo plano
 */
    public void delete(Contacto contacto){
        //administramos el hilo con el Executor
        ContactoDatabase.databaseWriteExecutor.execute(()->{
            mContactoDao.deleteByContacto(contacto);
        });
    }
    /*public void deleteById(int id){
        mContactoDao.deleteByContactoId(id);
    }*/
}
