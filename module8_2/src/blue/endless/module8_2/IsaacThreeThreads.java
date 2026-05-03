/*
 * CSD420: Advanced Java Programming
 * Module 8: Multithreading
 *   Assignment 2: Programming Assignment
 * 
 * Isaac Ellingson
 * 5/3/2026
 * 
 * I am doing a lot of guessing on the project requirements here because they are once again not expressed clearly in
 * the assignment text.
 * 
 * My interpretation is: start up three threads, each spitting out a different kind of random character - one letters,
 * one digits, and one symbols. These must spit characters individually into stdout or a JavaFX TextArea.
 * 
 * The result is printed gibberish that illustrates how your system allocates thread time. In my case, each time
 * slice is large enough for each thread to run several iterations of its little loop,
 * e.g. "wcpxaoy3852782%&))(*%$mqspnh".
 * 
 * Note that even though the methods on PrintWriter are not synchronized from the java side, the stdout PrintWriter
 * accesses a shared resource via an underlying OS call, which has the side effect of synchronizing on each
 * System.out.print() call.
 */


package blue.endless.module8_2;

import java.util.Random;

public class IsaacThreeThreads {
	public static final int ITERATIONS = 10_000;
	
	public static final char[] letters = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
	};
	
	public static final char[] digits = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
	};
	
	public static final char[] symbols = {
			'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'
	};
	
	/**
	 * Selects a random character from the provided pool.
	 * @param rnd The PRNG to use
	 * @param pool The pool of characters to select from
	 * @return The selected character.
	 */
	public static char randomElement(Random rnd, char[] pool) {
		return pool[rnd.nextInt(pool.length)];
	}
	
	/**
	 * Emits ten thousand random characters from the provided pool.
	 * 
	 * <p>This is the main objective of each of the three Threads.
	 * 
	 * @param pool The pool of characters to select from
	 */
	public static void emitCharacters(char[] pool) {
		Random rnd = new Random();
		for(int i=0; i<ITERATIONS; i++) {
			System.out.print(randomElement(rnd, pool));
		}
	}
	
	public static void main(String... args) {
		/*
		 * Since emitCharacters itself has too many arguments to be a Runnable, I'm doing something funky here called
		 * "function currying". The lambda '() -> emitCharacters(letters)' has "captured" the parameter 'letters'. It
		 * has gone from a void method accepting one argument down to a void method accepting no arguments - in other
		 * words, a Runnable. We curry each emitCharacters with a different pool so we can distinguish threads.
		 * 
		 * I do wish Java had built-in first-class function currying, but this is good enough.
		 */
		
		Thread lettersThread = new Thread(() -> emitCharacters(letters));
		Thread digitsThread = new Thread(() -> emitCharacters(digits));
		Thread symbolsThread = new Thread(() -> emitCharacters(symbols));
		
		lettersThread.start();
		digitsThread.start();
		symbolsThread.start();
		
		// Although these are not daemon threads, wait and try to make sure they finish executing before we exit.
		try {
			lettersThread.join();
			digitsThread.join();
			symbolsThread.join();
		} catch (InterruptedException ex) {} // do nothing, just terminate early.
	}
}
