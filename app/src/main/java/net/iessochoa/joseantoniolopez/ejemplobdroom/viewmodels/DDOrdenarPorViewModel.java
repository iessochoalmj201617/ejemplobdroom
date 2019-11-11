package net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.ContactoRepository;

import java.util.List;

//https://proandroiddev.com/mediatorlivedata-to-the-rescue-5d27645b9bc3
/*
Room no permite sentencias SQL com parámetro en el Order By, por lo que tendremos
que utilizar una consulta por cada tipo de ordenación
 */
public class DDOrdenarPorViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;
    //mantenemos el orden actual
    private String ordenadoPor = "nombre";
    //las listas ordenadas por..
    private LiveData<List<Contacto>> contactosOrdenadoPorNombreLiveData;
    private LiveData<List<Contacto>> contactosOrdenadoPorFechaLiveData;
    //Utilizamos un MediatorLiveData que en función de orden seleccionado asigna un LiveData u otro
    private MediatorLiveData<List<Contacto>> listaContactosMediatorLiveData;


    public DDOrdenarPorViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ContactoRepository(application);

        contactosOrdenadoPorFechaLiveData = mRepository.getContactosOrderByFecha();
        contactosOrdenadoPorNombreLiveData = mRepository.getContactosOrderByNombre();

        listaContactosMediatorLiveData = new MediatorLiveData<List<Contacto>>();
        listaContactosMediatorLiveData.addSource(contactosOrdenadoPorFechaLiveData, new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
                if (ordenadoPor.equals("nombre"))
                    listaContactosMediatorLiveData.setValue(contactosOrdenadoPorNombreLiveData.getValue());
            }
        });
        listaContactosMediatorLiveData.addSource(contactosOrdenadoPorNombreLiveData, new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
                if (ordenadoPor.equals("fecha"))
                    listaContactosMediatorLiveData.setValue(contactosOrdenadoPorFechaLiveData.getValue());
            }
        });
    }

    /*private List<Contacto> ordenadoPorLista() {
        if (ordenadoPor.equals("nombre"))
            return contactosOrdenadoPorNombreLiveData.getValue();
        else
            return contactosOrdenadoPorFechaLiveData.getValue();
    }*/

    public MediatorLiveData<List<Contacto>> getAllContactos() {
        return listaContactosMediatorLiveData;
    }

    public void setOrdenadoPor(String ordenActual) {
        ordenadoPor = ordenActual;
        if (ordenadoPor.equals("nombre"))
           listaContactosMediatorLiveData.setValue(contactosOrdenadoPorNombreLiveData.getValue());
        else
            listaContactosMediatorLiveData.setValue(contactosOrdenadoPorFechaLiveData.getValue());
    }

    //Inserción y borrado que se reflejará automáticamente gracias al observador creado en la
    //actividad
    public void insert(Contacto contacto) {
        mRepository.insert(contacto);
    }

    public void delete(Contacto contacto) {
        mRepository.delete(contacto);

    }
}
