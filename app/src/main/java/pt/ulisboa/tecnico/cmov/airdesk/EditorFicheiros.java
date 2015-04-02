package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private File WorkspaceDir;
    private String PathWorkspcaeDir;
    private String name;
    EditText editor;
    String ValorEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_ficheiros);

        Button btnLogin = (Button) findViewById(R.id.btnGravarAlteracoes);
        btnLogin.setOnClickListener(Gravar);

        editor=(EditText)findViewById(R.id.editor);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            PathWorkspcaeDir = extras.getString("WORKSPACE_DIR");
            name = extras.getString("NOME_FICHEIRO");
        }

        File fileWithinMyDir = new File(PathWorkspcaeDir);
        WorkspaceDir = new File(fileWithinMyDir, name);

        try {
            BufferedReader leitor = new BufferedReader(new FileReader(WorkspaceDir));
            String exemplo = leitor.readLine();
            editor.setText(exemplo);
            leitor.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



//
        //Toast.makeText(this, fileWithinMyDirs.getPath(), Toast.LENGTH_LONG).show();
    }

    private String getValueEditor(){
       ValorEditor = editor.getText().toString();
        return ValorEditor;
    }

    private View.OnClickListener Gravar=new View.OnClickListener(){
        public void onClick(View v){
            String valorEditText = getValueEditor();

           // File fileWithinMyDir4 = new File(mySubDir4, "test4.txt");
            FileOutputStream out1 = null;
            try {
                out1 = new FileOutputStream(WorkspaceDir);
                String string = valorEditText;
                byte[] b = string.getBytes();
                out1.write(b);
                out1.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

}
