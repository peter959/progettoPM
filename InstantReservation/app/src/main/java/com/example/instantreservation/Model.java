package com.example.instantreservation;

public class Model {
    private int image;
    private String title;
    private String descr;

    public Model(int image, String title, String descr) {
        this.image = image;
        this.title = title;
        this.descr = descr;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }


}