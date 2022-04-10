package com.example.eseo_minesweeper;

//Création de la classe représentant une ligne du classement
public class PlayerRanking implements java.io.Serializable{

    //Déclaration des variables
    private String score;
    private String seconds;
    private String minutes;

    //Constructeurs de la classe
    public PlayerRanking(int _score){
        this.score = String.valueOf(_score);
        setSeconds(String.valueOf(_score%60));
        setMinutes(String.valueOf((int) _score/60));
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getScore() {
        return score;
    }

    public String getSeconds() {
        return seconds;
    }

    public String getMinutes() {
        return minutes;
    }
}
