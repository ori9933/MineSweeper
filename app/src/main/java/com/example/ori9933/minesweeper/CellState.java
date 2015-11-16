package com.example.ori9933.minesweeper;

interface ICellStateChangedListener{
    void onChange();
    void onEndGame();
}

public class CellState{
    public static final int MINE_VALUE = -1;
    private CellStatus status;
    private int value;
    private ICellStateChangedListener listener;



    public CellStatus getStatus() {
        return status;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public void raiseStateChanged() {
        if(listener != null)
            listener.onChange();
    }

    public void raiseEndGame() {
        if(listener != null)
            listener.onEndGame();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void register(ICellStateChangedListener listener){
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

