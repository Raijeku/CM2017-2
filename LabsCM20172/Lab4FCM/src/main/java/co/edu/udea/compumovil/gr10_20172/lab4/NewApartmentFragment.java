package co.edu.udea.compumovil.gr10_20172.lab4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static android.app.Activity.RESULT_OK;
import static co.edu.udea.compumovil.gr10_20172.lab4.MainActivity.URL_APARTAMENTOS;
import static co.edu.udea.compumovil.gr10_20172.lab4.MainActivity.URL_APTCONTAINER_UP;
import static co.edu.udea.compumovil.gr10_20172.lab4.Notification.sendNotification;
import static co.edu.udea.compumovil.gr10_20172.lab4.Utility.getBytes;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewApartmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewApartmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewApartmentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RC_PHOTO_PICKER =  2;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button newButton;
    EditText nameApt, typeApt, priceApt, areaApt, bedroomsApt, bathroomsApt, descriptionApt, locationApt;
    ImageView imageApt;
    DatabaseHelper dbHelper;
    private Bitmap bitmap;
    private byte[] imagenSeleccionada;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference apartmentsDatabaseReference;
    private StorageReference apartmentsStorageReference;
    private DatabaseReference usersDatabaseReference;
    private ChildEventListener usersChildEventListener;
    private Uri selectedImage;
    private boolean imageSelected;
    private List<User> users;

    public NewApartmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewApartmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewApartmentFragment newInstance(String param1, String param2) {
        NewApartmentFragment fragment = new NewApartmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        users=new ArrayList<>();
        dbHelper=new DatabaseHelper(getContext());
        database = FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        apartmentsDatabaseReference=database.getReference().child("apartments");
        apartmentsStorageReference = storage.getInstance().getReference().child("apartment_images");
        usersDatabaseReference=database.getReference().child("users");
        usersChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                users.add(user);
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
        usersDatabaseReference.addChildEventListener(usersChildEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_apartment, container, false);

        nameApt=(EditText) view.findViewById(R.id.name_apt);
        typeApt=(EditText) view.findViewById(R.id.type_apt);
        priceApt=(EditText) view.findViewById(R.id.price_apt);
        areaApt=(EditText) view.findViewById(R.id.area_apt);
        bedroomsApt=(EditText) view.findViewById(R.id.bedrooms_apt);
        bathroomsApt=(EditText) view.findViewById(R.id.bathrooms_apt);
        descriptionApt=(EditText) view.findViewById(R.id.description_apt);
        locationApt=(EditText) view.findViewById(R.id.location_apt);
        imageApt=(ImageView) view.findViewById(R.id.imageView_apt);
        imageApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }
        });

        newButton = (Button) view.findViewById(R.id.new_apt);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView=null;
                boolean complete=true;
                if (TextUtils.isEmpty(nameApt.getText().toString())){
                    nameApt.setError(getString(R.string.error_field_required));
                    complete=false;
                    focusView=nameApt;
                }
                if (TextUtils.isEmpty(typeApt.getText().toString())){
                    typeApt.setError(getString(R.string.error_field_required));
                    complete=false;
                    focusView=typeApt;
                }
                if (TextUtils.isEmpty(priceApt.getText().toString())){
                    priceApt.setError(getString(R.string.error_field_required));
                    complete=false;
                    focusView=priceApt;
                }
                if (TextUtils.isEmpty(areaApt.getText().toString())){
                    areaApt.setError(getString(R.string.error_field_required));
                    complete=false;
                    focusView=areaApt;
                }
                if (TextUtils.isEmpty(bedroomsApt.getText().toString())){
                    bedroomsApt.setError(getString(R.string.error_field_required));
                    complete=false;
                    focusView=bedroomsApt;
                }
                if (TextUtils.isEmpty(bathroomsApt.getText().toString())){
                    bathroomsApt.setError(getString(R.string.error_field_required));
                    complete=false;
                    focusView=bathroomsApt;
                }
                if (TextUtils.isEmpty(descriptionApt.getText().toString())){
                    descriptionApt.setError(getString(R.string.error_field_required));
                    complete=false;
                    focusView=descriptionApt;
                }
                if (TextUtils.isEmpty(locationApt.getText().toString())){
                    locationApt.setError(getString(R.string.error_field_required));
                    complete=false;
                    focusView=locationApt;
                }
                if (selectedImage==null){
                    complete=false;
                    Toast.makeText(getContext(), R.string.image_not_selected, Toast.LENGTH_SHORT).show();
                }
                if (complete) {
                    /*SQLiteDatabase dbb = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    try {
                        values.put("IMAGEN",getBytes(((BitmapDrawable)imageApt.getDrawable()).getBitmap()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    values.put("NOMBRE", nameApt.getText().toString());
                    values.put("TIPO", typeApt.getText().toString());
                    values.put("VALOR", Float.parseFloat(priceApt.getText().toString()));
                    values.put("AREA", Float.parseFloat(areaApt.getText().toString()));
                    values.put("CUARTOS", Integer.parseInt(bedroomsApt.getText().toString()));
                    values.put("BANOS", Integer.parseInt(bathroomsApt.getText().toString()));
                    values.put("DESCRIPCION", descriptionApt.getText().toString());
                    values.put("UBICACION", locationApt.getText().toString());

                    dbb.insertWithOnConflict(APARTMENT_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);*/
                    agregarApartamento();

                    onButtonPressed();
                }
                else {
                    Log.d("focus",focusView.toString());
                    focusView.requestFocus();
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onNewApartmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void selectImage(View view){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);

        startActivityForResult(i, RC_PHOTO_PICKER);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            /*if (selectedImage!=null){

            }*/

            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
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
                imageApt.setImageBitmap(scaled);
            }

        }
    }

    /*private void sendImage(String url, final String nameImage) {
        StorageReference imageRef=apartmentsStorageReference.child(selectedImage.getLastPathSegment());
        imageRef.putFile(selectedImage);*/
        /*VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                //lblNombre.setText(resultResponse);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error subiendo la imagen", Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(multipartRequest);*/
    //}

    private void agregarApartamento() {
        StorageReference imageRef=apartmentsStorageReference.child(selectedImage.getLastPathSegment());
        imageRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl=taskSnapshot.getDownloadUrl();
                Apartment apartment = new Apartment(downloadUrl.toString(),0,typeApt.getText().toString(),Double.parseDouble(priceApt.getText().toString())
                        ,Integer.parseInt(bedroomsApt.getText().toString()),Double.parseDouble(areaApt.getText().toString()),descriptionApt.getText().toString(),
                        locationApt.getText().toString(),nameApt.getText().toString(),descriptionApt.getText().toString(),Integer.parseInt(bathroomsApt.getText().toString()));
                apartmentsDatabaseReference.push().setValue(apartment);
            }
        });
        for (User user:
             users) {
            sendNotification(
                    user.getId(), /*who the notification is meant for*/
                    "Ingresa a la aplicación para ver el nuevo apartamento ", /*Message to be displayed on the notification*/
                    "Nuevo apartamento", /*Message title*/
                    "chat_view" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/
            );
        }


        //sendImage();
        /*final StringRequest postRequest = new StringRequest(Request.Method.POST, URL_APARTAMENTOS,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //lblApellido.setText(response);

                            Una vez agregado el Estudiante con éxito procedemos a cargar la imagen

                        //Suponiendo que salga todo bien

                        Apartment apartment = new Gson().fromJson(response, Apartment.class);

                        String nombre = apartment.getId()+apartment.getImage(); //Nommbre de la imagen
                        sendImage(URL_APTCONTAINER_UP,nombre); //Subimos la imagen

                        onButtonPressed();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al crear el Apartamento", Toast.LENGTH_SHORT).show();
                        //Log.d("nada",error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String>  params = new HashMap<String, String>();
                params.put("Nombre", nameApt.getText().toString());
                params.put("Tipo", typeApt.getText().toString());
                params.put("Valor", priceApt.getText().toString());
                params.put("Area", areaApt.getText().toString());
                params.put("Cuartos", bedroomsApt.getText().toString());
                params.put("Banos", bathroomsApt.getText().toString());
                params.put("Descripcion", descriptionApt.getText().toString());
                params.put("Ubicacion", locationApt.getText().toString());
                params.put("Descripcioncorta",descriptionApt.getText().toString());
                params.put("Imagen", "img.jpg"); //Siguiendo el formato que de definió, también puede ser "img.png"

                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(postRequest);*/
    }

    private void attachDatabaseListener() {
        if (usersChildEventListener==null){
            usersChildEventListener=new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);
                    users.add(user);
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
            usersDatabaseReference.addChildEventListener(usersChildEventListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseListener();
    }

    private void detachDatabaseReadListener() {
        if (usersChildEventListener!=null){
            usersDatabaseReference.removeEventListener(usersChildEventListener);
            usersChildEventListener=null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        users=new ArrayList<>();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNewApartmentInteraction();
    }
}
