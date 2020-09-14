package com.example.bedtab;

public class Offer {
    private String imageURL;
    private String product;
    private String prize;
    private String description;

    public Offer(){
    }

    public Offer(String i, String p, String pz, String d){
        this.imageURL=i;
        this.product=p;
        this.prize=pz;
        this.description=d;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPrize() {
        return prize;
    }

    public String getProduct() {
        return product;
    }

    public String getDescription() {
        return description;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
