package co.edu.udea.compumovil.gr10_20172.lab4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static co.edu.udea.compumovil.gr10_20172.lab4.MainActivity.URL_USUARIOS;

public class LoginActivity extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    private static final int RC_GOOGLE_SIGN_IN = 1;
    public static String username;

    AutoCompleteTextView userText;
    EditText passwordText;
    String user, password;
    int id;
    boolean correct, correctUser, correctPassword;
    //DatabaseHelper dbHelper;
    List<User> users;
    View focusView;

    private FirebaseDatabase database;
    private DatabaseReference usersDatabaseReference;
    private FirebaseAuth auth;
    private ChildEventListener childEventListener;
    private FirebaseAuth.AuthStateListener authListener;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseStorage storage;
    private StorageReference usersStorageReference;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("572329938797-fb04b4b6r4g8egiqounvcdhl0o3r347f.apps.googleusercontent.com")
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getBaseContext(),"Connection failed.",Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        username=ANONYMOUS;

        SignInButton signInButton=(SignInButton)findViewById(R.id.google_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        userText=(AutoCompleteTextView) findViewById(R.id.email);
        passwordText=(EditText) findViewById(R.id.password);

        //dbHelper = new DatabaseHelper(this);

        /*if(isConnected()) {
            new HttpAsyncTask().execute(URL_USUARIOS);
        }*/

        database = FirebaseDatabase.getInstance();
        usersDatabaseReference=database.getReference().child("users");
        auth=FirebaseAuth.getInstance();
        users=new ArrayList<User>();

        //auth.signInAnonymously();

        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User addedUser = dataSnapshot.getValue(User.class);
                users.add(addedUser);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersDatabaseReference.addChildEventListener(childEventListener);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();

                if (user != null){

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        storage=FirebaseStorage.getInstance();
        usersStorageReference=storage.getReference().child("user_images");
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            } });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential= FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Boolean found=false;
                    final FirebaseUser firebaseUser = auth.getCurrentUser();
                    for (User user:
                            users) {
                        if (user.getId()==firebaseUser.getUid()){
                            found=true;
                        }
                    }
                    if (!found){
                        StorageReference imageRef=usersStorageReference.child(firebaseUser.getPhotoUrl().getLastPathSegment());
                        imageRef.putFile(firebaseUser.getPhotoUrl()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                @SuppressWarnings("VisibleForTests") Uri downloadUri=taskSnapshot.getDownloadUrl();
                                User user = new User(firebaseUser.getDisplayName(),"","NA"
                                        ,"",Double.parseDouble(firebaseUser.getPhoneNumber()),""
                                        ,firebaseUser.getEmail(),"","",downloadUri.toString(),auth.getCurrentUser().getUid());
                                usersDatabaseReference.push().setValue(user);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authListener);
        attachDatabaseListener();
        /*if(isConnected()) {
            new HttpAsyncTask().execute(URL_USUARIOS);
        }*/
    }

    private void attachDatabaseListener() {
        if (childEventListener==null){
            childEventListener=new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User addedUser = dataSnapshot.getValue(User.class);
                    users.add(addedUser);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            usersDatabaseReference.addChildEventListener(childEventListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener(authListener);
        detachDatabaseReadListener();
        users=new ArrayList<>();
    }

    private void detachDatabaseReadListener() {
        if (childEventListener!=null){
            usersDatabaseReference.removeEventListener(childEventListener);
            childEventListener=null;
        }
    }

    public void onRegisterClick(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onSignInClick(View view){

        //SQLiteDatabase db= dbHelper.getWritableDatabase();
        //String consultaSQL = "select * from " + USER_TABLE_NAME;
        focusView = null;
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

        /*int i=0;
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
        }*/

        intento=intentoPassword&&intentoUser;

        //if (correct==true) {

            if (intento){
                auth.signInWithEmailAndPassword(user,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Context context=getBaseContext();
                            SharedPreferences sharedPref = context.getSharedPreferences("SharedPref",context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("connected_user", user);
                            editor.putString("connected_password", password);
                            editor.putInt("connected_id",id);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            userText.setError(getString(R.string.error_incorrect));
                            focusView=userText;
                            focusView.requestFocus();
                        }
                    }
                });

        //}

        //else{


            }
        //}

    }

    public void onGoogleClick(View view){
        Log.d("onGoogleClic","Si");
        googleSignIn();
    }

    private void googleSignIn(){
        Log.d("onGoogleSignIn","Si");
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_GOOGLE_SIGN_IN){
            Log.d("onGoogleResult","Si");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            Log.d("onGoogleExito","Si");
            GoogleSignInAccount acct=result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("onGoogleRegistra","Si");
        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Boolean found=false;
                    final FirebaseUser firebaseUser = auth.getCurrentUser();
                    for (User user:
                         users) {
                        if (user.getId()==firebaseUser.getUid()){
                            found=true;
                        }
                    }
                    if (!found){
                        StorageReference imageRef=usersStorageReference.child(firebaseUser.getPhotoUrl().getLastPathSegment());
                        imageRef.putFile(firebaseUser.getPhotoUrl()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                @SuppressWarnings("VisibleForTests") Uri downloadUri=taskSnapshot.getDownloadUrl();
                                User user = new User(firebaseUser.getDisplayName(),"","NA"
                                        ,"",Double.parseDouble(firebaseUser.getPhoneNumber()),""
                                        ,firebaseUser.getEmail(),"","",downloadUri.toString(),auth.getCurrentUser().getUid());
                                usersDatabaseReference.push().setValue(user);
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(getBaseContext(),"No se realizó la autenticación.",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    /*public static String GET(String url){
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
    }*/

    /*private static String convertInputStreamToString(InputStream inputStream) throws IOException {
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


                for (User user : users) {
                    Toast.makeText(getBaseContext(), user.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }
    }*/

}
