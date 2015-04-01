package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.*;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.User_Tag;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Users;

/**
 * Created by ist168635 on 25-03-2015.
 */
public class User {

    private Users DataBase = null;
    private User_Tag Tag_Db = null;

    private File mydir = null;

    private static User instance;
    private String Username="";

    private List<Workspace> ownedWorkspaces= new ArrayList<Workspace>();
    private List<Workspace> RemoteWorkspaces= new ArrayList<Workspace>();

    public User(){}

    public User(String username, Users db, User_Tag TagDb, Context context){
        this.Username=username;
        this.DataBase=db;
        this.Tag_Db=TagDb;
        db.insert_Users(username);
        this.mydir = context.getDir(username, Context.MODE_PRIVATE);
    }


    public void addTag(String Tag){
        this.Tag_Db.insert_User_Tag(Tag,this.getUserName());
    }

    public void removeTag(String Tag){
        this.Tag_Db.delete_User_Tag(Tag);
    }

    public File getMydir() {
        return mydir;
    }

    public void newWorkspace(Boolean publico, String workspaceName, List<String> Tags,int max_quota,Context context){
        Workspace workspace= new Workspace(publico, workspaceName, Tags, max_quota, mydir);
        this.ownedWorkspaces.add(workspace);
    }

    public String getUserName(){
        return this.Username;
    }

    public static synchronized User getInstance(){
        if(instance==null){
            instance=new User();
        }
        return instance;
    }

    public List<Workspace> getOwnedWorkspaces(){
        return ownedWorkspaces;
    }

    public List<Workspace> getForeignWorkspaces(){
        return RemoteWorkspaces;
    }

    public Workspace getWorkspace(String workspace){
        for(Workspace work : this.getOwnedWorkspaces()){
            if(work.getName().compareTo(workspace)==0){
                return work;
            }
        }
        return null;
    }

    public void addRemoteWorkspace(Workspace workspace){
        RemoteWorkspaces.add(workspace);
    }

    public void removeRemoteWorkspace(Workspace workspace){
        RemoteWorkspaces.remove(workspace);
    }

    public boolean equals(User user){
        return this.Username.compareTo(user.getUserName())!=0;
    }

}

