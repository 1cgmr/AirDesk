package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;


public class NewFile extends ActionBarActivity {
    EditText NomeFicheiro;
    Button Criar;
    String workspace_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file);

       Criar= (Button) findViewById(R.id.btnConfirmarFile);
       Criar.setOnClickListener(CreateFile);
       NomeFicheiro = (EditText) findViewById(R.id.editTextNameFile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workspace_path = extras.getString("WORKSPACE_PATH");
        }

    }

    private View.OnClickListener CreateFile=new View.OnClickListener(){
        public void onClick(View v){

            String Fich = NomeFicheiro.getText().toString();
            // Toast.makeText(getActivity(), NomeFicheiro.getText().toString(), Toast.LENGTH_LONG).show();
            File workspace = new File(workspace_path);
            File fileWithinMyDir1 = new File(workspace, Fich);
            try {
                fileWithinMyDir1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finish();
        }
    };

}
