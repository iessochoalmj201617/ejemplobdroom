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

import io.reactivex.Single;

/*
Room no permite sentencias SQL com parámetro en el Order By, por lo que tendremos
que utilizar una consulta especial que podéis ver en la clase ContactoDao.
 */
public class DDOrdenarPorViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;
   /* utilizamos un HashMap con dos elementos:
    -el primero nos sirve para indicar el campo por el que ordenamos
    -el seguno para indicar si ordenamos ascendente o descendente*/
    private MutableLiveData<HashMap<String,Object>> condicionBusquedaLiveData;
    //Definimos
    private final String ORDER_BY="order_by";
    private final String ORDER="order";

    //podremos elegir ascendente y descendente
    public static final String ORDENAR_ASC="ASC";
    public static final String ORDENAR_DESC="DESC";



    //resultado de la consulta SQL, cuando cambien la condición, se activará el observador y actualiza en pantalla el RecyclerView
    private LiveData<List<Contacto>> listContactosLiveData;

    public DDOrdenarPorViewModel(@NonNull Application application) {
        super(application);
        mRepository=ContactoRepository.getInstance(application);

        condicionBusquedaLiveData =new MutableLiveData<HashMap<String,Object>>();
        //creamos el LiveData de condición de busqueda. Mantenemos los valores de la condición SQL en una sola
        //estructura HashMap para que se detecte las modificaciones de cualquiera de las dos
        HashMap<String, Object> condiciones=new HashMap<String, Object>();
        //asignamos valores iniciales
        condiciones.put(ORDER_BY,Contacto.NOMBRE);//toda la agenda
        condiciones.put(ORDER,ORDENAR_ASC);//fecha actual

        condicionBusquedaLiveData.setValue(condiciones);
        //switchMap nos  permite cambiar el livedata de la consulta SQL
        // al modificarse la consulta de busqueda(cuando cambia condicionBusquedaLiveData)

        /*listContactosLiveData = Transformations.switchMap(condicionBusquedaLiveData, new Function<HashMap<String,Object>, LiveData<List<Contacto>>>() {
            @Override
            public LiveData<List<Contacto>> apply(HashMap<String,Object> condiciones) {

                return mRepository.getContactosOrderBy((String) condiciones.get(ORDER_BY),(String)condiciones.get(ORDER));
            }
        });*/
        //version lambda
        listContactosLiveData=Transformations.switchMap(condicionBusquedaLiveData,condicionesMap ->
                mRepository.getContactosOrderBy((String) condicionesMap.get(ORDER_BY),(String)condicionesMap.get(ORDER)));
    }


    /**
     * Modifica la condición de busqueda:por fecha o nombre
     */
    public void setOrderByFecha() {
        //recuperamos el HashMap con las condiciones de búsqueda actuales
        HashMap<String, Object> condiciones= condicionBusquedaLiveData.getValue();
        condiciones.put(ORDER_BY,Contacto.FECHA_NACIMIENTO);
        //activa el observer del HashMap que activa el Transformations.swirchMap para que realice
        //la sentencia sql
        condicionBusquedaLiveData.setValue(condiciones);
    }
    public void setOrderByNombre() {
        //recuperamos el HashMap con las condiciones de búsqueda actuales
        HashMap<String, Object> condiciones= condicionBusquedaLiveData.getValue();
        condiciones.put(ORDER_BY,Contacto.NOMBRE);
        //activa el observer del HashMap que activa el Transformations.swirchMap para que realice
        //la sentencia sql
        condicionBusquedaLiveData.setValue(condiciones);
    }
    /**
     * Modifica la condición de busqueda
     * ordena Ascendente(ASC) o Descendente(DESC)
     */
    public void setOrderAsc(){
        HashMap<String, Object> condiciones= condicionBusquedaLiveData.getValue();
        condiciones.put(ORDER,ORDENAR_ASC);
        condicionBusquedaLiveData.setValue(condiciones);
    }
    public void setOrderDesc(){
        HashMap<String, Object> condiciones= condicionBusquedaLiveData.getValue();
        condiciones.put(ORDER,ORDENAR_DESC);
        condicionBusquedaLiveData.setValue(condiciones);
    }
    //Nos devuelve todos los contactos
    public LiveData<List<Contacto>> getAllContactos() {
        return listContactosLiveData;
    }

    /*rxJava: Este método nos permite recuperar el total de contactos con un observable de la
    libreria RXJava. La clase Single nos permite una única observación, es suficiente para una consulta
    única como es nuestro caso
     */
    public Single<Integer> geTotalContactos(){
        return mRepository.geTotalContactos();
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
