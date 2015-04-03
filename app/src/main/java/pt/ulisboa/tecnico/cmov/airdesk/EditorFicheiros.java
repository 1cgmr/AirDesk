package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
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

import static java.nio.charset.StandardCharsets.*;


public class EditorFicheiros extends ActionBarActivity {

    private File WorkspaceDir; //Ficheiro com todas as informações do workspace
    private String PathWorkspcaeDir; //Caminho do Workspace
    private String name; // Nome do ficheiro a modificar
    EditText editor; //editor com o conteudo de escrita e leitura do ficheiro
    String ValorEditor; // Valor actual do editor, antes de gravar no OnClick do botão

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_ficheiros);

        //Botão Gravar alterações
        Button btnLogin = (Button) findViewById(R.id.btnGravarAlteracoes);
        btnLogin.setOnClickListener(Gravar);

        // EditText "editor" para leitura e escrita do conteudo do ficheiro
        editor=(EditText)findViewById(R.id.editor);
        editor.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        Bundle extras = getIntent().getExtras();
        //recebe da outra activity o directorio do workspace e o nome do ficheiro a modificar ou ler;
        if (extras != null) {
            PathWorkspcaeDir = extras.getString("WORKSPACE_DIR");
            name = extras.getString("NOME_FICHEIRO");
        }

        //Path do Workspace onde se encontra o nosso ficheiro;
        //fileWithinMyDir - path do workspace actual, name - nome do ficheiro a modificar
        File fileWithinMyDir = new File(PathWorkspcaeDir);
        WorkspaceDir = new File(fileWithinMyDir, name);

        // TextView (Titulo) - Titulo com o nome do ficheiro actual a modificar;
        TextView txtNomeFich = (TextView) findViewById(R.id.textViewNomeFicheiro);
        txtNomeFich.setText(name);

        BufferedReader br;
        //operação de load do conteudo do ficheiro txt
        try {
            BufferedReader leitor = new BufferedReader(new FileReader(WorkspaceDir));
           // br= new BufferedReader(leitor);
            String read;
            StringBuilder builder = new StringBuilder("");

            while((read = leitor.readLine()) !=null){
                builder.append(read);
            }

            editor.setText(builder.toString());
            leitor.close();

         //   editor.setText(stringBuffer);

   //         String exemplo = leitor.readLine();
    //        editor.setText(exemplo);
    //        leitor.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // buffer for storing file contents in memory
   // StringBuffer stringBuffer = new StringBuffer("");
    // for reading one line
  //  String line = null;
// keep reading till readLine returns null
 //   while ((line = Buff.readLine()) != null) {
        // keep appending the to the buffer
       // stringBuffer.append(line);

    //guardar em memoria o valor do editText
    private String getValueEditor(){
        ValorEditor = editor.getText().toString();
        return ValorEditor;
    }
    //Gravar o conteudo do editText guardado anteriormente, para o respectivo ficheiro txt;

    private View.OnClickListener Gravar=new View.OnClickListener(){
        public void onClick(View v){
            String valorEditText = getValueEditor();

            FileOutputStream out1 = null;
            try {
                out1 = new FileOutputStream(WorkspaceDir);
                String string = valorEditText;
                byte[] b = string.getBytes();
                out1.write(b);
                out1.close();
                finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}