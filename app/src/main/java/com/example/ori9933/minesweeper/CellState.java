package com.example.ori9933.minesweeper;

interface CellStateChangedListener{
    void OnChange();
    void OnEndGame();
}

public class CellState{
    public static final int MINE_VALUE = -1;
    private CellStatus status;
    private int value;
    private CellStateChangedListener listener;



    public CellStatus getStatus() {
        return status;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public void raiseStateChanged() {
        if(listener != null)
            listener.OnChange();
    }

    public void raiseEndGame() {
        if(listener != null)
            listener.OnEndGame();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void register(CellStateChangedListener listener){
        this.listener = listener;
    }

    public void reset() {
        status = CellStatus.Initial;
        value = 0;
    }

    public CellErrorStatus getError(){
        if(status == CellStatus.Mine && value != MINE_VALUE){
            return CellErrorStatus.InvalidMine;
        }
        else if(status == CellStatus.Opened && value == MINE_VALUE){
            return CellErrorStatus.Mine;
        }
        return CellErrorStatus.None;
    }

}

enum CellErrorStatus{
    None,InvalidMine,Mine
}

enum CellStatus {
    Initial,Mine,Opened
}

