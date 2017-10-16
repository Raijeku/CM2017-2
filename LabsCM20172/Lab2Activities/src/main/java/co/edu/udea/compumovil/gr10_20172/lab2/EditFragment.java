package co.edu.udea.compumovil.gr10_20172.lab2;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static co.edu.udea.compumovil.gr10_20172.lab2.DatabaseHelper.USER_TABLE_NAME;
import static co.edu.udea.compumovil.gr10_20172.lab2.Utility.getBytes;
import static co.edu.udea.compumovil.gr10_20172.lab2.Utility.getImage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText birthDate;
    private EditText names,lastNames,phone,postalAddress,email,password,city;
    private ImageView image;
    Button edit;
    RadioGroup radioGroupEdit;
    private RadioButton radioButton, radioButton1, radioButton2;
    private int day,month,year;
    private DatabaseHelper dbHelper;
    private Bitmap bitmap;
    boolean correct;
    User user;
    int deleteId;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
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

        user=new User();
        this.dbHelper= new DatabaseHelper(getContext());
        SQLiteDatabase db= this.dbHelper.getWritableDatabase();
        String consultaSQL = "select * from " + USER_TABLE_NAME;

        Cursor cursor=db.rawQuery(consultaSQL,null);
        correct=false;
        boolean correctUser,correctPassword;

        if (cursor!=null){
            cursor.moveToFirst();
        }

        while (cursor.moveToNext()){
            correctUser=false;
            correctPassword=false;
            if (mParam1.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("EMAIL")))){
                correctUser=true;
            }

            if (mParam2.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("CONTRASENA")))){
                correctPassword=true;
            }
            if (correctUser&&correctPassword){
                correct=true;
                user.setNames(cursor.getString(cursor.getColumnIndex("NOMBRES")));
                user.setLastNames(cursor.getString(cursor.getColumnIndex("APELLIDOS")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
                user.setBirthDate(cursor.getString(cursor.getColumnIndex("NACIMIENTO")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("CONTRASENA")));
                user.setCity(cursor.getString(cursor.getColumnIndex("CIUDAD")));
                user.setPostalAddress(cursor.getString(cursor.getColumnIndex("DIRECCION")));
                user.setPhone(Double.parseDouble(cursor.getString(cursor.getColumnIndex("TELEFONO"))));
                user.setSex(cursor.getString(cursor.getColumnIndex("SEXO")));
                user.setImage(cursor.getBlob(cursor.getColumnIndex("IMAGEN")));
                deleteId=cursor.getInt(cursor.getColumnIndex("ID"));
                break;
            }
        }
    }
@Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view1 = inflater.inflate(R.layout.fragment_edit, container, false);

        birthDate=(EditText) view1.findViewById(R.id.birth_date_edit);
        names=(EditText) view1.findViewById(R.id.names_edit);
        lastNames=(EditText) view1.findViewById(R.id.last_names_edit);
        phone=(EditText) view1.findViewById(R.id.phone_edit);
        postalAddress=(EditText) view1.findViewById(R.id.postal_address_edit);
        email=(EditText) view1.findViewById(R.id.email_edit);
        password=(EditText) view1.findViewById(R.id.password_edit);
        city=(EditText) view1.findViewById(R.id.city_edit);
        edit=(Button) view1.findViewById(R.id.register_edit);
        image=(ImageView) view1.findViewById(R.id.imageView_edit);

            radioGroupEdit = (RadioGroup) view1.findViewById(R.id.radioGroup_edit);
        radioButton = (RadioButton) view1.findViewById(R.id.radioButton_edit);
        radioButton1 = (RadioButton) view1.findViewById(R.id.radioButton1_edit);

        names.setText(user.getNames());
        lastNames.setText(user.getLastNames());
        email.setText(user.getEmail());
        birthDate.setText(user.getBirthDate());
        password.setText(user.getPassword());
        city.setText(user.getCity());
        postalAddress.setText(user.getPostalAddress());
        phone.setText(String.valueOf(user.getPhone()));
        image.setImageBitmap(getImage(user.getImage()));

        if (user.getSex()=="@string/female"){
            radioButton.setChecked(true);
        } else{
            radioButton1.setChecked(true);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageViewClick(view);
            }
        });

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Date startingDate = new Date();
                day=startingDate.getDay();
                month=startingDate.getMonth();
                year=startingDate.getYear();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        birthDate.setText(i+"/"+(i1+1)+"/"+i2);
                    }
                }
                        ,day,month,year);
                datePickerDialog.show();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View view) {
                View focusView=null;
                boolean complete=true;
                if (image.getDrawable()==null){
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
                if (!(radioButton.isChecked()||radioButton1.isChecked())){
                    radioButton1.setError(getString(R.string.error_field_required));
                    complete=false;
                    focusView=radioButton;
                }

                if (complete) {

                    SQLiteDatabase dbb = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    try {
                        values.put("IMAGEN", getBytes(((BitmapDrawable)image.getDrawable()).getBitmap()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    values.put("USUARIO", email.getText().toString());
                    values.put("NOMBRES", names.getText().toString());
                    values.put("APELLIDOS", lastNames.getText().toString());
                    values.put("NACIMIENTO", birthDate.getText().toString());
                    values.put("TELEFONO", Double.parseDouble(phone.getText().toString()));
                    values.put("DIRECCION", postalAddress.getText().toString());
                    values.put("EMAIL", email.getText().toString());
                    values.put("CIUDAD", city.getText().toString());
                    values.put("CONTRASENA", password.getText().toString());

                    if (radioButton.isChecked()) {
                        values.put("SEXO", radioButton.getText().toString());
                    } else if (radioButton1.isChecked()) {
                        values.put("SEXO", radioButton1.getText().toString());
                    }

                    String consultaSQL = "id = " + deleteId;
                    dbb.updateWithOnConflict(USER_TABLE_NAME, values, consultaSQL, null, SQLiteDatabase.CONFLICT_IGNORE);

                    /*String consultaSQL = "id = "+deleteId;
                    dbb.delete(USER_TABLE_NAME,consultaSQL,null);*/

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("connected_user", email.getText().toString());
                    editor.putString("connected_password", password.getText().toString());
                    editor.commit();

                    onButtonPressed();
                }else {
                    focusView.requestFocus();
                }
            }
        });

        return view1;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onEditInteraction();
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

    public void onImageViewClick(View view){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bitmap != null) {
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,true);
                image.setImageBitmap(scaled);
            }

        }
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
        void onEditInteraction();
    }
}
