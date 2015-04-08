package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Invite;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.List_Tags_Workspaces;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Table_Workspace;

/**
 * Created by ist168635 on 25-03-2015.
 */
abstract public class Workspace {

    private String name;
    private String Path = null;
    private User Owner=null;

    public Workspace(User Owner,String WorkspaceName){
        this.name=WorkspaceName;
        this.Owner=Owner;
    }

    public User getOwner(){
        return this.Owner;
    }

    public abstract void addInvitedUser(User user);

    public abstract StringBuilder ReadFile(String textFileName);

    public abstract void addTag(String Tag);

    public abstract void removeTag(String Tag);

    public String getName(){
        return name;
    }

    public abstract long getQuota();

    public abstract List<String> getTags();

    public abstract boolean getPublico();

    public String toString(){
        return name;
    }

    public abstract File getMydir();
}
