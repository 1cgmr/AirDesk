package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.DataBase.List_Tags_Workspaces;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Table_Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;


public class New_Workspace extends ActionBarActivity {

//    private ArrayAdapter adapter;
    private List<String> Tags = new ArrayList<String>();
    Table_Workspace helper=null;
    User g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__workspace);

//        final ListView TagList= (ListView) findViewById(R.id.Tag_list);
 //       adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, Tags);
  //      TagList.setAdapter(adapter);

        helper=new Table_Workspace(this);
        AirDesk globals = (AirDesk) getApplicationContext();
        g=globals.getLoggedUser();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new__workspace, menu);
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

    //   public void AddTag(View v){
    //    final EditText editText = (EditText) findViewById(R.id.workspace_tag);
    //    if(editText.getText().toString().compareTo("")!=0) {
    //        Tags.add(0, editText.getText().toString());
    //        editText.setText("");
    //        adapter.notifyDataSetChanged();
    //    }
   // }

    public void NewWorkspace(View v){
        final EditText name = (EditText) findViewById(R.id.workspace_name);
        final CheckBox publico = (CheckBox) findViewById(R.id.publico);
        final EditText max_quota= (EditText) findViewById(R.id.maximum_workspace_size);

        AirDesk globals = (AirDesk) getApplicationContext();
        globals.NewWorkspace(publico.isChecked(),Tags,name.getText().toString(), Integer.parseInt(max_quota.getText().toString()));

        helper.insert_Workspace(name.getText().toString(), g.getUserName(), publico.isChecked(), Integer.parseInt(max_quota.getText().toString()));
        helper.close();
        startActivity(new Intent(New_Workspace.this, Owned_workspaces.class));
    }


}
