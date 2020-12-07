package com.grupobedher.bedtab.ViewHolders;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.grupobedher.bedtab.R;

    public class ChatViewHolder extends RecyclerView.ViewHolder{
        public TextView nombreUser;
        public TextView numTelefono;
        public TextView numNoti;
        public View v;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            numTelefono=itemView.findViewById(R.id.chat_numero);
            nombreUser=itemView.findViewById(R.id.chat_nombre);
            numNoti=itemView.findViewById(R.id.notnum);
            v=itemView;
        }
    }






