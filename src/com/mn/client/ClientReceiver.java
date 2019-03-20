package com.mn.client;

import java.io.*;
import com.mn.util.Report;

/**
 * Get text from Clients via Server
 *
 * @author Michal Nawrocki
 * @see com.mn.data.Message
 * @see com.mn.client.ClientSender
 * @see com.mn.server.ServerSender
 */
public class ClientReceiver extends Thread{

    private BufferedReader server;

    ClientReceiver(BufferedReader server){
        this.server = server;
    }

    public void run(){
        try{
            while(true){
                // Print text from Server
                String s = server.readLine();
                if(s != null)
                    System.out.println(s);
                else
                    Report.errorAndGiveUp("Connection to the server seems to have died.");
            }
        }catch(IOException e){
            Report.behaviour("You have been disconnected from the server");
            this.interrupt();
        }
    }
}
