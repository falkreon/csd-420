/*
 * CSD420: Advanced Java Programming
 * Module 6: Lists, Stacks, Queues and Priority Queues, Sets & Maps
 *   Assignment 2: Programming Assignment
 * 
 * Isaac Ellingson
 * 4/21/2026
 * 
 * I strongly recommend reviewing the assignment text and fixing it for future classes. It is absolute nonsense.
 * 
 * Our best guess for the purpose of the assignment is to implement Bubble Sort two ways, using Comparable and
 * Comparator for ordering. Then test using something similar to the example arrays provided.
 * 
 * I've implemented the Comparable version by creating a Comparator that compares Comparables, and then forwarding to
 * the Comparator version. This helps prevent copy-paste bugs and makes things much easier to read. The tests really are
 * using different methods though - the "using Comparable" test uses the instance method Integer::compareTo, whereas the
 * "using Comparator" test uses the static method Integer::compare. These really do embody Comparable and Comparator, so
 * I think it's a fair implementation of the assignment, at least as far as we can understand it.
 * 
 * Note that I have implemented an early-out mechanism. If no elements are swapped in a full pass of the array, meaning
 * the "Sorting Step" is the exact same list twice in a row, then there's nothing more we can do to sort the list, and
 * we end the method early. So you may see fewer steps than you expect. This requires one boolean of extra storage, and
 * cuts iteration count by about half on average for random orderings, so it's really worth it.
 */

package blue.endless.module6_2;

import java.util.Arrays;
import java.util.Comparator;

public class App {
	
	/**
	 * Does an in-place bubble sort of an array, sorting in natural order based on the elements' own comparison rules.
	 * @param <E> The type of element in the array
	 * @param array The array to sort
	 */
	public static <E extends Comparable<E>> void bubbleSort(E[] array) {
		// For DRY reasons because the two methods are nearly identical, we'll just forward to the Comparator version
		bubbleSort(array, (a, b) -> a.compareTo(b));
	}
	
	/**
	 * Does an in-place bubble sort of an array, in ascending order according to the Comparator provided.
	 * @param <E> The type of element in the array
	 * @param array The array to sort
	 * @param comparator The comparator to use for ordering
	 */
	public static <E> void bubbleSort(E[] array, Comparator<? super E> comparator) {
		// Exactly the same as above.
		for(int i=0; i<array.length-1; i++) {
			boolean anythingSwapped = false;
			
			for(int j=0; j<array.length-1; j++) {
				// We could have chosen elements j and j-1 with different for-loop settings, but they're equivalent.
				// The important thing is, we pick each two adjacent elements
				E a = array[j];
				E b = array[j+1];
				
				// If they're in the wrong order, swap them.
				// When you see comparisons written like this, you can swap the order of symbols around in your brain,
				// moving the comparison operator between the variables and reading it as "if (a > b)"
				if (comparator.compare(a, b) > 0) {
					array[j] = b;
					array[j + 1] = a;
					anythingSwapped = true;
				}
				
				// That's it, that's the algorithm. The big intuition here is that it takes length^2 steps for an
				// element all the way on the right to "bubble" all the way to the left, so we have an outer loop to
				// repeat the bubble algorithm just enough times for a worst-case sort.
			}
			System.out.println("  Sorting Step: " + Arrays.toString(array));
			
			// If the list was fully sorted (no swaps) on our last pass, we can bail early
			if (!anythingSwapped) return;
		}
	}
	
	private static Integer[] STARTING_TEST_VALUES = { 5, 3, 4, 9, 0, 1, 2, 7, 6, 8 };
	// private static Integer[] STARTING_TEST_VALUES = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
	
	public static void main(String... args) {
		Integer[] testValues = Arrays.copyOf(STARTING_TEST_VALUES, STARTING_TEST_VALUES.length);
		
		System.out.println("About to bubble sort using Comparable: " + Arrays.toString(testValues));
		bubbleSort(testValues);
		System.out.println("Bubble-Sorted Array: " + Arrays.toString(testValues));
		System.out.println();
		
		// Reset and go again with the other method
		testValues = Arrays.copyOf(STARTING_TEST_VALUES, STARTING_TEST_VALUES.length);
		System.out.println("About to bubble sort using Comparator: " + Arrays.toString(testValues));
		bubbleSort(testValues, Integer::compare);
		System.out.println("Bubble-Sorted Array: " + Arrays.toString(testValues));
		System.out.println();
	}
}
