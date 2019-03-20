package com.mn.util;

import static com.mn.util.Report.*;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.*;

/**
 * Tests for Report
 *
 * @author Michal Nawrocki
 * @see Report
 */
public class ReportTest{

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Test
    public void errorAndGiveUpTest(){
        systemErrRule.mute();
        exit.expectSystemExitWithStatus(1);
        errorAndGiveUp("Test");
        Assert.assertEquals("Test", systemErrRule.getLog());
    }
}
