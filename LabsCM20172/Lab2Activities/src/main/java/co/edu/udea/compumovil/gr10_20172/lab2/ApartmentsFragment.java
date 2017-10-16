package co.edu.udea.compumovil.gr10_20172.lab2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static co.edu.udea.compumovil.gr10_20172.lab2.DatabaseHelper.APARTMENT_TABLE_NAME;
import static co.edu.udea.compumovil.gr10_20172.lab2.DatabaseHelper.USER_TABLE_NAME;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApartmentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApartmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApartmentsFragment extends Fragment implements ApartmentAdapter.OnRecyclerItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int apartmentIndex=-1;
    private List<Apartment> apartments= new ArrayList<Apartment>();
    private DatabaseHelper dbHelper;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ApartmentAdapter mAdapter;

    public ApartmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApartmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApartmentsFragment newInstance(String param1, String param2) {
        ApartmentsFragment fragment = new ApartmentsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_apartments, container, false);
        rootView.setTag("ApartmentsFragment");

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        dbHelper=new DatabaseHelper(getContext());
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        String consultaSQL = "select * from " + APARTMENT_TABLE_NAME;

        Cursor cursor=db.rawQuery(consultaSQL,null);
        
        if (cursor!=null){
            cursor.moveToFirst();
        }

        Apartment apartment=new Apartment();

        while (cursor.moveToNext()){
            apartment.setPhotos(null);
            apartment.setAmountRooms(cursor.getInt(cursor.getColumnIndex("CUARTOS")));
            apartment.setArea(cursor.getDouble(cursor.getColumnIndex("AREA")));
            apartment.setDescription(cursor.getString(cursor.getColumnIndex("DESCRIPCION")));
            apartment.setLocation(cursor.getString(cursor.getColumnIndex("UBICACION")));
            apartment.setName(cursor.getString(cursor.getColumnIndex("NOMBRE")));
            apartment.setPropertyType(cursor.getString(cursor.getColumnIndex("TIPO")));
            apartment.setValue(cursor.getInt(cursor.getColumnIndex("VALOR")));
            apartment.setBathRooms(cursor.getInt(cursor.getColumnIndex("BANOS")));
            apartment.setPhotos(null);
            apartments.add(apartment);
        }

        mAdapter=new ApartmentAdapter(apartments,getContext(),this);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddClick();
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
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
        //void onFragmentInteraction(Uri uri);
        public void onItemSelected(Apartment apartment);
        public void onAddClick();
    }

    @Override
    public void onItemClicked(View v, int apartmentIndex){
        /*ApartmentAdapter.ApartmentViewHolder viewHolder = new ApartmentAdapter.ApartmentViewHolder(v);
        apartmentIndex=viewHolder.getAdapterPosition();*/
        Log.d("Second","true");
        mListener.onItemSelected(apartments.get(apartmentIndex));
    }
}
