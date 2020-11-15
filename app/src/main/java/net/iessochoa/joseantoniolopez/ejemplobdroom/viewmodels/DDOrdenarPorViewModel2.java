package net.iessochoa.joseantoniolopez.ejemplobdroom.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;
import net.iessochoa.joseantoniolopez.ejemplobdroom.repository.ContactoRepository;

import java.util.List;

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
public class DDOrdenarPorViewModel2 extends AndroidViewModel {
    //opciones para ordenar
    public static final String POR_NOMBRE=Contacto.NOMBRE;
    public static final String POR_FECHA=Contacto.FECHA_NACIMIENTO;

    private ContactoRepository mRepository;
    //mantenemos el orden actual
    private String ordenadoPor = POR_NOMBRE;
    //las listas ordenadas por..
    private LiveData<List<Contacto>> contactosOrdenadoPorNombreLiveData;
    private LiveData<List<Contacto>> contactosOrdenadoPorFechaLiveData;
    //Utilizamos un MediatorLiveData que en función de orden seleccionado asigna un LiveData u otro
    private MediatorLiveData<List<Contacto>> listaContactosMediatorLiveData;


    public DDOrdenarPorViewModel2(@NonNull Application application) {
        super(application);
        mRepository = ContactoRepository.getInstance(application);
        //recuperamos la listas ordenadas
        contactosOrdenadoPorFechaLiveData = mRepository.getContactosOrderByFecha();
        contactosOrdenadoPorNombreLiveData = mRepository.getContactosOrderByNombre();

        listaContactosMediatorLiveData = new MediatorLiveData<List<Contacto>>();
        //añadimos las dos fuentes posibles
        listaContactosMediatorLiveData.addSource(contactosOrdenadoPorFechaLiveData, new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
                //como cuando se añada o se elimine un elemento afecta a las dos listas SQL, asignamos la que
                //corresponda a la actual, ya que se ejecutarán los dos eventos
                if (ordenadoPor.equals(POR_FECHA))
                    listaContactosMediatorLiveData.setValue(contactos);
            }
        });
        listaContactosMediatorLiveData.addSource(contactosOrdenadoPorNombreLiveData, new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
                if (ordenadoPor.equals(POR_NOMBRE))
                    listaContactosMediatorLiveData.setValue(contactos);
            }
        });
    }



    public MediatorLiveData<List<Contacto>> getAllContactos() {
        return listaContactosMediatorLiveData;
    }

    /**
     * Nos permite asignar el orden actual de la lista
     * @param ordenActual
     */
    public void setOrdenadoPor(String ordenActual) {
        ordenadoPor = ordenActual;
        if (ordenadoPor.equals(POR_NOMBRE))
           listaContactosMediatorLiveData.setValue(contactosOrdenadoPorNombreLiveData.getValue());
        else
            listaContactosMediatorLiveData.setValue(contactosOrdenadoPorFechaLiveData.getValue());
    }

    //Inserción y borrado que se reflejará automáticamente gracias al observador creado en la
    //actividad
    public void insert(Contacto contacto) {
        mRepository.insert(contacto);
    }

    public void delete(Contacto contacto) {
        mRepository.delete(contacto);

    }
}
