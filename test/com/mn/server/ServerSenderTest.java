package com.mn.server;


import com.mn.data.Message;
import com.mn.data.MessageQueue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;

/**
 * Tests for ServerSender
 *
 * @author Michal Nawrocki
 * @see ServerSender
 */
public class ServerSenderTest{

    MessageQueue test_queue;
    ServerSender testSender;

    /**
     * Used for handling <code>System.err</code> logs
     */
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Before
    public void initializer(){
        test_queue = new MessageQueue();
        testSender = new ServerSender(test_queue, System.err);

        //Use to not get lots of error messages in terminal
        systemErrRule.mute();
    }


    @Test
    public void getNullTest(){
        test_queue.offer(new Message("test_sender", null));
        testSender.run();

        Assert.assertEquals("null\n", systemErrRule.getLog());
    }

    @Test
    public void getMessageTest(){
        test_queue.offer(new Message("test_sender", "test_message"));
        test_queue.offer(new Message("test_sender", null));
        testSender.run();

        Assert.assertEquals("From test_sender: test_message\nnull\n", systemErrRule.getLog());
    }


}
