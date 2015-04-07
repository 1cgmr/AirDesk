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

    public void close(){
        dbHelper.close();
    }

    public Cursor getAll(String WorkspaceName, String Owner){
        String [] args={WorkspaceName, Owner};
        return (dbHelper.getReadableDatabase().rawQuery("SELECT NomeUser, Workspace, Owner FROM Invite WHERE Workspace=? and Owner=?", args));
    }

    public String getNameUser(Cursor c){
        return(c.getString(0));
    }

    public Cursor getByNameUser(String nameuser){
        String[] args={nameuser};
        return(dbHelper.getReadableDatabase().rawQuery("SELECT NomeUser, Workspace, Owner FROM Invite WHERE NomeUser=?", args));
    }

}