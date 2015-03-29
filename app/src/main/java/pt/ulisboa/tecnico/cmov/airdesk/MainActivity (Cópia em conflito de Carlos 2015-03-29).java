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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pt.ulisboa.tecnico.cmov.airdesk.DataBase.DataBaseAccess;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.User_Tag;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Users;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_CODE = 1;
    private static Context context;
    Users Db=null;
    Cursor dataset_cursor=null;
    EditText editUser=null;
    String userEmail="";
    File mydir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_main);
            editUser = (EditText) findViewById(R.id.EditTextUserIdentification);
            Db=new Users(this);

            Button btnLogin = (Button) findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(Login);
            MainActivity.context = getApplicationContext();


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
            User g = User.getInstance();
            g.setUserName(userEmail);

            Cursor c=Db.getByEmailUser(userEmail);
            if(!c.moveToFirst()){
                Db.insert_Users(editUser.getText().toString());
                mydir = context.getDir(userEmail, Context.MODE_PRIVATE);
            }
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
