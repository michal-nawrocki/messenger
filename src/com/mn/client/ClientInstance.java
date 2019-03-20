package com.mn.client;

import java.io.*;
import java.net.*;
import com.mn.util.*;

/**
 * Initialize the Threads for Client's communication
 *
 * @author Michal Nawrocki
 * @see com.mn.Client
 * @see ClientSender
 * @see ClientReceiver
 */
public class ClientInstance extends Thread{

    private PrintStream toServer;
    private BufferedReader fromServer;
    private Socket server;

    ClientSender sender;
    ClientReceiver receiver;

    public void run(String nickname, String hostname){

        try{
            server = new Socket(hostname, Port.number);
            toServer = new PrintStream(server.getOutputStream());
            fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
        }catch(UnknownHostException e){
            Report.errorAndGiveUp("Unknown host: " + hostname);
        }catch(IOException e){
            Report.errorAndGiveUp("The server doesn't seem to be running " + e.getMessage());
        }

        // Start communication threads
        sender = new ClientSender(nickname, toServer);
        receiver = new ClientReceiver(fromServer);

        sender.start();
        receiver.start();

        // Show that I connected
        toServer.println(nickname);

        // Cleanup when closing
        try{
            sender.join();
            toServer.close();
            receiver.join();
            fromServer.close();
            server.close();
        }catch(IOException e){
            Report.errorAndGiveUp("Something wrong " + e.getMessage());
        }catch(InterruptedException e){
            Report.errorAndGiveUp("Unexpected interruption " + e.getMessage());
        }
    }
}
