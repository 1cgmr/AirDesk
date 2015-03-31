package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import android.app.Application;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ist168635 on 25-03-2015.
 */
public class AirDesk extends Application {
    private User LoggedUser;
    private List<User> ReachableUsers = new ArrayList<User>();
    private TextFile activeTextFile;
    private Workspace activeWorkspace;

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

        User newUser = new User(userName);
        ReachableUsers.add(newUser);
        LoggedUser = newUser;
    }

    /** funcao para criar um novo workspace
        input:
            publico:Boolean Define se o workspace e publico ou nao
            tags:List<string> lista das tags do Workspace
            workspaceName:String nome do novo workspace
    */
    public void NewWorkspace(Boolean publico, List<String> tags,String workspaceName,int max_quota){
        LoggedUser.newWorkspace(publico,workspaceName,tags, max_quota);
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

}