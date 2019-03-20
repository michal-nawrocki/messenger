package com.mn.server;

import com.mn.data.ClientTable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;

import java.io.*;

/**
 * Tests for ServerReceiver
 *
 * @author Michal Nawrocki
 * @see ServerReceiver
 */
public class ServerReceiverTest{

    ServerReceiver testReceiver;
    BufferedReader reader;
    ClientTable table;

    String inputString = new String();

    /**
     * Used for handling <code>System.err</code> logs
     */
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Before
    public void initializer(){

        reader = new BufferedReader(new StringReader(inputString));

        table = new ClientTable();
        table.add("test_user");

        testReceiver = new ServerReceiver("test_user", reader, table);

        //Use to not get lots of error messages in terminal
        systemErrRule.mute();
    }

    @Test
    public void quitSubroutineTest(){
        inputString = "quit\ntest_message";
        initializer();
        testReceiver.run();

        Assert.assertEquals("test_user quited\n", systemErrRule.getLog());
    }

    @Test
    public void noUserTest(){
        inputString = "Tom\ntest_message";
        initializer();
        testReceiver.run();

        //As it will constantly read for next line it will get a null and hence will want to quit
        Assert.assertEquals("Message for none present client Tom: test_message\ntest_user quited\n", systemErrRule.getLog());
    }

    @Test
    public void presentUserTest(){
        inputString = "test_user\ntest_message";
        initializer();
        testReceiver.run();

        //As it will constantly read for next line it will get a null and hence will want to quit
        Assert.assertEquals("test_user quited\n", systemErrRule.getLog());
    }
}
