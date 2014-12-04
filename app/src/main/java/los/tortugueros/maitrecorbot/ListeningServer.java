package los.tortugueros.maitrecorbot;

import android.app.Activity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import los.tortugueros.maitrecorbot.utils.Conf;

/**
 * Created by MagicMicky on 04/12/2014.
 */
public class ListeningServer extends Thread{
    private final OnMessageReceived mCallback;
    private boolean shouldRun;
    private DatagramSocket listeningSocket;
    private DatagramPacket packet;
    private final byte[] buffer;

    public ListeningServer(OnMessageReceived callback) throws SocketException {
        this.mCallback = callback;
        this.listeningSocket=new DatagramSocket(Conf.PORT);
        this.buffer = new byte[1024];
        this.packet = new DatagramPacket(buffer,buffer.length);
        this.shouldRun=true;
    }


    @Override
    public void run() {
        while(shouldRun) {
            try {
                listeningSocket.receive(packet);
                this.mCallback.onMessage(new String(packet.getData(), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void stopThread() {
        this.shouldRun=false;
    }
}
