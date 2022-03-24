package com.example.eseo_minesweeper.display;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.example.eseo_minesweeper.BoardAdapter;
import com.example.eseo_minesweeper.R;
import com.example.eseo_minesweeper.SettingsFragment;
import com.example.eseo_minesweeper.exceptions.IllegalGameConstructionException;
import com.example.eseo_minesweeper.logic.Cell;
import com.example.eseo_minesweeper.logic.MinesweeperGame;
import com.example.eseo_minesweeper.logic.MinesweeperGame.Action;

public class MinesweeperFragment extends Fragment {

    private static MinesweeperGame game;

    public MinesweeperFragment() {
        this.game = new MinesweeperGame();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main, container, false);
        GridView boardGridView = (GridView) view.findViewById(R.id.boardGridView);

        boardGridView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Cell cell = (Cell) parent.getAdapter().getItem(position);
                clickCell(cell, Action.PLAY);
            }

        });

        boardGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                Cell cell = (Cell) parent.getAdapter().getItem(position);

                if (cell.hasWarningFlag()) {
                    clickCell(cell, MinesweeperGame.Action.REMOVE_FLAG);
                }
                else {
                    clickCell(cell, MinesweeperGame.Action.SET_WARNING_FLAG);
                }

                return true;
            }
        });

        getActivity().invalidateOptionsMenu(); // called to center remaining_bombs_textview when returning from settings

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        if (game.gameHasEnded()) {
            startNewGame();
        }
        else{
            attachGameToGridView();
            refreshGameViews();
        }
    }


    public void startNewGame() {
        if (isAdded()) {
            try {

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

                //int rows = Integer.parseInt(sharedPref.getString(SettingsFragment.KEY_PREF_ROWS, "10"));
                //int columns = Integer.parseInt(sharedPref.getString(SettingsFragment.KEY_PREF_COLUMNS, "10"));
                //int bombs = Integer.parseInt(sharedPref.getString(SettingsFragment.KEY_PREF_BOMBS, "10"));

                //game.newGame(rows, columns, bombs);
                game.newGame(10, 10, 10);

                attachGameToGridView();
                refreshGameViews();

            } catch (IllegalGameConstructionException e) {
                createTooManyBombsDialog();
                e.printStackTrace();
            }

        }
    }

    public int remainingBombs() {
        return game.getBombsMinusWarningFlags();
    }


    private void attachGameToGridView() {
        GridView boardGridView = (GridView) getView().findViewById(R.id.boardGridView);
        boardGridView.setNumColumns(game.getBoard().getCells()[0].length);
        boardGridView.setAdapter(new BoardAdapter(getActivity(), game.getBoard()));
    }


    private void refreshGameViews() {
        refreshGameBoard();
        refreshRemainingBombsText();
    }


    private void refreshRemainingBombsText() {

        if (getActivity().findViewById(R.id.action_bombs_remaining_textview) != null) {

            TextView remainingBombsTextView = (TextView) getActivity().findViewById(R.id.action_bombs_remaining_textview);
            remainingBombsTextView.setText(String.valueOf(remainingBombs()));
        }
    }


    private void refreshGameBoard() {
        GridView boardGridView = (GridView) getView().findViewById(R.id.boardGridView);
        boardGridView.invalidateViews();
    }


    private void clickCell(Cell cell, Action action) {

        if (!game.gameHasEnded()) {
            game.play(cell, action);

            // Checked again to see if players move ended the game
            if (game.gameHasEnded()) {
                if (game.playerLost()) createPlayerLostDialog();
                if (game.playerWon()) createPlayerWonDialog();
            }
        }

        refreshGameViews();
    }


    private void createPlayerWonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You Won").setTitle("Game Over");
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void createPlayerLostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You Lost").setTitle("Game Over");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createTooManyBombsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Set number of bombs to less than the number of cells.").setTitle("More bombs than cells.");
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
