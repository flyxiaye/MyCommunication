/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import Client.ChatJFrame;
import Client.ClientSendThread;
import DataBase.VisitDB;
import MessageGroup.MessageBase;
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
public abstract class DealerBase {


//    public abstract void getToolkits();

    public abstract void serverDealer(MessageBase msg, DealerToolkits toolkits);

    public abstract void clientDealer(MessageBase msg, DealerToolkits toolkits);
}
