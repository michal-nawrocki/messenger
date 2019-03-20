package com.mn;

import java.net.*;
import java.io.*;
import com.mn.data.ClientTable;
import com.mn.server.*;
import com.mn.util.*;

/**
 * Starts and initializes server threads for sending Messages<br>
 *  * To run use: <code>java Server</code>
 *
 * @author Michal Nawrocki
 * @see ServerSender
 * @see ServerReceiver
 */
public class Server{

    public static void main(String[] args){

        // Table to be shared with all threads
        ClientTable clientTable = new ClientTable();

        ServerSocket serverSocket = null;

        try{
            serverSocket = new ServerSocket(Port.number);
        }catch(IOException e){
            Report.errorAndGiveUp("Couldn't listen on port " + Port.number);
        }

        try{
            // Loop for ever
            while(true){
                // Accept connections from Clients
                Socket socket = serverSocket.accept();

                // Get InputStream from Client
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String clientName = fromClient.readLine();

                Report.behaviour(clientName + " connected");

                // We add the client to the table:
                clientTable.add(clientName);

                // Thread_READ from Client
                (new ServerReceiver(clientName, fromClient, clientTable)).start();

                // Thread_WRITE to Client
                PrintStream toClient = new PrintStream(socket.getOutputStream());
                (new ServerSender(clientTable.getQueue(clientName), toClient)).start();
            }
        }catch(IOException e){
            Report.error("IO error " + e.getMessage());
        }
    }
}
