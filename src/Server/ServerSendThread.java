/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import exp.MessageBase;
import exp.MessageRecord;
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
        if (msg instanceof MessageRecord){
            while (!this.available); //发送阻塞
            int maxByte = 20480;
            byte[] recordDataBuf =  MessageBase.ObjectToByte(msg);
            for (int k = 0; ; k++){
                this.dataBuf = Arrays.copyOfRange(dataBuf, k * maxByte, Integer.min((k+1)*maxByte, recordDataBuf.length));
                packet = new DatagramPacket(dataBuf, dataBuf.length, this.toIP, this.toPort);
                if (dataBuf.length < maxByte)
                    break;
                try {
                    this.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerSendThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return;
        }
        while (!this.available); //发送阻塞
        this.dataBuf = MessageBase.ObjectToByte(msg);
        this.toIP = toIP;
        this.toPort = toPort;
//        System.out.println(msg.getId());
//        System.out.println(msg.getToName());
//        System.out.println(msg.getData());
        packet = new DatagramPacket(dataBuf, dataBuf.length, this.toIP, this.toPort);
        this.available = true;
        synchronized (this) {
            this.notify();
        }
    }

}
