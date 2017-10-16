package co.edu.udea.compumovil.gr10_20172.lab2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import static co.edu.udea.compumovil.gr10_20172.lab2.DatabaseHelper.USER_TABLE_NAME;


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
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2, int param3, double param4, String param5, List<Bitmap> param6, String param7, double param8) {
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
    }

    @Override
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

        descripcionDetail.setText(mParam1);
        ubicacionDetail.setText(mParam2);
        cuartosDetail.setText(String.valueOf(mParam3));
        areaDetail.setText(String.valueOf(mParam4));
        //fotoDetail.setImageBitmap(mParam6);
        tipoInmuebleDetail.setText(mParam7);
        valorDetail.setText(String.valueOf(mParam8));

        return view;
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
