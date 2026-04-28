package blue.endless.module8_2;

import java.util.Random;

public class IsaacThreeThreads {
	public static final int ITERATIONS = 10_000;
	public static final long MAX_SLEEP = 2;
	
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
	
	public static char randomElement(Random rnd, char[] pool) {
		return pool[rnd.nextInt(pool.length)];
	}
	
	public static void emitCharacters(char[] pool) {
		Random rnd = new Random();
		for(int i=0; i<ITERATIONS; i++) {
			// Helps the threads skew out into unpredictable orders instead of accidentally operating in lockstep
			try {
				Thread.sleep(rnd.nextLong(MAX_SLEEP));
			} catch (InterruptedException e) {
				// Do nothing. We'll just run early.
			}
			
			System.out.print(randomElement(rnd, pool));
		}
	}
	
	public static void main(String... args) {
		Thread lettersThread = new Thread(() -> emitCharacters(letters));
		Thread digitsThread = new Thread(() -> emitCharacters(digits));
		Thread symbolsThread = new Thread(() -> emitCharacters(symbols));
		
		lettersThread.start();
		digitsThread.start();
		symbolsThread.start();
	}
}
