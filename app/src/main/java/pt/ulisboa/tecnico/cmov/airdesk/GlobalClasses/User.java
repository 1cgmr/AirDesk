package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.*;

/**
 * Created by ist168635 on 25-03-2015.
 */
public class User {

    private static User instance;
    private String Username="";

    private List<Workspace> ownedWorkspaces= new ArrayList<Workspace>();
    private List<Workspace> RemoteWorkspaces= new ArrayList<Workspace>();
    private List<String> Tags =  new ArrayList<String>();

    private User(){}

    public User(String username){
        this.Username=username;
    }

    public void addTag(String Tag){
        Tags.add(Tag);
    }

    public void removeTag(String Tag){
        Tags.remove(Tag);
    }

    public void newWorkspace(Boolean publico, String workspaceName, List<String> Tags,int max_quota){
        Workspace workspace= new Workspace(publico, workspaceName, Tags, max_quota);
        this.ownedWorkspaces.add(workspace);
    }

    public String getUserName(){
        return this.Username;
    }

    public void setUserName(String texto){
        this.Username=texto;
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

