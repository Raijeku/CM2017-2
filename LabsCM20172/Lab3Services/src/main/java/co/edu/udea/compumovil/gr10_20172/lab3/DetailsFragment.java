package co.edu.udea.compumovil.gr10_20172.lab3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import static co.edu.udea.compumovil.gr10_20172.lab3.MainActivity.URL_APARTAMENTOS;
import static co.edu.udea.compumovil.gr10_20172.lab3.MainActivity.URL_APTCONTAINER_DOWN;
import static co.edu.udea.compumovil.gr10_20172.lab3.MainActivity.URL_CONTAINER_DOWN;
import static co.edu.udea.compumovil.gr10_20172.lab3.MainActivity.URL_USUARIOS;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";

    private ImageView fotoDetail;
    private TextView tipoInmuebleDetail, valorDetail, cuartosDetail, areaDetail, descripcionDetail, ubicacionDetail;
    Apartment apartment;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int mParam3;
    private double mParam4;
    private String mParam5;
    private Bitmap mParam6;
    private String mParam7;
    private double mParam8;

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static DetailsFragment newInstance(String param1, String param2, int param3, double param4, String param5, List<Bitmap> param6, String param7, double param8) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        args.putDouble(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        //args.putByteArray(ARG_PARAM6, param6.bi);
        args.putString(ARG_PARAM7, param7);
        args.putDouble(ARG_PARAM8, param8);
        fragment.setArguments(args);
        return fragment;
    }*/
    public static DetailsFragment newInstance(int param1) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getInt(ARG_PARAM3);
            mParam4 = getArguments().getDouble(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            //mParam6 = getArguments().getString(ARG_PARAM6);
            mParam7 = getArguments().getString(ARG_PARAM7);
            mParam8 = getArguments().getDouble(ARG_PARAM8);
        }

    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam3 = getArguments().getInt(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        fotoDetail = (ImageView) view.findViewById(R.id.foto_detail);
        tipoInmuebleDetail = (TextView) view.findViewById(R.id.tipo_inmueble_detail);
        valorDetail = (TextView) view.findViewById(R.id.valor_detail);
        cuartosDetail = (TextView) view.findViewById(R.id.cuartos_detail);
        areaDetail = (TextView) view.findViewById(R.id.area_detail);
        descripcionDetail = (TextView) view.findViewById(R.id.descripcion_detail);
        ubicacionDetail = (TextView) view.findViewById(R.id.ubicacion_detail);
        ubicacionDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLocationClick(view);
            }
        });

        /*descripcionDetail.setText(mParam1);
        ubicacionDetail.setText(mParam2);
        cuartosDetail.setText(String.valueOf(mParam3));
        areaDetail.setText(String.valueOf(mParam4));
        //fotoDetail.setImageBitmap(mParam6);
        tipoInmuebleDetail.setText(mParam7);
        valorDetail.setText(String.valueOf(mParam8));*/

        obtenerApartamento();

        return view;
    }

    private void obtenerApartamento() {
        final String id_Student = String.valueOf(mParam3);
        if ("".equals(id_Student)) {
            Toast.makeText(getContext(), "Ingrese un Id", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_APARTAMENTOS.concat("/").concat(id_Student), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onprofileobtener",String.valueOf(id_Student));

                        apartment = new Gson().fromJson(response.toString(), Apartment.class);

                        descripcionDetail.setText(apartment.getDescription());
                        ubicacionDetail.setText(apartment.getLocation());
                        cuartosDetail.setText(String.valueOf(apartment.getAmountRooms()));
                        areaDetail.setText(String.valueOf(apartment.getArea()));
                        //fotoDetail.setImageBitmap(mParam6);
                        tipoInmuebleDetail.setText(apartment.getPropertyType());
                        valorDetail.setText(String.valueOf(apartment.getValue()));

                        Glide.with(DetailsFragment.this)
                                .load(URL_APTCONTAINER_DOWN.concat(String.valueOf(apartment.getId())).concat(apartment.getImage()))
                                .into(fotoDetail);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error consultando información", Toast.LENGTH_SHORT).show();
                        //Log.d("nada2",error.getMessage());
                    }
                }
        );
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
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

    public void onLocationClick(View view){
        Uri intentInfo = Uri.parse("geo:0,0?q=".concat(ubicacionDetail.getText().toString()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentInfo);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
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
