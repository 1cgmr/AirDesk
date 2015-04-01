package pt.ulisboa.tecnico.cmov.airdesk.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hugo on 25/03/2015.
 */

public class DataBaseAccess extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="AirDeskDataBase.db";
    private static final int SCHEMA_VERSION=1;

    public DataBaseAccess(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT);");
        db.execSQL("CREATE TABLE List_Tags_Users (_id INTEGER PRIMARY KEY AUTOINCREMENT, Tag TEXT, idUser TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {    }
}

