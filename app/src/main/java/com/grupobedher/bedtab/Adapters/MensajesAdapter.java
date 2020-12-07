package com.grupobedher.bedtab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupobedher.bedtab.models.Mensaje;
import com.grupobedher.bedtab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.ChatUserViewHolder> {
    private List<Mensaje> mchatList;
    private Context context;
    private static final int message_left=0;
    private static final int message_right=1;
    public MensajesAdapter(Context c,List<Mensaje> chatlist){
        mchatList=chatlist;
        context=c;
    }

    @NonNull
    @Override
    public MensajesAdapter.ChatUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==message_left){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.left_message,parent,false);
            return new MensajesAdapter.ChatUserViewHolder(view);
        }else{
            View view2= LayoutInflater.from(parent.getContext()).inflate(R.layout.right_message,parent,false);
            return new MensajesAdapter.ChatUserViewHolder(view2);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserViewHolder holder, int position) {
        String mssg=mchatList.get(position).getMensaje();
        String tmpt=mchatList.get(position).getTimestamp();
        holder.msg.setText(mssg);
        holder.tstm.setText(tmpt);
    }

    @Override
    public int getItemCount() {
        return mchatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String id=user.getUid();
        if(mchatList.get(position).getSender().equals(id)){
            return message_right;
        }else{
            return message_left;
        }
    }

    public class ChatUserViewHolder  extends RecyclerView.ViewHolder{
        public TextView msg;
        public TextView tstm;
        public ChatUserViewHolder(@NonNull View itemView) {
            super(itemView);
            msg=itemView.findViewById(R.id.mensajeChat);
            tstm=itemView.findViewById(R.id.timestampChat);
        }
    }
}

