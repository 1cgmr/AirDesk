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
    private List<TextFile> listFiles;
    private GridviewAdapter mAdapter;
    private GridView gridView;
    private String NomeItemClicked;
    private pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.Workspace workspace;

    Table_Workspace helper=null;
    AirDesk globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_files);

        //Botão para criar um novo ficheiro, neste caso vai chamar o Dialog para inserir o nome do ficheiro a ser criado.
        Button btnCriarFile = (Button) findViewById(R.id.btnCriarFile);
        btnCriarFile.setOnClickListener(dialogFile);

        //Botão para gerar um ficheiro que ocupe restante ou total da quota máxima do workspace
        Button btnPopulate = (Button) findViewById(R.id.btnPopulate);
        btnPopulate.setOnClickListener(populateQuotaMax);

        // variaveis globais, para verificar qual o utilizador autenticado
        globals = (AirDesk) getApplicationContext();
        user=globals.getLoggedUser();
        workspace = globals.getActiveWorkspace();
        helper=new Table_Workspace(this);
        WorkspaceDir= workspace.getMydir();

        WorkspaceDirName = workspace.getName();
        PreencherListView();

        //Apresenta um menu para eliminar um determinado ficheiro quando o utilizador faz LongClick sobre o nome do workspace;
        registerForContextMenu(gridView);

        //Vai enviar para a activity "EditorFicheiro" o nome do ficheiro que pretende fazer modificações
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
               NomeItemClicked = mAdapter.getItem(position);
               Intent i = new Intent(getApplicationContext(), EditorFicheiros.class);
               i.putExtra("NOME_FICHEIRO", NomeItemClicked);
               startActivity(i);
            }
        });
    }

    public void PreencherListView(){
        listFiles = workspace.getListFiles();

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

                if(workspace.removeFile(NomeItemClicked)==true){
                    listFiles.remove(info.position);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Ficheiro Removido", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Não foi possivel remover o ficheiro", Toast.LENGTH_LONG).show();
                }
            default:
                return super.onContextItemSelected(item);
        }
    }

    // Guardar o nome do ficheiro que foi seleccionado na operações de LongClick, para saber qual vai ser eliminado.
    private void OnItemLongClickListener(int position) {
       NomeItemClicked = mAdapter.getItem(position);
    }

    // FUnção (onClick do botão AddFile) para chamar a actividade que vai criar um novo ficheiro.
    private View.OnClickListener dialogFile=new View.OnClickListener(){
        public void onClick(View v){
            startActivity(new Intent(Workspace.this, NewFile.class));
        }

    };

    //Função que vai criar um ficheiro com a quota máxima do workspace.
    private View.OnClickListener populateQuotaMax=new View.OnClickListener(){
        public void onClick(View v){
            long tamanhoWorkspace;
            if (workspace.CreateBigFile()==true){
                Toast.makeText(Workspace.this, "Ficheiro de teste (MAX Quota) criado!!!", Toast.LENGTH_LONG).show();
                PreencherListView();
            }
            else{
                Toast.makeText(Workspace.this, "O ficheiro já existe ou ultrapassou o limite máximo!!!", Toast.LENGTH_LONG).show();
            }
       }

    };

//Quando voltar tem que voltar a preencher a ListView, para reflectir as alterações (introdução de novos ficheiros)
    @Override
    protected void onRestart() {
        super.onRestart();
        PreencherListView();
    }

}