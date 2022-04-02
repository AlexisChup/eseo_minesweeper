package com.example.eseo_minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.eseo_minesweeper.display.MinesweeperFragmentOld;
import com.example.eseo_minesweeper.exceptions.IllegalGameConstructionException;
import com.example.eseo_minesweeper.logic.MinesweeperGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //---------------------------- Déclaration des variables ----------------------------
    private boolean mute = false;
    private int btState = 2; // 0 -> FLAG || 1 -> ? || 2 -> REVEAL

    //Barre boutons Haute
    private ImageButton btMute;
    private ImageButton btRank;
    private ImageButton btRetry;
    private ImageButton btQuit;

    //Barre boutons Basse
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
    private MinesweeperFragmentOld minesweeperFragmentOld;
    private List<CaseGame> caseGames;
    private static MinesweeperGame game;

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
//        minesweeperFragmentOld = new MinesweeperFragmentOld();

        game = new MinesweeperGame();
        try {
            game.newGame(10, 10, 10);
        } catch (IllegalGameConstructionException e) {
            e.printStackTrace();
        }
        caseGames = new ArrayList<>();
        addCases();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, caseGames.get(0));
//        for(CaseGame q : caseGames) {
//            ft.add(R.id.boardGridView,q);
//        }
        ft.commit();


    }

    private void addCases() {
        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {
                caseGames.add(CaseGame.newInstance(game));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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

        //Grid Fragment
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.boardGridView, minesweeperFragmentOld)
//                .commit();


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

}