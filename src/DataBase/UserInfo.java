/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.Connection;

/**
 *
 * @author ChxxxXL
 */
public class UserInfo {
    public String userName;
    public String ip ;
    public int port;
    public int state;
    public UserInfo(){}
    
    public UserInfo(String name, String ip, int port, int state){
        this.userName = name;
        this.ip = ip;
        this.port = port;
        this.state = state;
    }
}

