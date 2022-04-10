package com.example.eseo_minesweeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.eseo_minesweeper.CellState;
import com.example.eseo_minesweeper.PlayerRanking;
import com.example.eseo_minesweeper.R;
import com.example.eseo_minesweeper.fragments.Cell;
import com.example.eseo_minesweeper.fragments.RowCells;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //---------------------------- Déclaration des variables ----------------------------
    private boolean mute = false;
    private int btState = 2; // 0 -> FLAG || 1 -> ? || 2 -> REVEAL
    private int difficulty = 16;

    //Barre boutons Supérieure
    private ImageButton btMute;
    private ImageButton btRank;
    private ImageButton btRetry;
    private ImageButton btQuit;

    //Barre boutons Inférieure
    private ImageButton btFlag;
    private ImageButton btQstMark;
    private ImageButton btReveal;

    //Info Partie
    private TextView txtVNbBombs;
    private TextView txtVTime;

    //Liste déroulante
    private Spinner spinLevels;

    //MediaPlayer
    private MediaPlayer mediaPlayer;

    //Relatif aux SharedPreferences
    private static final String SHARED_PREFS = "SHARED_PREFS";
    private static final String LIST_PLAYER = "LIST_PLAYER";
    private ArrayList<PlayerRanking> playerRankingList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //Fragment
    private ArrayList<RowCells> listLine;

    //---------------------------- ---------------------------- ----------------------------

    //---------------------------- cycle de vie de l'app ----------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Récupération des éléments
        btMute = findViewById(R.id.btMute);
        btRank = findViewById(R.id.btRanking);
        btRetry = findViewById(R.id.btRetry);
        btQuit = findViewById(R.id.btQuit);

        txtVNbBombs = findViewById(R.id.action_bombs_remaining_textview);
        txtVTime = findViewById(R.id.txtTime);

        spinLevels = findViewById(R.id.spinnerLevel);
        spinLevels.setOnItemSelectedListener(this);

        btFlag = findViewById(R.id.btFlag);
        btFlag.setImageResource(R.drawable.flag_off);

        btQstMark = findViewById(R.id.btQuestionMark);
        btQstMark.setImageResource(R.drawable.question_mark_off);

        btReveal = findViewById(R.id.btReveal);
        btReveal.setImageResource(R.drawable.reveal_on);

        //Paramétrage spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinLevels.setAdapter(adapter);

        //Musique de fond
        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        //Récupération des données des SharedPreferences
        loadSharedData();

        listLine = new ArrayList<>();
        addRowsOfCells();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (RowCells frag : listLine) {
            ft.add(R.id.containerLigne, frag, null);
        }
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBombs();
        setupNbBombsAroundForEachCell();

        //-------------- Son de l'app --------------
        btMute.setOnClickListener(v -> {
            mute = !mute;
            if (mute) {
                mediaPlayer.pause();
                btMute.setImageResource(R.drawable.volume_off);
            }
            else {
                mediaPlayer.start();
                btMute.setImageResource(R.drawable.volume_up);
            }
        });

        //-------------- Affichage du classement --------------
        btRank.setOnClickListener(v -> {
            Intent rankingAct = new Intent(MainActivity.this, RankingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("LIST_PLAYER_RANK", playerRankingList); //ajout de la liste des joueurs
            rankingAct.putExtras(bundle);
            startActivity(rankingAct);
        });

        //-------------- Recommencer la partie --------------
        btRetry.setOnClickListener(v -> {
            //TODO Recommencer une nouvelle partie après validation
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                for (Fragment childFragment : fragment.getChildFragmentManager().getFragments()) {
                    fragment.getChildFragmentManager().beginTransaction().remove(childFragment).commit();
                }
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            addRowsOfCells();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for (RowCells frag : listLine) {
                ft.add(R.id.containerLigne, frag, null);
            }
            ft.commit();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    setupBombs();
                    setupNbBombsAroundForEachCell();
                }
            }, 50);

        });

        //-------------- Quitter l'app --------------
        btQuit.setOnClickListener(v -> finish());

        //-------------- BTs JEU --------------
        btFlag.setOnClickListener(v -> {
            // Passer en mode "FLAG" / Réinitialiser les autres bt
            if (btState != 0) {
                btFlag.setImageResource(R.drawable.flag_on);
                btState = 0; // PASSAGE EN MODE FLAG

                btQstMark.setImageResource(R.drawable.question_mark_off);
                btReveal.setImageResource(R.drawable.reveal_off);
            }
        });

        btQstMark.setOnClickListener(v -> {
            // Passer en mode "?" / Réinitialiser les autres bt
            if (btState != 1) {
                btQstMark.setImageResource(R.drawable.question_mark_on);
                btState = 1; // PASSAGE EN MODE FLAG

                btFlag.setImageResource(R.drawable.flag_off);
                btReveal.setImageResource(R.drawable.reveal_off);
            }
        });

        btReveal.setOnClickListener(v -> {
            // Passer en mode "REVEAL" / Réinitialiser les autres bt
            if (btState != 2) {
                btReveal.setImageResource(R.drawable.reveal_on);
                btState = 2; // PASSAGE EN MODE FLAG

                btFlag.setImageResource(R.drawable.flag_off);
                btQstMark.setImageResource(R.drawable.question_mark_off);
            }
        });

