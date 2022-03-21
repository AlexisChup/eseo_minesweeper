package com.example.eseo_minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private ImageButton btBack;

    private RecyclerView mRecyclerView;
    private List<PlayerRanking> playerRankingList;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        btBack = findViewById(R.id.btBack);

        mRecyclerView = findViewById(R.id.recyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent mainActivityIntent = getIntent();
        Bundle bundle = mainActivityIntent.getExtras();
        playerRankingList = (ArrayList<PlayerRanking>) bundle.getSerializable("LIST_PLAYER_RANK");

        customAdapter = new CustomAdapter((playerRankingList));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(customAdapter);

        btBack.setOnClickListener(v -> finish());
    }

    //TODO Créer le classement

}