package pt.ulisboa.tecnico.cmov.airdesk.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by Hugo on 28/03/2015.
 */
public class User_Tag {

    private DataBaseAccess dbHelper;

    public User_Tag(Context context){
        dbHelper= new DataBaseAccess(context);
    }

    public void insert_User_Tag(String Tag, String IdUser){
        ContentValues cv=new ContentValues();
        cv.put("Tag", Tag);
        cv.put("idUser", IdUser);
        dbHelper.getWritableDatabase().insert("List_Tags_Users", null, cv);
    }

    public void delete_User_Tag(String id){
        dbHelper.getWritableDatabase().delete("List_Tags_Users","_id=?", new String[] {id});
    }

    public void update_User_Tag(String id, String Tag){
        ContentValues cv= new ContentValues();
        String[] args={id};
        cv.put("Tag", Tag);
        dbHelper.getWritableDatabase().update("List_Tags_Users", cv, "_id=?", args);
    }

    public Cursor getAll(String IdUser){
        String [] args={IdUser};
        return (dbHelper.getReadableDatabase().rawQuery("SELECT _id, Tag, idUser FROM List_Tags_Users WHERE idUser=?", args));
    }

    public String getTag(Cursor c){
        return(c.getString(1));
    }

    public Cursor getById(String id){
        String[] args={id};
        return(dbHelper.getReadableDatabase().rawQuery("SELECT _id, Tag, idUser FROM List_Tags_Users WHERE _id=?", args));
    }

    public void close(){
        dbHelper.close();
    }
}
