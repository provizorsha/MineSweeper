package com.javarush.games.minesweeper;
import com.javarush.engine.cell.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;



public class MinesweeperGame extends Game {
    private final static int SIDE = 9;
    private GameObject [][] gameField = new GameObject [SIDE][SIDE];
    private int countMinesOnField;
    private boolean isMine;
    public int countMineNeighbors;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles = SIDE * SIDE;
    private int score;
    




    
    public void initialize() {
        setScreenSize(SIDE,SIDE);
        createGame();
       
    }
    

    
    private void createGame(){
        for (int x = 0; x< SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColor(y, x, Color.ORANGE);
                setCellValue(y,x,"");
                int randomNumber = getRandomNumber (10);
                if (randomNumber == 1) {
                    isMine = true;
                    countMinesOnField++;
                } else {
                    isMine = false;
                }
                gameField [x][y] = new GameObject(y,x, isMine);

            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }
    
    private List<GameObject> getNeighbors(GameObject gameObject){
        List<GameObject> nList = new ArrayList<GameObject>();
        for (int x = 0; x< SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if (gameObject != gameField[y][x] && Math.abs(gameObject.x - gameField[y][x].x) < 2 && Math.abs (gameObject.y - gameField[y][x].y)<2) {
                    nList.add(gameField[y][x]);
                }
            }
        }
        return nList;
    }
    
    
    
    private void countMineNeighbors(){
        List<GameObject>nList;
        for (int x = 0; x< SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if (!gameField[y][x].isMine) {
                    nList = getNeighbors(gameField[y][x]);
                    for (GameObject gameObject:nList){
                        if (gameObject.isMine) {
                            gameField[y][x].countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }
    private void openTile(int x, int y){
        if (!gameField[y][x].isOpen && !gameField[y][x].isFlag && !isGameStopped){
            gameField[y][x].isOpen = true;
            countClosedTiles --;

            if (gameField[y][x].isMine){
                setCellValueEx(x, y, Color.RED, MINE);
                gameOver();
            } else {
                int countMine = gameField[y][x].countMineNeighbors;
                if (countMine == 0) {
                    setCellValue(x,y,"");
                    for (GameObject gameObject : getNeighbors(gameField[y][x])) {
                        if (!gameObject.isOpen) {
                            openTile(gameObject.x, gameObject.y);
                            }
                        } 
                } else {
                    setCellNumber(x,y, countMine);
                }
                setCellColor (x, y, Color.GREEN);
                score +=5;
                setScore(score);
                if (countClosedTiles== countMinesOnField){
                win();
                }
            }
        }
    }
    @Override
    public void onMouseLeftClick (int x, int y){
        if (!isGameStopped){
            openTile(x, y);
        } else {
            restart();
        }
    }
        @Override
    public void onMouseRightClick (int x, int y){
        markTile(x, y);
    }

    
    private void markTile(int x, int y){
        if (!gameField[y][x].isOpen && !isGameStopped){ 
            if (gameField[y][x].isFlag){
                gameField[y][x].isFlag = false;
                countFlags ++;
                setCellValue(x,y,"");
                setCellColor(x,y,Color.ORANGE);
            } else if (countFlags > 0){
                gameField[y][x].isFlag = true;
                countFlags --;
                setCellValue(x,y, FLAG);
                setCellColor(x,y,Color.YELLOW);
            }
        }
    }
    
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog (Color.YELLOW, "The End" ,Color.BLACK,52);
    }
    private void win(){
        isGameStopped = true;
        showMessageDialog (Color.YELLOW, "Congratulation!!! You win!" ,Color.BLUE,52);
    }
    private void restart(){
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        countMinesOnField = 0;
        setScore(score);
        createGame();
        
    }
    
}

