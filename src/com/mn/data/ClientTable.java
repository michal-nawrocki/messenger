package com.mn.data;

import java.util.concurrent.*;

/**
 * Store clients {@link MessageQueue}s in a {@link ConcurrentMap}
 *
 * @author Michal Nawrocki
 */
public class ClientTable{

    /**
     * Map with Key - nickname, Value - corresponding {@link MessageQueue}
     */
    private ConcurrentMap<String, MessageQueue> queueTable = new ConcurrentHashMap<String, MessageQueue>();

    /**
     * Creates a new {@link MessageQueue} for the given nickname
     *
     * @param nickname String with username
     */
    public void add(String nickname){
        queueTable.put(nickname, new MessageQueue());
    }

    /**
     * Used for closing thread that correspond to given Client
     * <p>
     * The remove() in ClientTable first adds a message to the users queue that should be send to the user<br>
     * who wants to quit. Later, it removes the queue from the ConcurrentMap.
     * </p>
     *
     * @param nickname Client that closes connection
     * @param msg      nickname of client that wants to quit with the text being null
     */
    public void remove(String nickname, Message msg){
        if(!getQueue(nickname).equals(null)){
            MessageQueue rQ = getQueue(nickname);
            rQ.offer(msg);
            queueTable.remove(nickname);
        }
    }

    /**
     * Return MessageQueue for given user
     *
     * @param nickname Name of the client
     * @return {@link MessageQueue}
     */
    public MessageQueue getQueue(String nickname){
        return queueTable.get(nickname);
    }
}
