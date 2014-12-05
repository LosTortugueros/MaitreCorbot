package los.tortugueros.maitrecorbot;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import los.tortugueros.maitrecorbot.utils.Conf;

/**
 * Created by MagicMicky on 04/12/2014.
 */
public final class ListeningServer extends Thread{
    private static ListeningServer INSTANCE;
    //Callback for when a message is received
    private OnMessageReceived tCallback;
    //Whether or not the Thread should run
    private boolean aShouldRun;
    //The socket waiting for connection
    private static DatagramSocket rListeningSocket;
    //The data buffer
    private static byte[] aBuffer;
    //The packet received from the socket
    private static DatagramPacket cPacket;
    public static ListeningServer getInstance(OnMessageReceived callback) throws SocketException, UnknownHostException{
        if(INSTANCE == null)
            INSTANCE = new ListeningServer(callback);
        return INSTANCE;
    }
    /**
     * Creates a new Listening server
     * @param callback the object to call when we receive some message
     * @throws SocketException when the server couldn't start
     */
    private ListeningServer(OnMessageReceived callback) throws SocketException, UnknownHostException {
        tCallback = callback;
        aShouldRun =true;
        rListeningSocket =new DatagramSocket(Conf.PORT);
        aBuffer = new byte[2048];
        cPacket = new DatagramPacket(aBuffer, aBuffer.length);
      //e
        rListeningSocket.setBroadcast(true);
    }


    @Override
    public void run() {
        Log.d("OMG", "socket active");
        while(aShouldRun) {
            try {

                rListeningSocket.receive(cPacket);
                Log.d("OMG", "packet received");
                this.tCallback.onMessage(new String(cPacket.getData(), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        rListeningSocket.close();

    }

    /**
     * Stop the thread
     */
    public void stopThread() {
        this.aShouldRun =false;
    }

    public void updateCallback(OnMessageReceived callback) {
        this.tCallback=callback;
    }
}
