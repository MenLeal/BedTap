package com.example.bedtab.models;



public class Usuario {
    private String email;
    private String nombre;
    private String telefono;
    private String Psw;



    public Usuario(){
    }
    public Usuario(String e, String n, String t, String c){
        this.email=e;
        this.nombre=n;
        this.telefono=t;
        this.Psw=c;
    }



    public String getNombre(){
        return this.nombre;
    }

    public String getEmail(){
        return this.email;
    }

    public String getTelefono(){
        return this.telefono;
    }

    public String getCont(){
        return this.Psw;
    }

    public void setEmail(String correo){
        this.email=correo;
    }

    public void setNombre(String n){
        this.nombre=n;
    }

    public void setTelefono(String t){
        this.telefono=t;
    }

    public void setContra(String c){
        this.Psw=c;
    }


}

