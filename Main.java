package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Shehryar Ahmed
 * SA43897
 * 16345
 * Nicholas Duggar
 * NBD422
 * 16345
 * Slip days used: <0>
 * Fall 2018
 */

import java.util.Scanner;
import java.io.*;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        boolean simulation_is_running = true;
        while(simulation_is_running) {
        	String command = kb.nextLine();
            if(command.equals("quit")) {
            	simulation_is_running = false;
            }
            else if(command.equals("show")) {
            	Critter.displayWorld();
            }
            else if(command.length() >= 4) {
            	if(command.substring(0, 4).equals("step")){
                	String[] command_line = command.split(" ");
                	int count = 1;
                	try {
                		if(command_line.length > 1) {
                    		count = Integer.parseInt(command_line[1]);
                    	}
                		if(count <= 0) {
                			throw new Exception(); // because the count was bad!
                		}
                		for(int i = 0; i < count; i++) {
                			Critter.worldTimeStep();
                		}
                	}
                	catch(Exception e) {
                		System.out.println("error processing: " + command);
                	}
                	
                }
                else if(command.substring(0, 4).equals("seed")) {
                	String[] command_line = command.split(" ");
                	long seed = 0;
                	try {
                		seed = Long.parseLong(command_line[1]);
                		Critter.setSeed(seed);
                	}
                	catch(Exception e) {
                		System.out.println("error processing: " + command);
                	}
                	
                }
                else if(command.substring(0, 4).equals("make")) {
                	String[] command_line = command.split(" ");
                	int count = 1;
                	try {
                		if(command_line.length > 2) {
                			count = Integer.parseInt(command_line[2]); // will only be run if a third token was provided
                		}
                		for(int i = 0; i < count; i++) {
                			Critter.makeCritter(command_line[1]);
                		}
                	}
                	catch(Exception e) {
                		System.out.println("error processing: " + command);
                	}
                }
                else if(command.length() >= 5) {
                	if(command.substring(0, 5).equals("stats")) {
                		String[] command_line = command.split(" ");
                    	try {
                    		Critter.runStats(Critter.getInstances(command_line[1]));
                    	}
                    	catch(InvalidCritterException ice) {
                    		System.out.println("error processing: " + command);
                    	}
                    	catch(Exception e) {
                    		System.out.println("error processing: " + command);
                    	}
                	}
                }
                else {
                	System.out.println("invalid command: " + command);
                }
            }
            else {
            	System.out.println("invalid command: " + command);
            }
        }
        
        // TODO: fully test command parser
        // System.out.println("GLHF");
        
        /* Write your code above */
        System.out.flush();

    }
}
