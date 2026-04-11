/*
 * CSD420: Advanced Java Programming
 * Module 4: Generics & Lists, Stacks, and Queues
 *   Assignment 2: Programming Assignment
 * 
 * Isaac Ellingson
 * 4/11/2026
 * 
 * Does benchmarking wrong. This is a big effect we're testing. Positively huge. So we'll see the effect here: Iterators
 * are faster.
 * 
 * But in general, if we want to eliminate CPU power, cache misses, compiler optimizations, etc, and make sure we're
 * actually testing what we think we're testing, we'd want to be using JMH specifically. It's irresponsible to ask
 * students to develop a habit of rolling their own benchmarks, and never present them with the alternative.
 * 
 * JMH can be obtained at https://github.com/openjdk/jmh
 * or using the Gradle JMH plugin at https://github.com/melix/jmh-gradle-plugin
 * Maven instructions are in the JMH repository.
 * 
 * 
 * Right. So what this program does is benchmark a traditional for loop using indexed getters, against an iterator-based
 * enhanced for loop using Iterator::next. A list with 50,000 elements is used to make this effect quite large. Putting
 * the indexed getter inside a loop has O(n^2) complexity, making the operation take slightly under 2.5 billion steps,
 * as opposed to the 50k steps for the iterator. This dwarfs the effects of things like cache misses and unboxing, so
 * we see a clear signal, AS LONG AS javac and the JVM do not optimize our code into a no-op, which a better optimzing
 * compiler would do.
 * 
 * On my machine the results print as follows:
 * 
 * total(ms)     ms/op
 * ===================
 *         1      0.20  (iterator)
 *      3390    678.00  (for loop)
 * 
 * +/- about 5ms
 * Depending on which version of javac and the JVM you use, and what processor you're on, your results will vary.
 */


package blue.endless.module4_2;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

public class App {
	
	private static final String HEADER = "total(ms)     ms/op\n===================";
	
	/**
	 * Represents the results of one full set of tests.
	 */
	public static class TestResult {
		private long millisTotal;
		private double millisPerOp;
		
		private static final NumberFormat FORMAT =  NumberFormat.getNumberInstance();
		static {
			FORMAT.setMinimumFractionDigits(2);
			FORMAT.setMaximumFractionDigits(2);
		}
		
		/**
		 * Creates a new test result
		 * 
		 * @param millisTotal The total number of milliseconds to complete all operations
		 * @param millisPerOp The average number of milliseconds to complete one operation
		 */
		public TestResult(long millisTotal, double millisPerOp) {
			this.millisTotal = millisTotal;
			this.millisPerOp = millisPerOp;
		}
		
		/** Gets the total number of milliseconds it took to complete all operations. */
		public long getMillisTotal() { return millisTotal; }
		/** Gets the average number of milliseconds it took to complete each operation. */
		public double getMillisPerOp() { return millisPerOp; }
		
		@Override
		public String toString() {
			String totalString = Long.toString(millisTotal);
			while (totalString.length() < 9) totalString = " " + totalString;
			String eachString = FORMAT.format(millisPerOp);
			while (eachString.length() < 8) eachString = " " + eachString;
			
			return totalString + "  " + eachString;
		}
	}
	
	/**
	 * Benchmarks an operation.
	 * @param r             The operation to test, presented as a Runnable
	 * @param warmupCycles  How many times to run the operation *before* testing, to warm up the CPU and caches
	 * @param testCycles    How many times to test the operation
	 * @return              A TestResult indicating how long the tests took.
	 */
	public static TestResult test(Runnable r, int warmupCycles, int testCycles) {
		// Warmup is important so that we don't test cache misses or CPU power scaling
		System.out.println("Warming up...");
		for(int i=0; i<warmupCycles; i++) {
			r.run();
		}
		// Testing multiple times helps smooth over any remaining cache misses or background tasks
		System.out.println("Testing...");
		Instant start = Instant.now();
		for(int i=0; i<testCycles; i++) {
			r.run();
		}
		Instant end = Instant.now();
		long elapsedMillis = start.until(end, ChronoUnit.MILLIS);
		double millisPerOp = elapsedMillis / (double) testCycles;
		return new TestResult(elapsedMillis, millisPerOp);
	}
	
	/**
	 * Walks through the supplied List using enhanced-for, backed by the List's Iterator.
	 * @param list The List to iterate over
	 */
	public static void walkWithIterator(List<Integer> list) {
		for(Integer i : list) {
			// Do nothing
		}
	}
	
	/**
	 * Walks through the supplied List using a traditional for loop and retrieves elements using an indexed get.
	 * @param list The List to iterate over
	 */
	public static void walkWithForLoop(List<Integer> list) {
		for(int i=0; i<list.size(); i++) {
			list.get(i);
			// Do nothing
		}
	}
	
	
	public static void main(String... args) {
		
		// Create a List with 50,000 integers
		PrimitiveIterator.OfInt randomInts = new Random().ints(50_000).iterator();
		LinkedList<Integer> list = new LinkedList<Integer>();
		while(randomInts.hasNext()) list.add(randomInts.next());
		
		// Perform the tests
		TestResult iteratorResult = test(() -> walkWithIterator(list), 5, 5);
		TestResult forResult = test(() -> walkWithForLoop(list), 5, 5);
		
		// Display the results in a convenient table
		System.out.println();
		System.out.println(HEADER);
		System.out.println(iteratorResult + "  (iterator)");
		System.out.println(forResult + "  (for loop)");
	}
}
