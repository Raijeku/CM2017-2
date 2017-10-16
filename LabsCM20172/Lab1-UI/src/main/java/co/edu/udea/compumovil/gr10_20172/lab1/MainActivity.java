package co.edu.udea.compumovil.gr10_20172.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static co.edu.udea.compumovil.gr10_20172.lab1.RegisterActivity.bitmap;

public class MainActivity extends AppCompatActivity {

    TextView date;
    TextView names,lastNames,phone,postalAddress,email,password,city,sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        date=(TextView) findViewById(R.id.textView15);
        names=(TextView) findViewById(R.id.textView8);
        lastNames=(TextView) findViewById(R.id.textView9);
        phone=(TextView) findViewById(R.id.textView10);
        postalAddress=(TextView) findViewById(R.id.textView11);
        email=(TextView) findViewById(R.id.textView12);
        password=(TextView) findViewById(R.id.textView13);
        city=(TextView) findViewById(R.id.textView14);
        sex=(TextView) findViewById(R.id.textView16);


        SharedPreferences sharedPref = this.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        String namesT=sharedPref.getString("names_r","");
        String lastNamesT=sharedPref.getString("last_names_r","");
        String phoneT=sharedPref.getString("phone_r","");
        String postalAddressT=sharedPref.getString("postal_address_r","");
        String emailT=sharedPref.getString("email_r","");
        String passwordT=sharedPref.getString("password_r","");
        String cityT=sharedPref.getString("city_r","");
        String dateT=sharedPref.getString("date_r","");
        String sexT=sharedPref.getString("sex_r","");

        names.setText(names.getText().toString()+namesT);
        lastNames.setText(lastNames.getText().toString()+lastNamesT);
        phone.setText(phone.getText().toString()+phoneT);
        postalAddress.setText(postalAddress.getText().toString()+postalAddressT);
        email.setText(email.getText().toString()+emailT);
        password.setText(password.getText().toString()+passwordT);
        city.setText(city.getText().toString()+cityT);
        date.setText(date.getText().toString()+dateT);
        sex.setText(sex.getText().toString()+sexT);

        /*ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                Intent intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                this.finish();
                break;
        }
        return true;
    }

}
