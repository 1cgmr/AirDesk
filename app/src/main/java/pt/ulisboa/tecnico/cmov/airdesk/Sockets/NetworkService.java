package pt.ulisboa.tecnico.cmov.airdesk.Sockets;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;


import java.util.HashMap;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.Channel;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;

public class NetworkService extends Service {

    static final int NEW_FILE = 0;
    static final int MODIFIED_FILE = 1;
    static final int DELETE_FILE= 2;
    static final int DELETE_WORKSPACE= 3;
    static final int GET_WORKSPACE= 4;
    static final int CLOSE_SOCKET= 5;
    static final int REGISTER_CLIENT= 6;

    static HashMap<String, Messenger> updateAdapters = new HashMap<String, Messenger>();
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private SocketServer connectionManager;
    private Messenger mService = null;
    private boolean mBound = false;
    private SimWifiP2pInfo gInfo = null;
    private SimWifiP2pManager mManager = null;
    private Channel mChannel = null;

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mManager = new SimWifiP2pManager(mService);
            mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };

    public SimWifiP2pManager getManager() {
        return mManager;
    }

    public Channel getChannel() {
        return mChannel;
    }

    public SocketServer getConnectionManager(){
        return connectionManager;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public void initializeReceiver(){
        // initialize the WDSim API
        SimWifiP2pSocketManager.Init(getApplicationContext());

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        SimWifiP2pBroadcastReceiver receiver = new SimWifiP2pBroadcastReceiver(this);
        registerReceiver(receiver, filter);

        // initialize WifiDirectManager
        connectionManager = new SocketServer();

        //turn on wifi
        Intent intent = new Intent(this, SimWifiP2pService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;
    }

    class IncomingHandler extends Handler { // Handler of incoming messages from

        // clients.
        @Override
        public void handleMessage(Message msg) {
            byte[] image;
            String user, tweet;
            boolean privacy;
            switch (msg.what) {

                case NEW_FILE:

                case MODIFIED_FILE:

                case CLOSE_SOCKET:

                case DELETE_FILE:

                case DELETE_WORKSPACE:

                case REGISTER_CLIENT:

                case GET_WORKSPACE:

            }
        }

    }
    //----------
    private void unregisterAdapters(Message msg) {
        updateAdapters.remove(msg.getData().get("id"));
    }

    private void registerAdapters(Message msg) {
        updateAdapters.put(msg.getData().getString("id"), msg.replyTo);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyService", "Received start id " + startId + ": " + intent);
        return START_STICKY; // run until explicitly stopped.
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        closeSocket();

    }

    public void closeSocket() {
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    public void setGInfo(SimWifiP2pInfo ginfo2) {
        this.gInfo = ginfo2;
    }

    public SimWifiP2pInfo getGInfo() {
        return gInfo;
    }


}
