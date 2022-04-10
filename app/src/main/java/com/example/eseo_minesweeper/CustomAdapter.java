package com.example.eseo_minesweeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Création d'une class CustomAdapter permettant l'affichage du classement
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    //Déclaration d'un liste d'objet PlayerRanking
    private List<PlayerRanking> playerRankings;

    //Constructeur de la classe
    public CustomAdapter(List<PlayerRanking> playerRanking){
        this.playerRankings = playerRanking;
    }

    @NonNull
    @Override
    //Paramétrage de la View à sa création
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.text_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    //Permet d'ajouter à la vue l'ensemble des lignes
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.display(playerRankings.get(position));
    }

    @Override
    //Retourne le nombre d'objet contenu dans la liste
    public int getItemCount() { return playerRankings.size(); }

    //Création d'une classe MyViewHolder représentant une ligne du classement
    public class MyViewHolder extends RecyclerView.ViewHolder {
        //Déclaration des TextView
        private TextView mScore;
        private TextView mSeconds;
        private TextView mMinutes;

        //Constructeur de la classe
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mScore = itemView.findViewById(R.id.textViewScore);
            mSeconds = itemView.findViewById(R.id.textViewSeconds);
            mMinutes = itemView.findViewById(R.id.textViewMinutes);
        }

        //On affiche les infos correspondantes à chaque TextView
        public void display(PlayerRanking playerRanking){
            mScore.setText(playerRanking.getScore());
            mSeconds.setText(playerRanking.getSeconds());
            mMinutes.setText(playerRanking.getMinutes());
        }
    }
}