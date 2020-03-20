/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Server.ServerSendThread;
import MessageGroup.MessageBase;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL
 */
public class ClientSendThread extends Thread {

    DatagramSocket socket = null;
    DatagramPacket packet = null;
    InetAddress toIP = null;
    int toPort = 0;
    byte[] dataBuf;
    boolean available = true;      //发送使能

    public ClientSendThread(int fromPort, InetAddress toIP, int toPort) {
        try {
            socket = new DatagramSocket(fromPort);
            this.toIP = toIP;
            this.toPort = toPort;
        } catch (SocketException ex) {
            Logger.getLogger(ClientSendThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ClientSendThread(DatagramSocket socket, InetAddress toIP, int toPort) {
        this.socket = socket;
        this.toIP = toIP;
        this.toPort = toPort;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerSendThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while (true) {
            try {
                socket.send(packet);
                this.available = true;
                synchronized (this) {
                    this.wait();
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerSendThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerSendThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void sendMessage(MessageBase msg, InetAddress toIP, int toPort) {
        while (!this.available);//发送阻塞
        this.dataBuf = MessageBase.ObjectToByte(msg);
        this.toIP = toIP;
        this.toPort = toPort;
        packet = new DatagramPacket(dataBuf, dataBuf.length, this.toIP, this.toPort);
        this.available = false;
        synchronized (this) {
            this.notify();
        }
    }

    public void sendMessage(MessageBase msg) {
        while (!this.available);//发送阻塞
        this.dataBuf = MessageBase.ObjectToByte(msg);
        packet = new DatagramPacket(dataBuf, dataBuf.length, this.toIP, this.toPort);
        this.available = false;
        synchronized (this) {
            this.notify();
        }
    }
}
