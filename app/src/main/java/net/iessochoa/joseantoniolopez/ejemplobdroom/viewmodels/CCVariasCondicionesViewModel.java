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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
//En este ejemplo haremos algo parecido que en el B pero con dos condiciones diferentes
//Para conseguirlo, utilizaremos un livedata de tipo HashMap con dos valores, uno para
//cada condición. Si cambia uno de ellos, actualizará la sentencia SQL mediante la clase
//Transformation
// https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
public class CCVariasCondicionesViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;
    //utilizamos un HashMap con dos elementos: el primero nos sirve
    //para buscar por nombre y el segundo será una fecha con la que buscaremos los menores que la fecha
    private MutableLiveData<HashMap<String,Object>> condicionBusquedaLiveData;
    private final String NOMBRE="nombre";
    private final String FECHA_MENOR_QUE="fecha";

    //resultado de la consulta SQL, cuando cambien la condición, se activará el observador y actualiza en pantalla el RecyclerView
    private LiveData<List<Contacto>> listContactosLiveData;

    public CCVariasCondicionesViewModel(@NonNull Application application) {
        super(application);
        mRepository=ContactoRepository.getInstance(application);

        condicionBusquedaLiveData =new MutableLiveData<HashMap<String,Object>>();
        //creamos el LiveData de condición de busqueda. Mantenemos los valores de la condición SQL en una sola
        //estructura HashMap para que se detecte las modificaciones de cualquiera de las dos
        HashMap<String, Object> condiciones=new HashMap<String, Object>();
        //asignamos valores iniciales
        condiciones.put(NOMBRE,"");//toda la agenda
        condiciones.put(FECHA_MENOR_QUE,new Date());//fecha actual

        condicionBusquedaLiveData.setValue(condiciones);
        //switchMap nos  permite cambiar el livedata de la consulta SQL
        // al modificarse la consulta de busqueda(cuando cambia condicionBusquedaLiveData)

        listContactosLiveData = Transformations.switchMap(condicionBusquedaLiveData, new Function<HashMap<String,Object>, LiveData<List<Contacto>>>() {
            @Override
            public LiveData<List<Contacto>> apply(HashMap<String,Object> condiciones) {

                return mRepository.getByNombreFecha((String) condiciones.get(NOMBRE),(Date)condiciones.get(FECHA_MENOR_QUE));
            }
        });

    }
    /*
    cambiamos la condición de busqueda
     */
    public void setFechaMenorQue(Date fecha) {
        HashMap<String, Object> condiciones= condicionBusquedaLiveData.getValue();
        condiciones.put(FECHA_MENOR_QUE,fecha);
        condicionBusquedaLiveData.setValue(condiciones);
    }

    public void setNombre(String nombre) {
        HashMap<String, Object> condiciones= condicionBusquedaLiveData.getValue();
        condiciones.put(NOMBRE,nombre);
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
