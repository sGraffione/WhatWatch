package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Objects;


public class Database {

    //Costanti del db
    public static final String DB_NAME = "database.db";
    public static final int DB_VERSION = 1;

    //Costanti tabella Film
    public static final String FIL_TABLE = "film_data";

    public static String FILM_ROW = "film_row";
    public static final int FILM_ROW_COL = 0;

    public static String FILM_ID = "film_id";
    public static final int FILM_ID_COL = 1;

    public static String FILM_NAME = "film_name";
    public static final int FILM_NAME_COL = 2;

    public static String FILM_WATCHED = "film_watched";
    public static final int FILM_WATCHED_COL = 3;

    public static String FILM_IMG_URL = "film_img_url";
    public static final int FILM_IMG_URL_COL = 4;

    //public static String TIME_TAG = "time";
    //public static final int TIME_TAG_COL = 5;


    //Costanti tabella Tv
    public static final String TV_TABLE = "tv_data";

    public static String TV_ROW = "tv_row";
    public static final int TV_ROW_COL = 0;

    public static String TV_ID_SERIES = "tv_id_series";
    public static final int TV_ID_SERIES_COL = 1;

    public static String TV_ID_SEASON = "tv_id_season";
    public static final int TV_ID_SEASON_COL = 2;

    public static String TV_NAME = "tv_name";
    public static final int TV_NAME_COL = 3;

    public static String TV_EPISODE_CURRENT = "tv_episode_current";
    public static final int TV_EPISODE_CURRENT_COL = 4;

    public static String TV_EPISODE_MAX = "tv_episode_max";
    public static final int TV_EPISODE_MAX_COL = 5;

    public static String TV_SEASON_CURRENT = "tv_season_current";
    public static final int TV_SEASON_CURRENT_COL = 6;

    public static String TV_SEASON_MAX = "tv_season_max";
    public static final int TV_SEASON_MAX_COL = 7;

    public static String TV_WATCHED = "tv_watched";
    public static final int TV_WATCHED_COL = 8;

    public static String TV_IMG_URL_SERIES = "tv_img_url_series";
    public static int TV_IMG_URL_SERIES_COL = 9;

    public static String TV_IMG_URL_SEASON = "tv_img_url_season";
    public static int TV_IMG_URL_SEASON_COL = 10;

   // public static String TIME = "time";
    //public static final int TIME_COL = 11;


    //Query di creazione e drop delle due tabelle
    public static final String CREATE_FIL_TABLE =
            "CREATE TABLE " + FIL_TABLE + " (" + FILM_ROW + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FILM_ID + " INTEGER NOT NULL UNIQUE, " +
                    FILM_NAME + " STRING NOT NULL, " + FILM_WATCHED + " INTEGER NOT NULL, " + FILM_IMG_URL + " STRING);";

    public static final String DROP_FILM_TABLE = "DROP TABLE IF EXISTS " + FIL_TABLE;


    public static final String CREATE_TV_TABLE =
            "CREATE TABLE " + TV_TABLE + " (" + TV_ROW + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TV_ID_SERIES + " INTEGER NOT NULL, " +
                    TV_ID_SEASON + " INTEGER NOT NULL, "+ TV_NAME + " STRING NOT NULL, " + TV_EPISODE_CURRENT + " INTEGER NOT NULL, " +
                    TV_EPISODE_MAX + " INTEGER NOT NULL, " + TV_SEASON_CURRENT + " INTEGER NOT NULL, " +
                    TV_SEASON_MAX + " INTEGER NOT NULL, " + TV_WATCHED + " INTEGER NOT NULL, " + TV_IMG_URL_SERIES + " STRING, " +
                    TV_IMG_URL_SEASON + " STRING);";

    public static final String DROP_TV_TABLE = "DROP TABLE IF EXISTS " + TV_TABLE;


