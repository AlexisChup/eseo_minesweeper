package com.example.eseo_minesweeper.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eseo_minesweeper.R;

import java.util.ArrayList;

public class RowCells extends Fragment {
    private static final String ARG_NUM_COLUMNS = "numColumns";

    private int nbColumns;
    private ArrayList<Cell> listCells;

    public RowCells() {
   }

    public static RowCells newInstance(int numRow) {
        RowCells fragment = new RowCells();
        Bundle args = new Bundle();

        args.putInt(ARG_NUM_COLUMNS, numRow);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.nbColumns = getArguments().getInt(ARG_NUM_COLUMNS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_row_cells, container, false);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        listCells = new ArrayList<>();

        for (int i = 0; i < this.nbColumns; i++) {
            // *2 because nbBombs = nbCol *2
            listCells.add(Cell.newInstance(this.nbColumns * 2));
        }

        for (Cell frag : listCells) {
            ft.add(R.id.containerCellule, frag, null);
        }

        ft.commit();
        return view;
    }

    public ArrayList<Cell> getListCells() {
        return this.listCells;
    }
}