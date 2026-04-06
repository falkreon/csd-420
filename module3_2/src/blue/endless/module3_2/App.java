package blue.endless.module3_2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {
	public static <E> List<E> removeDuplicatesFunctional(ArrayList<E> list) {
		return list.stream()
				.distinct()
				.toList();
	}
	
	public static <E> ArrayList<E> removeDuplicates(ArrayList<E> list) {
		ArrayList<E> result = new ArrayList<>();
		for(E e : list) {
			if (!result.contains(e)) result.add(e);
		}
		
		return result;
	}
	
	
	public static void main(String... args) {
		// Create the ArrayList of random boxed ints
		ArrayList<Integer> list = new ArrayList<Integer>(new Random().ints(50, 1, 21).boxed().toList());
		
		System.out.println("Unfiltered list: "+list);
		
		List<Integer> functional = removeDuplicatesFunctional(list);
		ArrayList<Integer> traditional = removeDuplicates(list);
		
		System.out.println("--Raw function output--");
		System.out.println("  Using functional code to remove duplicates:  " + functional);
		System.out.println("  Using traditional code to remove duplicates: " + traditional);
		
		System.out.println();
		
		System.out.println("--Sorted for clarity--");
		functional = new ArrayList<>(functional); // Turn functional output into an ArrayList since the stream output is immutable by default
		functional.sort(Integer::compare);
		traditional.sort(Integer::compare);
		System.out.println("  Functional code:  " + functional);
		System.out.println("  Traditional code: " + traditional);
	}
}
