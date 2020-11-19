package com.example.bedtab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bedtab.Adapters.MensajesAdapter;
import com.example.bedtab.notificación.Data;
import com.example.bedtab.notificación.Sender;
import com.example.bedtab.notificación.Token;
import com.example.bedtab.models.Mensaje;
import com.example.bedtab.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private ImageButton back;
    private String nombre;
    private EditText etMensaje;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private  String mensaje;
    private FirebaseAuth fAuth;
    private String id;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String tstmp;
    private RecyclerView recyclerView;
    private List<Mensaje> mList;
    private MensajesAdapter adapter;
    private String hisid;
    private LinearLayoutManager linearLayoutManager;
    private boolean notify;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView=findViewById(R.id.rvMensajesA);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        etMensaje=findViewById(R.id.etMensajeA);
        ImageButton btnEnviar = findViewById(R.id.btnEnviarA);
        nombre=getIntent().getStringExtra("Nombre");
        hisid=getIntent().getStringExtra("id");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(nombre);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase2= FirebaseDatabase.getInstance().getReference();
        fAuth= FirebaseAuth.getInstance();
        id=fAuth.getCurrentUser().getUid();
        calendar= Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("dd/MMM/yyyy hh:mm a ");
        tstmp=simpleDateFormat.format(calendar.getTime());
        mList=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        mDatabase2.child("Chats").child("private").child(hisid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for(DataSnapshot sp: snapshot.getChildren()){
                    mList.add(sp.getValue(Mensaje.class));
                }
                adapter=new MensajesAdapter(ChatActivity.this,mList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mensaje=etMensaje.getText().toString().trim();
                notify=true;
                if(!mensaje.isEmpty()){
                    Map<String, Object> map=new HashMap<>();
                    map.put("sender",id);
                    map.put("mensaje",mensaje);
                    map.put("timestamp",tstmp);
                    String idmensaje=mDatabase.push().getKey();
                    map.put("id",idmensaje);
                    mDatabase.child("Chats").child("private").child(hisid).child(idmensaje).setValue(map);
                    etMensaje.setText("");
                    final DatabaseReference database=FirebaseDatabase.getInstance().getReference("Users").child("private").child(id);
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Usuario usuario=snapshot.getValue(Usuario.class);
                            if(notify){
                                sendNotification(hisid,usuario.getNombre(),mensaje);
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

}
