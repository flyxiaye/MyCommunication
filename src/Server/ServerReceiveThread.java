/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import MessageGroup.MessageBase;
import Notifier.ServerObservable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author ChxxxXL
 */
public class ServerReceiveThread extends Thread {

    DatagramSocket socket = null;
    DatagramPacket packet = null;
    InetAddress toIP = null;
    int toPort = 0;
    byte[] dataBuf;

    private ArrayList<ServerObservable> observers = new ArrayList<>();

    public void add(ServerObservable ob) {
        observers.add(ob);
    }

    public void delete(ServerObservable ob) {
        observers.remove(ob);
    }

    private static ServerReceiveThread receiveThread = null;

    public static ServerReceiveThread getServerReceiveThread(DatagramSocket socket) {
        if (receiveThread == null) {
            receiveThread = new ServerReceiveThread(socket);
        }
        return receiveThread;
    }

    public static ServerReceiveThread getServerReceiveThread() {
        return receiveThread;
    }

    private ServerReceiveThread(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {
//        jTextArea1.append("服务端启动！\n");
        dataBuf = new byte[20480];
        packet = new DatagramPacket(dataBuf, dataBuf.length);
        while (true) {
            try {
                socket.receive(packet);

                toIP = packet.getAddress();
                toPort = packet.getPort();
                byte[] datas = Arrays.copyOf(packet.getData(), packet.getLength());
                MessageBase msg = (MessageBase) MessageBase.ByteToObject(datas);
                observers.forEach((o) -> {
                    o.update(msg, toIP, toPort);
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