//        setupBombs();
//        setupNbBombsAroundForEachCell();
    }
    //---------------------------- ---------------------------- ----------------------------

    //---------------------------- SharedPreferences methods ----------------------------
    private void saveSharedData() {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(playerRankingList);
        editor.putString(LIST_PLAYER, json);
        editor.apply();
    }

    private void loadSharedData() {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LIST_PLAYER, null);
        playerRankingList = gson.fromJson(json, new TypeToken <ArrayList<PlayerRanking>>(){}.getType());

        if (playerRankingList == null) {
            playerRankingList = new ArrayList<>();
        }
    }
    //---------------------------- ---------------------------- ----------------------------

    //---------------------------- Sélection des niveaux ----------------------------
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Toast.makeText(MainActivity.this, (String) parent.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
        //TODO Changer le mode de jeu après validation
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
    //---------------------------- ---------------------------- ----------------------------

    public void onGameWon(){
        saveSharedData();
    }

    private void addRowsOfCells() {
        listLine.clear();

        for (int nbOfRow = 0; nbOfRow < difficulty; nbOfRow++) {
            listLine.add(RowCells.newInstance(difficulty));
        }
    }

    private void setupBombs() {
        for (int i = 0; i < difficulty; i++) {
            placeRandomBomb();
        }
    }

    private void placeRandomBomb() {
        Random randomGenerator = new Random();
        Cell cell;
        boolean isBombPlaced = false;
        int sizeGame = listLine.size();
        int randomRow, randomColumn;

        while(!isBombPlaced) {
            randomRow = randomGenerator.nextInt(sizeGame);
            randomColumn = randomGenerator.nextInt(sizeGame);

            cell = listLine.get(randomRow).getListCells().get(randomColumn);

            if (cell.getState() != CellState.BOMB.ordinal()) { ;
                cell.becomeBomb();
                isBombPlaced = true;
            }
        }
    }


    private void setupNbBombsAroundForEachCell() {
        int numRow = 0;
        int numCell = 0;

        for (RowCells row : listLine) {
            numCell = 0;

            for (Cell cell : row.getListCells()) {
                int nbBombsAround = calculateBombsAroundForOneCell(numRow, numCell);

                if (!(cell.getState() == CellState.BOMB.ordinal())) {
                    cell.setNbBombAround(nbBombsAround);
                }

                numCell++;
            }
            numRow++;
        }
    }




    private int calculateBombsAroundForOneCell(int numRow, int numCell) {
        int nbBombsAround = 0;
        int sizeGame = listLine.size();

        int minRow = numRow - 1;
        if (minRow < 0) {
            minRow = 0;
        }

        int maxRow = numRow + 1;
        if (maxRow > sizeGame - 1) {
            maxRow = sizeGame - 1;
        }

        int minCol = numCell - 1;
        if (minCol < 0) {
            minCol = 0;
        }

        int maxCol = numCell + 1;
        if (maxCol > sizeGame - 1) {
            maxCol = sizeGame - 1;
        }

        for (int i = minRow; i < maxRow + 1; i++) {
            for (int j = minCol; j < maxCol + 1; j++) {
                if (listLine.get(i).getListCells().get(j).getState() == CellState.BOMB.ordinal()) {
                    nbBombsAround++;
                }
            }
        }

        return nbBombsAround;
    }

    public void showCellUntilDiscoverBomb(Cell cellCliked) {
        int row;
        int column;

        int[] positionCellCliked = findPositionOfCellInBoard(cellCliked);

        row = positionCellCliked[0];
        column = positionCellCliked[1];

        Cell cell;

        int sizeGame = listLine.size();

        int minRow = row - 1;
        if (minRow < 0) {
            minRow = 0;
        }

        int maxRow = row + 1;
        if (maxRow > sizeGame - 1) {
            maxRow = sizeGame - 1;
        }

        int minCol = column - 1;
        if (minCol < 0) {
            minCol = 0;
        }

        int maxCol = column + 1;
        if (maxCol > sizeGame - 1) {
            maxCol = sizeGame - 1;
        }

        for (int i = minRow; i < maxRow + 1; i++) {
            for (int j = minCol; j < maxCol + 1; j++) {
                cell = listLine.get(i).getListCells().get(j);

                if (!(cell.getState() == CellState.BOMB.ordinal()) && !(cell.getState() == CellState.DISCOVERED.ordinal())) {
                    cell.setState(CellState.DISCOVERED.ordinal());
                    cell.affichageValeur();
                    if (cell.getNbBombAround() == 0) {
                        showCellUntilDiscoverBomb(cell);
                    }
                }
            }
        }
    }

    private int[] findPositionOfCellInBoard(Cell cell_) {
        int[] positions = new int[2];

        for (RowCells rowCells : listLine) {
            positions[1] = 0;

            for (Cell cell : rowCells.getListCells()) {
                if (cell == cell_) {
                    return positions;
                }

                positions[1]++;
            }

            positions[0]++;
        }

        return positions;
    }

    public void gameOver() {
        for (RowCells row : this.listLine) {
            for (Cell cell : row.getListCells()) {
                if (!cell.isBomb() || !(cell.getState() == CellState.BOMB.ordinal())) {
                } else {
                    cell.displayBomb();
                }
            }
        }
    }

    public void endGame(Cell cellClicked) {
        boolean isGameEnded = true;

        for (RowCells rowCells : this.listLine) {
            for (Cell cell : rowCells.getListCells()) {
                if (cell.isBomb() || (cell.getState() == CellState.DISCOVERED.ordinal()) || cellClicked == cell) {
                } else isGameEnded = false;
            }
        }
        // TODO : Handle the end of the game
        if (isGameEnded) {
        }
    }

}