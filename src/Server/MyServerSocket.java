/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL
 */
public class MyServerSocket {
    ServerSocket m_sListener;
    public MyServerSocket(int port, int count){
        try {
            m_sListener = new ServerSocket(port, count);
        } catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

class SocketThread implements Runnable{
    Socket clientSocket;
    
    public SocketThread(Socket s){
        this.clientSocket = s;
        
    }
    @Override
    public void run() {
         BufferedReader sIn = null;
        try {
            sIn = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            
            String s = sIn.readLine();
            while(!s.equals("Bye"))
            {
                
            }
            connectedClient.close();
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (IOException ex) {
            Logger.getLogger(SocketThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                sIn.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
