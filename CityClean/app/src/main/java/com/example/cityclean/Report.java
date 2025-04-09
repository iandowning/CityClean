package com.example.cityclean;

import java.io.Serializable;

public class Report implements Serializable {
    private int id;
    private String location;
    private String date;
    private String comment;
    private String imagePath;
    private boolean isClean;

    public Report(){}

    public Report(int id, double lat, double lng, String date, String comment, String imagePath, boolean isClean) {
        this.id = id;
        this.location = lat + ", " + lng;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
