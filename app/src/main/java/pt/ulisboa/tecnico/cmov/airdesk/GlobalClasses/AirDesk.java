package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Invite;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.List_Tags_Workspaces;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Table_Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.User_Tag;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Users;

/**
 * Created by ist168635 on 25-03-2015.
 */
public class AirDesk extends Application {

    private Users UserDb = null;
    private User_Tag USerTagDb = null;
    private List_Tags_Workspaces WorkspaceTagsDb= null;
    private Table_Workspace WorkspaceDb = null;
    private Invite inviteTable = null;

    private Context context = null;

    private User LoggedUser;
    private List<User> ReachableUsers = new ArrayList<User>();
    private TextFile activeTextFile;

    public Workspace getActiveWorkspace() {
        return activeWorkspace;
    }

    public void setActiveWorkspace(Workspace activeWorkspace) {
        this.activeWorkspace = activeWorkspace;
    }

    private Workspace activeWorkspace;

    public void setUsersTagDb(User_Tag db){
        USerTagDb=db;
    }
    public void setListTagsdb(List_Tags_Workspaces WTDb){ WorkspaceTagsDb=WTDb; }
    public void setWorkspaces(Table_Workspace TDB){ WorkspaceDb=TDB; }
    public void setInviteTable(Invite TDB){ inviteTable=TDB; }
    public void setUsersDb(Users db){
        UserDb=db;
    }
    public void setContext(Context context){
        this.context=context;
    }

    public User getLoggedUser(){
        return this.LoggedUser;
    }

    /** funcao para fazer o login do utilizador
        input:
            UserName:String nome do utilizador a logar-se
     */
    //tes
    public void logIn(String userName){
        for(User user : ReachableUsers){
            if(user.getUserName().equals(userName)){
                this.LoggedUser=user;
                return ;
            }
        }

        User newUser = new User(this,userName,UserDb,USerTagDb,WorkspaceDb,WorkspaceTagsDb,context,inviteTable);
        ReachableUsers.add(newUser);
        LoggedUser = newUser;
        this.populate();
    }

    /** funcao para criar um novo workspace
        input:
            publico:Boolean Define se o workspace e publico ou nao
            tags:List<string> lista das tags do Workspace
            workspaceName:String nome do novo workspace
    */
    public void NewWorkspace(Boolean publico, List<String> tags,String workspaceName,int max_quota){
        LoggedUser.newWorkspace(publico, workspaceName, tags, max_quota, context);
    }

    /** funcao para obter a lista dos workspaces do loggedUser
     *
     * @return lista dos Owned Workspaces
     */

    public List<Workspace> getOwnedWorkspaces(){
        return LoggedUser.getOwnedWorkspaces();
    }

    public List<Workspace> getForeignWorkspaces(){
        return LoggedUser.getForeignWorkspaces();
    }

    /** Funcao para adicionar uma nova tag ao Workspace actual(que esta na variavel activeWorkspace)
         input:
             Tag:String tag a ser acrescentada ao Workspace actual
     */
    public void addTagWorkspace(String Tag){
        activeWorkspace.addTag(Tag);
    }

    /** Funcao para adicionar uma nova tag de pesquisa ao user loggado
         input:
            Tag:String tag de pesquisa a ser acrescentada ao utilizador que fez loggin
     */
    public void addTagUser(String Tag){
        LoggedUser.addTag(Tag);
    }

    /** funcao para remover a tag do workspace actual(que esta na variavel activeWorkspace)
        input:
            Tag:String tag a ser removida do Workspace actual
     */
    public void removeTagWorkspace(String Tag){
        activeWorkspace.removeTag(Tag);
    }

    /** funcao para remover a tag de pesquisa do user loggado
         input:
            Tag:String tag de pesquisa a ser removida do utilizador que fez loggin
     */
    public void removeTagUser(String Tag){
        LoggedUser.removeTag(Tag);
    }

    /** função para convidar um utilizador para o workspace que está em activeWorkspace
            input:
                Username:String utilizador a ser convidado a entrar no workspace
     */
    public void invite(String UserName,String Workspace){
        Workspace InviteWorkspace = LoggedUser.getWorkspace(Workspace);
        if(Workspace==null) return;
        for(User user : ReachableUsers){
            if(user.getUserName().equals(UserName)){
                user.addRemoteWorkspace(InviteWorkspace);
                return;
            }
        }
    }

    /** funcao para actualizar os workspaces que um utilizador pode aceder
     *
     */
    public void actualizaWorkspaces(){

    }

    /**  função para criar um ficheiro no workspace que está em activeWorkspace
         input:
            FileName:String nome do ficheiro a ser introduzido no workspace que está em activeWorkspace
     */
    public void createWorkspaceFile(String FileName){

    }

    /** função para salvar o ficheiro activeTextFile no workspace que está em activeWorkspace
     *
     */
    public void saveWorkspaceFile(){

    }

    /** função para ler um ficheiro no workspace que está em activeWorkspace
         input:
            FileName:String nome do ficheiro a ser lido no workspace que está em activeWorkspace
     */
    public void readWorkspaceFile(String FileName){

    }

    /** função para apagar um ficheiro no workspace que está em activeWorkspace
            input:
                FileName:String nome do ficheiro a ser lido no workspace que está em activeWorkspace
     */
    public void deleteWorkspaceFile(String FileName){

    }


    //FILESSYSTEM (Funções relacionadas com o file system)

//Função para eliminar um determiando Workspace, elimiando primeiro todos os seus ficheiros de texto e logo depois a pasta do workspace;
    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }

    //funçao que percorre todos os ficheiros de um determinado workspace para verificar o seu tamanho (length)
    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size=f.length();
        }
        return size;
    }

    public void writeFile(File workspace, FileOutputStream out1, String valorEditText){
        try {
            out1 = new FileOutputStream(workspace);
            String string = valorEditText;
            byte[] b = string.getBytes();
            out1.write(b);
            out1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void populate(){
        //this.UserDb;
        //this.USerTagDb;
        //this.WorkspaceTagsDb;
        //this.WorkspaceDb;

        Cursor Workspaces = this.WorkspaceDb.getAll(this.LoggedUser.getUserName());
        while(Workspaces.moveToNext()){
            Cursor List_tags = this.WorkspaceTagsDb.getAll(Workspaces.getString(1));
            List<String> tagList = new ArrayList<String>();
            while(List_tags.moveToNext()){
                tagList.add(List_tags.getString(1));
            }

            this.LoggedUser.newWorkspace(Workspaces.getInt(2)>0,Workspaces.getString(0),tagList,Workspaces.getInt(3),context);
        }
        //this.WorkspaceTagsDb.getAll();

    }
}