package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import android.database.Cursor;

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
 * Created by 25 on 4/8/2015.
 */
public class WorkspaceLocal extends Workspace {

    private List<String> Tags = new ArrayList<String>();
    private List<User> InvitedUsers = new ArrayList<User>();
    private boolean publico = false;
    private int max_quota = 0;
    private String Path = null;
    private File mydir = null;
    private Table_Workspace workspace_db=null;
    private List_Tags_Workspaces workspace_tags_db=null;
    private Invite inviteTable = null;

    public WorkspaceLocal(User Owner, Boolean publico, String workspaceName, List<String> Tags, int max_quota,File myDir,Table_Workspace workspace_db,List_Tags_Workspaces workspace_Tags_db,Invite inviteTable){
        super(Owner,workspaceName);
        this.publico=publico;
        this.Tags.addAll(Tags);
        this.max_quota=max_quota;
        this.workspace_db=workspace_db;
        this.workspace_tags_db=workspace_Tags_db;
        this.inviteTable=inviteTable;

        this.mydir = new File(myDir,workspaceName);
        mydir.mkdir();

        this.mydir=mydir;
        Cursor cursor=workspace_db.getById(workspaceName, Owner.getUserName());
        if(cursor.getCount()==0) {
            workspace_db.insert_Workspace(workspaceName, Owner.getUserName(), publico, max_quota);
            workspace_db.close();
        }
        //this.Files
        //this.addInvitedUser(user);
    }

    public boolean addInvitedUser(User user){
        this.InvitedUsers.add(user);
        Cursor cursor = this.inviteTable.getTuple(getOwner().getUserName(),user.getUserName(),this.getName());
        if(cursor.getCount()==0) {
            this.inviteTable.insert_Invite(user.getUserName(), this.getName(), this.getOwner().getUserName());
            return true;
        }
        return false;
    }

    public StringBuilder ReadFile(String textFileName){
        StringBuilder builder = new StringBuilder("");
        File WorkspaceDir = new File(this.mydir.getPath()+"/"+textFileName);

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

    public long getQuota(){
        return max_quota;
    }

    public boolean getPublico(){
        return publico;
    }

    public List<String> getTags(){ return Tags;}

    public void addTag(String Tag){
        Tags.add(Tag);
    }

    public void removeTag(String Tag){
        Tags.remove(Tag);
    }

    public File getMydir(){
        return this.mydir;
    }
}
