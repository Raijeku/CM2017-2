package co.edu.udea.compumovil.gr10_20172.lab1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    EditText date;
    EditText names,lastNames,phone,postalAddress,email,password,city;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int day,month,year;
    static public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        date=(EditText) findViewById(R.id.editText12);
        names=(EditText) findViewById(R.id.editText5);
        lastNames=(EditText) findViewById(R.id.editText6);
        phone=(EditText) findViewById(R.id.editText7);
        postalAddress=(EditText) findViewById(R.id.editText8);
        email=(EditText) findViewById(R.id.editText9);
        password=(EditText) findViewById(R.id.editText10);
        city=(EditText) findViewById(R.id.editText11);

        Context context=this;
        SharedPreferences sharedPrefs = getSharedPreferences("SharedPref",context.MODE_PRIVATE);

    }

    public void pickDate(View view){
        Date startingDate = new Date();
        day=startingDate.getDay();
        month=startingDate.getMonth();
        year=startingDate.getYear();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date.setText(i+"/"+i1+"/"+i2);
            }
        }
        ,day,month,year);
        datePickerDialog.show();

    }

    public void register(View view){

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        Context context=this;
        SharedPreferences sharedPref = this.getSharedPreferences("SharedPref",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("names_r", names.getText().toString());
        editor.putString("last_names_r", lastNames.getText().toString());
        editor.putString("phone_r", phone.getText().toString());
        editor.putString("postal_address_r", postalAddress.getText().toString());
        editor.putString("email_r", email.getText().toString());
        editor.putString("password_r", password.getText().toString());
        editor.putString("city_r", city.getText().toString());
        editor.putString("date_r", date.getText().toString());
        editor.putString("sex_r",radioButton.getText().toString());
        editor.commit();

        this.finish();

    }

    public void selectImage(View view){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            /*String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();*/

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }

        }

    }


}
