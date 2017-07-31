package com.example.simone.whatwatch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.simone.whatwatch.WatchListDB.CREATE_FILM_TABLE;



public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_FILM_TABLE);

        db.execSQL("INSERT INTO film_data VALUES (1, 'Inception')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newVersion){
        Log.d("Film Table", "Upgrading db from version" + oldversion + " to " + newVersion);
        db.execSQL(WatchListDB.DROP_FILM_TABLE);
        onCreate(db);
    }

}
