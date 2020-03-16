/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL
 */
public class ChatRecordFactory {

    public Connection con;

    public ChatRecordFactory(Connection con) {
        this.con = con;
    }

    public void writeRecord(ChatRecord record) {
        try {
            String sql = "insert into chatrecord (fromname, toname, record) values (?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, record.fromName);
            stmt.setString(2, record.toName);
            stmt.setString(3, record.chatData);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Vector<ChatRecord> readChatRecords(String fromName, String toName) {
        Vector<ChatRecord> records = new Vector<>();
        try {
            String sql = "SELECT date, fromname, toname, record FROM chatrecord WHERE fromname = ? AND toname = ? "
                    + "OR fromname = ? AND toname = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, fromName);
            pst.setString(2, toName);
            pst.setString(3, toName);
            pst.setString(4, fromName);
            ResultSet rsRecord = pst.executeQuery();
            while (rsRecord.next()) {
                String date = rsRecord.getString("date");
                String from = rsRecord.getString("fromname");
                String to = rsRecord.getString("toname");
                String record = rsRecord.getString("record");
                ChatRecord cr = new ChatRecord(from, to, record);
                cr.date = date;
                records.addElement(cr);
            }
            pst.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return records;
    }
}
