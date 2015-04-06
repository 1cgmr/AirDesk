package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Table_Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;

import static java.nio.charset.StandardCharsets.*;

public class EditorFicheiros extends ActionBarActivity {
    private File WorkspaceDir; //Ficheiro com todas as informações do workspace
    private String PathWorkspcaeDir; //Caminho do Workspace
    private String name; // Nome do ficheiro a modificar
    EditText editor; //editor com o conteudo de escrita e leitura do ficheiro
    String ValorEditor; // Valor actual do editor, antes de gravar no OnClick do botão
    User g;
    Table_Workspace helper=null;
    StringBuilder builder;
    File fileWithinMyDir;
    AirDesk globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_ficheiros);

        // variaveis globais
        globals = (AirDesk) getApplicationContext();
        g=globals.getLoggedUser();
        helper=new Table_Workspace(this);

        //Botão Gravar alterações
        Button btnLogin = (Button) findViewById(R.id.btnGravarAlteracoes);
        btnLogin.setOnClickListener(Gravar);

        // EditText "editor" para leitura e escrita do conteudo do ficheiro
        editor=(EditText)findViewById(R.id.editor);
        editor.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        //recebe da activity anterior, o directorio do workspace e o nome do ficheiro a modificar ou ler;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PathWorkspcaeDir = extras.getString("WORKSPACE_DIR");
            name = extras.getString("NOME_FICHEIRO");
        }

        //Path do Workspace onde se encontra o nosso ficheiro;
        //fileWithinMyDir - path do workspace actual, name - nome do ficheiro a modificar
        fileWithinMyDir = new File(PathWorkspcaeDir);
        WorkspaceDir = new File(fileWithinMyDir, name);

        // TextView (Titulo) - Titulo com o nome do ficheiro actual a modificar;
        TextView txtNomeFich = (TextView) findViewById(R.id.textViewNomeFicheiro);
        txtNomeFich.setText(name);

        // operação de leitura do conteudo do ficheiro de texto, carregando o seu conteudo para o editor (editText)
        builder= globals.ReadFile(WorkspaceDir, editor);
    }

    //Função que retorna o valor do editText
    private String getValueEditor(){
        ValorEditor = editor.getText().toString();
        return ValorEditor;
    }

    //Gravar o conteudo do editText guardado anteriormente, para o respectivo ficheiro txt;
    private View.OnClickListener Gravar=new View.OnClickListener(){
        public void onClick(View v){
            String valorEditText = getValueEditor();
            long tamanhoWorkspace;

            //verificar qual o limite (quota) do workspace que pertence o ficheiro que esta a ser modificado
            Cursor c= helper.getByQuota(fileWithinMyDir.getName(), g.getUserName());
//            Toast.makeText(getApplication(), fileWithinMyDir.getName(), Toast.LENGTH_LONG).show();

            if(c.moveToFirst() == false){
                Toast.makeText(getApplication(), "Workspace não existe", Toast.LENGTH_LONG).show();
            }
            else {
                c.moveToFirst();
                Integer tamanhomax = helper.getQuota(c);
                helper.close();

                FileOutputStream out1 = null;
                // Função que escreve todas as alterações efectuadas no editText para o ficheiro seleccionado do directorio (WorkspaceDir)
                globals.writeFile(WorkspaceDir, out1, valorEditText);

                //Verifica se as alterações propostas cumprem os limites (quota) do workspace, caso tenha ultrapassado
                // será restaurado o conteudo anterior do ficheiro.
                tamanhoWorkspace = globals.getFolderSize(fileWithinMyDir);
                if (tamanhoWorkspace > tamanhomax) {
                    Toast.makeText(getApplication(), "Não foi possivel criar o ficheiro, ultrapassou os limites do Workspace.", Toast.LENGTH_LONG).show();
                    globals.writeFile(WorkspaceDir, out1, builder.toString());
                }
            }
                finish();
        }
    };
}