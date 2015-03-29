package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.*;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.Workspace;


public class owned_workspaces extends ActionBarActivity {

    private ArrayAdapter adapter;
    private List<Workspace> Owned_Workspaces = new ArrayList<Workspace>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_workspaces);

        AirDesk globals = (AirDesk) getApplicationContext();
        Owned_Workspaces.addAll(globals.getOwnedWorkspaces());

        final ListView OwnedList = (ListView) findViewById(R.id.Owned_workspaces_list);
        adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, Owned_Workspaces);
        OwnedList.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owned_workspaces, menu);
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

    public void sendInvitation(View v){
        Intent intent = new Intent(owned_workspaces.this, Send_invitation.class);
        startActivity(intent);
    }

}
