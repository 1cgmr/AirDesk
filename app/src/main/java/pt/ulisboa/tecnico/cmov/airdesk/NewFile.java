package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Table_Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.*;

public class NewFile extends ActionBarActivity {
    EditText NomeFicheiro;
    Button Criar;
    String workspace_path;
    Table_Workspace helper=null;
    User g;
    AirDesk globals;
    private File WorkspaceDir;
    private pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.Workspace workspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file);

        // variaveis globais
        globals = (AirDesk) getApplicationContext();
      //  g=globals.getLoggedUser();
       // helper=new Table_Workspace(this);

        workspace = globals.getActiveWorkspace();
     //   WorkspaceDir= workspace.getMydir();
      //  workspace_path=WorkspaceDir.getPath();

        //Botão para criar o ficheiro
        Criar= (Button) findViewById(R.id.btnConfirmarFile);
        Criar.setOnClickListener(CreateFile);
        //EditText que permitirá escolher o nome do ficheiro a ser criado
        NomeFicheiro = (EditText) findViewById(R.id.editTextNameFile);
    }

    private View.OnClickListener CreateFile=new View.OnClickListener(){
        public void onClick(View v){

            String Fich = NomeFicheiro.getText().toString();

            if((workspace.newFile(Fich))==true){
               Toast.makeText(NewFile.this, "Ficheiro criado com sucesso", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(NewFile.this, "Não foi possivel criar o ficheiro", Toast.LENGTH_LONG).show();
            }

            finish();
        }
    };
}