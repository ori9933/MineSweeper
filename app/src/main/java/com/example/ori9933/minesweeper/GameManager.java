package com.example.ori9933.minesweeper;


import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


interface IGameStatusListener {
    void onMinesLeftChanged(int mines);
    void onGameOver(boolean isGameWon);
}

public class GameManager {
    private static GameManager instance;
    public static final int GAME_SIZE = 10;

    private CellState[][] cells;
    private GameLevel level;
    private int minesLeft;
    private IGameStatusListener gameStatusListener;
    private boolean isGameRunning = false;
    long gameStartTimeInMili;
    int lastScore;


    private GameManager(){
        setGamesLevel(GameLevel.Easy);
        cells = new  CellState[GAME_SIZE][GAME_SIZE];
        for (int i=0;i<GAME_SIZE;i++){
            for(int j=0;j<GAME_SIZE;j++){
                cells[i][j] = new CellState(i,j);
            }
        }
    }

    public static GameManager getInstance(){
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    public void register(IGameStatusListener listener){
        this.gameStatusListener = listener;
    }

    public CellState[][] GetCellsStates(){
        return cells;
    }

    public void setGamesLevel(GameLevel level){
        this.level=level;
    }

    public void newGame(boolean notify){
        resetCells();
        randomizeMines();
        calculateCellsValues();

        if(notify){
            updateCells();
        }

        gameStartTimeInMili = System.currentTimeMillis();
        isGameRunning = true;
    }

    private void updateCells() {
        for (int i=0;i<GAME_SIZE;i++){
            for(int j=0;j<GAME_SIZE;j++){
                cells[i][j].raiseStateChanged();
            }
        }
    }

    public void openCell(CellState cellState){
        if(cellState.getValue() == 0){
            spreadSurroundingCells(cellState.getRow(), cellState.getCol());
        }
        else
        {
            cellState.setStatus(CellStatus.Opened);
        }

        verifyAndContinueGame(cellState);
    }

    private void spreadSurroundingCells(int row, int col) {
        CellState cellState = cells[row][col];
        CellStatus cellStatus = cellState.getStatus();

        if(cellStatus != CellStatus.Initial)
            return;

        if(cellState.getValue() > 0){
            cellState.setStatus(CellStatus.Opened);
            cellState.raiseStateChanged();
        }
        else if(cellState.getValue() == 0){
            cellState.setStatus(CellStatus.Opened);
            cellState.raiseStateChanged();

            for(int i = row-1; i<=row+1;i++){
                if(i<0 || i>=GAME_SIZE)
                    continue;
                for(int j = col-1; j<=col+1;j++){
                    if(j < 0 || j >= GAME_SIZE)
                        continue;
                    spreadSurroundingCells(i,j);
                }
            }
        }

    }

    public void SetCellMine(CellState cellState){
        CellStatus currentStatus = cellState.getStatus();
        if(currentStatus == CellStatus.Initial){
            cellState.setStatus(CellStatus.Mine);
            minesLeft--;
        }
        else if(currentStatus == CellStatus.Mine){
            cellState.setStatus(CellStatus.Initial);
            minesLeft++;
        }
        onMinesChanged();
        verifyAndContinueGame(cellState);
    }

    private void verifyAndContinueGame(CellState cellState){
        if(cellState.getError() != CellErrorStatus.Mine){
            cellState.raiseStateChanged();
            verifyGameWon();
        }
        else {
            onGameOver(false, cellState);
        }
    }

    private void verifyGameWon(){
        for (int i=0;i<GAME_SIZE;i++){
            for(int j=0;j<GAME_SIZE;j++){
                if(cells[i][j].getStatus() == CellStatus.Initial || cells[i][j].getError() != CellErrorStatus.None){
                    return;
                }
            }
        }
        onGameOver(true, null);
    }

    private void onGameOver(boolean isGameWon, CellState errorCell){

        isGameRunning = false;
        calculateScore(isGameWon);
        if(gameStatusListener != null){
            gameStatusListener.onGameOver(isGameWon);
        }

        for (int i=0;i<GAME_SIZE;i++){
            for(int j=0;j<GAME_SIZE;j++){
                cells[i][j].raiseEndGame(isGameWon,errorCell);
            }
        }
    }

    private void calculateScore(boolean isGameWon) {
        final int MAX_TIME = 1000;
        final int  POINTS_PER_MINE = 40;
        if(isGameWon){
            int secondsPassed = (int)(System.currentTimeMillis() - gameStartTimeInMili) / 1000;
            int scoreBase = secondsPassed >= MAX_TIME ? 0 : MAX_TIME - secondsPassed;
            lastScore = scoreBase/2 + getTotalMines()*POINTS_PER_MINE;
        }
        else{
            lastScore = 0;
        }
    }

    public int getLastScore(){
        return lastScore;
    }

    private void onMinesChanged(){
        if(gameStatusListener != null){
            gameStatusListener.onMinesLeftChanged(minesLeft);
        }
    }

    private void calculateCellsValues(){
        for (int i=0;i<GAME_SIZE;i++){
            for(int j=0;j<GAME_SIZE;j++){
                if(cells[i][j].getValue() != CellState.MINE_VALUE){
                    int surroundingMinesNumber =  getSurroundingMinesNumber(i,j);
                    cells[i][j].setValue(surroundingMinesNumber);
                }
            }
        }
    }

    private int getSurroundingMinesNumber(int row, int col){
        int sum = 0;
        for(int i = row-1; i<=row+1;i++){
            if(i<0 || i>=GAME_SIZE)
                continue;
            for(int j = col-1; j<=col+1;j++){
                if((i==row & j==col) || j < 0 || j >= GAME_SIZE)
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
        this.minesLeft = totalMines;
        onMinesChanged();
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

    public void OnOrientationDeviation(){
        if(isGameRunning == false)
            return;

        ArrayList<CellState> potentialCells = new ArrayList<CellState>();
        for (int i=0;i<GAME_SIZE;i++){
            for(int j=0;j<GAME_SIZE;j++){
                if( cells[i][j].getValue() == 0 || (cells[i][j].getValue() > 0 &&  cells[i][j].getStatus() == CellStatus.Opened)){
                    potentialCells.add(cells[i][j]);
                }
            }
        }

        if(potentialCells.size() == 0)
            return;

        Random rand = new Random();
        CellState cell = potentialCells.get(rand.nextInt(potentialCells.size()));
        cell.setValue(CellState.MINE_VALUE);
        if(cell.getStatus()==CellStatus.Opened)
            cell.setStatus(CellStatus.Initial);
        cell.raiseStateChanged();

        for (int i=cell.getRow()-1;i<cell.getRow()+1;i++){
            if(i<0 || i>=GAME_SIZE)
                continue;
            for(int j=cell.getCol()-1;j<cell.getCol()+1;j++){
                if((i==cell.getRow() & j==cell.getCol()) || j < 0 || j >= GAME_SIZE)
                    continue;
                if(cells[i][j].getValue() != CellState.MINE_VALUE){
                    cells[i][j].setValue(cells[i][j].getValue() + 1);
                    if(cells[i][j].getStatus()==CellStatus.Opened)
                        cells[i][j].setStatus(CellStatus.Initial);
                    cells[i][j].raiseStateChanged();
                }
            }
        }

        this.minesLeft++;
        onMinesChanged();
    }

}

enum GameLevel {
    Easy, Normal, Hard
}
