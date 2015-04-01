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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.Adapters.ItemBean;
import pt.ulisboa.tecnico.cmov.airdesk.Adapters.ListViewCustomAdapter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workspace_list);
        user = User.getInstance();

        context = this.getApplicationContext();
        userDir = context.getDir(user.getUserName(), Context.MODE_PRIVATE);

        Button btnSimples = (Button) findViewById(R.id.btnAddWorkspace);
        btnSimples.setOnClickListener(AddWorkspace);

        prepareArrayLists();
        lview3 = (ListView) findViewById(R.id.listView1);
        adapter = new ListViewCustomAdapter(this, itemList);
        lview3.setAdapter(adapter);

        registerForContextMenu(lview3);
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
        getMenuInflater().inflate(R.menu.menu_workspace, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.abrir_workspace:
                //startActivity(new Intent(Owned_workspaces.this, Workspace.class));

                OnItemLongClickListener(itemList, item);
                Log.d("ABC", "@onContextItemSelected "+NomeItemClicked);
                Intent i = new Intent(getApplicationContext(), Workspace.class);
                i.putExtra("WORKSPACE_ID", NomeItemClicked);
                startActivity(i);
                return true;

            case R.id.eliminar_workspace:
                for (final File fileEntry : userDir.listFiles()) {

                    OnItemLongClickListener(itemList, item);
                    String nome = fileEntry.getName();

                    if(nome.equals(NomeItemClicked)){
                        File file = new File(userDir.getPath() + "/" + nome);
                        Toast.makeText(this, "Tamanho da folder: "+ getFolderSize(file), Toast.LENGTH_LONG).show();
                        deleteDirectory(file);
                        itemList.remove(info.position);
                        adapter.notifyDataSetChanged();
                        return true;
                        }
                       }
            case R.id.convidar:
                OnItemLongClickListener(itemList, item);
                FragmentManager manager=getFragmentManager();
                Dialog_Send_Invitation myDialog= new Dialog_Send_Invitation();
                myDialog.show(manager, "MyDialog");
                return true;
            case R.id.tamanho_folder:
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void OnItemLongClickListener(ArrayList<Object> position, MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        ItemBean bean = (ItemBean) adapter.getItem(info.position);
        NomeItemClicked = bean.getTitle();

    }

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


}