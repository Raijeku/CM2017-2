package co.edu.udea.compumovil.gr10_20172.lab1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Date;

import static co.edu.udea.compumovil.gr10_20172.lab1.R.id.radioGroup;
import static co.edu.udea.compumovil.gr10_20172.lab1.RegisterActivity.bitmap;

public class EditActivity extends AppCompatActivity {

    TextView date;
    TextView names,lastNames,phone,postalAddress,email,password,city,sex;
    int day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        date=(EditText) findViewById(R.id.editText12);
        names=(EditText) findViewById(R.id.editText5);
        lastNames=(EditText) findViewById(R.id.editText6);
        phone=(EditText) findViewById(R.id.editText7);
        postalAddress=(EditText) findViewById(R.id.editText8);
        email=(EditText) findViewById(R.id.editText9);
        password=(EditText) findViewById(R.id.editText10);
        city=(EditText) findViewById(R.id.editText11);

        Context context=this;
        SharedPreferences sharedPref = getSharedPreferences("SharedPref",context.MODE_PRIVATE);
        String namesT=sharedPref.getString("names_r","");
        String lastNamesT=sharedPref.getString("last_names_r","");
        String phoneT=sharedPref.getString("phone_r","");
        String postalAddressT=sharedPref.getString("postal_address_r","");
        String emailT=sharedPref.getString("email_r","");
        String passwordT=sharedPref.getString("password_r","");
        String cityT=sharedPref.getString("city_r","");
        String dateT=sharedPref.getString("date_r","");
        String sexT=sharedPref.getString("sex_r","");

        date.setText(dateT);
        names.setText(namesT);
        lastNames.setText(lastNamesT);
        phone.setText(phoneT);
        postalAddress.setText(postalAddressT);
        email.setText(emailT);
        password.setText(passwordT);
        city.setText(cityT);
        sex.setText(sexT);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
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

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);

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
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }
}
