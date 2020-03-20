/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import MessageGroup.MessageBase;
import Notifier.Observable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL
 */
public class ClientReceiveThread extends Thread {

    private static ClientReceiveThread receiveThread = null;

    public static ClientReceiveThread getClientReceiveThread() {
        return receiveThread;
    }

    public static ClientReceiveThread getClientReceiveThread(DatagramSocket socket) {
        if (receiveThread == null) {
            receiveThread = new ClientReceiveThread(socket);
        }
        return receiveThread;

    }

    private ArrayList<Observable> observers = new ArrayList<>();

    public void add(Observable ob) {
        observers.add(ob);
    }

    public void delete(Observable ob) {
        observers.remove(ob);
    }

    DatagramSocket socket = null;
    DatagramPacket packet = null;
    byte[] dataBuf;

    private ClientReceiveThread(int fromPort) {
        try {
            socket = new DatagramSocket(fromPort);

        } catch (SocketException ex) {
            Logger.getLogger(ClientReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ClientReceiveThread(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        dataBuf = new byte[20480];
        packet = new DatagramPacket(dataBuf, dataBuf.length);
        while (true) {
            try {
                socket.receive(packet);
                byte[] datas = Arrays.copyOf(packet.getData(), packet.getLength());
                MessageBase msg = (MessageBase) MessageBase.ByteToObject(datas);
                observers.forEach((o) -> {
                    o.update(msg);
                });
            } catch (IOException ex) {
                Logger.getLogger(ClientReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
