package net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.repository.ContactoRepository;

import java.util.List;

/**                switchMap
 * https://www.it-swarm.net/es/android/como-y-donde-usar-transformations.switchmap/834875192/
 * En este caso, la sentencia sql depende de otro campo en el que el usuario puede realizar
 * una búsqueda, por lo tenemos la clase Transformation que nos permite modificar la condición
 * SQL cuando cuambien el campo de búsqueda que a su vez es un livedata
 */
public class BBCondicionBusquedaViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;
    //LiveData que depende de otros elementos
    private MutableLiveData<String> condicionBusquedaLiveData;
    // Lista de contactos devuelto por sql mediante Room;
    private LiveData<List<Contacto>> listaContactosLiveData;

    public BBCondicionBusquedaViewModel(@NonNull Application application) {
        super(application);
        mRepository=ContactoRepository.getInstance(application);

        //Este Livedata estará asociado al editext de busqueda por nombre
        condicionBusquedaLiveData=new MutableLiveData<String>();
        //en el primer momento no hay condición
        condicionBusquedaLiveData.setValue("");
        //switchMap nos me permite cambiar el livedata de la consulta SQL
        // al modificarse la consulta de busqueda(cuando cambia condicionBusquedaLiveData)

        /*listaContactosLiveData = Transformations.switchMap(condicionBusquedaLiveData, new Function<String, LiveData<List<Contacto>>>() {
            //cuando cambie condicionBusquedaLivedata, se llamará a esta función con el valor
            //nuevo en el parámetro nombre
            @Override
            public LiveData<List<Contacto>> apply(String nombre) {
                return mRepository.getByNombre(nombre);
            }
        });*/
        //version lambda
        listaContactosLiveData=Transformations.switchMap(condicionBusquedaLiveData,
                nombre -> mRepository.getByNombre(nombre));
    }
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
