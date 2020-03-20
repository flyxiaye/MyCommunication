/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import MessageGroup.MessageBase;
import MessageGroup.MessageNoraml;
import MessageGroup.MessageRecord;
import Notifier.ServerObservable;
import java.net.InetAddress;

/**
 *
 * @author ChxxxXL
 */
public class DealerFactory implements ServerObservable {

    @Override
    public void update(MessageBase msg, InetAddress ip, int port) {
        DealerBase db = createDealer(msg);
        if (db != null) {
            db.dealer(msg, ip, port);
        }
    }

    public DealerBase createDealer(MessageBase msg) {
        DealerBase db = null;
        if (msg instanceof MessageNoraml) {
            db = new DealerNormal();
        } else if (msg instanceof MessageRecord) {
            db = new DealerRecord();
        }
        return db;
    }

}
