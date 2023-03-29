package edu.northeastern.finalprojectcs5520;

public class SharePublicInfo {

    private String username;
    private String date;
    private String bodyWeight;
    private String bodyFat;

    public SharePublicInfo(String username, String date, String bodyWeight, String bodyFat) {
        this.username = username;
        this.date = date;
        this.bodyWeight = bodyWeight;
        this.bodyFat = bodyFat;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(String bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public String getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(String bodyFat) {
        this.bodyFat = bodyFat;
    }



}
