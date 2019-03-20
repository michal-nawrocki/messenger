package com.mn.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Tests for ClientReceiver
 *
 * @author Michal Nawrocki
 * @see ClientReceiver
 */
public class ClientReceiverTest{
    ClientReceiver receiverTest;
    BufferedReader reader;
    String inputString = new String();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Before
    public void initializer(){
        reader = new BufferedReader(new StringReader(inputString));
        receiverTest = new ClientReceiver(reader);

        systemErrRule.mute();
        systemOutRule.mute();
    }

    @Test
    public void getText(){
        exit.expectSystemExitWithStatus(1);
        inputString = "test_message\n";
        receiverTest.run();

        Assert.assertEquals("test_message\n", systemOutRule.getLog());
        Assert.assertEquals("Connection to the server seems to have died.\n", systemErrRule.getLog());
    }

    @Test
    public void getNull(){
        exit.expectSystemExitWithStatus(1);
        receiverTest.run();

        Assert.assertEquals("Connection to the server seems to have died.\n", systemErrRule.getLog());
    }

    @Test
    public void getException() throws IOException{
        reader.close();
        receiverTest.run();

        Assert.assertEquals("You have been disconnected from the server\n", systemErrRule.getLog());

    }
}
