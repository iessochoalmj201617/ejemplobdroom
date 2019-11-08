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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
//https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
public class CCVariasCondicionesViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;
    //utilizamos una ArrayList con dos elementos: el primero nos sirve
    //para buscar por nombre y el segundo para ordenarPor
    private MutableLiveData<HashMap<String,Object>> condicionBusquedaLiveData;
    private final String NOMBRE="nombre";
    private final String FECHA_MENOR_QUE="fecha";


    private LiveData<List<Contacto>> listContactosLiveData;

    public CCVariasCondicionesViewModel(@NonNull Application application) {
        super(application);
        mRepository=new ContactoRepository(application);

        condicionBusquedaLiveData =new MutableLiveData<HashMap<String,Object>>();
        //switchMap nos me permite cambiar el livedata de la consulta SQL
        // al modificarse la consulta de busqueda(cuando cambia condicionBusquedaLiveData)

        listContactosLiveData = Transformations.switchMap(condicionBusquedaLiveData, new Function<HashMap<String,Object>, LiveData<List<Contacto>>>() {
            @Override
            public LiveData<List<Contacto>> apply(HashMap<String,Object> condiciones) {
                //return mRepository.getByNombreOrderBy(condicion.get(0),condicion.get(1));
                return mRepository.getByNombreFecha((String) condiciones.get(NOMBRE),(Date)condiciones.get(FECHA_MENOR_QUE));
            }
        });
        ArrayList condiciones=new ArrayList();

        condiciones.add(NOMBRE,"%%");
        condiciones.add(Contacto.APELLIDO);
        condicionBusquedaLiveData.setValue(condiciones);
    }
    public void setFechaMenorQue(Date fecha) {
        ArrayList condiciones=(ArrayList) condicionBusquedaLiveData.getValue();
        condiciones.set(1,fecha);
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
