package edu.northeastern.finalprojectcs5520.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {

    public String username;
    public Map<String, Map> recordWeights;
    public Boolean personalInfoEntered;
    public int age;
    public int heightFeet;
    public int heightInches;
    public int currentWeight;
    public int goalWeight;
    public float goalBMI;
    public float goalFatRate;

//    public ArrayList<HashMap> receivedSticker;

    public User(String username){
        //Format date into mm/dd/yyyy
//        SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy");
//        Date date = new Date();
//        String formattedDate = formatter.format(date);

        this.username = username;
        this.recordWeights = new HashMap<String, Map>();
        this.personalInfoEntered = false;


    }
}