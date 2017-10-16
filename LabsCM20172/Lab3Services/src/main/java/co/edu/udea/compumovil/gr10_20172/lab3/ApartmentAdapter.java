package co.edu.udea.compumovil.gr10_20172.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import static co.edu.udea.compumovil.gr10_20172.lab3.MainActivity.URL_APTCONTAINER_DOWN;
import static co.edu.udea.compumovil.gr10_20172.lab3.MainActivity.URL_CONTAINER_DOWN;

/**
 * Created by Davquiroga on 17/09/2017.
 */

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ApartmentViewHolder> {
    private static OnRecyclerItemClickListener listener;
    private List<Apartment> apartmentList;
    private Context context;

    public ApartmentAdapter(List<Apartment> apartmentList, Context context, OnRecyclerItemClickListener onRecyclerItemClickListener){
        this.apartmentList=apartmentList;
        this.listener=onRecyclerItemClickListener;
        this.context=context;
    }

    public List<Apartment> getApartmentList() {
        return apartmentList;
    }

    public void setApartmentList(List<Apartment> apartmentList) {
        this.apartmentList = apartmentList;
    }

    @Override
    public int getItemCount() {
        return apartmentList.size();
    }

    @Override
    public void onBindViewHolder(ApartmentViewHolder holder, final int position) {
        Apartment apartment = apartmentList.get(position);
        holder.nameView.setText(apartment.getName());
        Glide.with(context)
                .load(URL_APTCONTAINER_DOWN.concat(String.valueOf(apartment.getId())).concat(apartment.getImage()))
                .into(holder.photoView);
        //holder.photoView.setImageBitmap(apartment.getPhotos().get(0));
        holder.propertyTypeView.setText(apartment.getPropertyType());
        holder.areaView.setText(String.valueOf(apartment.getValue()));
        holder.valueView.setText(String.valueOf(apartment.getValue()));
        holder.shortDescriptionView.setText(String.valueOf(apartment.getShortDescription()));
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onItemClicked(position);
                }
            }
        });*/
    }

    @Override
    public ApartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition =
            }
        });*/
        return new ApartmentViewHolder(itemView);
    }

    public interface OnRecyclerItemClickListener{
        void onItemClicked(View v, int position);
    }

    public static class ApartmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView photoView;
        protected TextView nameView;
        protected TextView propertyTypeView;
        protected TextView valueView;
        protected TextView areaView;
        protected TextView shortDescriptionView;

        public ApartmentViewHolder(View itemView) {
            super(itemView);
            photoView = (ImageView) itemView.findViewById(R.id.foto_view);
            nameView=(TextView) itemView.findViewById(R.id.nombre_view);
            propertyTypeView=(TextView) itemView.findViewById(R.id.tipo_inmueble_view);
            valueView=(TextView) itemView.findViewById(R.id.valor_view);
            areaView=(TextView) itemView.findViewById(R.id.area_view);
            shortDescriptionView=(TextView) itemView.findViewById(R.id.descripcion_corta_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("First","clicked");
            listener.onItemClicked(view,this.getLayoutPosition());
        }
    }

}