    //database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    //costruttore della classe
    public Database(Context context){
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
    public Film getFilmById(int id){
        String where = FILM_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(FIL_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Film film = getFilmFromCursor(cursor);
        if(cursor != null)
            cursor.close();
        this.closeDB();

        return film;
    }


    //Get dati di un film in base all'id
    public Tv getTvById(int id_series, int id_season){
        String where = TV_ID_SERIES + "= ? AND " + TV_ID_SEASON + "= ?";
        String[] whereArgs = {Integer.toString(id_series), Integer.toString(id_season)};

        this.openReadableDB();
        Cursor cursor = db.query(TV_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Tv tv = getTvFromCursor(cursor);
        if(cursor != null)
            cursor.close();
        this.closeDB();

        return tv;
    }


    public ArrayList<Film> getAllFilms(){
        this.openReadableDB();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FIL_TABLE, null);
        ArrayList<Film> films = new ArrayList<Film>();

        while(cursor.moveToNext()){
            films.add(getFilmFromCursor(cursor));
        }

        if (cursor != null){
            cursor.close();
        }
        this.closeDB();

        return films;
    }


    public ArrayList<Tv> getAllTv(){
        this.openReadableDB();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TV_TABLE, null);
        ArrayList<Tv> series = new ArrayList<Tv>();

        while(cursor.moveToNext()){
            series.add(getTvFromCursor(cursor));
        }

        if (cursor != null){
            cursor.close();
        }
        this.closeDB();

        return series;
    }


    public ArrayList<Object> getAllData(){
        this.openReadableDB();
        Cursor cursorFilms = db.rawQuery("SELECT * FROM " + FIL_TABLE, null);
        Cursor cursorSeries = db.rawQuery("SELECT * FROM " + TV_TABLE, null);
        ArrayList<Object> data = new ArrayList<>();

        while(cursorFilms.moveToNext()){
            data.add(getFilmFromCursor(cursorFilms));
        }

        while(cursorSeries.moveToNext()){
            data.add(getTvFromCursor(cursorSeries));
        }

        if (cursorFilms != null || cursorSeries != null){
            cursorFilms.close();
            cursorSeries.close();
        }
        this.closeDB();

        return data;
    }


    public ArrayList<Object> getFilter(int watched, String type, String order){
        this.openReadableDB();
        Cursor cursorFilm = null;
        Cursor cursorSeries = null;
        ArrayList<Object> elements = new ArrayList<Object>();

        if(type.equals("Movie")) {
            String where = FILM_WATCHED + "= ?";
            String[] whereArgs = {Integer.toString(watched)};
            if (order.equals("Recent")) {
                cursorFilm = db.query(FIL_TABLE, null, where, whereArgs, null, null, FILM_ROW + " DESC");
            } else {
                cursorFilm = db.query(FIL_TABLE, null, where, whereArgs, null, null, FILM_NAME);
            }
        } else if(type.equals("Tv_shows")){
            String where = TV_WATCHED + "= ?";
            String[] whereArgs = {Integer.toString(watched)};
            if (order.equals("Recent")) {
                cursorSeries = db.query(TV_TABLE, null, where, whereArgs, null, null, TV_ROW + " DESC");
            } else {
                cursorSeries = db.query(TV_TABLE, null, where, whereArgs, null, null, TV_NAME);
            }
        } else{
            String where = FILM_WATCHED + "= ?";
            String whereTv = TV_WATCHED + "= ?";
            String[] whereArgs = {Integer.toString(watched)};
            if (order.equals("Recent")) {
                cursorFilm = db.query(FIL_TABLE, null, where, whereArgs, null, null, FILM_ROW + " DESC");
                cursorSeries = db.query(TV_TABLE, null, whereTv, whereArgs, null, null, TV_ROW + " DESC");
            } else {
                cursorFilm = db.query(FIL_TABLE, null, where, whereArgs, null, null, FILM_NAME);
                cursorSeries = db.query(TV_TABLE, null, whereTv, whereArgs, null, null, TV_NAME);
            }
        }


        if (type.equals("Movie")) {
            while (cursorFilm.moveToNext()) {
                elements.add(getFilmFromCursor(cursorFilm));
            }
        } else if(type.equals("Tv_shows")){
            while (cursorSeries.moveToNext()) {
                elements.add(getTvFromCursor(cursorSeries));
            }
        } else{
            while (cursorFilm.moveToNext()) {
                elements.add(getFilmFromCursor(cursorFilm));
            }
            while (cursorSeries.moveToNext()) {
                elements.add(getTvFromCursor(cursorSeries));
            }
        }

        if (cursorFilm != null || cursorSeries != null){
            cursorFilm.close();
            cursorSeries.close();
        }
        this.closeDB();

        return elements;

    }


    public int getWatched(int id){
        String where = FILM_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(FIL_TABLE, null, where, whereArgs, null, null, null);


        if(cursor == null || cursor.getCount() == 0){
            this.closeDB();
            return 0;
        }
        else{
            try{
                int watched = cursor.getInt(FILM_WATCHED_COL);
                cursor.close();
                this.closeDB();
                return watched;
            }
            catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }
    }


    public int getWatched(int idSeries, int idSeason){
        String where = TV_ID_SERIES + "= ? AND " + TV_ID_SEASON + "= ?";
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};

        this.openReadableDB();
        Cursor cursor = db.query(TV_TABLE, null, where, whereArgs, null, null, null);


        if(cursor == null || cursor.getCount() == 0){
            this.closeDB();
            return 0;
        }
        else{
            try{
                int watched = cursor.getInt(TV_WATCHED_COL);
                cursor.close();
                this.closeDB();
                return watched;
            }
            catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }
    }


    public String getImgUrl(int id){
        String where = FILM_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};
        String[] column = {FILM_IMG_URL};

