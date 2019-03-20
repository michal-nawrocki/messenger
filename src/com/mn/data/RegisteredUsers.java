package com.mn.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import com.mn.util.Path;
import com.mn.util.Report;

/**
 * Used for checking user credentials by accessing a .tmp file storing all registered users
 *
 * @author Michal Nawrocki
 */
public class RegisteredUsers{

    private static Hashtable<String, String> registeredUsers = new Hashtable<String, String>();
    private static final String FILENAME = Path.users_Path;

    /**
     * Adds a new users, who's name is not used already into the hash table
     *
     * @param user User's nickname
     * @param pass User's password
     * @return true if has been stored,<br>false if user is already stored
     */
    public static boolean addUser(String user, String pass){
        readFile();

        if(user.length() > 0 && pass.length() > 0){
            if(!registeredUsers.containsKey(user)){
                registeredUsers.put(user, pass);
                saveFile();
                Report.behaviour("User: " + user + " has been registered");
                return true;
            }else{
                Report.behaviour("This username is already taken");
            }
        }
        return false;
    }


    /**
     * Returns a boolean if the provided input is correct with what is inside the hash table
     *
     * @param user User's nickname
     * @param pass User's password
     * @return true if credentials match, otherwise false
     */
    public static boolean checkUser(String user, String pass){
        readFile();

        if(user.length() > 0 && pass.length() > 0){
            if(registeredUsers.containsKey(user)){
                if(registeredUsers.get(user).equals(pass)){
                    Report.behaviour("You have logged in as user " + user);
                    return true;

                }else{
                    Report.behaviour("Wrong password!");
                }
            }else{
                Report.behaviour("There is no such username");
            }
        }

        return false;
    }

    /**
     * Check if username is stored
     *
     * @param user User's nikcname
     * @return true if present, otherwise false
     */
    public static boolean isRegistered(String user){

        return (registeredUsers.containsKey(user)) ? true : false;

    }

    private static boolean readFile(){

        try{
            FileInputStream fis = new FileInputStream(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            registeredUsers = (Hashtable<String, String>) ois.readObject();
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private static void saveFile(){
        try{
            FileOutputStream fos = new FileOutputStream(FILENAME);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(registeredUsers);
            oos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
    

