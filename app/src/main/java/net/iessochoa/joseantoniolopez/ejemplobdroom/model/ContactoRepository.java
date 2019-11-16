package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.Date;
import java.util.List;

public class ContactoRepository {
    private ContactoDao mContactoDao;
    private LiveData<List<Contacto>> mAllContactos;

    public ContactoRepository(Application application){
        ContactoDatabase db=ContactoDatabase.getDatabase(application);
        mContactoDao =db.contactoDao();
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
    /*
    Insertar nos obliga a crear tarea en segundo plano
     */
    public void insert(Contacto contacto){
        new inserAsyncTask(mContactoDao).execute(contacto);
    }

    private static class inserAsyncTask extends AsyncTask<Contacto,Void,Void> {
       private ContactoDao mAsyncTaskDao;
        public inserAsyncTask(ContactoDao mContactoDao) {
            mAsyncTaskDao=mContactoDao;
        }

        @Override
        protected Void doInBackground(Contacto... contactos) {
            mAsyncTaskDao.insert(contactos[0]);
            return null;
        }
    }
/*
Borrar
 */
    public void delete(Contacto contacto){
        new deleteAsyncTask(mContactoDao).execute(contacto);
    }
    private static class deleteAsyncTask extends AsyncTask<Contacto,Void,Void> {
        private ContactoDao mAsyncTaskDao;
        public deleteAsyncTask(ContactoDao mContactoDao) {
            mAsyncTaskDao=mContactoDao;
        }

        @Override
        protected Void doInBackground(Contacto... contactos) {
            mAsyncTaskDao.deleteByContacto(contactos[0]);
            return null;
        }
    }
    /*public void deleteById(int id){
        mContactoDao.deleteByContactoId(id);
    }*/
}
