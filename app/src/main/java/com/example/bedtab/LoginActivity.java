package com.example.bedtab;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button BtnRegistrar;
    private Button BtnEntrar;
    private EditText correoL;
    private EditText contraL;
    private String contra;
    private String correo;
    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private DatabaseReference mDatabase3;
    private String nombre;
    private String numtel;
    private AlertDialog dialog;
    private String t;
    private String id;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String tstmp;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BtnRegistrar=findViewById(R.id.BtnRegistrar);
        BtnEntrar=findViewById(R.id.BtnEntrar);
        correoL=findViewById(R.id.correoL);
        contraL=findViewById(R.id.contraL);
        nombre=getIntent().getStringExtra("Nomb");
        numtel=getIntent().getStringExtra("Num");
        t="Iniciando Sesión";
        fAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase2= FirebaseDatabase.getInstance().getReference();
        mDatabase3= FirebaseDatabase.getInstance().getReference();
        calendar= Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("dd/MMM/yyyy hh:mm a ");
        uid="5vuXOTy9SdTvYcWaaxfDx9ZeR7j2";
        tstmp=simpleDateFormat.format(calendar.getTime());
        //Boton Registrar
        BtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        //Boton correo y contra
        BtnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogin(t);
                correo=correoL.getText().toString().trim();
                contra=contraL.getText().toString().trim();
                if(!correo.isEmpty() && !contra.isEmpty()){
                    LogIn(correo, contra);
                }
                else{
                    Toast.makeText(LoginActivity.this, "ERROR Ingrese Datos VALIDOS", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }
    public void showLogin(String title){
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(LoginActivity.this);
        View v=getLayoutInflater().inflate(R.layout.progress,null);
        mBuilder.setView(v);
        dialog=mBuilder.create();
        dialog.setCancelable(false);
        TextView titulo=v.findViewById(R.id.dialogtext);
        titulo.setText(title);
        dialog.show();
    }

    //Funcion Login Correo y Contra
    public void LogIn(String c, String p){
        fAuth.signInWithEmailAndPassword(c, p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            FirebaseUser user= fAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                if(numtel==null && nombre==null){
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }else{
                                    Map<String, Object> map=new HashMap<>();
                                    map.put("email", correo);
                                    map.put("nombre",nombre);
                                    map.put("telefono", numtel);
                                    map.put("Psw", contra);
                                    id= fAuth.getCurrentUser().getUid();
                                    mDatabase.child("Users").child("private").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Map<String, Object> map2=new HashMap<>();
                                            map2.put("correo", correo);
                                            map2.put("name",nombre);
                                            map2.put("phone", numtel);
                                            map2.put("uid",id);
                                            mDatabase2.child("Users").child("public").push().setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Map<String, Object> map3=new HashMap<>();
                                                        map3.put("sender",uid);
                                                        map3.put("mensaje","¿En qué puedo ayudarle?");
                                                        map3.put("timestamp",tstmp);
                                                        String idmensaje = mDatabase.push().getKey();
                                                        map3.put("id",idmensaje);
                                                        mDatabase3.child("Chats").child("private").child(id).child(idmensaje).setValue(map3)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task2) {
                                                                        if(task2.isSuccessful()){
                                                                            Toast.makeText(LoginActivity.this,"Registrado",Toast.LENGTH_LONG);
                                                                            Intent i=new Intent(LoginActivity.this, MainActivity.class);
                                                                            startActivity(i);
                                                                            finish();
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                }
                                            });

                                        }
                                    });
                                }

                            }else{
                                fAuth.signOut();
                                Toast.makeText(LoginActivity.this, "ERROR Confirme correo electrónico, revise su bandeja de entrada", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "ERROR Verifique los datos ingresados", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        if(fAuth.getCurrentUser()!=null){
            super.onStart();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }
        else{
            super.onStart();
        }

    }





}
