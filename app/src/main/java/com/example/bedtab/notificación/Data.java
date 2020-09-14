package com.example.bedtab.notificación;

public class Data {
    private String user,body,title,sent;
    private Integer icon;
    public Data(){
    }
    public Data(String user,String body, String title, String sent,Integer icon){
        this.user=user;
        this.body=body;
        this.title=title;
        this.sent=sent;
        this.icon=icon;
    }

    public String getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public Integer getIcon() {
        return icon;
    }

    public String getSent() {
        return sent;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }
}

