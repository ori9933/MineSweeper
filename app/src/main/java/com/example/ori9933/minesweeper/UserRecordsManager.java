package com.example.ori9933.minesweeper;


import java.util.ArrayList;
import java.util.Collections;

public final class UserRecordsManager {
    private final int MAX_HEIGH_SCORES = 10;
    public static boolean isNewHighScore(int score){
        return true;
    }

    public static void SaveRecord(int score, String name){

    }

    public static ArrayList<Record> GetAllRecords(){
        ArrayList<Record> records = new ArrayList<Record>();
        records.add(new Record("Name1", 1000));
        records.add(new Record("Name2", 3000));
        records.add(new Record("Name3", 2000));
        records.add(new Record("Name4", 4000));
        Collections.sort(records);

        return records;
    }


}
