package co.edu.udea.compumovil.gr10_20172.lab4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Davquiroga on 7/09/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="apartments.db";
    public static final String APARTMENT_TABLE_NAME="apartment";
    public static final String USER_TABLE_NAME="user";
    public static final String RESOURCE_TABLE_NAME="resource";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + APARTMENT_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE TEXT, TIPO TEXT, VALOR REAL, AREA REAL, CUARTOS INTEGER, BANOS INTEGER, DESCRIPCION TEXT, UBICACION TEXT) ");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ USER_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, IMAGEN BLOB, USUARIO TEXT, NOMBRES TEXT, APELLIDOS TEXT, SEXO TEXT, NACIMIENTO TEXT, TELEFONO REAL, DIRECCION TEXT, EMAIL TEXT, CIUDAD TEXT, CONTRASENA TEXT )");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + RESOURCE_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE TEXT, IMAGEN BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+APARTMENT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+USER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+RESOURCE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
