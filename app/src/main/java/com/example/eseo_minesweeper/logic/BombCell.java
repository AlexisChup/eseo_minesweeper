package com.example.eseo_minesweeper.logic;

public class BombCell extends Cell{
    @Override
    public boolean isBomb() {
        return true;
    }
}
