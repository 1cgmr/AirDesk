package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import android.database.Cursor;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private List<TextFile> Files = new ArrayList<TextFile>();
    AirDesk AD;

    public WorkspaceLocal(User Owner, Boolean publico, String workspaceName, List<String> Tags, int max_quota,File myDir,Table_Workspace workspace_db,List_Tags_Workspaces workspace_Tags_db,Invite inviteTable){
        super(Owner, workspaceName);
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

    public List<TextFile> getListFiles(){
        Files.clear();
       // =  new ArrayList<TextFile>();
        for(final File fileEntry : mydir.listFiles()){
            //nome do ficheriro
            String nome = fileEntry.getName();
            TextFile file = new TextFile(nome,this.ReadFile(fileEntry.getName()).toString());
            this.Files.add(file);
        }
        return this.Files;
    }

    @Override
    public boolean removeFile(String Name) {
        File WorkspaceDir;
        String nome;
        WorkspaceDir= this.getMydir();

        for(final File fileEntry: WorkspaceDir.listFiles()){
            //nome do ficheriro
            nome = fileEntry.getName();
            if(nome.equals(Name)){
                fileEntry.delete();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean newFile(String name) {
        File WorkspaceDir;
        long sizeWorkspace;
        WorkspaceDir= this.getMydir();

            Integer tamanhomax = this.max_quota;
            //criar um ficheiro
            File fileWithinMyDir1 = new File(WorkspaceDir, name);
            try {
                fileWithinMyDir1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sizeWorkspace = AD.getFolderSize(WorkspaceDir);

            if (sizeWorkspace >= tamanhomax) {
                fileWithinMyDir1.delete();
                return false;
            }
            else{
                return true;
            }
    }

    @Override
    public boolean modifyFile(String name, String content) {
        StringBuilder builder = readFile(name);
        File WorkspaceDir;
        WorkspaceDir= this.getMydir();

        String valorEditText = content.toString();
        long tamanhoWorkspace;
        Integer tamanhomax;

        File file1 = new File(WorkspaceDir, name);
        FileOutputStream out1 = null;

        try {
            out1 = new FileOutputStream(file1);
            String string = valorEditText;
            byte[] b = string.getBytes();
            out1.write(b);
            out1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

       tamanhomax = this.max_quota;
       tamanhoWorkspace = AD.getFolderSize(WorkspaceDir);

       if (tamanhoWorkspace > tamanhomax) {
           try {
               out1 = new FileOutputStream(file1);
               String string = builder.toString();
               byte[] b = string.getBytes();
               out1.write(b);
               out1.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
           return false;
        }
        else{
           return true;
       }

    }

    @Override
    public StringBuilder readFile(String name) {
        File WorkspaceDir;
        StringBuilder builder = new StringBuilder("");
        WorkspaceDir= this.getMydir();
        File file1 = new File(WorkspaceDir, name);

        try {
            BufferedReader leitor = new BufferedReader(new FileReader(file1));
            String read;

            while((read = leitor.readLine()) !=null){
                builder.append(read+"\n");
            }

            leitor.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    @Override
    public boolean CreateBigFile(){
        File WorkspaceDir;
        WorkspaceDir= this.getMydir();
        Integer tamanhomax = this.max_quota;
        long sizeWorkspace = AD.getFolderSize(WorkspaceDir);

        if (sizeWorkspace <= tamanhomax) {
            //criar um ficheiro
            File file1 = new File(WorkspaceDir, "FileMaxSize.txt");
            if(!file1.exists()) {

                try {
                    file1.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                sizeWorkspace = AD.getFolderSize(WorkspaceDir);
                FileOutputStream out1 = null;
                String frase = "";

                while ((AD.getFolderSize(WorkspaceDir)) != tamanhomax) {
                    frase = frase + "a";
                    try {
                        out1 = new FileOutputStream(file1);
                        String string = frase;
                        byte[] b = string.getBytes();
                        out1.write(b);
                        out1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        }
        return false;
    }

}
