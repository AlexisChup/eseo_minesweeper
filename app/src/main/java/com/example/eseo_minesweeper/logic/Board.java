package com.example.eseo_minesweeper.logic;
import java.util.ArrayList;
import java.util.Collections;

public class Board {
    Cell[][] cells;

    public Board(int rows, int columns, int noOfBombs) {

        int noOfCells = rows * columns;

        if ( noOfBombs > noOfCells ) {
            //TODO: Handle error
        }

        ArrayList<Cell> cellList = createCellList(noOfBombs, noOfCells);

        Collections.shuffle(cellList);


        cells = new Cell[rows][columns];

        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[row].length; column++) {
                cells[row][column] = cellList.remove(0);
            }
        }

        setCellNeighbors();

    }



    private void setCellNeighbors() {

        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[row].length; column++) {

                ArrayList<Cell> neighborsList = new ArrayList<Cell>();

                for (int neighborRow = row - 1; neighborRow < row + 2; neighborRow++) {
                    for (int neighborColumn = column - 1; neighborColumn < column + 2; neighborColumn++) {

                        if (	neighborRow >= 0 && neighborRow < cells.length
                                && neighborColumn >= 0 && neighborColumn < cells[row].length &&
                                cells[neighborRow][neighborColumn] != cells[row][column]){

                            neighborsList.add(cells[neighborRow][neighborColumn]);

                        }
                    }
                }

                Cell[] neighborsArray = new Cell[neighborsList.size()];
                neighborsList.toArray(neighborsArray);
                cells[row][column].setNeighbors(neighborsArray);

            }
        }
    }





    private ArrayList<Cell> createCellList(int noOfBombs, int noOfCells) {
        ArrayList<Cell> cellList = new ArrayList<Cell>();

        for (int i = 0; i < noOfCells - noOfBombs; i++) {
            cellList.add(new Cell());
        }
        for (int i = 0; i < noOfBombs; i++) {
            cellList.add(new BombCell());
        }
        return cellList;
    }



    public void revealAllBombs() {

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.isBomb()) cell.revealCell();
            }
        }
    }

    public boolean allNonBombCellsRevealed() {

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (!cell.isRevealed() && !cell.isBomb()) {
                    return false;
                }
            }
        }

        return true;
    }


    public Cell[][] getCells() {
        return cells;
    }


}
