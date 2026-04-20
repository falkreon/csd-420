/*
 * CSD420: Advanced Java Programming
 * Module 5: Sets, Maps, and Sorting
 *   Assignment 2: Programming Assignment
 * 
 * Isaac Ellingson
 * 4/19/2026
 * 
 * Take an included corpus of words, in my case the entire script of Hamlet by William Shakespeare, and create a
 * de-duplicated, sorted collection of words. Print these words out in ascending lexicographic order (A-Z), and then
 * print them again in descending lexicographic order (Z-A).
 * 
 * All of this is completely automated in Java, so the fastest and clearest way to do this is an insertion sort.
 * 
 * First, I filter the incoming text line by line to split it into words, remove blank space and punctuation, and remove
 * word-like but non-word entries. I also lowercase the word based on our (USA) locale.
 * 
 * Next, I insert the word into a TreeSet created using its default configuration. Being a Set, a TreeSet will reject
 * duplicate inserts. TreeSet additionally sorts its contents based on a Comparator, the default Comparator being
 * identical to Objects::equals. This results in a Set of Strings being sorted in ascending lexicographic order.
 * 
 * Finally, to list the reverse Set, I don't even need to re-insert the data into a new Set with an opposite Comparator;
 * we can simply use the included reverse view of the original set, which just walks the underlying red-black tree in
 * the opposite order.
 * 
 * The result processes the entire corpus (Hamlet) in 0.02-0.03 seconds on my machine, so I think this is sufficient
 * for now.
 */
package blue.endless.module5_2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class App {
	private static final Set<String> NOT_WORDS = Set.of(
			"ii", "iii", "iv", "v", "vi"
			);
	
	public static void main(String... args) {
		System.out.println("Reading in file...");
		Path wordsPath = new File("collection_of_words.txt").toPath(); // Path.of() not available yet
		try (BufferedReader reader = Files.newBufferedReader(wordsPath)) {
			TreeSet<String> words = new TreeSet<>();
			
			while(true) {
				String line = reader.readLine();
				if (line == null) break;
				
				String[] parts = line.split("\\b");
				for(String word : parts) {
					String cleanWord = word.trim().toLowerCase(Locale.US);
					if (cleanWord.isEmpty()) continue; // Reject blank spaces
					if (!Character.isLetter(cleanWord.charAt(0))) continue; // Reject "!", "?", "'?", etc.
					if (NOT_WORDS.contains(cleanWord)) continue; // Reject roman numerals etc.
					words.add(cleanWord);
				}
				
			}
			
			System.out.println("Reading complete.");
			
			System.out.println();
			System.out.println("Words in ascending lexicographic order:");
			System.out.println(words);
			
			System.out.println();
			System.out.println("Words in descending lexicographic order:");
			System.out.println(words.descendingSet()); // reversed() not available yet, but we'd use that in modern java
		} catch (NoSuchFileException nsfe) {
			System.out.println("Could not find the input file. Location checked based on your current working directory: " + wordsPath.toAbsolutePath().toString());
		} catch (IOException ex) {
			System.out.println("There was a problem reading the file. Please show this error to a developer:");
			ex.printStackTrace();
		}
		
	}
}
