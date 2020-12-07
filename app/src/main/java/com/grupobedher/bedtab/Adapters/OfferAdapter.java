package com.grupobedher.bedtab.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.grupobedher.bedtab.models.Offer;
import com.grupobedher.bedtab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    private ArrayList<Offer> mOfferList;
    private Context context;
    private Offer mOffer;
    private OnItemClickListener mlistener;
    private static final int admin = 0;
    private static final int us = 1;

    public interface OnItemClickListener{
        void deleteItem(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener=listener;
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgproducto;
        public TextView product;
        public TextView prize;
        public TextView descrip;
        public ImageView btndelete;
        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            //Recuperamos de la vista las variables
            imgproducto=itemView.findViewById(R.id.imgprod);
            product=itemView.findViewById(R.id.producto);
            prize=itemView.findViewById(R.id.price);
            descrip=itemView.findViewById(R.id.description);
        }
        public OfferViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            //Recuperamos de la vista las variables
            imgproducto=itemView.findViewById(R.id.imgprod);
            product=itemView.findViewById(R.id.producto);
            prize=itemView.findViewById(R.id.price);
            descrip=itemView.findViewById(R.id.description);
            btndelete=itemView.findViewById(R.id.btndelete);

            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.deleteItem(position);
                        }
                    }
                }
            });
        }

    }
    public OfferAdapter(Context c,ArrayList<Offer> offerlist){
        mOfferList=offerlist;
        context=c;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType==admin){
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_itemdelete,parent,false);
                OfferViewHolder ofvh2=new OfferViewHolder(v, mlistener);
                return ofvh2;
            }
            else{
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item,parent,false);
                OfferViewHolder ofvh=new OfferViewHolder(v);
                return ofvh;
            }

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

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String id=user.getEmail();
        if(id.equals("bedherapp@gmail.com")){
            return admin;
        }else{
            return us;
        }
    }
}
