package com.example.ori9933.minesweeper;


import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public final class UserRecordsManager {

    private final int MAX_HEIGH_SCORES = 10;
    private static UserRecordsManager instance;

    List<Record> records;


    public static UserRecordsManager getInstance(){
        if(instance == null){
            instance = new UserRecordsManager();
        }
        return instance;
    }

    private UserRecordsManager(){
        loadAllRecords();
    }

    public Collection<Record> GetAllRecords(){
        return records;
    }


    public boolean isNewHighScore(int score){
        if(records.size() < MAX_HEIGH_SCORES)
            return true;

        for (Record record: records) {
            if(record.getScore() < score)
                return true;
        }

        return false;
    }

    public void SaveRecord(int score, String name, double latitude, double longitude){
        if(records.size() >= MAX_HEIGH_SCORES)
            records.remove(records.get(records.size()-1));
        records.add(new Record(name,score, latitude, longitude));
        Collections.sort(records);
        saveRecords();
    }



    public static final String PREFS_NAME = "MyPrefsFile";
    private String Records = "recordsData";
    Type collectionType = new TypeToken<List<Record>>(){}.getType();

    private SharedPreferences prefs = MyApp.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    public void saveRecords () {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(Records, new Gson().toJson(records));
        editor.commit();
    }

    private void loadAllRecords() {
        records = new Gson().fromJson(prefs.getString(Records, null), collectionType);
        if(records == null)
            records = new ArrayList<Record>();
    }


}
