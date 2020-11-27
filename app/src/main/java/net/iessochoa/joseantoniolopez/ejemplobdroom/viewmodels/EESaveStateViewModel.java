package net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.repository.ContactoRepository;

import java.util.List;
//matar a proceso en android:
// c:\Android\Sdk\platform-tools>adb shell am force-stop net.iessochoa.joseantoniolopez.ejemplobdroom
//https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate
//https://codelabs.developers.google.com/codelabs/android-lifecycles/#6
public class EESaveStateViewModel  extends AndroidViewModel {
    private static final String STATUS_BUSQUEDA="campo_busqueda";
    private ContactoRepository mRepository;
    //LiveData que depende de otros elementos
    private MutableLiveData<String> condicionBusquedaLiveData;
    // Lista de contactos devuelto por sql mediante Room;
    private LiveData<List<Contacto>> listaContactosLiveData;
    //añadadimos el estado
    private SavedStateHandle mState;
//añadimos un nueva constructor
public EESaveStateViewModel(@NonNull Application application,SavedStateHandle savedStateHandle) {
    super(application);
    mState = savedStateHandle;
    mRepository=ContactoRepository.getInstance(application);

    //Este Livedata estará asociado al editext de busqueda por nombre
    condicionBusquedaLiveData=new MutableLiveData<String>();
    //en primer momento no hay condición
    condicionBusquedaLiveData.setValue("");

    //version lambda
    listaContactosLiveData= Transformations.switchMap(condicionBusquedaLiveData, nombre -> mRepository.getByNombre(nombre));
}
    /*public EESaveStateViewModel(@NonNull Application application) {
        super(application);
        mRepository=ContactoRepository.getInstance(application);

        //Este Livedata estará asociado al editext de busqueda por nombre
        condicionBusquedaLiveData=new MutableLiveData<String>();
        //en primer momento no hay condición
        condicionBusquedaLiveData.setValue("");
        
        //version lambda
        listaContactosLiveData= Transformations.switchMap(condicionBusquedaLiveData, nombre -> mRepository.getByNombre(nombre));
    }*/
    public LiveData<List<Contacto>> getByNombre()
    {
        return listaContactosLiveData;
    }
    public void setCondicionBusqueda(String condicionBusqueda) {

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
