package com.example.cityclean;

import java.io.Serializable;

public class Report implements Serializable {
    private int id;
    private String address;
    private String date;
    private String comment;
    private String imagePath;
    private boolean isClean;

    public Report(){}
    public Report(int id, String address, String date, String comment, String imagePath, boolean isClean) {
        this.id = id;
        this.address = address;
        this.date = date;
        this.comment = comment;
        this.imagePath = imagePath;
        this.isClean = isClean;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getImagePath() {
        return imagePath;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isClean() {
        return isClean;
    }

    public void setClean(boolean clean) {
        isClean = clean;
    }
}
