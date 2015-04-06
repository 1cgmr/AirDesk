package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.Adapters.ItemBean;
import pt.ulisboa.tecnico.cmov.airdesk.Adapters.ListViewCustomAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Table_Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;


public class Owned_workspaces extends Activity  {

    ListView lview3;
    ListViewCustomAdapter adapter;
    private ArrayList<Object> itemList;
    private ItemBean bean;
    private String NomeItemClicked;
    User user;
    private File userDir;
    private static Context context;
    String nome;
    Table_Workspace helper=null;
    AirDesk globals;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workspace_list);

        helper=new Table_Workspace(this);

        // variaveis globais
        globals = (AirDesk) getApplicationContext();
        user=globals.getLoggedUser();

        userDir =  globals.getLoggedUser().getMydir();

        Button btnSimples = (Button) findViewById(R.id.btnAddWorkspace);
        btnSimples.setOnClickListener(AddWorkspace);

        PreencherListView();

        registerForContextMenu(lview3);

        lview3.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {


                bean = (ItemBean) adapter.getItem(position);
                NomeItemClicked=bean.getTitle();
                Intent i = new Intent(getApplicationContext(), Workspace.class);
                i.putExtra("WORKSPACE_ID", NomeItemClicked);
                finish();
                startActivity(i);
            }
        });
    }

    public void PreencherListView(){
        prepareArrayLists();
        lview3 = (ListView) findViewById(R.id.listView1);
        adapter = new ListViewCustomAdapter(this, itemList);
        lview3.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        PreencherListView();
    }

    private View.OnClickListener AddWorkspace=new View.OnClickListener(){
        public void onClick(View v){
            startActivity(new Intent(Owned_workspaces.this, New_Workspace.class));
        }
    };

    public void prepareArrayLists()
    {
        itemList = new ArrayList<>();

        for(final File fileEntry : userDir.listFiles()){
            AddObjectToList(fileEntry.getName(), "Nº Ficheiros: "+fileEntry.listFiles().length);
        }
    }

    // Add one item into the Array List
    public void AddObjectToList(String title, String desc)
    {
        bean = new ItemBean();
        bean.setDescription(desc);
        bean.setTitle(title);
        itemList.add(bean);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_owned_workspaces, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.eliminar_workspace:
                for (final File fileEntry : userDir.listFiles()) {

                    OnItemLongClickListener(itemList, item);
                    nome = fileEntry.getName();
                    //verificar se o workspace actual é o mesmo que foi carrgado "clicked"
                    if(nome.equals(NomeItemClicked)){
                        File file = new File(userDir.getPath() + "/" + nome);
                        //eliminar workspace
                        globals.deleteDirectory(file);
                        //eliminar o workspace do ListView
                        itemList.remove(info.position);
                        adapter.notifyDataSetChanged();
                        //eliminar o workspace da base de dados
                        helper.delete_Workspace(NomeItemClicked, user.getUserName());
                        return true;
                    }
                }
            case R.id.convidar:
                OnItemLongClickListener(itemList, item);
                FragmentManager manager=getFragmentManager();
                Dialog_Send_Invitation myDialog= new Dialog_Send_Invitation();
                myDialog.setGlobals((AirDesk) getApplicationContext());
                myDialog.setWorkspace(NomeItemClicked);
                myDialog.show(manager, "MyDialog");
                return true;
            case R.id.tamanho_folder:
                ItemBean bean = (ItemBean) adapter.getItem(info.position);
                NomeItemClicked=bean.getTitle();
                File file = new File(userDir.getPath() + "/" + NomeItemClicked);
                Toast.makeText(this, "Tamanho da folder: "+ globals.getFolderSize(file), Toast.LENGTH_LONG).show();
                return true;
            case R.id.lista_tags:
                bean = (ItemBean) adapter.getItem(info.position);
                NomeItemClicked=bean.getTitle();
                Intent i = new Intent(getApplicationContext(), Workspace_Tags_List.class);
                i.putExtra("WORKSPACE_ID", NomeItemClicked);
                finish();
                startActivity(i);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //Guardar o nome do workspace que foi escolhido através do ItemLongClick da ListView.
    private void OnItemLongClickListener(ArrayList<Object> position, MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ItemBean bean = (ItemBean) adapter.getItem(info.position);
        NomeItemClicked = bean.getTitle();
    }

}