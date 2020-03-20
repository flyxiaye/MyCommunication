/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Notifier;

import MessageGroup.MessageBase;
import java.net.InetAddress;

/**
 *
 * @author ChxxxXL
 */
public interface ServerObservable {
    public void update(MessageBase msg, InetAddress ip, int port);
}
