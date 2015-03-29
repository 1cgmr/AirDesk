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

    public void delete_Users(String id){
        dbHelper.getWritableDatabase().delete("Users", "_id=?", new String[]{id});
    }

    public void update_Users(String id, String email){
        ContentValues cv= new ContentValues();
        String[] args={id};
        cv.put("email", email);
        dbHelper.getWritableDatabase().update("Users", cv, "_id=?", args);
    }

    public Cursor getByIdUser(String id){
        String[] args={id};
        return(dbHelper.getReadableDatabase().rawQuery("SELECT _id, email FROM Users WHERE _id=?", args));
    }

    public Cursor getByEmailUser(String email){
        String[] args={email};
        return(dbHelper.getReadableDatabase().rawQuery("SELECT _id, email FROM Users WHERE email=?", args));
    }

    public void close(){
        dbHelper.close();
    }

}
