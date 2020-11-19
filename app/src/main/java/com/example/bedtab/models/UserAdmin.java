package com.example.bedtab.models;

public class UserAdmin {
    private String name;
    private String phone;
    private String correo;
    private String uid;

    public UserAdmin() {
    }
    public UserAdmin(String c, String n, String nt,String u) {
        this.correo=c;
        this.name=n;
        this.phone=nt;
        this.uid=u;
    }
    public String getUid(){return this.uid;}
    public String getName(){
        return this.name;
    }
    public String getPhone(){
        return this.phone;
    }
    public String getCorreo(){
        return this.correo;
    }
    public void setUid(String ui){
        this.uid=ui;
    }
   public void setName(String name){
        this.name=name;
   }
   public void setPhone(String numero){
        this.phone=numero;
   }
   public void setCorreo(String c){
        this.correo=c;
   }
}

