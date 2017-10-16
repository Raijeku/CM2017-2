package co.edu.udea.compumovil.gr10_20172.lab3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Davquiroga on 23/09/2017.
 */

public class ApartmentDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="apartments.db";
    public static final String APARTMENT_TABLE_NAME="apartment";

    public ApartmentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + APARTMENT_TABLE_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE TEXT, TIPO TEXT, VALOR REAL, AREA REAL, CUARTOS INTEGER, BANOS INTEGER, DESCRIPCION TEXT, UBICACION TEXT ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+APARTMENT_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
