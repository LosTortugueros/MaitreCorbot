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
    private final OnMessageReceived tCallback;
    private boolean aShouldRun;
    private DatagramSocket rListeningSocket;
    private DatagramPacket aPacket;
    private final byte[] cBuffer;
    //e
    public ListeningServer(OnMessageReceived callback) throws SocketException {
        this.tCallback = callback;
        this.rListeningSocket =new DatagramSocket(Conf.PORT);
        this.cBuffer = new byte[1024];
        this.aPacket = new DatagramPacket(cBuffer, cBuffer.length);
        this.aShouldRun =true;
    }


    @Override
    public void run() {
        while(aShouldRun) {
            try {
                rListeningSocket.receive(aPacket);
                this.tCallback.onMessage(new String(aPacket.getData(), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void stopThread() {
        this.aShouldRun =false;
    }
}
