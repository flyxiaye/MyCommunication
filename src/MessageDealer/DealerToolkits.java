/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import Client.ChatJFrame;
import Client.ClientSendThread;
import DataBase.VisitDB;
import Server.ServerSendThread;
import Server.ShareOnlineStateThread;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import javax.swing.JTextArea;

/**
 *
 * @author ChxxxXL
 */
public class DealerToolkits {

    InetAddress toIP = null;
    int toPort = 0;
    byte[] dataBuf;
    VisitDB DataBaseStream = null;
    JTextArea jTextArea1;
    ServerSendThread serverSender = null;
    ClientSendThread clientSender = null;
    ShareOnlineStateThread shareOnlineStateThread = null;
    JList jList1 = null;
    Map<String, ChatJFrame> cfMap = null;//保存所有对话框

    public void setAddress(InetAddress ip, int port) {
        this.toIP = ip;
        this.toPort = port;
    }

    public void setServerThread(ServerSendThread serverSender) {
        this.serverSender = serverSender;
    }

    public void setClientThread(ClientSendThread clientSendThread) {
        this.clientSender = clientSendThread;
    }

    public void setDataBase(VisitDB db) {
        this.DataBaseStream = db;
    }

    public void setJComponent(JList jList, JTextArea jta) {
        this.jList1 = jList;
        this.jTextArea1 = jta;
    }

    public void setMap(Map map) {
        this.cfMap = map;
    }

    public void setShareOnlineThread(ShareOnlineStateThread th) {
        this.shareOnlineStateThread = th;
    }
}
