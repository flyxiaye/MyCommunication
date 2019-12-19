package exp;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageExp implements Serializable{
    
    private static final long serialVersionUID = 16418942341264L; 
    public static final int NORMAL_MESSAGE = 0;
    public static final int USER_MESSAGE = 1;
    public static final int HEART_MESSAGE = 2;
    public static final int LOGIN_MESSAGE = 3;
    public static final int SINGUP_MESSAGE = 4;
    public static final int RECORD_MESSAGE = 5;

    private int id;//0普通消息 //1在线用户列表 //2心跳包 //3登陆信息 //4注册信息 //5聊天记录信息
    private String toName;
    private String data;

    public MessageExp(int id, String toName, String data) {
        this.setId(id);
        this.setToName(toName);
        this.setData(data);
    }

    public MessageExp(int id, String data){
        this.setData(data);
        this.setId(id);
    }

    public MessageExp(int id) {
        this.setId(id);
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
        } catch (IOException ex) {
            Logger.getLogger(MessageExp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MessageExp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oi.close();
            } catch (IOException ex) {
                Logger.getLogger(MessageExp.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(MessageExp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oo.close();
            } catch (IOException ex) {
                Logger.getLogger(MessageExp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bytes;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
