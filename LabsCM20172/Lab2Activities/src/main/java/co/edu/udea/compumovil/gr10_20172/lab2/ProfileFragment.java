package co.edu.udea.compumovil.gr10_20172.lab2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static co.edu.udea.compumovil.gr10_20172.lab2.DatabaseHelper.USER_TABLE_NAME;
import static co.edu.udea.compumovil.gr10_20172.lab2.Utility.getImage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    DatabaseHelper dbHelper;

    TextView namesView,lastNamesView,birthDateView,sexView,emailView,passwordView,phoneView,postalAddressView,cityView;
    ImageView photoView;
    User user;
    boolean correct;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        dbHelper= new DatabaseHelper(getContext());
        SQLiteDatabase db= dbHelper.getWritableDatabase();
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
                user.setImage(cursor.getBlob(cursor.getColumnIndex("IMAGEN")));
                user.setNames(cursor.getString(cursor.getColumnIndex("NOMBRES")));
                user.setLastNames(cursor.getString(cursor.getColumnIndex("APELLIDOS")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
                user.setBirthDate(cursor.getString(cursor.getColumnIndex("NACIMIENTO")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("CONTRASENA")));
                user.setCity(cursor.getString(cursor.getColumnIndex("CIUDAD")));
                user.setPostalAddress(cursor.getString(cursor.getColumnIndex("DIRECCION")));
                user.setPhone(Double.parseDouble(cursor.getString(cursor.getColumnIndex("TELEFONO"))));
                user.setSex(cursor.getString(cursor.getColumnIndex("SEXO")));
                break;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        photoView = (ImageView) v.findViewById(R.id.image_p);
        namesView = (TextView) v.findViewById(R.id.names_p);
        lastNamesView = (TextView) v.findViewById(R.id.last_names_p);
        birthDateView = (TextView) v.findViewById(R.id.birth_date_p);
        emailView = (TextView) v.findViewById(R.id.email_p);
        passwordView = (TextView) v.findViewById(R.id.password_p);
        postalAddressView = (TextView) v.findViewById(R.id.postal_address_p);
        phoneView = (TextView) v.findViewById(R.id.phone_p);
        cityView = (TextView) v.findViewById(R.id.city_p);
        sexView = (TextView) v.findViewById(R.id.sex_p);


        //if (correct){
            photoView.setImageBitmap(getImage(user.getImage()));
            namesView.setText(user.getNames());
            lastNamesView.setText(user.getLastNames());
            birthDateView.setText(user.getBirthDate());
            emailView.setText(user.getEmail());
            passwordView.setText(user.getPassword());
            postalAddressView.setText(user.getPostalAddress());
            phoneView.setText(String.valueOf(user.getPhone()));
            cityView.setText(user.getCity());
            sexView.setText(user.getSex());
        //}

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
