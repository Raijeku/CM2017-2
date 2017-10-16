package co.edu.udea.compumovil.gr10_20172.lab3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static co.edu.udea.compumovil.gr10_20172.lab3.DatabaseHelper.USER_TABLE_NAME;
import static co.edu.udea.compumovil.gr10_20172.lab3.MainActivity.URL_USUARIOS;

public class LoginActivity extends AppCompatActivity {
    AutoCompleteTextView userText;
    EditText passwordText;
    String user, password;
    int id;
    boolean correct, correctUser, correctPassword;
    //DatabaseHelper dbHelper;
    List<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userText=(AutoCompleteTextView) findViewById(R.id.email);
        passwordText=(EditText) findViewById(R.id.password);

        //dbHelper = new DatabaseHelper(this);

        if(isConnected()) {
            new HttpAsyncTask().execute(URL_USUARIOS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isConnected()) {
            new HttpAsyncTask().execute(URL_USUARIOS);
        }
    }

    public void onRegisterClick(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onSignInClick(View view){

        //SQLiteDatabase db= dbHelper.getWritableDatabase();
        //String consultaSQL = "select * from " + USER_TABLE_NAME;
        View focusView = null;
        boolean intentoPassword=false, intentoUser=false, intento=false;

        //Cursor cursor=db.rawQuery(consultaSQL,null);
        correct=false;
        user=userText.getText().toString();
        password=passwordText.getText().toString();
        userText.setError(null);
        passwordText.setError(null);

        if (TextUtils.isEmpty(password)){
            passwordText.setError(getString(R.string.error_field_required));
            focusView = passwordText;
        }
        else {
            intentoPassword=true;
        }

        if (TextUtils.isEmpty(user)) {
            userText.setError(getString(R.string.error_field_required));
            focusView = userText;
        }
        else{
            intentoUser=true;
        }

        /*if (cursor!=null){
            cursor.moveToFirst();
        }

        while (cursor.moveToNext()){
            correctUser=false;
            correctPassword=false;
            if (user.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("EMAIL")))){
                correctUser=true;
            }

            if (password.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("CONTRASENA")))){
                correctPassword=true;
            }
            if (correctUser&&correctPassword){
                correct=true;
                break;
            }
        }*/

        int i=0;
        while (i<users.size()){
            correctUser=false;
            correctPassword=false;
            if (user.equalsIgnoreCase(users.get(i).getEmail())){
                correctUser=true;
            }
            if (password.equalsIgnoreCase(users.get(i).getPassword())){
                correctPassword=true;
            }
            if (correctPassword&&correctUser){
                correct=true;
                id=users.get(i).getId();
                Log.d("login",String.valueOf(id));
                break;
            }
            //Log.d("usuario"+i,user+password+users.get(i).getEmail()+users.get(i).getPassword());
            i++;
        }

        intento=intentoPassword&&intentoUser;

        if (correct==true) {

            Context context=this;
            SharedPreferences sharedPref = this.getSharedPreferences("SharedPref",context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("connected_user", user);
            editor.putString("connected_password", password);
            editor.putInt("connected_id",id);
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        else{
            if (intento){
                userText.setError(getString(R.string.error_incorrect));
                focusView=userText;
            }
            focusView.requestFocus();
        }

    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            //etResponse.setText(result);
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

            users=new ArrayList<>();
            Gson gson=new Gson();

            if (!result.equalsIgnoreCase("")) {
                users = Arrays.asList(gson.fromJson(result, User[].class));


                /*for (User user : users) {
                    Toast.makeText(getBaseContext(), user.toString(), Toast.LENGTH_LONG).show();
                }*/

            }

        }
    }

}
