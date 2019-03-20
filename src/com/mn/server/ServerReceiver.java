package com.mn.server;

import java.io.*;
import java.util.LinkedList;
import com.mn.data.*;
import com.mn.util.*;


/**
 * Get messages from clients and put them in a corresponding queue
 *
 * @author Michal Nawrocki
 */
public class ServerReceiver extends Thread{
    private String myClientsName;
    private BufferedReader myClient;
    private ClientTable clientTable;


    /**
     * Constructs a new ServerReceiver Thread to get data from given Client
     *
     * @param client_name   Nickname of Client
     * @param client_buffer Buffer of Client
     * @param table         Server's clientTable
     */
    public ServerReceiver(String client_name, BufferedReader client_buffer, ClientTable table){
        myClientsName = client_name;
        myClient = client_buffer;
        clientTable = table;
    }

    /**
     * Thread implementation to receive data from Client
     */
    @Override
    public void run(){
        try{
            while(true){
                String recipient = myClient.readLine();
                String text = myClient.readLine();

                // Quit subroutine AND sudden disconnection
                if(recipient == null || recipient.equals("quit")){
                    Message msg = new Message(myClientsName, null);

                    // Removing the user
                    clientTable.remove(myClientsName, msg);
                    Report.behaviour(myClientsName + " quited");
                    break;
                }

                // Group subroutine
                if(recipient != null && text != null){
                    GroupTable.readFile();
                    if(GroupTable.groupTable.containsKey(recipient)){
                        Message msg = new Message(myClientsName + " to group " + recipient, text);

                        LinkedList<String> users = GroupTable.getGroup(recipient).getMembers();

                        for(String u : users){
                            MessageQueue recipientsQueue = clientTable.getQueue(u);
                            if(recipientsQueue != null)
                                recipientsQueue.offer(msg);
                            else
                                Report.error("Message for none present client " + u + ": " + text);
                        }
                        continue;
                    }

                    Message msg = new Message(myClientsName, text);
                    MessageQueue recipientsQueue = clientTable.getQueue(recipient);
                    if(recipientsQueue != null)
                        recipientsQueue.offer(msg);
                    else
                        Report.error("Message for none present client "
                                + recipient + ": " + text);
                }
            }
        }catch(IOException e){
            Report.error("Something went wrong with the client "
                    + myClientsName + " " + e.getMessage());
        }
    }
}

