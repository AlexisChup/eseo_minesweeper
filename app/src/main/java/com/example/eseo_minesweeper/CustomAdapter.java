package com.example.eseo_minesweeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<PlayerRanking> playerRankings;

    public CustomAdapter(List<PlayerRanking> playerRanking){
        this.playerRankings = playerRanking;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.text_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.display(playerRankings.get(position));
    }

    @Override
    public int getItemCount() { return playerRankings.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mRank;
        private TextView mPlayerName;
        private TextView mTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mRank = itemView.findViewById(R.id.textViewRank);
            mPlayerName = itemView.findViewById(R.id.textViewPlayer);
            mTime = itemView.findViewById(R.id.textViewTime);
        }

        public void display(PlayerRanking playerRanking){
            mRank.setText(playerRanking.getRank());
            mPlayerName.setText(playerRanking.getName());
            mTime.setText(playerRanking.getTime());
        }
    }
}