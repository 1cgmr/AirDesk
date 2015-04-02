package pt.ulisboa.tecnico.cmov.airdesk.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by Hugo on 02/04/2015.
 */
public class List_Tags_Workspaces {

    private DataBaseAccess dbHelper;

    public List_Tags_Workspaces(Context context){
        dbHelper= new DataBaseAccess(context);
    }

    public void insert_User_Tag(String Tag, String NomeWorkspace){
        ContentValues cv=new ContentValues();
        cv.put("Tag", Tag);
        cv.put("NomeWorkspace", NomeWorkspace);
        dbHelper.getWritableDatabase().insert("List_Tags_Workspaces", null, cv);
    }

    public void delete_User_Tag(String Tag){
        dbHelper.getWritableDatabase().delete("List_Tags_Workspaces","Tag=?", new String[] {Tag});
    }

    public void update_User_Tag(String Tag){
        ContentValues cv= new ContentValues();
        String[] args={Tag};
        cv.put("Tag", Tag);
        dbHelper.getWritableDatabase().update("List_Tags_Workspaces", cv, "Tag=?", args);
    }

    public Cursor getAll(String NomeWorkspace){
        String [] args={NomeWorkspace};
        return (dbHelper.getReadableDatabase().rawQuery("SELECT Tag, NomeWorkspace FROM List_Tags_Workspaces WHERE NomeWorkspace=?", args));
    }

    public String getTag(Cursor c){
        return(c.getString(0));
    }

    public Cursor getByTag(String Tag){
        String[] args={Tag};
        return(dbHelper.getReadableDatabase().rawQuery("SELECT Tag, NomeWorkspace FROM List_Tags_Workspaces WHERE Tag=?", args));
    }

    public void close(){
        dbHelper.close();
    }
}
