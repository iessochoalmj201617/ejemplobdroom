package net.iessochoa.joseantoniolopez.ejemplobdroom.model;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * SQLite no tiene campo de tipo fecha. Room, mediante esta clase, nos transformará automáticamente
 * los datos de tipo Date a Long. Para ello tenemos que indicarlo en la clase ContactoDatabase
 */
public class TransformaFechaSQLite {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
