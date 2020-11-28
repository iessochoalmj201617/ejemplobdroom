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
/*Cuando el proceso de la app es destruido por necesidad de recursos, el ViewModel es
destruido también. Podemos guardar en SavedStateHandle la información que queramos
para no perder la experiencia de usuario.
el los siguiente artículos tenéis más información
https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate
https://codelabs.developers.google.com/codelabs/android-lifecycles/#6
 matar a proceso en android:
minimiza la app con botón home
En consola vamos al direcctorio del sdk \Android\Sdk\platform-tools>
adb shell
su
 ps -A | grep ejemplodbroom
kill numero_proceso
//*/
public class EESaveStateViewModel  extends AndroidViewModel {
    //los valores los guardamos como clave/valor
    private static final String STATUS_BUSQUEDA="campo_busqueda";

    private ContactoRepository mRepository;
    //LiveData que depende de otros elementos
    private MutableLiveData<String> condicionBusquedaLiveData;
    // Lista de contactos devuelto por sql mediante Room;
    private LiveData<List<Contacto>> listaContactosLiveData;
    //El SavedStateHandle me permite recuperar los datos ante una destrucción del proceso
    private SavedStateHandle mState;
//añadimos un nueva constructor
public EESaveStateViewModel(@NonNull Application application,SavedStateHandle savedStateHandle) {
    super(application);
    mState = savedStateHandle;
    mRepository=ContactoRepository.getInstance(application);

    //Este Livedata estará asociado al editext de busqueda por nombre
    condicionBusquedaLiveData=new MutableLiveData<String>();
    //en primer momento no hay condición
    // si venimos de una reconstrucción la recuperamos
    String condicion=mState.get(STATUS_BUSQUEDA);
    if(condicion==null)
        setCondicionBusqueda("");
    else
        setCondicionBusqueda(condicion);
    //version lambda
    listaContactosLiveData= Transformations.switchMap(condicionBusquedaLiveData, nombre -> mRepository.getByNombre(nombre));
}

    public LiveData<List<Contacto>> getByNombre()
    {
        return listaContactosLiveData;
    }
    public void setCondicionBusqueda(String condicionBusqueda) {
    //si cambia la condición de búsqueda, la guardamos en el SaveState
        mState.set(STATUS_BUSQUEDA,condicionBusqueda);
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
