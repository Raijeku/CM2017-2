package co.edu.udea.compumovil.gr10_20172.lab4;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static co.edu.udea.compumovil.gr10_20172.lab4.DatabaseHelper.APARTMENT_TABLE_NAME;
import static co.edu.udea.compumovil.gr10_20172.lab4.MainActivity.URL_APARTAMENTOS;

public class MyService extends Service {
    DatabaseHelper dbHelper;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper=new DatabaseHelper(getBaseContext());
        obtenerApartamentos();
    }

    private void obtenerApartamentos() {


    }
}
