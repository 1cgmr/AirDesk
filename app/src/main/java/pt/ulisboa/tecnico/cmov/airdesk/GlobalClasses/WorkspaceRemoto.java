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

    private Workspace workspaceLocal = null;
    private List<TextFile> Files = new ArrayList<TextFile>();

    public WorkspaceRemoto(User Owner, Workspace parent, File mydir){
        super(Owner,parent.getName());
        this.workspaceLocal=parent;

        for(final File fileEntry : mydir.listFiles()){
            //nome do ficheriro
            String nome = fileEntry.getName();
            TextFile file = new TextFile(nome,this.ReadFile(fileEntry.getName()));
            this.Files.add(file);
        }

    }

    @Override
    public void addInvitedUser(User user) {
        workspaceLocal.addInvitedUser(user);
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
}
