package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Invite;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.List_Tags_Workspaces;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Table_Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.User_Tag;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Users;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_CODE = 1;
    private static Context context;
    File mydir;
    Users Db=null;
    Cursor dataset_cursor=null;
    EditText editUser=null;
    String userEmail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_main);
            editUser = (EditText) findViewById(R.id.EditTextUserIdentification);

            User_Tag UserTagDb = new User_Tag(this);
            Db=new Users(this);
            //
            List_Tags_Workspaces WorkspaceTagsDb= new List_Tags_Workspaces(this);
            Table_Workspace WorkspaceDb = new Table_Workspace(this);
            Invite inviteTable = new Invite(this);

            // variaveis globais
            AirDesk globals = (AirDesk) getApplicationContext();
            globals.setUsersDb(Db);
            globals.setUsersTagDb(UserTagDb);
            globals.setListTagsdb(WorkspaceTagsDb);
            globals.setWorkspaces(WorkspaceDb);
            globals.setInviteTable(inviteTable);

            Button btnLogin = (Button) findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(Login);
            MainActivity.context = getApplicationContext();

            globals.setContext(context);
        }
        catch (Exception e){
            Log.e("ERROR", "ERROR IN CODE:" + e.toString());
            e.printStackTrace();
        }
    }
    public void onDestroy(){
        super.onDestroy();
        Db.close();
    }

    private View.OnClickListener Login=new View.OnClickListener(){
        public void onClick(View v){
            userEmail=editUser.getText().toString();

            // variaveis globais
            AirDesk globals = (AirDesk) getApplicationContext();
            globals.logIn(userEmail);

            startActivity(new Intent(MainActivity.this, Activity_Menu.class));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
