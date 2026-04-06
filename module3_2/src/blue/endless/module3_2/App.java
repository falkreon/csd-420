/*
 * CSD420: Advanced Java Programming
 * Module 3: Generics
 *   Assignment 2: Programming Assignment
 * 
 * Isaac Ellingson
 * 4/6/2026
 * 
 * Takes in an ArrayList and returns a new ArrayList with duplicates removed. I've done this three ways, each step
 * generalizing the problem further, clarifying its requirements and examining its assumptions.
 */

package blue.endless.module3_2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class App {
	
	/**
	 * What the assignment asked for: Takes the input list, and returns a copy of the list with duplicate elements
	 * removed.
	 * 
	 * @param <E> The type of element in the source List
	 * @param list The source List
	 * @return A duplicate of the source List, with the same iteration order, but with only distinct elements.
	 */
	public static <E> ArrayList<E> removeDuplicates(ArrayList<E> list) {
		ArrayList<E> result = new ArrayList<>();
		for(E e : list) {
			if (!result.contains(e)) result.add(e);
		}
		
		return result;
	}
	
	
	/**
	 * It is desirable to relax the requirements in a few ways: The input could be any Collection, and the output could
	 * be any SequencedCollection - since SequencedCollection isn't available till Java 21, we use List here.
	 * 
	 * <p>I believe this functional implementation is extremely clear - it reads very similarly to a SELECT DISTINCT
	 * SQL statement or C# LINQ expression, and does exactly what you would expect from that understanding. We could
	 * simplify it even further with the Java 16+ toList terminal operation.
	 * 
	 * <p>Performance-wise, it would be better to use this Stream as part of a larger map-reduce operation rather than
	 * calling the terminal operation right here. As-written this method probably performs slightly worse than the first
	 * method.
	 * 
	 * @param <E> The type of element in the source collection
	 * @param list The source collection
	 * @return A duplicate of the source collection, with the same iteration order, but with only distinct elements.
	 */
	public static <E> List<E> removeDuplicatesFunctional(Collection<E> list) {
		return list.stream()
				.distinct()
				.collect(Collectors.toList()); // normally in Java 16+ we could use toList() here
	}
	
	/**
	 * More generally, rather than filtering the list once, and then allowing duplicates to sneak back in afterwards, we
	 * probably want to express this as an invariant of the collection itself, and enforce that on an ongoing basis. If
	 * the original iteration order is important to us, we can use a LinkedHashSet to accomplish this. If it is not, we
	 * can fall back to our trusty HashSet. This will continue to ensure duplicates don't sneak in while still allowing
	 * us to iterate through its items in insertion order (if desired).
	 * 
	 * <p>Once again, SequencedCollection or SequencedSet would be desirable return types but they aren't available yet,
	 * so we use Collection. 
	 * 
	 * @param <E> The type of element in the source collection
	 * @param list The source collection
	 * @return A duplicate of the source collection, with the same iteration order, but with only distinct elements.
	 */
	public static <E> Collection<E> removeDuplicatesSetInsertion(Collection<E> list) {
		return new LinkedHashSet<>(list);
	}
	
	
	
	/**
	 * Takes in a Collection, and returns a sorted List with its contents.
	 * @param collection The source collection
	 * @return A sorted copy of the collection
	 */
	public static List<Integer> sorted(Collection<Integer> collection) {
		return collection.stream().sorted().collect(Collectors.toList());
	}
	
	
	public static void main(String... args) {
		// Create the ArrayList of random boxed ints
		ArrayList<Integer> list = new ArrayList<Integer>(new Random().ints(50, 1, 21).boxed().toList());
		
		System.out.println("Unfiltered list: "+list);
		
		System.out.println();
		
		ArrayList<Integer> traditional = removeDuplicates(list);
		List<Integer> functional = removeDuplicatesFunctional(list);
		Collection<Integer> insertion = removeDuplicatesSetInsertion(list);
		
		System.out.println("--Raw function output--");
		System.out.println("  Using traditional code to remove duplicates: " + traditional);
		System.out.println("  Using functional code to remove duplicates:  " + functional);
		System.out.println("  Using set insertion to remove duplicates:    " + insertion);
		
		System.out.println();
		
		System.out.println("--Sorted for clarity--");
		System.out.println("  Traditional code:  " + sorted(traditional));
		System.out.println("  Functional code:   " + sorted(functional));
		System.out.println("  Set Insertion code:" + sorted(insertion));
	}
}
