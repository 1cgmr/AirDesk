package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.*;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Invite;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.List_Tags_Workspaces;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Table_Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.User_Tag;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Users;

/**
 * Created by ist168635 on 25-03-2015.
 */
public class User {

    private Users DataBase = null;
    private User_Tag Tag_Db = null;
    private Table_Workspace workspace_db=null;
    private List_Tags_Workspaces workspace_tags_db=null;
    private Invite inviteTable = null;
    private AirDesk AirDesk=null;

    private File mydir = null;

    private static User instance;
    private String Username="";
    AirDesk airDesk;

    private List<Workspace> ownedWorkspaces= new ArrayList<Workspace>();
    private List<Workspace> RemoteWorkspaces= new ArrayList<Workspace>();
    private List<TextFile> RemoteFiles= new ArrayList<TextFile>();

    public User(){}

    public User(AirDesk airdesk,String username, Users db, User_Tag TagDb,Table_Workspace workspace_db,List_Tags_Workspaces workspace_Tags_db, Context context,Invite inviteTable){
        this.AirDesk=airdesk;
        this.Username=username;
        this.DataBase=db;
        this.Tag_Db=TagDb;
        this.workspace_db=workspace_db;
        this.workspace_tags_db=workspace_Tags_db;
        this.inviteTable=inviteTable;
        Cursor cursor=db.getById(username);
        if(cursor.getCount()==0) {
            db.insert_Users(username);
        }
        this.mydir = context.getDir(username, Context.MODE_PRIVATE);
    }

    public AirDesk getAirDesk(){
        return this.AirDesk;
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
        Workspace workspace = new Workspace(this,publico, workspaceName, Tags, max_quota, mydir,this.workspace_db,this.workspace_tags_db,this.inviteTable);
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
        workspace.addInvitedUser(this);
    }

    public void removeRemoteWorkspace(Workspace workspace){
        RemoteWorkspaces.remove(workspace);
    }

    public boolean equals(User user){
        return this.Username.compareTo(user.getUserName())!=0;
    }


//REMOTE FILES (TEXT FILE)

    public void addRemoteFile(TextFile textFile){
        RemoteFiles.add(textFile);
    }

    public List<TextFile> getTextFile(){
        return RemoteFiles;
    }

    public void newTextFile(String name, StringBuilder conteudo){
        TextFile textFile= new TextFile(name, conteudo);
        this.RemoteFiles.add(textFile);
    }

    public void listaFicheirosRemotos(File DirWorkspace){
        StringBuilder aux;
        for(final File fileEntry : DirWorkspace.listFiles()){
            aux = airDesk.ReadFile(fileEntry);
            newTextFile(fileEntry.getName(), aux);
        }
    }

    public TextFile getTextFile(String textFile){
        for(TextFile file : this.getTextFile()){
            if(file.getNameFile().compareTo(textFile)==0){
                return file;
            }
        }
        return null;
    }
}
