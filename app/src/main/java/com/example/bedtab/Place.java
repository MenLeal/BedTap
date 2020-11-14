package com.example.bedtab;

public class Place {
    private int imgplace;
    private String nomplace;
    private String adress;
    private String geo;
    private String numplace;
    public Place(){}

    public Place(int imgplace, String nomplace, String adress, String geo, String numplace){
        this.imgplace=imgplace;
        this.nomplace=nomplace;
        this.adress=adress;
        this.geo=geo;
        this.numplace=numplace;
    }


    public int getImgplace() {
        return imgplace;
    }

    public void setImgplace(int imgplace) {
        this.imgplace = imgplace;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getAdress() {
        return adress;
    }

    public String getNomplace() {
        return nomplace;
    }

    public void setNomplace(String nomplace) {
        this.nomplace = nomplace;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getNumplace() {
        return numplace;
    }

    public void setNumplace(String numplace) {
        this.numplace = numplace;
    }
}
