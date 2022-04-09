package com.example.eseo_minesweeper.fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eseo_minesweeper.CellState;
import com.example.eseo_minesweeper.activities.MainActivity;
import com.example.eseo_minesweeper.R;

public class Cell extends Fragment {
    private static final String ARG_NB_CELLS = "nombreCellules";

    private Integer nbCellsInGame;

    private ImageView cellImage; // Is the cell image
    private int state = CellState.HIDDEN.ordinal();
    private boolean gameOver = false;
    private boolean isBomb = false;
    private int nbBombAround = 0;
    private static final int HIDDEN = 10;

    public int getNbBombAround() {
        return nbBombAround;
    }

    public void setNbBombAround(int nbBombAround) {
        this.nbBombAround = nbBombAround;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public Cell() {
        // Required empty public constructor
    }

    public static Cell newInstance(Integer nbCellsInGame_) {
        Cell fragment = new Cell();
        Bundle args = new Bundle();

        args.putInt(ARG_NB_CELLS, nbCellsInGame_);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nbCellsInGame = getArguments().getInt(ARG_NB_CELLS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cell, container, false);
        this.cellImage = view.findViewById(R.id.cell);
        this.cellImage.setOnLongClickListener(v -> {
            if (this.getState() == CellState.FLAG.ordinal()) {
                this.cellImage.setImageResource(R.drawable.full_tile);
                if (isBomb()) {
                    this.setState(CellState.BOMB.ordinal());
                }
                else this.setState(CellState.HIDDEN.ordinal());
                this.cellImage.getDrawable();
            } else {
                this.cellImage.setImageResource(R.drawable.flag_game);
                this.setState(CellState.FLAG.ordinal());
            }


            return true;
        });
        this.cellImage.setOnClickListener(v -> {
            if (!gameOver) {
                if (this.getState() == CellState.BOMB.ordinal()) {
                    ((MainActivity) getActivity()).gameOver();
                } else {
                    affichageValeur();
                    if (getNbBombAround() == 0)
                        ((MainActivity) getActivity()).showCellUntilDiscoverBomb(this);
                }
                ((MainActivity) getActivity()).endGame(this);
            }
        });


        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams layout_lp = new LinearLayout.LayoutParams(
                (int)(width/ nbCellsInGame), LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layout_lp);
        return view;
    }


    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
        if (state == CellState.DISCOVERED.ordinal()) {
            affichageValeur();
        }
    }

    public void becomeBomb() {
        setState(CellState.BOMB.ordinal());
        setBomb(true);
    }

    public void displayBomb() {
        if (this.getState() == CellState.BOMB.ordinal()) {
            gameOver = true;
            this.cellImage.setImageResource(R.drawable.bomb);
        }
    }

    public int getCellImage() {
        switch (getNbBombAround()) {
            case 0:
                return R.drawable.discovered_tile;
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
            case HIDDEN:
                return R.drawable.full_tile;
            default:
                return R.drawable.full_tile;
        }
    }

    public void affichageValeur() {
        this.cellImage.setImageResource(getCellImage());
    }

}