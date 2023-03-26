package edu.northeastern.finalprojectcs5520.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {

    public String username;
    public Map<String, Map> recordWeights;
//    public ArrayList<HashMap> receivedSticker;

    public User(String username){
        //Format date into mm/dd/yyyy
        SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy");
        Date date = new Date();
        String formattedDate = formatter.format(date);

        this.username = username;
        this.recordWeights = new HashMap<String, Map>();

        Map info = new HashMap<>();
        info.put("recordWeight","150");
        info.put("bodyFatPercent","20");
        info.put("public", false);

        recordWeights.put(formattedDate,info);
//        sentSticker.put("1",0);
//        sentSticker.put("2",0);
//        sentSticker.put("3",0);
//        sentSticker.put("4",0);

//        this.receivedSticker = new ArrayList<>();
//        Map receivedStickerHashmap = new HashMap();
//        receivedStickerHashmap.put("sender","kevin");
//        receivedStickerHashmap.put("time", date);
//        receivedStickerHashmap.put("sticker", 1);

//        receivedSticker.add((HashMap) receivedStickerHashmap);


    }
}