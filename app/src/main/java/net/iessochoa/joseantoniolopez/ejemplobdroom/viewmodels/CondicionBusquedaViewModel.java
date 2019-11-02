package net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.ContactoRepository;

import java.util.List;

public class CondicionBusquedaViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;

    //LiveData que depende de otros elementos
    private MutableLiveData<String> condicionBusquedaLiveData;
    // private String condicionBusqueda;
    private LiveData<List<Contacto>> listaContactosLiveData;
    public CondicionBusquedaViewModel(@NonNull Application application) {
        super(application);mRepository=new ContactoRepository(application);


        //comprobación de Livedata que cambia por condición de otro.Nos evita tener que crearlo
        condicionBusquedaLiveData=new MutableLiveData<String>();
        //en primer momento no hay condición
        condicionBusquedaLiveData.setValue("");
        //switchMap que me permite cambiar el livedata al modificarse la consulta de busqueda
        listaContactosLiveData = Transformations.switchMap(condicionBusquedaLiveData, new Function<String, LiveData<List<Contacto>>>() {
            @Override
            public LiveData<List<Contacto>> apply(String nombre) {

                return mRepository.getByNombre("%"+nombre+"%");
            }
        });
    }
    public LiveData<List<Contacto>> getByNombre()
    {
        return listaContactosLiveData;
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
