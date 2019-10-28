package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Contacto.class},version=1)
public abstract class ContactoDatabase extends RoomDatabase {
    public abstract ContactoDao contactoDao();
    private static volatile ContactoDatabase INSTANCE;
    static ContactoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactoDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContactoDatabase.class, "contacto_database")
                            .addCallback(sRoomDatabaseCallback)//para ejecutar al crear o al abrir
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(INSTANCE).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
          //  new PopulateDbAsync(INSTANCE).execute();
        }
    };
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ContactoDao mDao;

        PopulateDbAsync(ContactoDatabase db) {
            mDao = db.contactoDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            mDao.deleteAll();

           // Word word = new Word("Hello");
            Contacto contacto=new Contacto("Pepe","Lopez","8888888");
            mDao.insert(contacto);
            contacto = new Contacto("Maria","Perez","6666666");
            mDao.insert(contacto);
            return null;
        }
    }
}
