package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;


public class Dialog_New_File extends DialogFragment {

    Button Criar;
    EditText NomeFicheiro;
    String caminhoWorkspace;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.activity_dialog_new_file, null);

        Criar= (Button) view.findViewById(R.id.btnConfirmarFile);
        Criar.setOnClickListener(sim);

        NomeFicheiro = (EditText) view.findViewById(R.id.editTextNameFile);
        setCancelable(false);
        return view;
    }

    private View.OnClickListener sim=new View.OnClickListener(){
        public void onClick(View v) {


        }
    };
}
