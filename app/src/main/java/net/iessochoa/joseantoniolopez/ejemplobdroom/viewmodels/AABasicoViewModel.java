package net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.repository.ContactoRepository;

import java.util.List;

/**
 *https://developer.android.com/topic/libraries/architecture/viewmodel
 * En este ejemplo, recuperamos
 */
public class AABasicoViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;
    private LiveData<List<Contacto>> mAllContactos;
    public AABasicoViewModel(@NonNull Application application) {
        super(application);
        mRepository=ContactoRepository.getInstance(application);
        //Recuperamos el LiveData de todos los contactos
        mAllContactos=mRepository.getAllContactos();
    }
    public LiveData<List<Contacto>> getAllContactos()
    {
        return mAllContactos;

    }
    //Inserción y borrado que se reflejará automáticamente gracias al observador creado en la
    //actividad
    public void insert(Contacto contacto){
        mRepository.insert(contacto);
    }
    public void delete(Contacto contacto){ mRepository.delete(contacto); }
}
