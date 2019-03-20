package com.mn.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.mn.util.Path;


/**
 * Store and manipulate GroupTable containing all Groups in the system
 *
 * @author Michal Nawrocki
 * @see Group
 */
public class GroupTable{

    public static ConcurrentMap<String, Group> groupTable = new ConcurrentHashMap<String, Group>();
    private static final String FILENAME = Path.groups_Path;

    /**
     * Creates a new Group and stores it
     *
     * @param groupName Name of the Group
     * @param adminName User's nickname that is the admin of the group
     * @return true if group has been added, otherwise false
     */
    public static synchronized boolean makeGroup(String groupName, String adminName){
        readFile();
        if(!groupTable.containsKey(groupName)){
            groupTable.put(groupName, new Group(adminName));
            saveFile();
            return true;
        }
        return false;
    }

    /**
     * Check if groupName is already taken
     *
     * @param groupName Group's name
     * @return true if name is present, false otherwise
     */
    public static synchronized boolean isGroup(String groupName){
        readFile();

        return (groupTable.containsKey(groupName)) ? true : false;
    }

    /**
     * Retrieves the Group matching the name
     *
     * @param groupName
     * @return {@link Group}
     */
    public static synchronized Group getGroup(String groupName){
        readFile();
        return groupTable.get(groupName);
    }

    /**
     * Updates the Group object stored in the Table
     *
     * @param groupName Group's name
     * @param group     Group to be changed
     * @return true if Group was present and has been updated, otherwise false
     */
    public static synchronized boolean updateGroup(String groupName, Group group){
        if(isGroup(groupName)){
            groupTable.put(groupName, group);
            saveFile();
            return true;
        }else{
            return false;
        }
    }

    public static synchronized void readFile(){

        try{
            FileInputStream fis = new FileInputStream(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            groupTable = (ConcurrentMap<String, Group>) ois.readObject();
        }catch(Exception e){
            e.printStackTrace();
            //Make a new Copy

        }
    }

    protected static synchronized void saveFile(){
        try{
            FileOutputStream fos = new FileOutputStream(FILENAME);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(groupTable);
            oos.close();
        }catch(IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
