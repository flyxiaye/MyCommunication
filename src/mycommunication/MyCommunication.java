/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycommunication;

import Client.Info;
import exp.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author ChxxxXL
 */
public class MyCommunication {

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://47.98.160.254:3306/userinfo?useSSL=false&serverTimezone=UTC";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "sunmoon";
    static final String PASS = "hxc515151";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            System.out.println(socket.getPort());
            
        } catch (SocketException ex) {
            Logger.getLogger(MyCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        String host = "47.98.160.254";
////        String host = "183.251.18.165";
//        try {
//            InetAddress remoteHost = InetAddress.getByName(host);
//            int remotePort = 8000;
//            BufferedInputStream bin = new BufferedInputStream(System.in);
//            DatagramSocket socket = new DatagramSocket();
//            while (true) {
//                byte[] dataBuf = new byte[512];
//                int len = bin.read(dataBuf);
//                MessageExp msg = new MessageExp(MessageExp.LOGIN_MESSAGE, "flyxia cxl123");
//                dataBuf = MessageExp.ObjectToByte(msg);
//                DatagramPacket packet = new DatagramPacket(dataBuf, dataBuf.length, remoteHost, remotePort);
//                socket.send(packet);
////                dataBuf = new byte[512];
//                packet = new DatagramPacket(dataBuf, dataBuf.length);
//                socket.receive(packet);
//                dataBuf = Arrays.copyOf(packet.getData(), packet.getLength());
//                String newString = new String(dataBuf);
//                System.out.println(newString);
////                msg = (MessageExp)MessageExp.ByteToObject(dataBuf);
////                System.out.println(msg.getData());
//                System.out.println("ok");
//            }
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(MyCommunication.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(MyCommunication.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
}


//class Rec {
//
//    public static void main(String[] args) {
//        //服务端
//        byte[] dataBuf = new byte[512];
//        DatagramSocket socket = new DatagramSocket(8000);
//        DatagramPacket packet = new DatagramPacket(dataBuf, dataBuf.length);
//        socket.receive(packet);
//        InetAddress ClientIP = packet.getAddress();
//        int ClientPort = packet.getPort(); //=7777
//        packet = new DatagramPacket(dataBuf, dataBuf.length, ClientIP, ClientPort);
//        socket.send(packet);
//    }
//}
//class Send{
//    public static void main(String[] args) {
//        byte[] dataBuf = new byte[512];
//        DatagramSocket socket = new DatagramSocket(7777);
//        InetAddress remoteHost = InetAddress.getByName("127.0.0.1");
//        int remotePort = 8000;
//        DatagramPacket packet = new DatagramPacket(dataBuf, dataBuf.length, remoteHost, remotePort);
//        socket.send(packet);
//        packet.getPort
//    }
//}
