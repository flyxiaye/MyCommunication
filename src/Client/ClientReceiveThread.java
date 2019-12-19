/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import exp.MessageExp;
import exp.MessageRecord;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

/**
 *
 * @author ChxxxXL
 */
public class ClientReceiveThread extends Thread {
    
    DatagramSocket socket = null;
    DatagramPacket packet = null;
    InetAddress toIP = null;
    int toPort = 0;
    byte[] dataBuf;
    volatile private JList jList1;
    public Map<String, ChatJFrame> cfMap = new HashMap<String, ChatJFrame>();//保存所有对话框
    private ClientSendThread sender = null;
    
    public void setSender(ClientSendThread sender) {
        this.sender = sender;
    }
    
    public void addChatFrame(String keyString, ChatJFrame chatJFrame) {
        if (cfMap.containsKey(keyString)) {
            cfMap.replace(keyString, chatJFrame);
        }
        cfMap.put(keyString, chatJFrame);
    }
    
    public void deleteChatFrame(String keyString) {
        cfMap.remove(keyString);
    }
    
    public ClientReceiveThread(int fromPort, JList jList1) {
        try {
            socket = new DatagramSocket(fromPort);
            
        } catch (SocketException ex) {
            Logger.getLogger(ClientReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.jList1 = jList1;
        this.jList1.setModel(new DefaultListModel());
    }
    
    public ClientReceiveThread(DatagramSocket socket, JList jList1) {
        this.socket = socket;
        this.jList1 = jList1;
        this.jList1.setModel(new DefaultListModel());
    }
    
    public ClientReceiveThread(DatagramSocket socket, JList jList1, ClientSendThread sender) {
        this.socket = socket;
        this.jList1 = jList1;
        this.jList1.setModel(new DefaultListModel());
        this.sender = sender;
    }
    
    @Override
    public void run() {
        dataBuf = new byte[2048];
        packet = new DatagramPacket(dataBuf, 2048);
        while (true) {
            try {
                socket.receive(packet);
                byte[] datas = Arrays.copyOf(packet.getData(), packet.getLength());
                MessageExp msg = (MessageExp) MessageExp.ByteToObject(datas);
                switch (msg.getId()) {
                    case MessageExp.NORMAL_MESSAGE: {    //普通消息
                        String fromName = msg.getToName();
                        if (cfMap.containsKey(fromName)) {
                            ChatJFrame tmpFrame = cfMap.get(fromName);
                            tmpFrame.addMessage(msg);
                            tmpFrame.setVisible(true);
                        } else {
                            //弹出窗口
                            ChatJFrame chatJFrame = new ChatJFrame(sender, this, fromName);
                            this.addChatFrame(fromName, chatJFrame);
                            new Thread(chatJFrame).start();
                            chatJFrame.addMessage(msg);
                        }
                    }
                    break;
                    case MessageExp.USER_MESSAGE: {   //显示在线用户列表信息
                        String onlineUser = msg.getData();
                        String s[] = onlineUser.split(" ");
                        DefaultListModel lm = (DefaultListModel) this.jList1.getModel();
                        lm.clear();
                        for (String str : s) {
                            if (!str.equals(Info.userName)) {
                                lm.addElement(str);
                            }
                        }
                    }
                    break;
                    case MessageExp.RECORD_MESSAGE: {  //聊天记录
                        MessageRecord msgR = (MessageRecord) msg;
                        new Thread(new ChatRecordJDialog(
                                new JFrame(), false, msgR.getToName(), msgR)).start();
                    }
                    break;
                    default:
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}