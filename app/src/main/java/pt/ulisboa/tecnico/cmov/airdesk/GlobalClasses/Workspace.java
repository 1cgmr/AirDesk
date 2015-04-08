package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import android.content.Context;
import android.database.Cursor;
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
public class Workspace {

    private String name;
    private List<TextFile> Files = new ArrayList<TextFile>();
    private List<String> Tags = new ArrayList<String>();
    private List<User> InvitedUsers = new ArrayList<User>();
    private boolean publico = false;
    private int max_quota = 0;
    private String Path = null;
    private File mydir = null;
    private Table_Workspace workspace_db=null;
    private List_Tags_Workspaces workspace_tags_db=null;
    private Invite inviteTable = null;
    private User Owner=null;

    public Workspace(User Owner,Boolean publico, String workspaceName, List<String> Tags, int max_quota,File myDir,Table_Workspace workspace_db,List_Tags_Workspaces workspace_Tags_db,Invite inviteTable){
        this.name=workspaceName;
        this.publico=publico;
        this.Tags.addAll(Tags);
        this.max_quota=max_quota;
        this.workspace_db=workspace_db;
        this.workspace_tags_db=workspace_Tags_db;
        this.inviteTable=inviteTable;
        this.Owner=Owner;

        this.mydir = new File(myDir,workspaceName);
        mydir.mkdir();

        this.mydir=mydir;
        Cursor cursor=workspace_db.getById(workspaceName, Owner.getUserName());
        if(cursor.getCount()==0) {
            workspace_db.insert_Workspace(workspaceName, Owner.getUserName(), publico, max_quota);
            workspace_db.close();
        }
        //this.Files
        for(final File fileEntry : this.mydir.listFiles()){
            //nome do ficheriro
            String nome = fileEntry.getName();
            TextFile file = new TextFile(nome,this.ReadFile(fileEntry));
            this.Files.add(file);
        }
        //this.addInvitedUser(user);
    }

    public User getOwner(){
        return this.Owner;
    }

    public void addInvitedUser(User user){
        this.InvitedUsers.add(user);
        this.inviteTable.insert_Invite(user.getUserName(),this.getName(),this.getOwner().getUserName());
    }

    public StringBuilder ReadFile(File WorkspaceDir){
        StringBuilder builder = new StringBuilder("");

        try {
            BufferedReader leitor = new BufferedReader(new FileReader(WorkspaceDir));
            // br= new BufferedReader(leitor);
            String read;

            while((read = leitor.readLine()) !=null){
                builder.append(read+"\n");
            }
            //editor.setText(builder.toString());
            leitor.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    public String getName(){
        return name;
    }

    public void addTag(String Tag){
        Tags.add(Tag);
    }

    public void removeTag(String Tag){
        Tags.remove(Tag);
    }

    public String toString(){
        return name;
    }

    public void addFileWorkspace(TextFile file){
        Files.add(file);
    }

    public File getMydir(){
        return this.getMydir();
    }
}
