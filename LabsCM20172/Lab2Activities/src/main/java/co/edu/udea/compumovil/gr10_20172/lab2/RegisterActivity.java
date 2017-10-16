package co.edu.udea.compumovil.gr10_20172.lab2;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;
import java.util.Date;

import static co.edu.udea.compumovil.gr10_20172.lab2.DatabaseHelper.USER_TABLE_NAME;
import static co.edu.udea.compumovil.gr10_20172.lab2.Utility.getBytes;

public class RegisterActivity extends AppCompatActivity {
    private EditText birthDate;
    private EditText names,lastNames,phone,postalAddress,email,password,city;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int day,month,year;
    private ImageView imageView;
    DatabaseHelper dbHelper;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        birthDate=(EditText) findViewById(R.id.birth_date);
        names=(EditText) findViewById(R.id.names);
        lastNames=(EditText) findViewById(R.id.last_names);
        phone=(EditText) findViewById(R.id.phone);
        postalAddress=(EditText) findViewById(R.id.postal_address);
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        city=(EditText) findViewById(R.id.city);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        imageView = (ImageView) findViewById(R.id.imageView);

        dbHelper = new DatabaseHelper(this);

    }

    public void pickDate(View view){
        Date startingDate = new Date();
        day=startingDate.getDay();
        month=startingDate.getMonth();
        year=startingDate.getYear();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                birthDate.setText(i+"/"+(i1+1)+"/"+i2);
            }
        }
                ,day,month,year);
        datePickerDialog.show();

    }

    public void onRegisterClick(View view) throws IOException {
        View focusView=null;
        boolean complete=true;
        if (imageView.getDrawable()==null){
            complete=false;
        }
        if (TextUtils.isEmpty(email.getText().toString())){
            email.setError(getString(R.string.error_field_required));
            complete=false;
            focusView=email;
        }
        if (TextUtils.isEmpty(names.getText().toString())){
            names.setError(getString(R.string.error_field_required));
            complete=false;
            focusView=names;
        }
        if (TextUtils.isEmpty(lastNames.getText().toString())){
            lastNames.setError(getString(R.string.error_field_required));
            complete=false;
            focusView=lastNames;
        }
        if (TextUtils.isEmpty(birthDate.getText().toString())){
            birthDate.setError(getString(R.string.error_field_required));
            complete=false;
            focusView=birthDate;
        }
        if (TextUtils.isEmpty(phone.getText().toString())){
            phone.setError(getString(R.string.error_field_required));
            complete=false;
            focusView=phone;
        }
        if (TextUtils.isEmpty(postalAddress.getText().toString())){
            postalAddress.setError(getString(R.string.error_field_required));
            complete=false;
            focusView=postalAddress;
        }
        if (TextUtils.isEmpty(city.getText().toString())){
            city.setError(getString(R.string.error_field_required));
            complete=false;
            focusView=city;
        }
        if (TextUtils.isEmpty(password.getText().toString())){
            password.setError(getString(R.string.error_field_required));
            complete=false;
            focusView=password;
        }
        if (radioGroup.getCheckedRadioButtonId()==-1){
            radioButton.setError(getString(R.string.error_field_required));
            complete=false;
            focusView=radioButton;
        }

        if (complete) {

            SQLiteDatabase dbb = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("IMAGEN",getBytes(((BitmapDrawable)imageView.getDrawable()).getBitmap()));
            values.put("USUARIO", email.getText().toString());
            values.put("NOMBRES", names.getText().toString());
            values.put("APELLIDOS", lastNames.getText().toString());
            values.put("NACIMIENTO", birthDate.getText().toString());
            values.put("TELEFONO", Double.parseDouble(phone.getText().toString()));
            values.put("DIRECCION", postalAddress.getText().toString());
            values.put("EMAIL", email.getText().toString());
            values.put("CIUDAD", city.getText().toString());
            values.put("CONTRASENA", password.getText().toString());

            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            //Log.d("valor",String.valueOf(selectedId));
            values.put("SEXO", radioButton.getText().toString());

            dbb.insertWithOnConflict(USER_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

            this.finish();
        }
        else {
            focusView.requestFocus();
        }
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

            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bitmap != null) {
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,true);
                imageView.setImageBitmap(scaled);
            }

        }
    }

}
