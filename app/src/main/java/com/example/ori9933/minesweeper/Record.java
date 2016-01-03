package com.example.ori9933.minesweeper;


public class Record implements Comparable<Record> {

    private String name;
    private int score;

    public Record(String name, int score){
        this.name = name;
        this.score=score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Record another) {
        return this.score - another.score;
    }
}
