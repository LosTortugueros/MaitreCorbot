package los.tortugueros.maitrecorbot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import los.tortugueros.maitrecorbot.utils.Conf;

/**
 * Created by MagicMicky on 04/12/2014.
 */
public class ListeningServer extends Thread{
    //Callback for when a message is received
    private final OnMessageReceived tCallback;
    //Whether or not the Thread should run
    private boolean aShouldRun;
    //The socket waiting for connection
    private DatagramSocket rListeningSocket;
    //The data buffer
    private final byte[] aBuffer;
    //The packet received from the socket
    private DatagramPacket cPacket;

    /**
     * Creates a new Listening server
     * @param callback the object to call when we receive some message
     * @throws SocketException when the server couldn't start
     */
    public ListeningServer(OnMessageReceived callback) throws SocketException {
        this.tCallback = callback;
        this.aShouldRun =true;

        this.rListeningSocket =new DatagramSocket(Conf.PORT);
        this.aBuffer = new byte[1024];
        this.cPacket = new DatagramPacket(aBuffer, aBuffer.length);
           //e
    }


    @Override
    public void run() {
        while(aShouldRun) {
            try {
                rListeningSocket.receive(cPacket);
                this.tCallback.onMessage(new String(cPacket.getData(), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Stop the thread
     */
    public void stopThread() {
        this.aShouldRun =false;
    }
}
