package com.example.simone.whatwatch;

/**
 * Created by raimo on 31/07/2017.
 */

// Classe per definire la tabella, in questo caso definisce le colonne che contengono i dati di un film


public class FilmDescriptionDB {

    private int id;
    private String name;

    public FilmDescriptionDB() {};

    public FilmDescriptionDB(String name) {
        this.name = name;
    }

    public FilmDescriptionDB(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setBoth(int id, String name){
        this.id = id;
        this.name = name;
    }
}
