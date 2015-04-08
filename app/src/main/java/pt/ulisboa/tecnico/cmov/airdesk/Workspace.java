package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.Adapters.GridviewAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.Adapters.ItemBean;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Table_Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;

public class Workspace extends ActionBarActivity {

    private User user;
    private File UserDir;
    private File WorkspaceDir;
    private ItemBean bean;
    private String WorkspaceDirName;
    //private ArrayList<String> listFiles;
    private List<TextFile> listFiles;
    private GridviewAdapter mAdapter;
    private GridView gridView;
    String nome;
    private String NomeItemClicked;
    private pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.Workspace workspace;

    Table_Workspace helper=null;
    AirDesk globals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_files);

        Button btnCriarFile = (Button) findViewById(R.id.btnCriarFile);
        btnCriarFile.setOnClickListener(dialogFile);

        Button btnPopulate = (Button) findViewById(R.id.btnPopulate);
        btnPopulate.setOnClickListener(populate);

        // variaveis globais, para verificar qual o utilizador autenticado
        globals = (AirDesk) getApplicationContext();
        user=globals.getLoggedUser();
        workspace = globals.getActiveWorkspace();
        helper=new Table_Workspace(this);

        // verificar qual o directorio do utilizador autenticado
        UserDir = globals.getLoggedUser().getMydir();
        // Recebe da actividade anterior o nome do Workspace que foi seleccionado
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            WorkspaceDirName = extras.getString("WORKSPACE_ID");
        }

        PreencherListView();

        registerForContextMenu(gridView);

        //Vai enviar para a activity "EditorFicheiro" o caminho do workspace e o nome do ficheiro que pretende fazer modificações
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
               NomeItemClicked = mAdapter.getItem(position);
               Intent i = new Intent(getApplicationContext(), EditorFicheiros.class);
               i.putExtra("WORKSPACE_DIR", workspace.getMydir());
               i.putExtra("NOME_FICHEIRO", NomeItemClicked);
               startActivity(i);
            }
        });
    }

//    public void PreencherListView(){
//        listFiles = new ArrayList<String>();
//        //ciclo que vai procurar qual o workspace seleccionado, e guardar num ficheiro.
//        for(final File fileEntry : UserDir.listFiles()){
//            if(fileEntry.getName().equals(WorkspaceDirName)){
//                WorkspaceDir = fileEntry;
//            }
//        }
//
//        //Preencher um ArrayList com todos os nomes dos ficheiros presentes no workspace seleccionado.
//        for(final File fileEntry : WorkspaceDir.listFiles()){
//            listFiles.add(fileEntry.getName());
//        }
//
//        //Operações para preencher a GridView com os valores recolhidos anteriormente.
//        mAdapter = new GridviewAdapter(this,listFiles);
//        this.gridView = (GridView) findViewById(R.id.gridView);
//        gridView.setAdapter(mAdapter);
//
//        //Afectar o TextView com o nome do Workspace actualmente seleccionado (Titulo do ecrã);
//        TextView textViewName = (TextView) findViewById(R.id.textViewName);
//        textViewName.setText(WorkspaceDirName);
//    }

    public void PreencherListView(){
        listFiles = workspace.getListFiles();
        //ciclo que vai procurar qual o workspace seleccionado, e guardar num ficheiro.


        //Operações para preencher a GridView com os valores recolhidos anteriormente.
        mAdapter = new GridviewAdapter(this,listFiles);
        this.gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);

        //Afectar o TextView com o nome do Workspace actualmente seleccionado (Titulo do ecrã);
        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName.setText(WorkspaceDirName);
    }

    // o menu desta activity apenas vai ter uma operação remoção de um determiando ficheiro
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_workspace, menu);
    }

    // Verificar atraves de um LongOnClick qual o ficheiro a ser eliminado.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.eliminar_file:
                //chama a função OnItemLongClickListener para afectar a variavel NomeItemClicked, para verificar qual o ficheiro seleccionado.
                OnItemLongClickListener(info.position);
                for(final File fileEntry : WorkspaceDir.listFiles()){
                    //nome do ficheriro
                    nome = fileEntry.getName();
                    // verificar se o presente ficheiro (fisico) é igual ao valor do nome do ficheiro seleccionado.
                    // se sim, vai ser eliminado fisicamente (localmente), da lista de ficheiros e reflectido na GridView
                    if(nome.equals(NomeItemClicked)){
                        listFiles.remove(info.position);
                        mAdapter.notifyDataSetChanged();
                        fileEntry.delete();
                        return true;
                    }
                }
            default:
                return super.onContextItemSelected(item);
        }
    }

    // Guardar o nome do ficheiro que foi seleccionado na operações de LongClick, para saber qual vai ser eliminado.
    private void OnItemLongClickListener(int position) {
       NomeItemClicked = mAdapter.getItem(position);
    }

    // FUnção (onClick do botão AddFile) para chamar a actividade que vai criar um novo ficheiro. Passando o caminho (path) do workspace
    private View.OnClickListener dialogFile=new View.OnClickListener(){
        public void onClick(View v){
            Intent i = new Intent(getApplicationContext(), NewFile.class);
            i.putExtra("WORKSPACE_PATH", WorkspaceDir.getPath());
            startActivity(i);
        }

    };

    private View.OnClickListener populate=new View.OnClickListener(){
        public void onClick(View v){
            long tamanhoWorkspace;
            Cursor c= helper.getByQuota(WorkspaceDir.getName(), user.getUserName());

            if(c.moveToFirst() == false){
                Toast.makeText(getApplication(), "Workspace não existe", Toast.LENGTH_LONG).show();
            }
            else {
                c.moveToFirst();
                // guardar apneas o valor da quota máxima presente no cursor (c).
                Integer tamanhomax = helper.getQuota(c);
                helper.close();

                //criar um ficheiro
                File file1 = new File(WorkspaceDir, "FileMaxSize.txt");
                try {
                    file1.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //função getFolderSiza retorna o tamanho global do workspace
                long sizeWorkspace = globals.getFolderSize(WorkspaceDir);
                FileOutputStream out1 = null;
                String frase="";

                while ((globals.getFolderSize(WorkspaceDir)) != tamanhomax){
                    frase = frase+"a";
                    globals.writeFile(file1, out1, frase);
                }
            }

            //Preencher a ListVIew para reflectir a introdução do novo ficheiro de teste (MAX_QUOTA)
            PreencherListView();
            Toast.makeText(getApplication(), "Ficheiro de teste (MAX_Quota) criado!!!", Toast.LENGTH_LONG).show();
        }

    };

//Quando voltar tem que voltar a preencher a ListView, para reflectir as alterações (introdução de novos ficheiros)
    @Override
    protected void onRestart() {
        super.onRestart();
        PreencherListView();
    }

}