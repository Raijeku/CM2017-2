package co.edu.udea.compumovil.gr10_20172.lab3;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import static co.edu.udea.compumovil.gr10_20172.lab3.DatabaseHelper.APARTMENT_TABLE_NAME;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean keepSigned = sharedPref.getBoolean("keep_signed_in", false);
        String user=sharedPref.getString("connected_user", "");

        /*String defaultValue = getResources().getString(R.string.burned_default);
        String burned = sharedPref.getString(getString(R.string.burned), defaultValue);

        if (burned.equalsIgnoreCase("false")){
            DatabaseHelper dbHelper= new DatabaseHelper(this);
            SQLiteDatabase dbb= dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("NOMBRE", "Nombre");
            values.put("TIPO", "Tipo");
            values.put("VALOR", 1000000.5);
            values.put("AREA", 70.3);
            values.put("CUARTOS", 4);
            values.put("BANOS", 2);
            values.put("DESCRIPCION", "Es un apartamento muy grande, ésta descripción es una prueba");
            values.put("UBICACION", "Universidad de Antioquia");

            dbb.insertWithOnConflict(APARTMENT_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            dbb.insertWithOnConflict(APARTMENT_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            dbb.insertWithOnConflict(APARTMENT_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            dbb.insertWithOnConflict(APARTMENT_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            dbb.insertWithOnConflict(APARTMENT_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);


        }*/

        if (!keepSigned){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if(user.equalsIgnoreCase("")){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        } else{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
