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
    pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.Workspace workspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file);

        // variaveis globais
        globals = (AirDesk) getApplicationContext();
        workspace = globals.getActiveWorkspace();
        g=globals.getLoggedUser();
        helper=new Table_Workspace(this);

        //Botão para criar o ficheiro
        Criar= (Button) findViewById(R.id.btnConfirmarFile);
        Criar.setOnClickListener(CreateFile);
        //EditText que permitirá escolher o nome do ficheiro a ser criado
        NomeFicheiro = (EditText) findViewById(R.id.editTextNameFile);

        //rebece da activity anterior qual o workspace a ser trabalhado, ou seja, que vão ser criados os ficheiros.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workspace_path = extras.getString("WORKSPACE_PATH");
        }
    }

    private View.OnClickListener CreateFile=new View.OnClickListener(){
        public void onClick(View v){
            long sizeWorkspace;
           //Fich representa o ficheiro de texto a ser criado, com o nome preenchido no EditText (NomeFicheiro)
            String Fich = NomeFicheiro.getText().toString();
            //workspace é o workspace actual, onde vão ser criados os ficheiros de texto
            File workspaceFile = new File(workspace_path);
            //O cursor recebe todos os tuplos da base de dados que cumpram os requisitos definidos, ou seja, vai retornar
            // a quota máxima de um determinado workspace do utilizador (owner).
            Cursor c= helper.getByQuota(workspaceFile.getName(), g.getUserName());

            if(c.moveToFirst() == false){
                Toast.makeText(getApplication(), "Workspace não existe", Toast.LENGTH_LONG).show();
            }
            else {
                c.moveToFirst();
                // guardar apneas o valor da quota máxima presente no cursor (c).
                Integer tamanhomax = helper.getQuota(c);
                helper.close();

                //criar um ficheiro
                File fileWithinMyDir1 = new File(workspaceFile, Fich);
                try {
                    fileWithinMyDir1.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                workspace.newFile(Fich);

                //função getFolderSiza retorna o tamanho global do workspace
                sizeWorkspace = globals.getFolderSize(workspaceFile);

                //verificar se o utilizador ultrapassou os limites (quota) do workspace quando cria um determinado ficheiro
                // se o utilizador ultrapassou os limites o ficheiro é removido para cumprir a quota definida
                if (sizeWorkspace >= tamanhomax) {
                    Toast.makeText(NewFile.this, "Não foi possivel criar o ficheiro, ultrapassou os limites do Workspace.", Toast.LENGTH_LONG).show();
                    fileWithinMyDir1.delete();
                }
            }
            finish();
        }
    };

}
