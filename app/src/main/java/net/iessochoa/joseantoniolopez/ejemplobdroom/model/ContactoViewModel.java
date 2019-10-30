package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import android.app.Application;

import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

public class ContactoViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;
    private LiveData<List<Contacto>> mAllContactos;
    //LiveData que depende de otros elementos
    private MutableLiveData<String> condicionBusquedaLiveData;
   // private String condicionBusqueda;
    private LiveData<List<Contacto>> listaContactos;




    public ContactoViewModel(Application application){
        super(application);
        mRepository=new ContactoRepository(application);
        //Comprobación de todos los datos
        mAllContactos=mRepository.getAllContactos();

        //comprobación de Livedata que cambia por condición de otro.Nos evita tener que crearlo
        condicionBusquedaLiveData=new MutableLiveData<String>();
        //condicionBusqueda="";
        condicionBusquedaLiveData.setValue("");
        //switchMap que me permite cambiar el livedata al modificarse la consulta de busqueda
        listaContactos = Transformations.switchMap(condicionBusquedaLiveData, new Function<String, LiveData<List<Contacto>>>() {
            @Override
            public LiveData<List<Contacto>> apply(String nombre) {
                /*if(nombre.equals(""))
                    return mRepository.getAllContactos();
                else*/
                    return mRepository.getByNombre("%"+nombre+"%");
            }
        });
    }
    public LiveData<List<Contacto>> getAllContactos()
        {
            return mAllContactos;

        }
    public LiveData<List<Contacto>> getByNombre()
    {
        //mAllContactos=mRepository.getByNombre("%"+nombre+"%");
        return listaContactos;
    }
    public void setCondicionBusqueda(String condicionBusqueda) {
        //this.condicionBusqueda = condicionBusqueda;
        condicionBusquedaLiveData.setValue(condicionBusqueda);
    }
    //Inserción y borrado que se reflejará automáticamente gracias al observador creado en la
    //actividad
    public void insert(Contacto contacto){
        mRepository.insert(contacto);
    }
    public void delete(Contacto contacto){
        mRepository.delete(contacto);

    }

}
