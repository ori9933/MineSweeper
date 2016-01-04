package com.example.ori9933.minesweeper;


public class Record implements Comparable<Record> {

    private String name;
    private int score;
    private double latitude;
    private double longitude;


    public Record(String name, int score, double latitude, double longitude) {
        this.name = name;
        this.score=score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Record another) {
        return  another.score - this.score;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
