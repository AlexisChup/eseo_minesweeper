package com.example.eseo_minesweeper;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private boolean mute = false;

    private Button btMute;
    private Button btRank;
    private Button btRetry;
    private Button btQuit;

    private Button btFlag;
    private Button btQstMark;
    private Button btReveal;

    private TextView txtVNbBombs;
    private TextView txtVTime;

    private Spinner spinLevels;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Récupération des éléments
        btMute = findViewById(R.id.btMute);
        btRank = findViewById(R.id.btRanking);
        btRetry = findViewById(R.id.btRetry);
        btQuit = findViewById(R.id.btQuit);

        txtVNbBombs = findViewById(R.id.txtNbBombs);
        txtVTime = findViewById(R.id.txtTime);

        spinLevels = findViewById(R.id.spinnerLevel);
        spinLevels.setOnItemSelectedListener(this);

        btFlag = findViewById(R.id.btFlag);
        btQstMark = findViewById(R.id.btQuestionMark);
        btReveal = findViewById(R.id.btReveal);

        //Paramétrage spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.levels, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinLevels.setAdapter(adapter);

        //Musique de fond
        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        mediaPlayer.start();

        mediaPlayer.setLooping(true);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Toast.makeText(MainActivity.this, (String) parent.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        btMute.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                mute = !mute;
                if (mute) {
                    mediaPlayer.pause();
                    btMute.setBackgroundColor(Color.RED);
                }
                else {
                    mediaPlayer.start();
                    btMute.setBackgroundColor(Color.MAGENTA);
                }
            }
        });
    }
}