package com.example.simone.whatwatch;

/**
 * Created by raimo on 31/07/2017.
 */

public class WatchListDB {

    //Costanti del db
    public static final String DB_NAME = "watchlist.db";
    public static final int DB_VERSION = 1;

    //Costanti della tabella FilmDescription
    public static final String FILM_TABLE = "film_data";

    public static final String FILM_ID = "film_id";
    public static final int FILM_ID_COL = 0;

    public static String FILM_NAME = "film_name";
    public static final int FILM_NAME_COL = 1;

    public static final String CREATE_FILM_TABLE =
            "CREATE TABLE" + FILM_TABLE + " (" + FILM_ID + " TEXT   NOT NULL UNIQUE);";

    public static final String DROP_FILM_TABLE =
            "DROP TABLE IF EXISTS" + FILM_TABLE;



}
