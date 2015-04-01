package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.DialogFragment;
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


public class Dialog_Send_Invitation extends DialogFragment {
    Button btnInvitation;
    EditText UserId;
    AirDesk context;
    String workspace;

    public Dialog_Send_Invitation(){}

    public void setGlobals(AirDesk applicationContext) {
        this.context=applicationContext;
    }

    public void setWorkspace(String workspace){
        this.workspace=workspace;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.activity_dialog_send_invitation, null);

        btnInvitation= (Button) view.findViewById(R.id.btnInitation);
        btnInvitation.setOnClickListener(Invitation);

        btnInvitation= (Button) view.findViewById(R.id.btnCancel);
        btnInvitation.setOnClickListener(Cancel);

        UserId = (EditText) view.findViewById(R.id.editTextInvitation);
        setCancelable(false);
        return view;
    }


    private View.OnClickListener Invitation=new View.OnClickListener(){
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Utilizador convidado", Toast.LENGTH_LONG).show();//editTextInvitation
            context.invite(UserId.getText().toString(),workspace);
            dismiss();
        }
    };

    private View.OnClickListener Cancel=new View.OnClickListener(){
        public void onClick(View v) {
            dismiss();
        }
    };






}
