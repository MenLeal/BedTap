package com.example.bedtab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bedtab.Place;
import com.example.bedtab.R;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {
    private ArrayList<Place> mPlaceList;
    private Context context;
    private Place mPlace;
    public class PlacesViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgplace;
        public TextView nomplace;
        public TextView adress;
        public ImageButton location;
        public ImageButton numPlace;

        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            //Recuperamos de la vista las variables

            imgplace=itemView.findViewById(R.id.imgplace);
            nomplace=itemView.findViewById(R.id.nomplace);
            adress=itemView.findViewById(R.id.adress);
            location=itemView.findViewById(R.id.location);
            numPlace=itemView.findViewById(R.id.numPlace);
        }
    }
    public PlacesAdapter(Context c,ArrayList<Place> placelist){
        mPlaceList= placelist;
        context=c;
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item,parent,false);
        PlacesViewHolder ofvh=new PlacesViewHolder(v);
        return ofvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {
        mPlace=mPlaceList.get(position);
        holder.nomplace.setText(mPlace.getNomplace());
        holder.adress.setText(mPlace.getAdress());
        holder.imgplace.setImageResource(mPlace.getImgplace());
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }


}
