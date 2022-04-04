package com.example.eseo_minesweeper;

//Création de la classe représentant une ligne du classement
public class PlayerRanking implements java.io.Serializable{

    //Déclaration des variables
    private String rank;
    private String name;
    private String time;

    //Constructeurs de la classe
    public PlayerRanking(String _rank, String _name, String _time){
        this.rank = _rank;
        this.name = _name;
        this.time = _time;
    }

    //Les Setters et Getters pour chaque variable
    public void setName(String name) {
        this.name = name;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

}
