package com.grupobedher.bedtab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
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
import java.util.regex.Pattern;



public class RegisterActivity extends AppCompatActivity {
     private EditText BNombre;
     private EditText BEmail;
     private EditText BContra;
     private EditText BContraC;
     private EditText BNumT;
     private Button BRegist;
     private String nombre;
     private String email;
     private String contra;
     private String contraC;
     private String numtel;
    private FirebaseAuth mAuth;
    private AlertDialog dialog;
    private static final String t ="Registrando Usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Recuperacion del Layout

        BNombre=findViewById(R.id.Nombre);
        BEmail=findViewById(R.id.email);
        BContra=findViewById(R.id.psw);
        BContraC=findViewById(R.id.pswc);
        BNumT=findViewById(R.id.telefono);
        BRegist=findViewById(R.id.BRegistrar);
        mAuth=FirebaseAuth.getInstance();


        //Boton Registro
        BRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre=BNombre.getText().toString().trim();
                email=BEmail.getText().toString().trim();
                contra=BContra.getText().toString().trim();
                contraC=BContraC.getText().toString().trim();
                numtel=BNumT.getText().toString().trim();
                boolean ve=validarEmail(email);
                boolean contv=contraC.contentEquals(contra);
                int contl=contra.length();
                int contcl=contraC.length();
                int telength=numtel.length();

                //Validación Campos
                if(!nombre.isEmpty() && !email.isEmpty() && !contra.isEmpty() && !contraC.isEmpty() && !numtel.isEmpty()){
                    if( contv && contl>5 && ve && telength==10){
                        showRegist(t);
                        RegistrarUsuario(email,contra);
                    }else{
                        if(!contv){
                            Toast.makeText(RegisterActivity.this,"Debe introducir la misma contraseña", Toast.LENGTH_SHORT).show();
                        }
                        else if(contl<6 || contcl<6) {
                            Toast.makeText(RegisterActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                        }
                        else if(!ve){
                            Toast.makeText(RegisterActivity.this, "Correo Inválido", Toast.LENGTH_SHORT).show();
                        }
                        else if(telength != 10){
                            Toast.makeText(RegisterActivity.this, "Número Inválido", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    Toast.makeText(RegisterActivity.this,"Debe rellenar todos los campos",Toast.LENGTH_SHORT).show();
                }

            }
        });//Boton Registro
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void RegistrarUsuario(String em, String con){
        mAuth.createUserWithEmailAndPassword(em, con)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            sendVerificationEmail();
                            preRegist(email,nombre,numtel);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void preRegist(String email, String nombre, String numtel) {
        InputMethodManager inputMethodManager =  (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        SharedPreferences sharedPreferences = getSharedPreferences("PreRegis", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("num",numtel);
        editor.putString("email",email);
        editor.putString("nomb", nombre);
        editor.apply();
    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show();
        }else{
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseAuth.getInstance().signOut();
                                Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                overridePendingTransition(0, 0);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                            }
                        }
                    });
        }

    }
    public void showRegist(String title){
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(RegisterActivity.this);
        View v=getLayoutInflater().inflate(R.layout.progress,null);
        mBuilder.setView(v);
        dialog=mBuilder.create();
        dialog.setCancelable(false);
        TextView titulo=v.findViewById(R.id.dialogtext);
        titulo.setText(title);
        dialog.show();
    }





}


