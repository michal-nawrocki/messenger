package com.mn.data;

/**
 * Data structure used by Server to store data transferred
 *
 * @author Michal Nawrocki
 * @see MessageQueue
 */
public class Message{

    /**
     * Name of sender
     */
    private final String sender;

    /**
     * Message to be displayed to Recipient
     */
    private final String text;

    /**
     * Constructs message with sender and message text
     *
     * @param sender Senders nickname
     * @param text   Message for receiver
     */
    public Message(String sender, String text){
        this.sender = sender;
        this.text = text;
    }

    /**
     * Returns message field of Message
     *
     * @return Message for receiver
     */
    public String getText(){
        return text;
    }

    /**
     * Gives sender's nickname and his message
     *
     * @return Sender name with the corresponding message
     */
    @Override
    public String toString(){
        return "From " + sender + ": " + text;
    }
}
