package pt.ulisboa.tecnico.cmov.airdesk.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by Hugo on 07/04/2015.
 */
public class Invite {
    private DataBaseAccess dbHelper;

    public Invite(Context context){
        dbHelper= new DataBaseAccess(context);
    }

    public void insert_Invite(String NomeUser, String WorkspaceName, String Owner){
        ContentValues cv=new ContentValues();
        cv.put("NomeUser", NomeUser);
        cv.put("Workspace", WorkspaceName);
        cv.put("Owner", Owner);
        dbHelper.getWritableDatabase().insert("Invite", null, cv);
    }

    public void delete_Invite(String NomeUser, String WorkspaceName, String Owner){
        dbHelper.getWritableDatabase().delete("Invite", "NomeUser=? and Workspace=? and Owner=?", new String[]{NomeUser, WorkspaceName,Owner});
    }

    public Cursor getById(String id){
        String[] args={id};
        return(dbHelper.getReadableDatabase().rawQuery("SELECT NomeUser, Workspace FROM Invite WHERE Owner=?", args));
    }


    public void close(){
        dbHelper.close();
    }

}