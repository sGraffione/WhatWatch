package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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


    //Costanti tabella personal data

    public static String PERSONAL_TABLE = "personal_data";

    public static String PERSONAL_ROW = "personal_row";
    public static int PERSONAL_ROW_COL = 0;

    public static String FILMS_SEEN = "films_seen";
    public static int FILMS_SEEN_COL = 1;

    public static String SERIES_SEEN = "series_seen";
    public static int SERIES_SEEN_COL = 2;

    public static String FILMS_TIME = "films_time";
    public static int FILMS_TIME_COL = 3;

    public static String SERIES_TIME = "series_time";
    public static int SERIES_TIME_COL = 4;


    //Query di creazione e drop delle due tabelle
    public static final String CREATE_FIL_TABLE =
            "CREATE TABLE " + FIL_TABLE + " (" + FILM_ROW + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FILM_ID + " INTEGER NOT NULL UNIQUE, " +
                    FILM_NAME + " STRING NOT NULL, " + FILM_WATCHED + " INTEGER NOT NULL, " + FILM_IMG_URL + " STRING);";

    public static final String DROP_FILM_TABLE = "DROP TABLE IF EXISTS " + FIL_TABLE;


    public static final String CREATE_TV_TABLE =
            "CREATE TABLE " + TV_TABLE + " (" + TV_ROW + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TV_ID_SERIES + " INTEGER NOT NULL, " +
                    TV_ID_SEASON + " INTEGER NOT NULL, " + TV_NAME + " STRING NOT NULL, " + TV_EPISODE_CURRENT + " INTEGER NOT NULL, " +
                    TV_EPISODE_MAX + " INTEGER NOT NULL, " + TV_SEASON_CURRENT + " INTEGER NOT NULL, " +
                    TV_SEASON_MAX + " INTEGER NOT NULL, " + TV_WATCHED + " INTEGER NOT NULL, " + TV_IMG_URL_SERIES + " STRING, " +
                    TV_IMG_URL_SEASON + " STRING);";

    public static final String DROP_TV_TABLE = "DROP TABLE IF EXISTS " + TV_TABLE;


    public static final String CREATE_PERSONAL_TABLE =
            "CREATE TABLE " + PERSONAL_TABLE + " (" + PERSONAL_ROW + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FILMS_SEEN + " INTEGER NOT NULL, " +
                    SERIES_SEEN + " INTEGER NOT NULL, " + FILMS_TIME + " INTEGER NOT NULL, " + SERIES_TIME + " INTEGER NOT NULL);";

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
        ArrayList<Object> elements = new ArrayList<Object>();

        if(type.equals("Movie")) {
            Cursor cursorFilm = null;
            String where = FILM_WATCHED + "= ?";
            String[] whereArgs = {Integer.toString(watched)};
            if (order.equals("Recent")) {
                cursorFilm = db.query(FIL_TABLE, null, where, whereArgs, null, null, FILM_ROW + " DESC");
            } else {
                cursorFilm = db.query(FIL_TABLE, null, where, whereArgs, null, null, FILM_NAME);
            }
            while (cursorFilm.moveToNext()) {
                elements.add(getFilmFromCursor(cursorFilm));
            }
            if (cursorFilm != null){
                cursorFilm.close();
            }
        } else if(type.equals("Tv_shows")){
            Cursor cursorSeries = null;
            String where = TV_WATCHED + "= ?";
            String[] whereArgs = {Integer.toString(watched)};
            if (order.equals("Recent")) {
                if(watched == 1){
                    cursorSeries = db.query(TV_TABLE, null, where, whereArgs, TV_ID_SERIES, null, TV_ROW + " DESC");
                }
                else{
                    cursorSeries = db.query(TV_TABLE, null, where, whereArgs, null, null, TV_ROW + " DESC");
                }
            } else {
                if(watched == 1){
                    cursorSeries = db.query(TV_TABLE, null, where, whereArgs, TV_ID_SERIES, null, TV_NAME);
                }else{
                    cursorSeries = db.query(TV_TABLE, null, where, whereArgs, null, null, TV_NAME);
                }
            }
            while (cursorSeries.moveToNext()) {
                elements.add(getTvFromCursor(cursorSeries));
            }
            if (cursorSeries != null){
                cursorSeries.close();
            }
        } else{
            Cursor cursorFilm = null;
            Cursor cursorSeries = null;
            String where = FILM_WATCHED + "= ?";
            String whereTv = TV_WATCHED + "= ?";
            String[] whereArgs = {Integer.toString(watched)};
            if (order.equals("Recent")) {
                cursorFilm = db.query(FIL_TABLE, null, where, whereArgs, null, null, FILM_ROW + " DESC");
                if(watched == 1){
                    cursorSeries = db.query(TV_TABLE, null, whereTv, whereArgs, TV_ID_SERIES, null, TV_ROW + " DESC");
                }else{
                    cursorSeries = db.query(TV_TABLE, null, whereTv, whereArgs, null, null, TV_ROW + " DESC");
                }
            } else {
                cursorFilm = db.query(FIL_TABLE, null, where, whereArgs, null, null, FILM_NAME);
                if(watched == 1){
                    cursorSeries = db.query(TV_TABLE, null, whereTv, whereArgs, TV_ID_SERIES, null, TV_NAME);
                }else{
                    cursorSeries = db.query(TV_TABLE, null, whereTv, whereArgs, null, null, TV_NAME);
                }
            }
            while (cursorFilm.moveToNext()) {
                elements.add(getFilmFromCursor(cursorFilm));
            }
            while (cursorSeries.moveToNext()) {
                elements.add(getTvFromCursor(cursorSeries));
            }
            if (cursorFilm != null || cursorSeries != null){
                cursorFilm.close();
                cursorSeries.close();
            }
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
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};

        this.openReadableDB();
        Cursor cursor = db.rawQuery("SELECT * FROM tv_data WHERE tv_id_series = ? AND tv_id_season = ?", whereArgs);
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


    public boolean verifyIdSeries(int idSeries){
        int watched = 0;
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(watched)};

        this.openReadableDB();
        Cursor cursor = db.rawQuery("SELECT * FROM tv_data WHERE tv_id_series = ? AND tv_watched = ?", whereArgs);
        if(cursor.getCount() == 0){
            cursor.close();
            this.closeDB();
            return false;
        }else{
            cursor.close();
            this.closeDB();
            return true;
        }
    }


    public boolean verifySeasonWatched(int idSeries, int idSeason){
        int watched = 1;
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason), Integer.toString(watched)};

        this.openReadableDB();
        Cursor cursor = db.rawQuery("SELECT * FROM tv_data WHERE tv_id_series = ? AND tv_id_season = ? AND tv_watched = ?", whereArgs);
        if(cursor.getCount() == 0){
            cursor.close();
            this.closeDB();
            return false;
        }else{
            cursor.close();
            this.closeDB();
            return true;
        }
    }


    public boolean verifyEpisodeMax(int idSeries, int idSeason){
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};
        this.openReadableDB();

        Cursor cursor = db.rawQuery("SELECT * FROM tv_data WHERE tv_id_series = ? AND tv_id_season = ?", whereArgs);
        cursor.moveToFirst();
        int current = cursor.getInt(TV_EPISODE_CURRENT_COL);
        int max = cursor.getInt(TV_EPISODE_MAX_COL);

        cursor.close();
        db.close();

        return (current == max);
    }


    public boolean verifySeasonMax(int idSeries, int idSeason){
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};
        this.openReadableDB();

        Cursor cursor = db.rawQuery("SELECT * FROM tv_data WHERE tv_id_series = ? AND tv_id_season = ?", whereArgs);
        cursor.moveToFirst();
        int current = cursor.getInt(TV_SEASON_CURRENT_COL);
        int max = cursor.getInt(TV_SEASON_MAX_COL);

        cursor.close();
        db.close();

        return (current == max);
    }


    public long insertFilm(Film film){
        ContentValues in = new ContentValues();
        in.put(FILM_ID, film.getId());
        in.put(FILM_NAME, film.getName());
        in.put(FILM_WATCHED, film.getWatched());
        in.put(FILM_IMG_URL, film.getImgUrl());

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
        in.put(TV_EPISODE_MAX, series.getEpisodeMax());
        in.put(TV_SEASON_CURRENT, series.getSeasonCurrent());
        in.put(TV_SEASON_MAX, series.getSeasonMax());
        in.put(TV_WATCHED, series.getWatched());
        in.put(TV_IMG_URL_SERIES, series.getImgUrlSeries());
        in.put(TV_IMG_URL_SEASON, series.getImgUrlSeason());

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
        up.put(TV_EPISODE_MAX, update.getEpisodeMax());
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


    /*public int updateEpisode(int idSeries, int idSeason){
        String where = TV_ID_SERIES + "= ? AND " + TV_ID_SEASON + "= ?";
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(0)};
        String[] column = {TV_EPISODE_CURRENT, TV_EPISODE_MAX};
        ContentValues ep = new ContentValues();
        int current_ep = 0;
        int max_ep = 5;


        this.openWriteableDB();

        Cursor cursor = db.rawQuery("SELECT * FROM tv_data WHERE tv_id_series = ? AND tv_watched = ?", whereArgs);
        if(cursor == null || cursor.getCount() == 0){
            cursor.close();
            this.closeDB();
            return 0;
        }
        else{
            try{
                current_ep = cursor.getInt(TV_EPISODE_CURRENT_COL);
                max_ep = cursor.getInt(TV_EPISODE_MAX_COL);

                if (current_ep < max_ep){
                    current_ep += 1;
                }

                ep.put(TV_EPISODE_CURRENT, current_ep);
                db.update(TV_TABLE, ep, where, whereArgs);
            }
            catch (Exception e){
                //return 0;
                e.printStackTrace();
            }
        }

        cursor.close();
        this.closeDB();

        return current_ep;
    }*/


    public int updateEpisode(int idSeries, int idSeason){
        int watched = 0;
        int currentEp = 1;
        String where = TV_ID_SERIES + " = ? AND " + TV_ID_SEASON + " = ?";
        String[] whereArgs = {Integer.toString(idSeries), Integer.toString(idSeason)};
        this.openWriteableDB();
        Cursor episode;
        episode = db.rawQuery("SELECT tv_episode_current, tv_episode_max FROM " + TV_TABLE + " WHERE tv_id_series = " + idSeries + " AND tv_id_season = " +
                idSeason + " AND tv_watched = " + watched, null);
        try{
            episode.moveToFirst();
            currentEp = episode.getInt(episode.getColumnIndex(TV_EPISODE_CURRENT));
            int maxEp = episode.getInt(episode.getColumnIndex(TV_EPISODE_MAX));
            if(currentEp < maxEp){
                currentEp = currentEp + 1;
            }else{
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        ContentValues up = new ContentValues();
        up.put(TV_EPISODE_CURRENT, currentEp);
        db.update(TV_TABLE, up, where, whereArgs);
        episode.close();
        db.close();
        return currentEp;

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


    public int deleteSeason(int idSeries, int idSeason){
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
                        cursor.getInt(TV_EPISODE_CURRENT_COL),
                        cursor.getInt(TV_EPISODE_MAX_COL),
                        cursor.getInt(TV_SEASON_CURRENT_COL),
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


    //                              //
    //METODI PER LA TABELLA PERSONAL//
    //                              //

    public int updateFilmsSeen(){
        int watched = 1;
        String[] whereArgs = {Integer.toString(watched)};

        this.openWriteableDB();
        Cursor cursor = db.rawQuery("SELECT * FROM film_data WHERE film_watched = ?", whereArgs);
        int seen = cursor.getCount();
        cursor.close();

        ContentValues up = new ContentValues();
        up.put(FILMS_SEEN, seen);
        db.update(PERSONAL_TABLE, up, null, null);
        this.closeDB();
        return seen;
    }


    public int updateSeriesSeen(){
        int watched = 1;
        String[] whereArgs = {Integer.toString(watched)};
        int count;
        int max;
        int seen = 0;

        this.openWriteableDB();
        //TODO testare con un solo cursore
        Cursor cursor = db.rawQuery("SELECT count(*) FROM tv_data WHERE tv_watched = ? GROUP BY tv_id_series", whereArgs);
        Cursor seasonMax = db.rawQuery("SELECT tv_season_max FROM tv_data WHERE tv_watched = ? GROUP BY tv_id_series", whereArgs);

        if(cursor == null || cursor.getCount() == 0 || seasonMax == null || seasonMax.getCount() == 0){
            cursor.close();
            this.closeDB();
            return 0;
        }
        else{
            try{
                while(cursor.moveToNext() || seasonMax.moveToNext()){
                   count = cursor.getInt(cursor.getColumnIndex("count(*)"));
                   max = seasonMax.getInt(TV_SEASON_MAX_COL);
                   if(count == max){
                       seen += 1;
                   }
                }

                cursor.close();
                seasonMax.close();
            }
            catch (Exception e){
                return 0;
            }
        }

        ContentValues up = new ContentValues();
        up.put(SERIES_SEEN, seen);
        db.update(PERSONAL_TABLE, up, null, null);
        db.close();
        return seen;
    }


    public int updateFilmTime(int duration){
        this.openWriteableDB();
        Cursor timeCursor = db.rawQuery("SELECT films_time FROM personal_data", null);
        int time = timeCursor.getInt(FILMS_TIME_COL);
        time += duration;
        ContentValues up = new ContentValues();
        up.put(FILMS_TIME, time);
        db.update(PERSONAL_TABLE, up, null, null);
        timeCursor.close();
        db.close();
        return time;
    }


    public int updateSeriesTime(int duration){
        this.openWriteableDB();
        Cursor timeCursor = db.rawQuery("SELECT series_time FROM personal_data", null);
        int time = timeCursor.getInt(SERIES_TIME_COL);
        time += duration;
        ContentValues up = new ContentValues();
        up.put(SERIES_TIME, time);
        db.update(PERSONAL_TABLE, up, null, null);
        timeCursor.close();
        db.close();
        return time;
    }


    public int getFilmsSeen(){
        this.openReadableDB();
        Cursor cursor = db.rawQuery("SELECT films_seen, FROM personal_data", null);
        int time = cursor.getInt(FILMS_SEEN_COL);
        cursor.close();
        db.close();
        return time;
    }


    public int getSeriesSeen(){
        this.openReadableDB();
        Cursor cursor = db.rawQuery("SELECT series_seen FROM personal_data", null);
        int time = cursor.getInt(SERIES_SEEN_COL);
        cursor.close();
        db.close();
        return time;
    }


    public int getFilmsTime(){
        this.openReadableDB();
        Cursor cursor = db.rawQuery("SELECT films_time FROM personal_data", null);
        int time = cursor.getInt(FILMS_TIME_COL);
        cursor.close();
        db.close();
        return time;
    }


    public int getSeriesTime(){
        this.openReadableDB();
        Cursor cursor = db.rawQuery("SELECT series_time FROM personal_data", null);
        int time = cursor.getInt(SERIES_TIME_COL);
        cursor.close();
        db.close();
        return time;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        this.openWriteableDB();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = db.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }



}