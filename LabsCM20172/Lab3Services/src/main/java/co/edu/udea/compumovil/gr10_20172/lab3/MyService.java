package co.edu.udea.compumovil.gr10_20172.lab3;

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

import static co.edu.udea.compumovil.gr10_20172.lab3.DatabaseHelper.APARTMENT_TABLE_NAME;
import static co.edu.udea.compumovil.gr10_20172.lab3.MainActivity.URL_APARTAMENTOS;

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

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_APARTAMENTOS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<Apartment> apartmentsList=new ArrayList<>();
                        Gson gson=new Gson();

                        if (!response.toString().equalsIgnoreCase("")) {
                            apartmentsList = Arrays.asList(gson.fromJson(response.toString(), Apartment[].class));
                            /*for (Apartment user : users) {
                                Toast.makeText(getBaseContext(), user.toString(), Toast.LENGTH_LONG).show();
                            }*/
                        }

                        SQLiteDatabase dbb = dbHelper.getWritableDatabase();
                        ContentValues values=new ContentValues();
                        int i=0;
                        for (Apartment apartment :
                                apartmentsList) {

                            //values.put("IMAGEN",apartment.getImage());
                            values.put("NOMBRE", apartment.getName());
                            values.put("TIPO", apartment.getPropertyType());
                            values.put("VALOR", apartment.getValue());
                            values.put("AREA", apartment.getArea());
                            values.put("CUARTOS", apartment.getAmountRooms());
                            values.put("BANOS", apartment.getBathRooms());
                            values.put("DESCRIPCION", apartment.getDescription());
                            values.put("UBICACION", apartment.getLocation());

                            String consultaSQL = "id = " + i;
                            dbb.updateWithOnConflict(APARTMENT_TABLE_NAME, values, consultaSQL, null, SQLiteDatabase.CONFLICT_IGNORE);
                            i++;

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error consultando información", Toast.LENGTH_SHORT).show();
                        //Log.d("nada2",error.getMessage());
                    }
                }
        );
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(jsonObjectRequest);
    }
}
