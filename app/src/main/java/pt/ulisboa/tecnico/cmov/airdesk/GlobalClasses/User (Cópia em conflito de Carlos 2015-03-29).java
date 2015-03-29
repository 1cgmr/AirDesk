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

    public void newWorkspace(Boolean publico, String workspaceName, List<String> Tags){
        Workspace workspace= new Workspace(publico, workspaceName, Tags);
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

    public void addRemoteWorkspace(Workspace workspace){
        RemoteWorkspaces.add(workspace);
    }

    public void removeRemoteWorkspace(Workspace workspace){
        RemoteWorkspaces.remove(workspace);
    }

    public List<Workspace> getOwnedWorkspaces() {
        return ownedWorkspaces;
    }

    public List<Workspace> getRemoteWorkspaces() {
        return RemoteWorkspaces;
    }

}

