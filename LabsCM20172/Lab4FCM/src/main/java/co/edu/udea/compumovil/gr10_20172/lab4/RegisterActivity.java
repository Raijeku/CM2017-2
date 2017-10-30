package co.edu.udea.compumovil.gr10_20172.lab4;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static co.edu.udea.compumovil.gr10_20172.lab4.MainActivity.URL_CONTAINER_UP;
import static co.edu.udea.compumovil.gr10_20172.lab4.MainActivity.URL_USUARIOS;
import static co.edu.udea.compumovil.gr10_20172.lab4.Utility.getBytes;

public class RegisterActivity extends AppCompatActivity {
    private EditText birthDate;
    private EditText names,lastNames,phone,postalAddress,email,password,city;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int day,month,year;
    private ImageView imageView;
    DatabaseHelper dbHelper;
    private Bitmap bitmap;
    private byte[] imagenSeleccionada;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference usersDatabaseReference;
    private StorageReference usersStorageReference;
    private Uri selectedImage;

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
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        usersDatabaseReference=database.getReference().child("users");
        storage=FirebaseStorage.getInstance();
        usersStorageReference=storage.getReference().child("user_images");
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();

                if (user != null){
                    Context context=getBaseContext();
                    SharedPreferences sharedPref = context.getSharedPreferences("SharedPref",context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("connected_user", email.getText().toString());
                    editor.putString("connected_password", password.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
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

            /*SQLiteDatabase dbb = dbHelper.getWritableDatabase();

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

            dbb.insertWithOnConflict(USER_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);*/



            /*Map<String, String> comment = new HashMap<String, String>();
            comment.put("subject", "Using the GSON library");
            comment.put("message", "Using libraries is convenient.");
            String json = new GsonBuilder().create().toJson(comment, Map.class);*/

            //String json=new GsonBuilder().create().toJson(user,User.class);
            //makeRequest("http://192.168.1.56:3000/api/Usuarios", json);

            agregarUsuario();

        }
        else {
            focusView.requestFocus();
        }
    }

    public void selectImage(View view){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 1);
    }

    private void sendImage(String url, final String nameImage) {

        /*VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                //lblNombre.setText(resultResponse);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error subiendo la imagen", Toast.LENGTH_SHORT).show();
                //Log.d("nada3", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("image", new DataPart(nameImage, imagenSeleccionada, "image/jpeg"));
                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("SessionId", mSessionId);
                return headers;
            }
        };
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);*/
    }

    private void agregarUsuario() {

        auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    StorageReference imageRef=usersStorageReference.child(selectedImage.getLastPathSegment());
                    imageRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadUri=taskSnapshot.getDownloadUrl();
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            radioButton = (RadioButton) findViewById(selectedId);
                            User user = new User(names.getText().toString(),lastNames.getText().toString(),(String)radioButton.getText()
                                    ,birthDate.getText().toString(),Double.parseDouble(phone.getText().toString()),postalAddress.getText().toString()
                                    ,email.getText().toString(),password.getText().toString(),city.getText().toString(),downloadUri.toString(),auth.getCurrentUser().getUid());
                            usersDatabaseReference.push().setValue(user);
                        }
                    });
                }
            }
        });

        /*final StringRequest postRequest = new StringRequest(Request.Method.POST, URL_USUARIOS,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //lblApellido.setText(response);

                            Una vez agregado el Estudiante con éxito procedemos a cargar la imagen

                        //Suponiendo que salga todo bien

                        User user = new Gson().fromJson(response, User.class);
                        Log.d("imagen",response);

                        String nombre = user.getId()+user.getImage(); //Nommbre de la imagen
                        Log.d("imagen",nombre);
                        sendImage(URL_CONTAINER_UP,nombre); //Subimos la imagen

                        finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error al crear el Usuario", Toast.LENGTH_SHORT).show();
                        //Log.d("nada",error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Nombres", names.getText().toString());
                params.put("Apellidos", lastNames.getText().toString());
                params.put("Nombreusuario", email.getText().toString());
                params.put("Sexo", radioButton.getText().toString());
                params.put("Nacimiento", birthDate.getText().toString());
                params.put("Telefono", phone.getText().toString());
                params.put("Direccion", postalAddress.getText().toString());
                params.put("Email", email.getText().toString());
                params.put("Contrasena", password.getText().toString());
                params.put("Ciudad", city.getText().toString());
                params.put("Imagen", "img.jpg"); //Siguiendo el formato que de definió, también puede ser "img.png"

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(postRequest);*/
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();

            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bitmap != null) {
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,true);
                try {
                    imagenSeleccionada=getBytes(scaled);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageView.setImageBitmap(scaled);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener(authListener);
    }
}
