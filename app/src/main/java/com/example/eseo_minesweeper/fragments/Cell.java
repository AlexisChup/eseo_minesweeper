package com.example.eseo_minesweeper.fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eseo_minesweeper.activities.MainActivity;
import com.example.eseo_minesweeper.R;

public class Cell extends Fragment {
    private static final String ARG_NB_CELLS = "nbCells";

    public static final int HIDDEN = 300;
    public static final int REVEALED = 400;
    public static final int QUESTION = 500;
    public static final int FLAG = 600;


    private int nbBombAround = 0;
    private int stateCell = HIDDEN;

    private boolean isGameEnded = false;
    private boolean isBomb = false;

    private Integer nbCellsInGame;
    private ImageView cellImage; // Is the cell image

    public int getStateCell() {
        return stateCell;
    }

    public void setStateCell(int stateCell) {
        this.stateCell = stateCell;
    }

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

        this.cellImage.setOnClickListener(v -> {
            if (!isGameEnded) {
                String userClickAction = getUserClickAction();
                int nbOfFlagLeft = getNbOfFlagLeft();
                switch(userClickAction) {
                    case "flag":
                        if(stateCell != REVEALED && nbOfFlagLeft != 0 && stateCell != FLAG) {
                            displayFlag();
                            setNbOfFlagLeft(nbOfFlagLeft - 1);
                        } else if(stateCell == FLAG) {
                            displayHiddenCell();
                            setNbOfFlagLeft(nbOfFlagLeft + 1);
                        }

                        break;
                    case "question":
                        if(stateCell == HIDDEN) {
                            displayQuestionMark();
                        } else if(stateCell == FLAG) {
                            displayQuestionMark();
                            setNbOfFlagLeft(nbOfFlagLeft + 1);
                        } else if(stateCell == QUESTION) {
                            displayHiddenCell();
                        }

                        break;
                    case "reveal":
                        if (isBomb) {
                            ((MainActivity) getActivity()).displayAllBombs();
                        } else {
                            revealCell();

                            if (getNbBombAround() == 0) {
                                ((MainActivity) getActivity()).showCellUntilDiscoverBomb(this);
                            }
                        }

                        break;
                    default:
                        Log.e("CLICK_CELL", "UserClickAction undefined");
                }

                ((MainActivity) getActivity()).checkGameIsOver(this);
            }
        });


        // Get the same size for every cells
        int widthPixelsScreen = Resources.getSystem().getDisplayMetrics().widthPixels;

        LinearLayout.LayoutParams layout_lp = new LinearLayout.LayoutParams(
                (int)(widthPixelsScreen/ nbCellsInGame),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        view.setLayoutParams(layout_lp);

        return view;
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

    public String getUserClickAction () {
        return (String) getActivity().findViewById(R.id.btReveal).getTag();
    }

    public int getNbOfFlagLeft () {
        TextView tv = (TextView) getActivity().findViewById(R.id.action_bombs_remaining_textview);
        return Integer.parseInt((String) tv.getText());
    }

    public void setNbOfFlagLeft (int nbOfFlagLeft) {
        TextView tv = (TextView) getActivity().findViewById(R.id.action_bombs_remaining_textview);
        tv.setText(String.valueOf(nbOfFlagLeft));
    }

    public void revealCell() {
        this.setStateCell(Cell.REVEALED);
        this.cellImage.setImageResource(getCellImage());
    }

    public void displayHiddenCell () {
        this.setStateCell(Cell.HIDDEN);
        this.cellImage.setImageResource(R.drawable.full_tile);
    }

    public void displayFlag () {
        this.setStateCell(Cell.FLAG);
        this.cellImage.setImageResource(R.drawable.flag_game);
    }

    public void displayQuestionMark () {
        this.setStateCell(Cell.QUESTION);
        this.cellImage.setImageResource(R.drawable.question_mark_on);
    }

    public void displayBomb () {
        this.setStateCell(Cell.REVEALED);
        this.cellImage.setImageResource(R.drawable.bomb);
    }
}