package pt.ulisboa.tecnico.cmov.airdesk.Sockets;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;import java.lang.Override;import java.lang.String;import java.lang.Thread;import java.lang.Void;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;

/**
 * Created by 25 on 5/8/2015.
 */
public class SocketServer extends AsyncTask<Void, SimWifiP2pSocket, Void> {
    private SimWifiP2pSocketServer mSrvSocket ;
    private SimWifiP2pSocket mCliSocket;
    private int GOPort;

    @Override
    protected void onPreExecute() {
        try {
            mSrvSocket = new SimWifiP2pSocketServer(GOPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                SimWifiP2pSocket sock = mSrvSocket.accept();
                if (mCliSocket != null && mCliSocket.isClosed()) {
                    mCliSocket = null;
                }
                if (mCliSocket != null) {
                    Log.d("Socket error", "Closing accepted socket because mCliSocket still active.");
                    sock.close();
                } else {
                    publishProgress(sock);
                }
            } catch (IOException e) {
                Log.d("Error accepting socket:", e.getMessage());
                break;
                //e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(SimWifiP2pSocket... values) {
        SimWifiP2pSocket s=values[0];

        try {
            BufferedReader InFromClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter OutFromClient= new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            String st;

            while ((st = InFromClient.readLine()) != null) {
                //processar entradas
                //enviar respostas
            }
        } catch (IOException e) {
            Log.d("Error reading socket:", e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        try {
            mSrvSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void entry_process(String entry){
        String[] Parsed_Entry = entry.split(" ");
        //Cliente pede para ler ficheiro
        if(Parsed_Entry[0].equals("Read_File")){
            //TODO readFile
        }
        // Cliente pede lista de ficheiros de um dado workspace
        else if(Parsed_Entry[0].equals("Get_List_Files")){
            //TODO Get_List_Files
        }
        // Cliente pede para remover um ficheiro de um Workspace
        else if(Parsed_Entry[0].equals("Remove_File")){
            //TODO Remove_File
        }
        // Cliente pede para ser criado um novo ficheiro
        else if(Parsed_Entry[0].equals("New_File")){
            //TODO New_File
        }
        // Cliente pede para modificar o conteudo de um ficehrio
        else if(Parsed_Entry[0].equals("Modify_File")){
            //TODO Modify_File
        }
        // Cliente pede todos os workspaces que sao relevantes para a sua lista de tags
        else if(Parsed_Entry[0].equals("Get_Workspace_By_Tags")){
            //TODO Get_Workspace_By_Tags
        }
        // Cliente envia para o Servidor os workspaces para o qual o servidor foi convidado
        else if(Parsed_Entry[0].equals("Get_Workspace_Invitation")){
            //TODO Get_Workspace_Invitation
        }
        // Cliente envia para o servidor os workspaces que que foram alterados
        else if(Parsed_Entry[0].equals("Refresh_Workspace")){
            //TODO Refresh_Workspace
        }
    }
}
