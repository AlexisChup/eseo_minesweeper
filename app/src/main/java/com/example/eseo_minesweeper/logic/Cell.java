package com.example.eseo_minesweeper.logic;
import com.example.eseo_minesweeper.logic.Flags.Flag;

public class Cell {
    private boolean revaled = false;

    private Flag flag = Flag.NONE;
    private Cell[] neighbors = {};

    public void setNeighbors(Cell[] neighbors) {
        this.neighbors = neighbors;
    }
    public Cell[] getNeighbors() {
        return neighbors;
    }

    public int noOfNeighborIsBomb() {
        int noOfNeighborBombs = 0;
        for (Cell neighbor : neighbors) {
            if (neighbor.isBomb()) noOfNeighborBombs++;
        }
        return noOfNeighborBombs;
    }

    public boolean hasBombNeighbor() {
        return noOfNeighborIsBomb() > 0;
    }

    public void revealCell() {
        revaled = true;
    }

    public boolean isRevealed() {
        return revaled;
    }

    public boolean isBomb() {
        return false;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public Flag getFlag() {
        return flag;
    }

    public boolean hasWarningFlag() {
        return flag == Flag.WARNING;
    }
}
