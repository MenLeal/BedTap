package com.example.bedtab.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bedtab.ChatActivity;
import com.example.bedtab.ViewHolders.ChatViewHolder;
import com.example.bedtab.models.Mensaje;
import com.example.bedtab.Adapters.MensajesAdapter;
import com.example.bedtab.R;
import com.example.bedtab.notificación.Data;
import com.example.bedtab.notificación.Sender;
import com.example.bedtab.notificación.Token;
import com.example.bedtab.models.UserAdmin;
import com.example.bedtab.models.Usuario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class HomeFragment extends Fragment {
    private EditText etMensaje;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String tstmp;
    private RecyclerView recyclerView;
    private List<Mensaje> mList;
    private MensajesAdapter adapter;
    private String id;
    private String uid;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private DatabaseReference mDatabase3;
    private FirebaseAuth fAuth;
    private String mensaje;
    private String admin;
    private Mensaje msga;
    private boolean notify;
    private RequestQueue requestQueue;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=root.findViewById(R.id.rvMensajes);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        etMensaje=root.findViewById(R.id.etMensaje);
        ImageButton btnEnviar = root.findViewById(R.id.btnEnviar);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase2= FirebaseDatabase.getInstance().getReference();
        mDatabase3= FirebaseDatabase.getInstance().getReference();
        fAuth= FirebaseAuth.getInstance();
        id=fAuth.getCurrentUser().getUid();
        calendar= Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("dd/MMM/yyyy hh:mm a ");
        tstmp=simpleDateFormat.format(calendar.getTime());
        mList=new ArrayList<>();
        admin=fAuth.getCurrentUser().getEmail();
        updateToken(FirebaseInstanceId.getInstance().getToken());
        notify=false;
        uid="5vuXOTy9SdTvYcWaaxfDx9ZeR7j2";
        requestQueue= Volley.newRequestQueue(getActivity());

        if(admin.equals("bedherapp@gmail.com")){
            btnEnviar.setVisibility(View.GONE);
            etMensaje.setVisibility(View.GONE);
            Query query =mDatabase3
                    .child("Users").child("public");
            FirebaseRecyclerOptions<UserAdmin> options =
                    new FirebaseRecyclerOptions.Builder<UserAdmin>()
                            .setQuery(query, UserAdmin.class)
                            .build();
            FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<UserAdmin, ChatViewHolder>(options) {
                @Override
                public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chats_item, parent, false);
                    return new ChatViewHolder(view);
                }

                @Override
                protected void onBindViewHolder( final ChatViewHolder holder, int position, final UserAdmin model) {
                    holder.numNoti.setVisibility(View.GONE);
                    holder.nombreUser.setText(model.getName());
                    DatabaseReference mData=FirebaseDatabase.getInstance().getReference();
                    mData.child("Chats").child("private").child(model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot sp: snapshot.getChildren()){
                                mList.add(sp.getValue(Mensaje.class));
                            }
                            msga = mList.get(mList.size() - 1);
                            String msg = msga.getMensaje();
                            String mid = msga.getId();
                            if (mList.get(mList.size()-1).getSender() .equals(model.getUid())) {
                                holder.numNoti.setVisibility(View.VISIBLE);
                                holder.numTelefono.setText(msg);
                                int tamlist = mList.size() - 1;
                                int numensaje = 0;
                                while (mList.get(tamlist).getSender().equals(model.getUid())) {
                                    numensaje += 1;
                                    tamlist -= 1;
                                }
                                holder.numNoti.setText("" + numensaje);
                            } else {
                                holder.numTelefono.setText(msg);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    holder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.numNoti.setVisibility(View.GONE);
                            Intent i=new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("Nombre",model.getName());
                            i.putExtra("id",model.getUid());
                            startActivity(i);
                        }
                    });
                }
            };
            adapter.startListening();
            recyclerView.setAdapter(adapter);
        }
        else{
            mDatabase2.child("Chats").child("private").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    for(DataSnapshot sp: snapshot.getChildren()){
                        mList.add(sp.getValue(Mensaje.class));
                    }
                    adapter=new MensajesAdapter(getContext(),mList);
                    recyclerView.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notify=true;
                    mensaje=etMensaje.getText().toString().trim();
                    if(!mensaje.isEmpty()){
                            Map<String, Object> map=new HashMap<>();
                            map.put("sender",id);
                            map.put("mensaje",mensaje);
                            map.put("timestamp",tstmp);
                            String idmensaje = mDatabase.push().getKey();
                            map.put("id",idmensaje);
                            mDatabase.child("Chats").child("private").child(id).child(idmensaje).setValue(map);
                            etMensaje.setText("");

                            final DatabaseReference database=FirebaseDatabase.getInstance().getReference("Users").child("private").child(id);
                            database.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Usuario usuario=snapshot.getValue(Usuario.class);
                                    if(notify){
                                        sendNotification(uid,usuario.getNombre(),mensaje);
                                    }
                                    notify=false;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    }

                }
            });
        }

        return root;
    }

    private void sendNotification(final String uid, final String nombre1, final String mensaje1) {
        DatabaseReference alltokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=alltokens.orderByKey().equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sp: snapshot.getChildren()){
                    Token token=sp.getValue(Token.class);
                    Data data=new Data(id,nombre1+": "+mensaje1,"Nuevo Mensaje",uid, R.drawable.notificon);

                    Sender sender =new Sender(data,token.getToken());

                    try {
                        JSONObject senderJsonObj=new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("JSON_Response","onResponse"+response.toString());
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON_Response","onResponse"+error.toString());
                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> headers=new HashMap<>();
                                headers.put("Content-Type","application/json");
                                headers.put("Authorization","key=AAAA9OXdTuE:APA91bEnve3mmwFIJy3ltR2TcjJ8jWvr3S3-ZBXvtEH1FS1JNBAkh-va9z-PgNaDoUDc_9wxCiLch7Qnd1wZv5bZPb4iLd-FJ1RXXf3rfTmaWT3Vn5Yg1jxbYdC3DzqsLCPWPPukpYHW");
                                return headers;
                            }
                        };

                        requestQueue.add(jsonObjectRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateToken(String token) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        ref.child(id).setValue(token1);
    }

}