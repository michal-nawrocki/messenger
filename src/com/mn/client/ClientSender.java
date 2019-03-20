package com.mn.client;

import java.io.*;
import com.mn.Client;
import com.mn.util.Report;
import com.mn.data.*;

/**
 * Read user input and send messages to Server
 *
 * @author Michal Nawrocki
 * @see ClientInstance
 * @see com.mn.Server
 */
public class ClientSender extends Thread{

    public String nickname;
    private PrintStream server;
    public boolean isLoggedIn;

    ClientSender(String nickname, PrintStream server){
        this.nickname = nickname;
        this.server = server;
        isLoggedIn = false;
    }

    public void run(){
        // use the method readLine:
        BufferedReader user = Client.user;

        try{
            while(true){
                String input = user.readLine();

                if(input.toLowerCase().equals("help")){
                    Report.behaviour("*******************************************************************************************************"
                            + "\n" + "1.'help' return - shows commands accepted by the program with parameters and short description"
                            + "\n" + "2.'message' return 'username' return 'text' return - Sends the text to the user of name username."
                            + "\n" + "3.'group-message' return 'groupname' return 'text' = Sends the text to every user of group groupname. You have to be a member to send and receive messages."
                            + "\n" + "4.'group-make' return 'groupname' - Creates a group of the given name. You become automatically the admin of this group."
                            + "\n" + "5.'group-addrequest' return 'groupname' - Sends a request to the group to be added. A admin can accept or decline your request."
                            + "\n" + "6.'group-requestaccept' return 'groupname' return 'username' - Accepts the join request of user. You have to be an admin of that group to accept."
                            + "\n" + "7.'group-requestdecline' return 'groupname' return 'username' - Rejects the join request of user. You have to be an admin of that group to reject."
                            + "\n" + "8.'group-adduser' return 'groupname' return 'username' - Adds a registered user to the group. You have to be an admin to add users."
                            + "\n" + "9.'group-addadmin' return 'groupname' return 'username' - Adds admin rights to the given user. The user has to be that group to be given admin rights."
                            + "\n" + "10.'group-removeuser' return 'groupname' return 'username' - Removes the user from that group (also removes admin rights). You have to be an admin to remove users."
                            + "\n" + "11.'group-removeadmin' return 'groupname' return 'username' - Removes admin rights from the given user. The user has to be an admin. You have to be an admin to use this command."
                            + "\n" + "12.'group-removeself' return 'groupname' return - Removes you from the the given group."
                            + "\n" + "13.'logout' return - You logout from the server. You then can 'login', 'register' or 'quit'."
                            + "\n" + "14.'quit' return - Kills the application."
                            + "\n" + "*******************************************************************************************************");
                }

                if(input.toLowerCase().equals("message")){
                    String recipient = user.readLine();
                    String text = user.readLine();
                    server.println(recipient);
                    server.println(text);
                }

                if(input.toLowerCase().equals("group-message")){
                    String groupName = user.readLine();
                    if(GroupTable.isGroup(groupName)){
                        Group _group = GroupTable.getGroup(groupName);

                        if(_group.isMember(nickname)){
                            Report.behaviour("Sending message to group: " + groupName);
                            String text = user.readLine();
                            server.println(groupName);
                            server.println(text);
                        }else{
                            Report.behaviour("You are not a member of this group");
                        }
                    }else{
                        Report.behaviour("There is no such group!");
                    }

                }

                if(input.toLowerCase().equals("group-make")){
                    String groupName = user.readLine();
                    if(GroupTable.makeGroup(groupName, nickname)){
                        Report.behaviour("You have successfully created the group: " + groupName);
                    }else{
                        Report.behaviour("The groupname is already being used!");
                    }
                }

                if(input.toLowerCase().equals("group-addrequest")){
                    String groupName = user.readLine();

                    if(GroupTable.isGroup(groupName)){
                        Group _group = GroupTable.getGroup(groupName);

                        if(!_group.isMember(nickname)){
                            if(_group.addRequest(nickname)){
                                GroupTable.updateGroup(groupName, _group);
                                Report.behaviour("You have successfully made a request to be added to group " + groupName);
                            }else{
                                Report.behaviour("You are already send a request");
                            }
                        }else{
                            Report.behaviour("You are already a member of this group");
                        }
                    }else{
                        Report.behaviour("There is no such group");
                    }
                }

                if(input.toLowerCase().equals("group-requestaccept")){
                    String groupName = user.readLine();
                    String userName = user.readLine();

                    if(GroupTable.isGroup(groupName)){
                        Group _group = GroupTable.getGroup(groupName);

                        if(_group.isAdmin(nickname)){
                            if(_group.requestAccept(userName)){
                                GroupTable.updateGroup(groupName, _group);
                                Report.behaviour("You have succefully added user " + userName + " to the group " + groupName);
                                server.println(userName);
                                server.println("You have been added to the group " + groupName + " by me");
                            }else{
                                Report.behaviour("This user is not on the request queue");
                            }
                        }else{
                            Report.behaviour("You are not a admin of this group");
                        }
                    }else{
                        Report.behaviour("There is no such group");
                    }
                }

                if(input.toLowerCase().equals("group-requestdecline")){
                    String groupName = user.readLine();
                    String userName = user.readLine();

                    if(GroupTable.isGroup(groupName)){
                        Group _group = GroupTable.getGroup(groupName);

                        if(_group.isAdmin(nickname)){
                            if(_group.requestDecline(userName)){
                                GroupTable.updateGroup(groupName, _group);
                                Report.behaviour("You have succefully removed user " + userName + " from the request queue");
                                server.println(userName);
                                server.println("You have been removed to the request queue of group " + groupName);
                            }else{
                                Report.behaviour("This user is not on the request queue");
                            }
                        }else{
                            Report.behaviour("You are not a admin of this group");
                        }
                    }else{
                        Report.behaviour("There is no such group");
                    }
                }

                if(input.toLowerCase().equals("group-adduser")){
                    String groupName = user.readLine();
                    String userName = user.readLine();

                    if(GroupTable.isGroup(groupName)){
                        Group _group = GroupTable.getGroup(groupName);

                        if(_group.isAdmin(nickname)){
                            if(RegisteredUsers.isRegistered(userName)){
                                if(!_group.isMember(userName)){
                                    _group.addUser(userName);
                                    GroupTable.updateGroup(groupName, _group);
                                    Report.behaviour("You have succefully added " + userName + " to group " + groupName);
                                }else{
                                    Report.behaviour("This user is already in the group");
                                }
                            }else{
                                Report.behaviour("This user is not registered!");
                            }
                        }else{
                            Report.behaviour("You don't have admin rights in this group");
                        }
                    }else
                        Report.behaviour("There is no such group!");
                }

                if(input.toLowerCase().equals("group-addadmin")){
                    String groupName = user.readLine();
                    String userName = user.readLine();

                    if(GroupTable.isGroup(groupName)){
                        Group _group = GroupTable.getGroup(groupName);

                        if(_group.isAdmin(nickname)){
                            if(_group.makeAdmin(userName)){
                                Report.behaviour(userName + " has been given admin rights");
                                GroupTable.updateGroup(groupName, _group);
                            }else{
                                Report.behaviour("This user is already an admin or nor part of this group!");
                            }
                        }else{
                            Report.behaviour("You don't have have admin rights");
                        }
                    }else
                        Report.behaviour("There is no such group!");
                }

                if(input.toLowerCase().equals("group-removeuser")){
                    String groupName = user.readLine();
                    String userName = user.readLine();

                    if(GroupTable.isGroup(groupName)){
                        Group _group = GroupTable.getGroup(groupName);

                        if(_group.isAdmin(nickname)){
                            if(_group.removeUser(userName)){

                                if(_group.removeAdmin(userName)){
                                    Report.behaviour("You have removed admin permissions of user " + userName);
                                }
                                GroupTable.updateGroup(groupName, _group);
                                Report.behaviour("You have removed " + userName + " from group " + groupName);

                                server.println(userName);
                                server.println("You have been removed from group " + groupName + " by me");

                            }else{
                                Report.behaviour("This user is not a member of this group");
                            }
                        }else{
                            Report.behaviour("You don't have admin rights in this group");
                        }
                    }else{
                        Report.behaviour("There is no such group");
                    }
                }

                if(input.toLowerCase().equals("group-removeself")){
                    String groupName = user.readLine();
                    String userName = nickname;

                    if(GroupTable.isGroup(groupName)){
                        Group _group = GroupTable.getGroup(groupName);

                        if(_group.removeUser(userName)){

                            if(_group.removeAdmin(userName)){
                                Report.behaviour("You have removed admin permissions of user " + userName);
                            }
                            GroupTable.updateGroup(groupName, _group);
                            Report.behaviour("You have removed " + userName + " from group " + groupName);

                        }else{
                            Report.behaviour("This user is not a member of this group");
                        }
                    }else{
                        Report.behaviour("There is no such group");
                    }
                }

                if(input.toLowerCase().equals("group-removeadmin")){
                    String groupName = user.readLine();
                    String userName = user.readLine();

                    if(GroupTable.isGroup(groupName)){
                        Group _group = GroupTable.getGroup(groupName);

                        if(_group.isAdmin(nickname)){
                            if(_group.removeAdmin(userName)){
                                GroupTable.updateGroup(groupName, _group);
                                Report.behaviour("You have removed admin rights from user " + userName);

                            }else{
                                Report.behaviour("This user is not a member of this group");
                            }
                        }else{
                            Report.behaviour("You don't have admin rights in this group");
                        }
                    }else{
                        Report.behaviour("There is no such group");
                    }
                }

                if(input.toLowerCase().equals("logout") || input.toLowerCase().equals("quit")){
                    server.println("quit");
                    server.println((String) null);
                    if(input.toLowerCase().equals("logout")) Client.isLoggedIn = false;
                    if(input.toLowerCase().equals("quit")) Client.shouldQuit = true;
                    break;
                }
            }
        }catch(IOException e){
            Report.errorAndGiveUp("Communication broke in ClientSender" + e.getMessage());
        }
    }
}