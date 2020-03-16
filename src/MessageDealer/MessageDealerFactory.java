/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import Client.ChatJFrame;
import Client.ClientSendThread;
import DataBase.VisitDB;
import MessageGroup.*;
import Server.ServerSendThread;
import Server.ShareOnlineStateThread;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import javax.swing.JTextArea;

/**
 *
 * @author ChxxxXL
 */
public class MessageDealerFactory{
    
    public DealerBase createrDealer(MessageBase msg){
        if (msg instanceof MessageNoraml) return new DealerNormal();
        else if (msg instanceof MessageHeart) return new DealerHeart();
        else if (msg instanceof MessageLoginInfo) return new DealerLoginInfo();
        else if (msg instanceof MessageOnlineUser) return new DealerOnlineUser();
        else if (msg instanceof MessageRecord) return new DealerRecord();
        else if (msg instanceof MessageSignUpInfo) return new DealerSignUpInfo();
        else return null;
    }

}
