package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactoViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;
    private LiveData<List<Contacto>> mAllContactos;

    public ContactoViewModel(Application application){
        super(application);
        mRepository=new ContactoRepository(application);
        mAllContactos=mRepository.getAllContactos();
    }
    public LiveData<List<Contacto>> getAllContactos()
        {
            return mAllContactos;

        }
    public LiveData<List<Contacto>> getByNombre(String nombre)
    {
        mAllContactos=mRepository.getByNombre("%"+nombre+"%");
        return mAllContactos;
    }
    public void insert(Contacto contacto){
        mRepository.insert(contacto);
    }
    public void delete(Contacto contacto){
        mRepository.delete(contacto);

    }

}
