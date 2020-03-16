/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import MessageGroup.MessageBase;
import MessageGroup.MessageRecord;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerSendThread extends Thread {

    DatagramSocket socket = null;
    DatagramPacket packet = null;
    InetAddress toIP = null;
    int toPort = 0;
    byte[] dataBuf;
    boolean available = true;      //发送使能

    public ServerSendThread(int formPort) {
        try {
            this.socket = new DatagramSocket(formPort);
        } catch (SocketException ex) {
            Logger.getLogger(ServerSendThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ServerSendThread(DatagramSocket socket) {
        this.socket = socket;
    }

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
        while (!this.available); //发送阻塞
        this.dataBuf = MessageBase.ObjectToByte(msg);
        this.toIP = toIP;
        this.toPort = toPort;
        packet = new DatagramPacket(dataBuf, dataBuf.length, this.toIP, this.toPort);
        this.available = true;
        synchronized (this) {
            this.notify();
        }
    }

}
