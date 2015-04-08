package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.Adapters.ItemBean;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.*;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.Workspace;


public class Foreign_workspaces extends ActionBarActivity {


    private ArrayAdapter adapter;
    private List<WorkspaceRemoto> Foreign_Workspaces = new ArrayList<WorkspaceRemoto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_workspaces);

        AirDesk globals = (AirDesk) getApplicationContext();
        Foreign_Workspaces.addAll(globals.getForeignWorkspaces());

        final ListView foreignList = (ListView) findViewById(R.id.Foreign_workspaces_list);
        adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, Foreign_Workspaces);
        foreignList.setAdapter(adapter);

        foreignList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                WorkspaceRemoto workspaceRemoto = (WorkspaceRemoto) adapter.getItem(position);// variaveis globais
                AirDesk globals = (AirDesk) getApplicationContext();
                globals.setActiveWorkspace(workspaceRemoto);

                Toast.makeText(getApplication(), "Tamanho: "+workspaceRemoto.getListFiles().size(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplication(), workspaceRemoto.getName(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplication(), "Privado? "+workspaceRemoto.getPublico(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplication(), "Tamanho da quota: "+workspaceRemoto.getQuota(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), pt.ulisboa.tecnico.cmov.airdesk.Workspace.class);
                i.putExtra("WORKSPACE_ID", workspaceRemoto.getName());
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_foreign_workspaces, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
