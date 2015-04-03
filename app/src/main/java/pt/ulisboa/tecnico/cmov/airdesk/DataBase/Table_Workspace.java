package pt.ulisboa.tecnico.cmov.airdesk.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by Hugo on 02/04/2015.
 */
public class Table_Workspace {
    private DataBaseAccess dbHelper;

    public Table_Workspace(Context context){
        dbHelper= new DataBaseAccess(context);
    }

    public void insert_Workspace(String Nome, String Owner, Boolean Publico, Integer Quota){
        ContentValues cv=new ContentValues();
        cv.put("Nome", Nome);
        cv.put("Owner", Owner);
        cv.put("Publico", Publico);
        cv.put("Quota", Quota);
        dbHelper.getWritableDatabase().insert("Table_Workspace", null, cv);
    }

    public void delete_Workspace(String Nome, String Owner){
        dbHelper.getWritableDatabase().delete("Table_Workspace", "Nome=? and Owner=?", new String[]{Nome, Owner});
    }

    public void close(){
        dbHelper.close();
    }
}
