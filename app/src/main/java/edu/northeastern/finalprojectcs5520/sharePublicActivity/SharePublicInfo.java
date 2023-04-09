package edu.northeastern.finalprojectcs5520.sharePublicActivity;

public class SharePublicInfo {

    private String username;
    private String date;
    private String bodyWeight;
    private String bodyFat;


    private String bodyBmi;

    public SharePublicInfo(String username, String date, String bodyWeight, String bodyFat, String bodyBmi) {
        this.username = username;
        this.date = date;
        this.bodyWeight = bodyWeight;
        this.bodyFat = bodyFat;
        this.bodyBmi = bodyBmi;
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

    public String getBodyBmi() {
        return bodyBmi;
    }

    public void setBodyBmi(String bodyBmi) {
        this.bodyBmi = bodyBmi;
    }


}
