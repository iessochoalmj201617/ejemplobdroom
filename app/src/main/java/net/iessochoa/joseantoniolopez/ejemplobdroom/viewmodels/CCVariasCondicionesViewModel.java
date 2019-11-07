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

import java.util.ArrayList;
import java.util.List;
//https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
public class CCVariasCondicionesViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;
    //utilizamos una ArrayList con dos elementos: el primero nos sirve
    //para buscar por nombre y el segundo para ordenarPor
    private MutableLiveData<List<String>> condicionBusquedaLiveData;


    private LiveData<List<Contacto>> listContactosLiveData;

    public CCVariasCondicionesViewModel(@NonNull Application application) {
        super(application);
        mRepository=new ContactoRepository(application);

        condicionBusquedaLiveData =new MutableLiveData<List<String>>();
        //switchMap nos me permite cambiar el livedata de la consulta SQL
        // al modificarse la consulta de busqueda(cuando cambia condicionBusquedaLiveData)

        listContactosLiveData = Transformations.switchMap(condicionBusquedaLiveData, new Function<List<String>, LiveData<List<Contacto>>>() {
            @Override
            public LiveData<List<Contacto>> apply(List<String> condicion) {
                //return mRepository.getByNombreOrderBy(condicion.get(0),condicion.get(1));
                return mRepository.getContactosOrderBy(condicion.get(1));
            }
        });
        ArrayList condiciones=new ArrayList();

        condiciones.add("%%");
        condiciones.add(Contacto.APELLIDO);
        condicionBusquedaLiveData.setValue(condiciones);
    }
    public void setOrdenarPor(String ordenarPor) {
        ArrayList condiciones=(ArrayList) condicionBusquedaLiveData.getValue();
        condiciones.set(1,ordenarPor);
        condicionBusquedaLiveData.setValue(condiciones);
    }

    public void setNombre(String nombre) {
        ArrayList condiciones=(ArrayList) condicionBusquedaLiveData.getValue();
        condiciones.set(0,"%"+nombre+"%");
        condicionBusquedaLiveData.setValue(condiciones);
    }
    public LiveData<List<Contacto>> getListContactosLiveData() {
        return listContactosLiveData;
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
