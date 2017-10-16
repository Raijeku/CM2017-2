package co.edu.udea.compumovil.gr10_20172.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static co.edu.udea.compumovil.gr10_20172.lab2.DatabaseHelper.APARTMENT_TABLE_NAME;
import static co.edu.udea.compumovil.gr10_20172.lab2.DatabaseHelper.USER_TABLE_NAME;
import static co.edu.udea.compumovil.gr10_20172.lab2.Utility.getBytes;


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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button newButton;
    EditText nameApt, typeApt, priceApt, areaApt, bedroomsApt, bathroomsApt, descriptionApt, locationApt;
    ImageView imageApt;
    DatabaseHelper dbHelper;
    private Bitmap bitmap;

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
        dbHelper=new DatabaseHelper(getContext());
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
                if (complete) {
                    SQLiteDatabase dbb = dbHelper.getWritableDatabase();

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

                    dbb.insertWithOnConflict(APARTMENT_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                    onButtonPressed();
                }
                else {
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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);

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
                imageApt.setImageBitmap(scaled);
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
        void onNewApartmentInteraction();
    }
}
