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

/**
 * Esta clase nos comunica con SQLite. Mantenemos una sola instancia mediante el patrón
 * Sigleton
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

    static ContactoDatabase getDatabase(final Context context) {
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
            new PopulateDbAsync(INSTANCE).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //si queremos realizar alguna tarea cuando se abre
        }
    };

    //tarea en segundo plano que carga los datos
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ContactoDao mDao;

        PopulateDbAsync(ContactoDatabase db) {
            mDao = db.contactoDao();
        }
//código que se ejecuta en segundo plano
        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
           // mDao.deleteAll();
            // añadimos unos datos de ejemplo
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
            Contacto contacto = null;
            try {
                contacto = new Contacto("Pepe", "Lopez", "8888888", formatoDelTexto.parse("12-3-2000"));
                mDao.insert(contacto);
                contacto = new Contacto("Maria", "Perez", "6666666", formatoDelTexto.parse("12-3-2002"));
                mDao.insert(contacto);
                contacto = new Contacto("Juan", "Pomez", "66633333", formatoDelTexto.parse("12-3-2005"));
                mDao.insert(contacto);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
