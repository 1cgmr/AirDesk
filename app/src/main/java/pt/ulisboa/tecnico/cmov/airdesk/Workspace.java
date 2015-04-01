package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.Adapters.GridviewAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;


public class Workspace extends ActionBarActivity {

    private User user;
    private File UserDir;
    private File WorkspaceDir;
    private String WorkspaceDirName;
    private ArrayList<String> listFiles;
    private GridviewAdapter mAdapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_files);

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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Toast.makeText(Workspace.this, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workspace, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
