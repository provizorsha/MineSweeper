package com.javarush.games.minesweeper;
import com.javarush.engine.cell.*;


public class GameObject {

    
    public int x, y;
    public boolean isMine;
    public int countMineNeighbors;
    public boolean isOpen;
    public boolean isFlag;
    
    public GameObject(int x, int y, boolean isMine) {
    this.x = x;
    this.y = y;
    this.isMine = isMine;
    }
    
}
