package com.mn.util;

/**
 * Handle logging information that is displayed in terminal
 *
 * @author Michal Nawrocki
 */
public class Report{

    /**
     * Print information
     *
     * @param message String to be printed
     */
    public static void behaviour(String message){
        System.err.println(message);
    }

    /**
     * Print error information
     *
     * @param message String to be printed
     */
    public static void error(String message){
        System.err.println(message);
    }

    /**
     * Print error and exit programm
     *
     * @param message String to be printed
     */
    public static void errorAndGiveUp(String message){
        Report.error(message);
        System.exit(1);
    }
}
