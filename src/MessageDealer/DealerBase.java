/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import MessageGroup.MessageBase;
import java.net.InetAddress;

/**
 *
 * @author ChxxxXL
 */
public abstract class DealerBase {
    public abstract void dealer(MessageBase msg, InetAddress ip, int port);
}
