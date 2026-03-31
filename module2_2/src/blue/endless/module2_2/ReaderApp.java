/*
 * CSD420: Advanced Java Programming
 * Module 2: Binary I/O and Recursion
 *   Assignment 2: Programming Assignment
 * 
 * Isaac Ellingson
 * 3/30/2026
 * 
 * Main assignment notes are in {@link WriterApp}.
 * 
 * This program takes a previously written file containing an array of RandomNumberEntry structs (which are themselves
 * each an array of five ints and an array of five doubles), reads it all into memory, and then displays the data.
 */

package blue.endless.module2_2;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class ReaderApp {
	public static final Path dataFile = WriterApp.dataFile;
	
	public static void main(String... args) {
		List<RandomNumberEntry> entries = new ArrayList<>();
		try (DataInputStream in = new DataInputStream(Files.newInputStream(dataFile))) {
			// We trust EOF to break us from this loop
			while(true) {
				entries.add(RandomNumberEntry.fromInput(in));
			}
		} catch (EOFException ex) {
			// Do nothing. We *are* supposed to hit the end of the file eventually.
		} catch (NoSuchFileException ex3) {
			// The file isn't present - we can display a more specific and helpful message for this one
			System.out.println("The data file hasn't been created yet. Please run WriterApp first to create the file that this program reads.");
			System.exit(-1);
		} catch (IOException ex2) {
			// IOExceptions here are uncommon and may represent program errors, so go ahead and produce a stack trace
			// which the user can present to a developer for assistance.
			System.out.println("A problem occurred when reading the data file.");
			ex2.printStackTrace();
			System.exit(-1);
		}
		
		// All complete entries have been read now. Print them out.
		for(int i=0; i<entries.size(); i++) {
			RandomNumberEntry entry = entries.get(i);
			System.out.println("Entry " + (i+1) + ":");
			System.out.println("  "+entry.describeInts());
			System.out.println("  "+entry.describeDoubles());
		}
	}
}