        this.openReadableDB();
        Cursor cursor = db.query(FIL_TABLE, column, where, whereArgs, null, null, null);

        if(cursor == null || cursor.getCount() == 0){
            cursor.close();
            this.closeDB();
            return null;
        }
        else{
            try{
                String url = cursor.getString(FILM_IMG_URL_COL);
                cursor.close();
                this.closeDB();
                return url;
            }
            catch (Exception e){
                return null;
            }
        }
    }


    public String getImgUrl(int idSeries, int idSeason, String img){
        String where = TV_ID_SERIES + "= ? AND " + TV_ID_SEASON + "= ?";
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};
        String[] column = {TV_IMG_URL_SERIES, TV_IMG_URL_SEASON};

        this.openReadableDB();
        Cursor cursor = db.query(TV_TABLE, column, where, whereArgs, null, null, null);

        if(cursor == null || cursor.getCount() == 0){
            cursor.close();
            this.closeDB();
            return null;
        }
        else{
            try{
                if(img == "season") {
                    String url = cursor.getString(TV_IMG_URL_SEASON_COL);
                    cursor.close();
                    this.closeDB();
                    return url;
                } else{
                    String url = cursor.getString(TV_IMG_URL_SERIES_COL);
                    cursor.close();
                    this.closeDB();
                    return url;
                }
            }
            catch (Exception e){
                return null;
            }
        }
    }


    public int verifyId(int id){
        int flag;
        String where = FILM_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(FIL_TABLE, null, where, whereArgs, null, null, null);
        if(cursor.getCount() == 0){
            flag = 0;
            this.closeDB();
            return flag;
        }else{
            flag = 1;
            cursor.close();
            this.closeDB();
            return flag;
        }
    }


    public int verifyId(int idSeries, int idSeason){
        int flag;
        String where = TV_ID_SERIES + "= ? AND " + TV_ID_SEASON + "= ?";
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};

        this.openReadableDB();
        Cursor cursor = db.query(TV_TABLE, null, where, whereArgs, null, null, null);
        if(cursor.getCount() == 0){
            flag = 0;
            this.closeDB();
            return flag;
        }else{
            flag = 1;
            cursor.close();
            this.closeDB();
            return flag;
        }
    }


    public long insertFilm(Film film){
        ContentValues in = new ContentValues();
        in.put(FILM_ID, film.getId());
        in.put(FILM_NAME, film.getName());
        in.put(FILM_WATCHED, film.getWatched());
        in.put(FILM_IMG_URL, film.getImgUrl());
        //in.put(TIME_TAG, System.currentTimeMillis()/1000);

        this.openWriteableDB();
        long rowID = db.insert(FIL_TABLE, null, in);
        this.closeDB();

        return rowID;
    }


    public long insertSeries(Tv series){
        ContentValues in = new ContentValues();
        in.put(TV_ID_SERIES, series.getIdSeries());
        in.put(TV_ID_SEASON, series.getIdSeason());
        in.put(TV_NAME, series.getName());
        in.put(TV_EPISODE_CURRENT, series.getEpisodeCurrent());
        in.put(TV_EPISODE_MAX, series.getEpisodeCurrent());
        in.put(TV_SEASON_CURRENT, series.getSeasonCurrent());
        in.put(TV_SEASON_MAX, series.getSeasonMax());
        in.put(TV_WATCHED, series.getWatched());
        in.put(TV_IMG_URL_SERIES, series.getImgUrlSeries());
        in.put(TV_IMG_URL_SEASON, series.getImgUrlSeason());
        //in.put(TIME, System.currentTimeMillis()/1000);

        this.openWriteableDB();
        long rowID = db.insert(TV_TABLE, null, in);
        this.closeDB();

        return rowID;
    }


    public int updateFilm(Film update){
        ContentValues up = new ContentValues();
        up.put(FILM_ID, update.getId());
        up.put(FILM_NAME, update.getName());
        up.put(FILM_WATCHED, update.getWatched());
        up.put(FILM_IMG_URL, update.getImgUrl());

        String where =  FILM_ID + "=?";
        String[] whereArgs = {String.valueOf(update.getId())};

        this.openWriteableDB();
        int rowCount = db.update(FIL_TABLE, up, where, whereArgs);
        this.closeDB();

        return rowCount;
    }


    public int updateSeries(Tv update){
        ContentValues up = new ContentValues();
        up.put(TV_ID_SERIES, update.getIdSeries());
        up.put(TV_ID_SEASON, update.getIdSeason());
        up.put(TV_NAME, update.getName());
        up.put(TV_EPISODE_CURRENT, update.getEpisodeCurrent());
        up.put(TV_EPISODE_MAX, update.getEpisodeCurrent());
        up.put(TV_SEASON_CURRENT, update.getSeasonCurrent());
        up.put(TV_SEASON_MAX, update.getSeasonMax());
        up.put(TV_WATCHED, update.getWatched());
        up.put(TV_IMG_URL_SERIES, update.getImgUrlSeries());
        up.put(TV_IMG_URL_SEASON, update.getImgUrlSeason());

        String where =  TV_ID_SERIES + "= ? AND " + TV_ID_SEASON + "= ?";
        String[] whereArgs = {String.valueOf(update.getIdSeries()), String.valueOf(update.getIdSeason())};

        this.openWriteableDB();
        int rowCount = db.update(TV_TABLE, up, where, whereArgs);
        this.closeDB();

        return rowCount;
    }


    public int updateEpisode(int idSeries, int idSeason){
        String where = TV_ID_SERIES + "= ? AND " + TV_ID_SEASON + "= ?";
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};
        String[] column = {TV_EPISODE_CURRENT, TV_EPISODE_MAX};
        ContentValues ep = new ContentValues();


        this.openWriteableDB();

        Cursor cursor = db.query(TV_TABLE, column, where, whereArgs, null, null, null);
        int current_ep = cursor.getInt(TV_EPISODE_CURRENT_COL);
        int max_ep = cursor.getInt(TV_EPISODE_MAX_COL);
        cursor.close();

        if (current_ep < max_ep){
            current_ep += 1;
        }

        ep.put(TV_EPISODE_CURRENT, current_ep);
        db.update(TV_TABLE, ep, where, whereArgs);

        this.closeDB();

        return current_ep;
    }


    public int updateSeason(int idSeries, int idSeason){
        String where = TV_ID_SERIES + "= ? AND " + TV_ID_SEASON + "= ?";
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};
        String[] column = {TV_SEASON_CURRENT, TV_SEASON_MAX};
        ContentValues sn = new ContentValues();


        this.openWriteableDB();

        Cursor cursor = db.query(TV_TABLE, column, where, whereArgs, null, null, null);
        int current_sn = cursor.getInt(TV_SEASON_CURRENT_COL);
        int max_sn = cursor.getInt(TV_SEASON_MAX_COL);
        cursor.close();

        if (current_sn < max_sn){
            current_sn += 1;
        }

        sn.put(TV_EPISODE_CURRENT, current_sn);
        db.update(TV_TABLE, sn, where, whereArgs);

        this.closeDB();

        return current_sn;
    }


    public int updateWatched(int id){
        String where = FILM_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};
        ContentValues w = new ContentValues();
        int watched = 1;

        this.openWriteableDB();

        w.put(FILM_WATCHED, watched);
        db.update(FIL_TABLE, w, where, whereArgs);

        this.closeDB();

        return watched;
    }


    public int updateWatched(int idSeries, int idSeason){
        String where = TV_ID_SERIES + "= ? AND " + TV_ID_SEASON + "= ?";
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};
        ContentValues w = new ContentValues();
        int watched = 1;

        this.openWriteableDB();

        w.put(TV_WATCHED, watched);
        db.update(TV_TABLE, w, where, whereArgs);

        this.closeDB();

        return watched;
    }


    public int deleteFilm(int id){
        String where = FILM_ID + "=?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(FIL_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }


    public int deleteSeries(int idSeries, int idSeason){
        String where = TV_ID_SERIES + "= ? AND " + TV_ID_SEASON + "= ?";
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};

        this.openWriteableDB();
        int rowCount = db.delete(TV_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }



    //                                          //
    //FUNZIONE PER ESTRARRE I DATI DAL CURSORSE //
    //                                          //
    private static Film getFilmFromCursor(Cursor cursor){
        if(cursor == null || cursor.getCount() == 0){
            return null;
        }
        else{
            try {
                Film film = new Film(
                        cursor.getInt(FILM_ID_COL),
                        cursor.getString(FILM_NAME_COL),
                        //cursor.getString(FILM_TYPE_COL),
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


    private static Tv getTvFromCursor(Cursor cursor){
        if(cursor == null || cursor.getCount() == 0){
            return null;
        }
        else{
            try {
                Tv tv = new Tv(
                        cursor.getInt(TV_ID_SERIES_COL),
                        cursor.getInt(TV_ID_SEASON_COL),
                        cursor.getString(TV_NAME_COL),
                        //cursor.getInt(TV_EPISODE_CURRENT_COL),
                        cursor.getInt(TV_EPISODE_MAX_COL),
                        //cursor.getInt(TV_SEASON_CURRENT_COL),
                        cursor.getInt(TV_SEASON_MAX_COL),
                        cursor.getInt(TV_WATCHED_COL),
                        cursor.getString(TV_IMG_URL_SERIES_COL),
                        cursor.getString(TV_IMG_URL_SEASON_COL)
                );
                return tv;
            }
            catch (Exception e){
                return  null;
            }
        }
    }
}
