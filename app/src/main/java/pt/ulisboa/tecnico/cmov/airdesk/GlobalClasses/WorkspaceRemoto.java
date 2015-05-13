package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

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
public class WorkspaceRemoto extends Workspace{

    private WorkspaceLocal workspaceLocal = null;
    private List<TextFile> Files = new ArrayList<TextFile>();
    //TODO para ter acesso a lista remotamente
    private List<String> File_Names= new ArrayList<String>();

    public WorkspaceRemoto(String Owner, String name, List<String> fn){
        super(new User(Owner),name);
        for(String st : fn){
            this.File_Names.add(st);
        }
    }
    //TODO !para ter acesso a lista remotamente

    public WorkspaceRemoto(User Owner, WorkspaceLocal parent){
        super(Owner,parent.getName());
        this.workspaceLocal=parent;
        Files.addAll(workspaceLocal.getListFiles());

    }

    @Override
    public boolean addInvitedUser(User user) {
        return workspaceLocal.addInvitedUser(user);
    }

    @Override
    public StringBuilder ReadFile(String textFileName){
        return this.workspaceLocal.ReadFile(textFileName);
    }

    @Override
    public void addTag(String Tag) {
        workspaceLocal.addTag(Tag);
    }

    @Override
    public void removeTag(String Tag) {
        workspaceLocal.removeTag(Tag);
    }

    @Override
    public long getQuota() {
        return workspaceLocal.getQuota();
    }

    @Override
    public List<String> getTags() {
        return workspaceLocal.getTags();
    }

    @Override
    public boolean getPublico() {
        return false;
    }

    @Override
    public File getMydir() {
        return null;
    }

    @Override
    public List<TextFile> getListFiles() {
        return this.workspaceLocal.getListFiles();
    }

    @Override
    public boolean removeFile(String Name) {
        this.workspaceLocal.removeFile(Name);
        return true;
    }

    @Override
    public boolean newFile(String name) {
        if(this.workspaceLocal.newFile(name)){
            this.Files.add(new TextFile(name));
            return true;
        }
        return false;
    }

    @Override
    public boolean modifyFile(String name, String content) {

        if(this.workspaceLocal.modifyFile(name,content)){
            for(TextFile file : this.Files){
                if(file.getNameFile().equals(name)){
                    this.Files.remove(file);
                    this.Files.add(new TextFile(name,content));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public StringBuilder readFile(String name) {
        return this.workspaceLocal.readFile(name);
    }

    @Override
    public boolean CreateBigFile(){
        return this.workspaceLocal.CreateBigFile();
    }

}
