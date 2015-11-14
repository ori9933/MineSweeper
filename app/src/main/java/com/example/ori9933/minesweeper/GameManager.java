package com.example.ori9933.minesweeper;


import java.util.Random;


public class GameManager {
    private static GameManager instance;
    private final int GAME_SIZE = 10;

    private CellState[][] cells;
    private GameLevel level;

    private GameManager(){
        cells = new  CellState[GAME_SIZE][GAME_SIZE];
        for (int i=0;i<GAME_SIZE;i++){
            for(int j=0;j<GAME_SIZE;j++){
                cells[i][j] = new CellState();
            }
        }
    }

    public static GameManager getInstance(){
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    public void setGamesLevel(GameLevel level){
        this.level=level;
    }

    public void newGame(){
        resetCells();
        randomizeMines();
        calculateCellsValues();
    }

    public void openCell(CellState cellState){
        cellState.setStatus(CellStatus.Opened);
        verifyAndContinueGame(cellState);
    }

    public void addMineToCell(CellState cellState){
        cellState.setStatus(CellStatus.Mine);
        verifyAndContinueGame(cellState);
    }

    private void verifyAndContinueGame(CellState cellState){
        if(cellState.getError() == CellErrorStatus.None){
            cellState.raiseStateChanged();
        }
        else {
            for (int i=0;i<GAME_SIZE;i++){
                for(int j=0;j<GAME_SIZE;j++){
                    cells[i][j].raiseEndGame();
                }
            }
        }
    }

    private void calculateCellsValues(){
        for (int i=0;i<GAME_SIZE;i++){
            for(int j=0;j<GAME_SIZE;j++){
                int surroundingMinesNumber =  getSurroundingMinesNumber(i,j);
                cells[i][j].setValue(surroundingMinesNumber);
            }
        }
    }

    private int getSurroundingMinesNumber(int row, int col){
        int sum = 0;
        for(int i = row-1; i<=row+1;i++){
            for(int j = col-1; j<=col+1;j++){
                if(i==j || i<0 || j < 0 || row == GAME_SIZE || col == GAME_SIZE)
                    continue;
                if(cells[i][j].getValue() == CellState.MINE_VALUE){
                    sum++;
                }
            }
        }
        return sum;
    }

    private void randomizeMines() {
        int totalMines = getTotalMines();
        int row,col;
        Random rand = new Random();
        while (totalMines > 0){
            row = rand.nextInt(GAME_SIZE);
            col = rand.nextInt(GAME_SIZE);
            if(cells[row][col].getValue() != CellState.MINE_VALUE){
                cells[row][col].setValue(CellState.MINE_VALUE);
                totalMines--;
            }
        }
    }

    private void resetCells() {
        for (int i=0;i<GAME_SIZE;i++){
            for(int j=0;j<GAME_SIZE;j++){
                cells[i][j].reset();
            }
        }
    }

    private int getTotalMines(){
        switch (level){
            case Easy:
                return 10;
            case Normal:
                return 30;
            case Hard:
                return 60;
            default:
                return 10;
        }
    }

}

enum GameLevel {
    Easy, Normal, Hard
}
