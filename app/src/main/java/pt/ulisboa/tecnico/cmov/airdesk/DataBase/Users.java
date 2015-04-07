package pt.ulisboa.tecnico.cmov.airdesk.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by Hugo on 28/03/2015.
 */
public class Users {

    private DataBaseAccess dbHelper;

    public Users(Context context){
        dbHelper= new DataBaseAccess(context);
    }

    public void insert_Users(String email){
        ContentValues cv=new ContentValues();
        cv.put("email", email);
        dbHelper.getWritableDatabase().insert("Users", "email", cv);
    }

    public void close(){
        dbHelper.close();
    }

    public Cursor getById(String id){
        String[] args={id};
        return(dbHelper.getReadableDatabase().rawQuery("SELECT email FROM Users WHERE email=?", args));
    }

}
