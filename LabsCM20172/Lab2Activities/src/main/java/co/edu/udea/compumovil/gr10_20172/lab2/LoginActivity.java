package co.edu.udea.compumovil.gr10_20172.lab2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import static co.edu.udea.compumovil.gr10_20172.lab2.DatabaseHelper.USER_TABLE_NAME;

public class LoginActivity extends AppCompatActivity {
    AutoCompleteTextView userText;
    EditText passwordText;
    String user, password;
    boolean correct, correctUser, correctPassword;
    DatabaseHelper dbHelper;
    List<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userText=(AutoCompleteTextView) findViewById(R.id.email);
        passwordText=(EditText) findViewById(R.id.password);

        dbHelper = new DatabaseHelper(this);
    }

    public void onRegisterClick(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onSignInClick(View view){

        SQLiteDatabase db= dbHelper.getWritableDatabase();
        String consultaSQL = "select * from " + USER_TABLE_NAME;
        View focusView = null;
        boolean intentoPassword=false, intentoUser=false, intento=false;

        Cursor cursor=db.rawQuery(consultaSQL,null);
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

        if (cursor!=null){
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
        }

        intento=intentoPassword&&intentoUser;

        if (correct==true) {

            Context context=this;
            SharedPreferences sharedPref = this.getSharedPreferences("SharedPref",context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("connected_user", user);
            editor.putString("connected_password", password);
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

}
