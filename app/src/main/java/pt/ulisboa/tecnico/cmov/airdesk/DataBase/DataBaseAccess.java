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

//    db.execSQL("CREATE TABLE Users (_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT);");

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (email TEXT PRIMARY KEY);");
        db.execSQL("CREATE TABLE List_Tags_Users (_id INTEGER PRIMARY KEY AUTOINCREMENT, Tag TEXT, idUser TEXT);");
        db.execSQL("CREATE TABLE Table_Workspace (Nome TEXT,Owner TEXT, Publico BOOLEAN, Quota INTEGER, PRIMARY KEY(Nome, Owner));");
        db.execSQL("CREATE TABLE List_Tags_Workspaces (_id INTEGER PRIMARY KEY AUTOINCREMENT, Tag TEXT, NomeWorkspace TEXT);");
        db.execSQL("CREATE TABLE Invite(NomeUser TEXT, Workspace TEXT, Owner TEXT, PRIMARY KEY(NomeUser, Owner, Workspace));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {    }
}