package com.example.eseo_minesweeper.logic;

import com.example.eseo_minesweeper.exceptions.IllegalGameConstructionException;
import com.example.eseo_minesweeper.logic.Flags.Flag;

import java.util.ArrayList;

public class MinesweeperGame {

    // All possible moves by player
    public enum Action {
        PLAY, SET_WARNING_FLAG, SET_QUESTION_FLAG, REMOVE_FLAG
    }

    // All possible states of the game
    private enum GameState {
        ONGOING, STOPPED, PLAYER_WON, PLAYER_LOST
    }


    private Board gameBoard;
    private GameState gameState = GameState.STOPPED;

    private int noOfBombs;
    private int noOfWarningFlags;



    public MinesweeperGame() {

        noOfBombs = 0;
        noOfWarningFlags = 0;

        gameBoard = new Board(0, 0, 0);

    }


    public void newGame(int rows, int columns, int bombs) throws IllegalGameConstructionException {

        gameBoard = new Board(rows, columns, bombs);
        gameState = GameState.ONGOING;

        noOfBombs = bombs;
        noOfWarningFlags = 0;
    }


    public void play(Cell cell, Action action) {

        if (!gameHasEnded()) {

            switch (action) {

                case PLAY:
                    playCell(cell);
                    break;
                case SET_WARNING_FLAG:
                    putFlagOnCell(cell);
                    break;
                case SET_QUESTION_FLAG:
                    putQuestionOnCell(cell);
                    break;
                case REMOVE_FLAG:
                    removeFlagFromCell(cell);
                    break;
                default:
                    break;
            }
        }

    }


    private void playCell(Cell cell) {

        if (!cell.hasWarningFlag()) {

            if (cell.isBomb()){
                cell.revealCell();
                playerLoses();
            }

            else {
                revealNeighbors(cell);
                if (gameBoard.allNonBombCellsRevealed()) playerWins();
            }

        }

    }


    private void revealNeighbors(Cell cell) {

        ArrayList<Cell> queue = new ArrayList<Cell>();

        if (!cell.isRevealed()) queue.add(cell);

        while (!queue.isEmpty()) {
            Cell current = queue.remove(0);
            current.revealCell();

            if (!current.hasBombNeighbor()){

                for (Cell neighbor : current.getNeighbors()) {

                    if (	!neighbor.isRevealed() 		&&
                            !neighbor.isBomb() 			&&
                            !neighbor.hasWarningFlag()
                    ) {
                        queue.add(neighbor);
                        neighbor.revealCell();
                    }
                }
            }
        }
    }


    private void playerWins() {
        gameState = GameState.PLAYER_WON;
        gameBoard.revealAllBombs();
    }


    public boolean playerWon() {
        return gameState == GameState.PLAYER_WON;
    }


    private void playerLoses() {
        gameState = GameState.PLAYER_LOST;
        gameBoard.revealAllBombs();
    }

    public boolean playerLost() {
        return gameState == GameState.PLAYER_LOST;
    }


    private void removeFlagFromCell(Cell cell) {
        if (cell.hasWarningFlag()) {
            cell.setFlag(Flag.NONE);
            noOfWarningFlags--;
        }
    }

    private void putFlagOnCell(Cell cell) {
        if (!cell.isRevealed()) {
            cell.setFlag(Flag.WARNING);
            noOfWarningFlags++;
        }
    }


    private void putQuestionOnCell(Cell cell) {
        cell.setFlag(Flag.QUESTION);
    }

    public int getBombsMinusWarningFlags() {
        return noOfBombs - noOfWarningFlags;
    }


    public boolean gameHasEnded() {
        return gameState != GameState.ONGOING;
    }



    public Board getBoard() {
        return gameBoard;
    }

}

