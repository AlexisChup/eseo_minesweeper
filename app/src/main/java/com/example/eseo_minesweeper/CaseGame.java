package com.example.eseo_minesweeper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eseo_minesweeper.logic.MinesweeperGame;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CaseGame#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaseGame extends Fragment {
    private static MinesweeperGame game;

    public CaseGame(MinesweeperGame gameS) {
        // Required empty public constructor
        game = gameS;
    }

    public static CaseGame newInstance(MinesweeperGame gameS) {
        CaseGame fragment = new CaseGame(gameS);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_case_game, container, false);
    }
}