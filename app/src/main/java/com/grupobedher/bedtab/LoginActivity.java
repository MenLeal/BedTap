package com.grupobedher.bedtab;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grupobedher.bedtab.models.UserAdmin;

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
    private static  final String t = "Iniciando Sesion";
    private String id;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy hh:mm a ");
    private String tstmp;
    private static final String uid="5vuXOTy9SdTvYcWaaxfDx9ZeR7j2";
    private static final String numAd="9868632211";
    private static final String nombAd="Administrador";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BtnRegistrar=findViewById(R.id.BtnRegistrar);
        BtnEntrar=findViewById(R.id.BtnEntrar);
        correoL=findViewById(R.id.correoL);
        contraL=findViewById(R.id.contraL);
        SharedPreferences sharedPreferences = getSharedPreferences("PreRegis", Context.MODE_PRIVATE);
        nombre=sharedPreferences.getString("nomb", "");
        numtel=sharedPreferences.getString("num", "");
        fAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase2= FirebaseDatabase.getInstance().getReference();
        mDatabase3= FirebaseDatabase.getInstance().getReference();
        calendar= Calendar.getInstance();
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
    public void LogIn(final String c, String p){
        fAuth.signInWithEmailAndPassword(c, p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            FirebaseUser user= fAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                if(numtel.isEmpty() && nombre.isEmpty()){
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    guardarDatos(c);
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
                                                                            Toast.makeText(LoginActivity.this,"Registrado",Toast.LENGTH_LONG).show();
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

    private void guardarDatos(final String correo) {
        String id = fAuth.getUid();
        InputMethodManager inputMethodManager =  (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        SharedPreferences sharedPreferences = getSharedPreferences("PreRegis", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (id.equals(uid)){
            editor.putString("num",numAd);
            editor.putString("email",correo);
            editor.putString("nomb", nombAd);
            editor.apply();
        }else{
            Query query =FirebaseDatabase.getInstance().getReference("Users").child("public").orderByChild("uid").equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot sp: snapshot.getChildren()){
                        UserAdmin u = sp.getValue(UserAdmin.class);
                        numtel = u.getPhone();
                        nombre = u.getName();
                    }
                    editor.putString("num",numtel);
                    editor.putString("email",correo);
                    editor.putString("nomb", nombre);
                    editor.apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
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
