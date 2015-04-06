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

    public void insert_Workspace_Tag(String Tag, String NomeWorkspace){
        ContentValues cv=new ContentValues();
        cv.put("Tag", Tag);
        cv.put("NomeWorkspace", NomeWorkspace);
        dbHelper.getWritableDatabase().insert("List_Tags_Workspaces", null, cv);
    }

    public void delete_Workspace_Tag(String id){
        dbHelper.getWritableDatabase().delete("List_Tags_Workspaces","_id=?", new String[] {id});
    }

    public void update_Workspace_Tag(String id, String Tag){
        ContentValues cv= new ContentValues();
        String[] args={id};
        cv.put("Tag", Tag);
        dbHelper.getWritableDatabase().update("List_Tags_Workspaces", cv, "_id=?", args);
    }

    public Cursor getAll(String NomeWorkspace){
        String [] args={NomeWorkspace};
        return (dbHelper.getReadableDatabase().rawQuery("SELECT _id, Tag, NomeWorkspace FROM List_Tags_Workspaces WHERE NomeWorkspace=?", args));
    }

    public String getTag(Cursor c){
        return(c.getString(1));
    }

    public Cursor getById(String id){
        String[] args={id};
        return(dbHelper.getReadableDatabase().rawQuery("SELECT _id, Tag, NomeWorkspace FROM List_Tags_Workspaces WHERE _id=?", args));
    }

    public void close(){
        dbHelper.close();
    }
}