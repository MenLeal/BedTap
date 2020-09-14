package com.example.bedtab.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bedtab.R;

public class OfferViewHolder extends RecyclerView.ViewHolder{
    public TextView nombreOffer;
    public TextView precioOffer;
    public TextView descripOffer;
    public ImageView fotoProducto;

    public OfferViewHolder(@NonNull View itemView) {
        super(itemView);
        nombreOffer=itemView.findViewById(R.id.producto);
        precioOffer=itemView.findViewById(R.id.price);
        descripOffer=itemView.findViewById(R.id.description);
        fotoProducto=itemView.findViewById(R.id.imgprod);
    }
}

