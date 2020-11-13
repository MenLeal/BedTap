package com.example.bedtab.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bedtab.Offer;
import com.example.bedtab.R;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    private ArrayList<Offer> mOfferList;
    private Context context;
    private Offer mOffer;
    public class OfferViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgproducto;
        public TextView product;
        public TextView prize;
        public TextView descrip;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            //Recuperamos de la vista las variables
            imgproducto=itemView.findViewById(R.id.imgprod);
            product=itemView.findViewById(R.id.producto);
            prize=itemView.findViewById(R.id.price);
            descrip=itemView.findViewById(R.id.description);
        }
    }
    public OfferAdapter(Context c,ArrayList<Offer> offerlist){
        mOfferList=offerlist;
        context=c;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item,parent,false);
        OfferViewHolder ofvh=new OfferViewHolder(v);
        return ofvh;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        mOffer=mOfferList.get(position);
        holder.product.setText(mOffer.getProduct());
        holder.prize.setText(mOffer.getPrize());
        holder.descrip.setText(mOffer.getDescription());
        Glide.with(context).load(mOffer.getImageURL()).into(holder.imgproducto);

    }

    @Override
    public int getItemCount() {
        return mOfferList.size();
    }


}
