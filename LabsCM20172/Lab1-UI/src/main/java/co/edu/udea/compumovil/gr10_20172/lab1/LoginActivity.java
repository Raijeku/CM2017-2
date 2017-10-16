package co.edu.udea.compumovil.gr10_20172.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText userText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userText=(EditText) findViewById(R.id.editText);
        passwordText=(EditText) findViewById(R.id.editText2);
    }

    public void openRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void openMain(View view){
        Context context=this;
        SharedPreferences sharedPrefs = getSharedPreferences("SharedPref",context.MODE_PRIVATE);
        String user=sharedPrefs.getString("email_r","");
        String password=sharedPrefs.getString("password_r","");

        if (user.equalsIgnoreCase(userText.getText().toString())&&password.equalsIgnoreCase(passwordText.getText().toString())) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), R.string.incorrect, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }
}
