package com.mn.data;

import java.util.concurrent.*;

/**
 * Blocking Message Queue used managing Users Messages
 *
 * @author Michal Nawrocki
 */
public class MessageQueue{

    /**
     * Implementing LinkedBlockingQueue for {@link Message}
     */
    private BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();

    /**
     * Store the specifiend message into queue
     *
     * @param m {@link Message} to be stored
     */
    public void offer(Message m){
        queue.offer(m);
    }

    /**
     * Gets and removes head of the queue once that element becomes available
     *
     * @return {@link Message}
     */
    public Message take(){

        while(true){
            try{
                return (queue.take());
            }catch(InterruptedException e){
                // Wait until we can get the element
            }
        }
    }
}
