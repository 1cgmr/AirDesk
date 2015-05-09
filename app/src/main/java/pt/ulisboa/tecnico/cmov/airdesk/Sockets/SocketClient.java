package pt.ulisboa.tecnico.cmov.airdesk.Sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;import java.lang.String;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;

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
    //funcoes expecificas podem ir para o remote workspace

}
