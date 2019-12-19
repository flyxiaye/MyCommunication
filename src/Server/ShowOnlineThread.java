/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import exp.VisitDB;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 *
 * @author ChxxxXL
 */
public class ShowOnlineThread extends Thread {

    //显示在线列表
    JList<String> jList1 = null;
    VisitDB DataBaseStream = null;

    public ShowOnlineThread(VisitDB DataBaseStream, JList<String> jList1) {
        this.DataBaseStream = DataBaseStream;
        this.jList1 = jList1;
        //修改原有的默认String类型列表
        DefaultListModel lm = new DefaultListModel();
        jList1.setModel(lm);
    }

    public void run() {
        //显示在线用户列表
        while (true) {
            try {
                Statement stmt = DataBaseStream.con.createStatement();
                String sql = "SELECT name FROM userinfo WHERE state = 1";
                ResultSet rs = stmt.executeQuery(sql);
                DefaultListModel lm = (DefaultListModel) (jList1.getModel());
                lm.clear();
                while (rs.next()) {
                    String s = rs.getString("name");
                        lm.addElement(s);     
                }
                sleep(1000);
            } catch (SQLException ex) {
                Logger.getLogger(ShowOnlineThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ShowOnlineThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
