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
que utilizar una consulta por cada tipo de ordenación si queremos que el usuario pueda
mostras los datos ordenados por diferentes campos.
Vamos utilizar la clase MediatorLiveData
https://developer.android.com/reference/android/arch/lifecycle/MediatorLiveData
Esta clase te permite observar un objeto que depende de otros LiveData combinados.
Nosotros tendremos las dos consultas SQL de ordenación separadas y en funcíon de que
el usuario elija ordenar por nombre o por fecha, le asignaremos a mediator el livedata que
corresponda.
En la Actividad, observaremos el MediatosLiveData, que cambiaremos cuando se elija fecha o nombre como
método de ordanación.
Está basado en el siguiente artículo
//https://proandroiddev.com/mediatorlivedata-to-the-rescue-5d27645b9bc3

 */
public class DDOrdenarPorViewModel extends AndroidViewModel {
    private ContactoRepository mRepository;
    //utilizamos un HashMap con dos elementos: el primero nos sirve
    //para buscar por nombre y el segundo será una fecha con la que buscaremos los menores que la fecha
    private MutableLiveData<HashMap<String,Object>> condicionBusquedaLiveData;
    //ordenaremos por nombre y por fecha
    private final String ORDER_BY="order_by";
    private final String ORDER="order";
    public static final String ORDENAR_POR_NOMBRE=Contacto.NOMBRE;
    public static final String ORDENAR_POR_FECHA=Contacto.FECHA_NACIMIENTO;
    //podremos elegir ascendente y descendente
    public static final String ORDENAR_ASC="ASC";
    public static final String ORDENAR_DESC="DESC";
    public enum OrderBy
    {
        NOMBRE, FECHA;
    }
    public enum  Order
    {
        ASC,DESC;
    }


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
     * Modifica la condición de busqueda
     * @param orderBy: ordena por Nombre o por fecha
     */
    public void setOrderBy(OrderBy orderBy) {
        HashMap<String, Object> condiciones= condicionBusquedaLiveData.getValue();
        String ordenar="";
        switch (orderBy){
            case FECHA:
                ordenar=Contacto.FECHA_NACIMIENTO;
                break;
            case NOMBRE:
                ordenar=Contacto.NOMBRE;
                break;
        }
        condiciones.put(ORDER_BY,ordenar);
        condicionBusquedaLiveData.setValue(condiciones);
    }

    /**
     * Modifica la condición de busqueda
     * @param order: ordena Ascendente(ASC) o Descendente(DESC)
     */
    public void setOrder(Order order){
        HashMap<String, Object> condiciones= condicionBusquedaLiveData.getValue();

        condiciones.put(ORDER,order.toString());
        condicionBusquedaLiveData.setValue(condiciones);
    }
    public LiveData<List<Contacto>> getAllContactos() {
        return listContactosLiveData;
    }
    //rxJava
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
