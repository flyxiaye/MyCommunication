/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL
 */

public abstract class MessageBase implements Serializable{
    private static final long serialVersionUID = 16418942341264L; 
    public static final int NORMAL_MESSAGE = 0;
    public static final int USER_MESSAGE = 1;
    public static final int HEART_MESSAGE = 2;
    public static final int LOGIN_MESSAGE = 3;
    public static final int SINGUP_MESSAGE = 4;
    public static final int RECORD_MESSAGE = 5;
    
    public int id;
    public String fromName;
    public MessageBase(int id, String fromName){
        this.id = id;
        this.fromName = fromName;
    }
    public MessageBase(int id){
        this.id = id;
    }
    
    public synchronized static Object ByteToObject(byte[] bytes) {
        ObjectInputStream oi = null;
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            oi = new ObjectInputStream(bi);
            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                oi.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return obj;
    }

    public synchronized static byte[] ObjectToByte(java.lang.Object obj) {
        ObjectOutputStream oo = null;
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                oo.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return bytes;
    }
}






