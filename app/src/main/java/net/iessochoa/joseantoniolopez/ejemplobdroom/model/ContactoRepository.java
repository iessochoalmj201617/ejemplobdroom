package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactoRepository {
    private ContactoDao mContactoDao;
    private LiveData<List<Contacto>> mAllContactos;

    ContactoRepository(Application application){
        ContactoDatabase db=ContactoDatabase.getDatabase(application);
        mContactoDao =db.contactoDao();
        mAllContactos=mContactoDao.getAllContactos();
    }
    LiveData<List<Contacto>> getAllContactos(){
        return mAllContactos;
    }
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
}
