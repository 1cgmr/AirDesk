package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.DataBase.List_Tags_Workspaces;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Table_Workspace;

/**
 * Created by ist168635 on 25-03-2015.
 */
public class Workspace {

    private String name;
    private List<TextFile> Files = new ArrayList<TextFile>();
    private List<String> Tags = new ArrayList<String>();
    private boolean publico = false;
    private int max_quota = 0;
    private String Path = null;
    private File mydir = null;
    private Table_Workspace workspace_db=null;
    private List_Tags_Workspaces workspace_tags_db=null;

    public Workspace(String Owner,Boolean publico, String workspaceName, List<String> Tags, int max_quota,File myDir,Table_Workspace workspace_db,List_Tags_Workspaces workspace_Tags_db){
        this.name=workspaceName;
        this.publico=publico;
        this.Tags.addAll(Tags);
        this.max_quota=max_quota;
        this.workspace_db=workspace_db;
        this.workspace_tags_db=workspace_Tags_db;

        File mydir = new File(myDir,workspaceName);
        mydir.mkdir();

        workspace_db.insert_Workspace(workspaceName, Owner, publico, max_quota);
        workspace_db.close();
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
