/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycommunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL
 */
public class UDPServer {

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(8000);
            while (true) {
                byte[] dataBuf = new byte[512];
                DatagramPacket packet = new DatagramPacket(dataBuf, dataBuf.length);
                socket.receive(packet);
                int len = packet.getLength();
                String str = new String(dataBuf, 0, len);
                System.out.println(str);
                SocketAddress clientHost = packet.getSocketAddress();
                dataBuf = "OK!".getBytes();
                packet = new DatagramPacket(dataBuf, dataBuf.length, clientHost);
                socket.send(packet);
            }
        } catch (SocketException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
