package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//import database.WatchListDB;

import static database.Database.CREATE_FIL_TABLE;
import static database.Database.CREATE_PERSONAL_TABLE;
import static database.Database.CREATE_TV_TABLE;


public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_FIL_TABLE);
        db.execSQL(CREATE_TV_TABLE);
        db.execSQL(CREATE_PERSONAL_TABLE);
        db.execSQL("INSERT INTO personal_data VALUES (0, 0, 0, 0, 0)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newVersion){

    }

}
