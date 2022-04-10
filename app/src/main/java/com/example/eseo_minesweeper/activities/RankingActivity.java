package com.example.eseo_minesweeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eseo_minesweeper.CustomAdapter;
import com.example.eseo_minesweeper.PlayerRanking;
import com.example.eseo_minesweeper.R;

import java.util.ArrayList;
import java.util.List;

//Activity représentant le classement
public class RankingActivity extends AppCompatActivity {

    //Déclaration des variables
    private ImageButton btBack;

    private RecyclerView mRecyclerView;
    private List<PlayerRanking> playerRankingList;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        //Association des variables à leur view dans l'activité
        btBack = findViewById(R.id.btBack);
        mRecyclerView = findViewById(R.id.recyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Récupération de l'Intent à partir de l'activity MainActivity
        Intent mainActivityIntent = getIntent();
        //Récupération des données incluses dans l'Intent de l'activité
        Bundle bundle = mainActivityIntent.getExtras();
        //Récupération des données sérialisées grâce à la clé "LIST_PLAYER_RANKING" (créée dans MainActivity)
        playerRankingList = (ArrayList<PlayerRanking>) bundle.getSerializable("LIST_PLAYER_RANK");
        Log.e("what", String.valueOf(playerRankingList.size()));

        //On instancie un nouveau CustomAdapter à partir des données récupérées
        customAdapter = new CustomAdapter((playerRankingList));

        //On ajoute dans le recyclerView de l'Activité le customAdapter instancié
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(customAdapter);

        //On quitte l'activité lors du clic
        btBack.setOnClickListener(v -> finish());
    }

    //TODO Créer le classement

}