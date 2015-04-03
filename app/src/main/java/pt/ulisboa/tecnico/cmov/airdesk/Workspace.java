package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.Adapters.GridviewAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.Adapters.ItemBean;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;


public class Workspace extends ActionBarActivity {

    private User user;
    private File UserDir;
    private File WorkspaceDir;
    private ItemBean bean;
    private String WorkspaceDirName;
    private ArrayList<String> listFiles;
    private GridviewAdapter mAdapter;
    private GridView gridView;
    String nome;
    private String NomeItemClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_files);

        Button btnCriarFile = (Button) findViewById(R.id.btnCriarFile);
        btnCriarFile.setOnClickListener(dialogFile);

        // variaveis globais
        AirDesk globals = (AirDesk) getApplicationContext();
        user=globals.getLoggedUser();

        UserDir = globals.getLoggedUser().getMydir();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            WorkspaceDirName = extras.getString("WORKSPACE_ID");
        }

        listFiles = new ArrayList<String>();
        for(final File fileEntry : UserDir.listFiles()){
            //listFiles.add(fileEntry.getName());
            if(fileEntry.getName().equals(WorkspaceDirName)){
                WorkspaceDir = fileEntry;
            }
        }

        for(final File fileEntry : WorkspaceDir.listFiles()){
            listFiles.add(fileEntry.getName());
        }

        mAdapter = new GridviewAdapter(this,listFiles);
        this.gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);

        registerForContextMenu(gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
               NomeItemClicked = mAdapter.getItem(position);
               Intent i = new Intent(getApplicationContext(), EditorFicheiros.class);
               i.putExtra("WORKSPACE_DIR", WorkspaceDir.getPath());
               i.putExtra("NOME_FICHEIRO", NomeItemClicked);
               startActivity(i);
            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_workspace, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.eliminar_file:
                OnItemLongClickListener(info.position);
                for(final File fileEntry : WorkspaceDir.listFiles()){

                    nome = fileEntry.getName();

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

    private void OnItemLongClickListener(int position) {
       NomeItemClicked = mAdapter.getItem(position);
    }

    private View.OnClickListener dialogFile=new View.OnClickListener(){
        public void onClick(View v){
           FragmentManager manager=getFragmentManager();
           Dialog_New_File myDialog= new Dialog_New_File();
           myDialog.show(manager, "Dialog_New_File");
        }
    };

}
