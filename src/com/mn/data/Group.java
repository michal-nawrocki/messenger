package com.mn.data;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Holds data for a Group
 *
 * @author Michal Nawrocki
 */
public class Group implements Serializable{
    private LinkedList<String> members;
    private LinkedList<String> admins;
    private LinkedList<String> requests;

    public Group(String adminName){
        members = new LinkedList<>();
        members.add(adminName);

        admins = new LinkedList<>();
        admins.add(adminName);

        requests = new LinkedList<>();
        GroupTable.saveFile();
    }

    public synchronized boolean addUser(String userName){
        if(!members.contains(userName)){
            members.add(userName);
            GroupTable.saveFile();
            return true;
        }
        return false;
    }

    public synchronized boolean addRequest(String userName){
        if(!requests.contains(userName)){
            requests.add(userName);
            GroupTable.saveFile();
            return true;
        }
        return false;
    }

    public synchronized boolean requestAccept(String userName){
        if(requests.contains(userName)){
            requests.remove(userName);
            addUser(userName);
            GroupTable.saveFile();
            return true;
        }
        return false;
    }

    public synchronized boolean requestDecline(String userName){
        if(requests.contains(userName)){
            requests.remove(userName);
            GroupTable.saveFile();
            return true;
        }
        return false;
    }

    public synchronized boolean makeAdmin(String userName){
        if(members.contains(userName)){
            if(!admins.contains(userName)){
                admins.add(userName);
                GroupTable.saveFile();
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public synchronized boolean removeAdmin(String userName){
        if(isAdmin(userName)){
            admins.remove(userName);
            GroupTable.saveFile();
            return true;
        }
        return false;
    }

    public synchronized boolean removeUser(String userName){
        if(members.contains(userName)){
            members.remove(userName);
            removeAdmin(userName);
            GroupTable.saveFile();
            return true;
        }

        return false;
    }

    public synchronized boolean isMember(String userName){
        GroupTable.readFile();
        if(members.contains(userName)) return true;
        return false;
    }

    public synchronized boolean isAdmin(String userName){
        GroupTable.readFile();
        if(admins.contains(userName)) return true;
        return false;
    }

    public synchronized LinkedList<String> getMembers(){
        GroupTable.readFile();
        return members;
    }

    public synchronized LinkedList<String> getAdmins(){
        GroupTable.readFile();
        return admins;
    }

    public synchronized LinkedList<String> getRequests(){
        GroupTable.readFile();
        return requests;
    }
}
