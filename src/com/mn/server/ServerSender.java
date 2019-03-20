package com.mn.server;

import java.io.*;
import com.mn.data.*;

/**
 * Continuously read from message queue of given client and send to client the {@link Message}
 *
 * @author Michal Nawrocki
 * @see MessageQueue
 */
public class ServerSender extends Thread{
    private MessageQueue clientQueue;
    private PrintStream client;

    public ServerSender(MessageQueue q, PrintStream c){
        clientQueue = q;
        client = c;
    }

    /**
     * Thread implementation to send {@link Message}s to Client
     */
    @Override
    public void run(){
        while(true){
            Message msg = clientQueue.take();

            if(msg.getText() == null){
                client.println((String) null);
                break;
            }

            client.println(msg);
        }
    }
}
