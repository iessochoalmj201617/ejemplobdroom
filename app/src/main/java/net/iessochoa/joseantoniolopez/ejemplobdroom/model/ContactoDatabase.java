package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Esta clase nos comunica con SQLite. Mantenemos una sola instancia mediante el patrón
 * Sigleton
 * https://developer.android.com/training/data-storage/room/prepopulate
 */
//le indicamos las entidades de la base de datos y la versión
@Database(entities = {Contacto.class}, version = 1)
//Nos transforma automáticamente las fechas a entero
@TypeConverters({TransformaFechaSQLite.class})
public abstract class ContactoDatabase extends RoomDatabase {
    //Permite el acceso a los metodos CRUD
    public abstract ContactoDao contactoDao();

    //la base de datos
    private static volatile ContactoDatabase INSTANCE;
    //para el manejo de base de datos con room necesitamos realzar las tareas CRUD en hilos,
    //las consultas Select que devuelve LiveData, Room crea los hilos automáticamente, pero para las
    //insercciones, acutalizaciones y borrado, tenemos que crear el hilo nosotros
    //Utilizaremos ExecutorService para el control de los hilos. Para saber más información de la clase
    //https://www.youtube.com/watch?v=Hc5xo-JjIMQ
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static ContactoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactoDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            //nombre del fichero de la base de datos
                            ContactoDatabase.class, "agenda_database")
                            //nos permite realizar tareas cuando es nueva o se ha creado una
                            //nueva versión del programa
                            .addCallback(sRoomDatabaseCallback)//para ejecutar al crear o al abrir
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //crearemos una tarea en segundo plano que nos permite cargar los datos de ejemplo la primera
    //vez que se abre la base de datos
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
           // new PopulateDbAsync(INSTANCE).execute();
            //creamos algunos contactos en un hilo
            databaseWriteExecutor.execute(()->{
                //obtenemos la base de datos
                ContactoDao mDao=INSTANCE.contactoDao();
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
                Contacto contacto = null;
                //creamos unos contactos
                try {
                    contacto = new Contacto("Pepe", "Lopez", "8888888", formatoDelTexto.parse("12-3-2000"));
                    mDao.insert(contacto);
                    contacto = new Contacto("Maria", "Perez", "6666666", formatoDelTexto.parse("12-3-2002"));
                    mDao.insert(contacto);
                    contacto = new Contacto("Juan", "Pomez", "66633333", formatoDelTexto.parse("12-3-2005"));
                    mDao.insert(contacto);
                    contacto = new Contacto("Pili", "Martinez", "66633333", formatoDelTexto.parse("12-3-2004"));
                    mDao.insert(contacto);
                    contacto = new Contacto("Fele", "Lillo", "66633333", formatoDelTexto.parse("12-3-2003"));
                    mDao.insert(contacto);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //si queremos realizar alguna tarea cuando se abre
        }
    };

}
