package com.example.ori9933.minesweeper;


import java.util.ArrayList;
import java.util.Collections;

public final class UserRecordsManager {

    private final int MAX_HEIGH_SCORES = 10;
    private static UserRecordsManager instance;

    ArrayList<Record> records;


    public static UserRecordsManager getInstance(){
        if(instance == null){
            instance = new UserRecordsManager();
        }
        return instance;
    }

    private UserRecordsManager(){
        loadRecords();
    }

    private void loadRecords(){
        records = new ArrayList<Record>();
        records.add(new Record("Name1", 1000,32.066653, 34.869389));
        records.add(new Record("Name2", 3000, 32.084690, 34.863038));
        records.add(new Record("Name3", 2000, 0, 0));
        Collections.sort(records);
    }

    public ArrayList<Record> GetAllRecords(){
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
        records.add(new Record(name,score, latitude, longitude));
    }


}
