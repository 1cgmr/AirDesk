package pt.ulisboa.tecnico.cmov.airdesk.Sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.WorkspaceRemoto;

/**
 * Created by 25 on 5/8/2015.
 */
public class SocketClient {
    private String GO;
    private String actual;
    private String GOIP;
    private int GOPort;
    private SocketServer server;

    public SocketClient(String GO, String actual,String GOIP){
        this.GO=GO;
        this.actual=actual;
        this.GOIP=GOIP;
    }

    private boolean isGO(){
        return GO.equals(actual);
    }

    private String send_message(String message){
        SimWifiP2pSocket mCliSocket;
        try {
            mCliSocket = new SimWifiP2pSocket(GOIP,GOPort);
            BufferedReader InFromServer = new BufferedReader(new InputStreamReader(mCliSocket.getInputStream()));
            BufferedWriter OutFromServer = new BufferedWriter(new OutputStreamWriter(mCliSocket.getOutputStream()));
            mCliSocket.getOutputStream().write(message.getBytes());
            return InFromServer.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getIP(String nome){
        String message = "getIp "+nome;
        return send_message(message);
    }

    private SimWifiP2pSocket askSocket(String nome){
        try {
            return new SimWifiP2pSocket(getIP(nome),GOPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<WorkspaceRemoto> ask_workspaces_by_tag(List<String> tags){
        List<WorkspaceRemoto> new_workspaces = new ArrayList<WorkspaceRemoto>();

        String message="Get_Workspace_By_Tags";
        for(String str : tags){
            message+=" "+str;
        }

        for(SimWifiP2pDevice device : this.server.get_device_list().getDeviceList()) {
            try {
                SimWifiP2pSocket mCliSocket = new SimWifiP2pSocket(device.getVirtIp(), GOPort);
                BufferedWriter OutToServer= new BufferedWriter(new OutputStreamWriter(mCliSocket.getOutputStream()));
                OutToServer.write(message);

                InputStream InFromServer= mCliSocket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(InFromServer);



            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return new_workspaces;
    }
    //funcoes expecificas podem ir para o remote workspace

}
