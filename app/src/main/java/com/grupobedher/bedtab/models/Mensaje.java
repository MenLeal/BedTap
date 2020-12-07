package com.grupobedher.bedtab.models;
public class Mensaje {
    private String mensaje;
    private String sender;
    private String timestamp;
    private String id;
    private Boolean isSeen;

    public Mensaje() {
    }

    public Mensaje(String mensaje, String sender, String timestamp,String id) {
        this.mensaje = mensaje;
        this.sender = sender;
        this.timestamp = timestamp;
        this.id=id;
    }


    public String getId() {
        return id;
    }
    public void setId(String key1){
        this.id=key1;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

