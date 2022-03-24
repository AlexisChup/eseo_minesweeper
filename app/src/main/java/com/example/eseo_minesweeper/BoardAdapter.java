package com.example.eseo_minesweeper;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.eseo_minesweeper.logic.Board;
import com.example.eseo_minesweeper.logic.Cell;

public class BoardAdapter extends BaseAdapter {

    private Context context;

    private Cell[] cellArray;


    public BoardAdapter(Context c, Board board) {

        context = c;

        ArrayList<Cell> cells = new ArrayList<Cell>();

        for (Cell[] row : board.getCells()){
            for (Cell cell : row){
                cells.add(cell);
            }
        }

        this.cellArray = new Cell[cells.size()];
        cells.toArray(this.cellArray);
    }

    @Override
    public int getCount() {
        return cellArray.length;
    }

    @Override
    public Object getItem(int position) {
        return cellArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new SquareImageView(context);


        int resId = getImageResourceForCell(cellArray[position]);
        imageView.setImageResource(resId);


        return imageView;
    }

    private int getImageResourceForCell(Cell cell) {

        if (cell.isRevealed()){

            if (cell.isBomb()) return R.drawable.bomb;
            if (cell.hasBombNeighbor()) {

                switch (cell.noOfNeighborIsBomb()) {
                    case 1:
                        return R.drawable.one;
                    case 2:
                        return R.drawable.two;
                    case 3:
                        return R.drawable.three;
                    case 4:
                        return R.drawable.four;
                    case 5:
                        return R.drawable.five;
                    case 6:
                        return R.drawable.six;
                    case 7:
                        return R.drawable.seven;
                    case 8:
                        return R.drawable.eight;
                    default:
                        return R.drawable.discovered_tile;
                }

            }
            return R.drawable.discovered_tile;
        }
        if (cell.hasWarningFlag()) return R.drawable.flag_game;

        return R.drawable.full_tile;
    }


}
