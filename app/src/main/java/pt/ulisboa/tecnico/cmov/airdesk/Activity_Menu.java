package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;

public class Activity_Menu extends ActionBarActivity {

    User g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_menu);

        g = User.getInstance();

        final TextView mTextView = (TextView) findViewById(R.id.textViewUserEmail);
       mTextView.setText(g.getUserName());


        Button btnLogin = (Button) findViewById(R.id.btnListTags);
        btnLogin.setOnClickListener(ListTags);

        Button btnForeignWorkspaces= (Button) findViewById(R.id.btnForeignWorkspaces);
        btnForeignWorkspaces.setOnClickListener(FWorkspaces);

        Button btnNewWorkspaces= (Button) findViewById(R.id.btnNewWorkspaces);
        btnNewWorkspaces.setOnClickListener(NewWorkspaces);

        Button btnOwnedWorkspaces= (Button) findViewById(R.id.btnOwnedWorkspaces);
        btnOwnedWorkspaces.setOnClickListener(OwnedWorkspaces);

        inicializaWorkspaceFalsos(); //Para debug
    }

    private View.OnClickListener OwnedWorkspaces=new View.OnClickListener(){
        public void onClick(View v){
            startActivity(new Intent(Activity_Menu.this, owned_workspaces.class));
        }
    };

    private View.OnClickListener NewWorkspaces=new View.OnClickListener(){
        public void onClick(View v){
          startActivity(new Intent(Activity_Menu.this, New_Workspace.class));
        }
    };

    private View.OnClickListener FWorkspaces=new View.OnClickListener(){
        public void onClick(View v){
            startActivity(new Intent(Activity_Menu.this, Foreign_workspaces.class));
        }
    };

    private View.OnClickListener ListTags=new View.OnClickListener(){
        public void onClick(View v){
            startActivity(new Intent(Activity_Menu.this, User_Tags_List.class));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__menu, menu);
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

    public void inicializaWorkspaceFalsos(){
        //=============CRIA PASTAS PARA WORKSPACES=============//
        Context context = this.getApplicationContext();
        File mydir = context.getDir(g.getUserName(), Context.MODE_PRIVATE);
        Log.d("ABC", g.getUserName());
        File mySubDir1 = new File(mydir, "Workspace1");
        mySubDir1.mkdir();
        File mySubDir2 = new File(mydir, "Workspace2");
        mySubDir2.mkdir();
        File mySubDir3 = new File(mydir, "Workspace3");
        mySubDir3.mkdir();
        File mySubDir4 = new File(mydir, "Workspace4");
        mySubDir4.mkdir();
        //ESCREVER FILES NOS WORKSPACES
        File fileWithinMyDir1 = new File(mySubDir1, "test1.txt");
        File fileWithinMyDir2 = new File(mySubDir2, "test2.txt");
        File fileWithinMyDir3 = new File(mySubDir3, "test3.txt");
        File fileWithinMyDir4 = new File(mySubDir4, "test4.txt");
        FileOutputStream out1, out2, out3, out4 = null;
        try {
            out1 = new FileOutputStream(fileWithinMyDir1);
            out2 = new FileOutputStream(fileWithinMyDir2);
            out3 = new FileOutputStream(fileWithinMyDir3);
            out4 = new FileOutputStream(fileWithinMyDir4);
            String string = "teste de esctirebj erggrtg";
            byte[] b = string.getBytes();
            out1.write(b);
            out1.close();
            out2.write(b);
            out2.close();
            out3.write(b);
            out3.close();
            out4.write(b);
            out4.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //=================================================//
    }
}
