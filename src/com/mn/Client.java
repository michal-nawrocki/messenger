package com.mn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.mn.client.ClientInstance;
import com.mn.data.RegisteredUsers;
import com.mn.util.*;

/**
 * Used for running the Client program and handling user input<br>
 * To run use: <code>java Client server-hostname</code>
 *
 * @author Michal Nawrocki
 * @see ClientInstance
 */

public class Client{

    public static String nickname;
    public static boolean isLoggedIn;
    public static BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
    public static boolean shouldQuit = false;

    private static ClientInstance client = new ClientInstance();

    public static void main(String[] args) throws InterruptedException{

        // Check correct usage:
        if(args.length != 1){
            Report.errorAndGiveUp("Usage: java Client server-hostname");
        }

        String hostname = args[0];
        Report.behaviour("*********************************Java Network-Messenger Application v3.0*********************************"
                + "\n" + "1.'help' return - shows commands accepted by the program with parameters and short description."
                + "\n" + "2.'login' return 'username' return 'password' - Logins into the given account. The user has to be registered/password has to be correct."
                + "\n" + "3.'register' return 'username' return 'password' - Registers the given user. The username has to be unique."
                + "\n" + "4.'logout' - Logouts from the given account."
                + "\n" + "5.'quit' - Kills this application."
                + "\n" + "*******************************************************************************************************");

        try{
            // Then loop forever sending messages to recipients via the server:
            while(!shouldQuit){

                String input = user.readLine();

                if(input.toLowerCase().equals("help")){
                    Report.behaviour("*********************************Java Network-Messenger Application v3.0*********************************"
                            + "\n" + "You have typed 'help'. This section will inform you which commands are accepted and a description."
                            + "\n\n" + "1.'help' return - shows commands accepted by the program with parameters and short description."
                            + "\n" + "2.'login' return 'username' return 'password' - Logins into the given account. The user has to be registered/password has to be correct."
                            + "\n" + "3.'register' return 'username' return 'password' - Registers the given user. The username has to be unique."
                            + "\n" + "4.'logout' - Logouts from the given account."
                            + "\n" + "5.'quit' - Kills this application."
                            + "\n" + "*******************************************************************************************************");
                    continue;
                }

                if(input.toLowerCase().equals("quit")){
                    shouldQuit = true;
                    continue;
                }

                if(input.toLowerCase().equals("logout") && isLoggedIn){
                    isLoggedIn = false;
                    Report.behaviour("You have logged out!");
                    Thread.interrupted();
                    continue;
                }

                if(input.toLowerCase().equals("register") && !isLoggedIn){
                    String userName = user.readLine();
                    String passWord = user.readLine();

                    if(RegisteredUsers.addUser(userName, passWord)){
                        continue;
                    }
                }

                if(input.toLowerCase().equals("login") && !isLoggedIn){
                    String userName = user.readLine();
                    String passWord = user.readLine();

                    if(RegisteredUsers.checkUser(userName, passWord)){
                        isLoggedIn = true;
                        nickname = userName;
                        client.run(userName, hostname);
                        continue;
                    }
                }

                Report.behaviour("Not recognized command. Check 'help' for more information");
            }
        }catch(IOException e){
            Report.errorAndGiveUp("Communication broke in ClientSender"
                    + e.getMessage());
        }
    }
}
