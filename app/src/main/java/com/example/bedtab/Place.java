package com.example.bedtab;

public class Place {
    private int imgplace;
    private String nomplace;
    private String adress;


    public Place(){}

    public Place(int imgplace, String nomplace, String adress){
        this.imgplace=imgplace;
        this.nomplace=nomplace;
        this.adress=adress;
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
}
