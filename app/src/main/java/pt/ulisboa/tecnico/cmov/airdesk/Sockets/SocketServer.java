package pt.ulisboa.tecnico.cmov.airdesk.Sockets;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;import java.lang.Override;
import java.lang.String;
import java.lang.Thread;import java.lang.Void;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.WorkspaceLocal;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.WorkspaceRemoto;
import pt.ulisboa.tecnico.cmov.airdesk.R;

/**
 * Created by 25 on 5/8/2015.
 */
public class SocketServer extends AsyncTask<Void, SimWifiP2pSocket, Void> {
    private SimWifiP2pSocketServer mSrvSocket ;
    private SimWifiP2pSocket mCliSocket;
    private int GOPort=R.string.port;
    private AirDesk aplication;
    private SimWifiP2pInfo info;
    private SimWifiP2pDeviceList device_list;

    private Map<String,String> device_maping = new HashMap<String,String>();

    public SocketServer(AirDesk aplication){
        this.aplication=aplication;
    }

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
            String st;

            while ((st = InFromClient.readLine()) != null) {
                //processar entradas
                //enviar respostas
                entry_process(st,s);
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

    private void entry_process(String entry,SimWifiP2pSocket writerSocket){
        String[] Parsed_Entry = entry.split(" ");
        //Cliente pede para ler ficheiro
        if(Parsed_Entry[0].equals("Read_File")){
            //TODO readFile
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
        // Cliente pergunta ao servidor quem é o GO
        // Devolve IP do GO em String
        else if(Parsed_Entry[0].equals("Ask_Is_Go")){
            try {
                OutputStream OutToClient = writerSocket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(OutToClient);
                oos.writeBoolean(this.info.askIsGO());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Cliente pede todos os workspaces que sao relevantes para a sua lista de tags
        // devolve uma arrayList de workspaceRemoto.
        else if(Parsed_Entry[0].equals("Get_Workspace_By_Tags")){
            List<WorkspaceRemoto> workspaces = new ArrayList<WorkspaceRemoto>();
            for(int i=1;i<Parsed_Entry.length;i++){
                for (WorkspaceLocal workspace : aplication.getOwnedWorkspaces()){
                    if(workspace.getTags().contains(Parsed_Entry[i])) workspaces.add(new WorkspaceRemoto(workspace.getOwner().getUserName(), workspace.getName(), new ArrayList<String>()));
                }
            }
            try {
                OutputStream OutToClient = writerSocket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(OutToClient);
                oos.writeObject(workspaces);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mCliSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Cliente envia para o Servidor os workspaces para o qual o servidor foi convidado
        else if(Parsed_Entry[0].equals("Get_Workspace_Invitation")){
            //TODO Get_Workspace_Invitation
            //ler o workspace para o qual foi convidado
        }
        // Cliente envia para o servidor os workspaces que foram alterados
        else if(Parsed_Entry[0].equals("Refresh_Workspace")){
            //TODO Refresh_Workspace
            //ler workspace alterado
        }
        // Cliente(GO) envia para o servidor(Peer) um pedido de autenticação com o nome. (fazer depois do login)
        else if(Parsed_Entry[0].equals("Refresh_IP_Name")){
            String IP = Parsed_Entry[1];
            String name = Parsed_Entry[2];
            device_maping.put(name,IP);
        }
    }

    public SimWifiP2pDeviceList get_device_list(){
        return this.device_list;
    }

    public SimWifiP2pInfo get_info(){
        return this.info;
    }

}
