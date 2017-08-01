package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;


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

    public static String FILM_TYPE = "film_type";
    public static final int FILM_TYPE_COL = 2;

    public static String FILM_EP = "film_episode";
    public static final int FILM_EP_COL = 3;

    public static String FILM_WATCHED = "film_watched";
    public static final int FILM_WATCHED_COL = 4;

    public static String FILM_IMG_URL = "film_img_url";
    public static final int FILM_IMG_URL_COL = 5;



    public static final String CREATE_FILM_TABLE =
            "CREATE TABLE " + FILM_TABLE + " (" + FILM_ID + " INTEGER PRIMARY KEY, " + FILM_NAME + " TEXT   NOT NULL UNIQUE, " + FILM_TYPE + " TEXT NOT NULL, " +
                    FILM_EP + " INTEGER, " + FILM_WATCHED + " INTEGER NOT NULL, " + FILM_IMG_URL + " TEXT);";

    public static final String DROP_FILM_TABLE =
            "DROP TABLE IF EXISTS " + FILM_TABLE;



    //database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    //costruttore della classe
    public WatchListDB(Context context){
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }


    //metodo per aprire il database in sola lettura
    private void openReadableDB(){
        db = dbHelper.getReadableDatabase();
    }

    //metodo per aprire il dababase in scrittura/lettura
    private void openWriteableDB(){
        db = dbHelper.getWritableDatabase();
    }

    //metodo per la chiusura
    private void closeDB(){
        if(db != null)
            db.close();
    }




    //                                  //
    //METODI PER ESTRARRE I DATI DAL DB //
    //                                  //

    //Get dati di un film in base all'id
    public FilmDescriptionDB getFilmById(int id){
        String where = FILM_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(FILM_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        FilmDescriptionDB film = getFilmFromCursor(cursor);
        if(cursor != null)
            cursor.close();
        this.closeDB();

        return film;
    }


    //Get dei dati di un film in base al nome
    public FilmDescriptionDB getFilm(String name){
        String where = FILM_NAME + "= ?";
        String[] whereArgs = {name};

        this.openReadableDB();
        Cursor cursor = db.query(FILM_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        FilmDescriptionDB film = getFilmFromCursor(cursor);
        if(cursor != null)
            cursor.close();
        this.closeDB();

        return film;
    }


    //Get di tutti i dati
    public ArrayList<FilmDescriptionDB> getAll(){
        this.openReadableDB();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FILM_TABLE, null);

        cursor.moveToFirst();

        ArrayList<FilmDescriptionDB> films = new ArrayList<FilmDescriptionDB>();

        while(cursor.moveToNext()){
            films.add(getFilmFromCursor(cursor));
        }

        if (cursor != null){
            cursor.close();
        }
        this.closeDB();

        return films;
    }


    //Estrazione del campo img_url dal db
    public String getImgUrl(int id){
        String where = FILM_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};
        String[] column = {FILM_IMG_URL};

        this.openReadableDB();
        Cursor cursor = db.query(FILM_TABLE, column, where, whereArgs, null, null, null);
        this.closeDB();

        if(cursor == null || cursor.getCount() == 0){
            return null;
        }
        else{
            try{
                return cursor.getString(FILM_IMG_URL_COL);
            }
            catch (Exception e){
                return null;
            }
        }
    }

    //                                  //
    //METODI PER MODIFICARE IL DATABASE //
    //                                  //
    public long insertFilm(FilmDescriptionDB film){
        ContentValues in = new ContentValues();
        in.put(FILM_ID, film.getId());
        in.put(FILM_NAME, film.getName());
        in.put(FILM_TYPE , film.getType());
        in.put(FILM_EP , film.getEpisode());
        in.put(FILM_WATCHED , film.getWatched());
        in.put(FILM_IMG_URL , film.getImg());

        this.openWriteableDB();
        long rowID = db.insert(FILM_TABLE, null, in);
        this.closeDB();

        return rowID;
    }


    public int updateFilm(FilmDescriptionDB film){
        ContentValues up = new ContentValues();
        up.put(FILM_NAME, film.getName());
        up.put(FILM_ID, film.getId());

        String where =  FILM_ID + "=?";
        String[] whereArgs = {String.valueOf(film.getId())};

        this.openWriteableDB();
        int rowCount = db.update(FILM_TABLE, up, where, whereArgs);
        this.closeDB();

        return rowCount;
    }


    public int deleteFilm(int id){
        String where = FILM_ID + "=?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(FILM_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }


    //                                          //
    //FUNZIONE PER ESTRARRE I DATI DAL CURSORSE //
    //                                          //
    private static FilmDescriptionDB getFilmFromCursor(Cursor cursor){
        if(cursor == null || cursor.getCount() == 0){
            return null;
        }
        else{
            try {
                FilmDescriptionDB film = new FilmDescriptionDB(
                        cursor.getInt(FILM_ID_COL),
                        cursor.getString(FILM_NAME_COL),
                        cursor.getString(FILM_TYPE_COL),
                        cursor.getInt(FILM_EP_COL),
                        cursor.getInt(FILM_WATCHED_COL),
                        cursor.getString(FILM_IMG_URL_COL)
                );
                return film;
            }
            catch (Exception e){
                return  null;
            }
        }
    }

}
