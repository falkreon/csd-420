/*
 * CSD420: Advanced Java Programming
 * Module 2: Binary I/O and Recursion
 *   Assignment 2: Programming Assignment
 * 
 * Isaac Ellingson
 * 3/30/2026
 * 
 * This program is kind of a copy of one in the previous class: open a file, creating it if necessary, and append some
 * random numbers to the end.
 * 
 * The file itself will start with 20 bytes bearing (5) integers in 32-bit big-endian signed twos-complement, with no
 * padding. This is followed by 40 bytes bearing (5) floating point numbers encoded as 64-bit IEEE-754 double-precision
 * floats in big-endian order, followed by another 20 bytes of integers and so on until the entire file's contents are
 * read.
 * 
 * We will use DataOutputStream and DataInputStream to achieve this with very little work.
 * 
 * The only in-memory specification we have is that the random integers and doubles should be stored in an int[] and
 * double[], respectively. I'm going to consider this a list-of-structs and create a domain object that holds an int[5]
 * and a double[5]. We call this RandomNumberEntry.
 */

package blue.endless.module2_2;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class WriterApp {
	public static final Path dataFile = new File(".", "ellingson_datafile.dat").toPath();
	
	public static void main(String... args) {
		
		/*
		 * The first thing we're asked to do is write five random ints and five random doubles to the file.
		 * We have encapsulated basically all of this logic into RandomNumberEntry, so we just call into that.
		 * 
		 * Out here, outside the class, we create the file if needed, and set the write cursor to the end of the file if
		 * needed, and construct a DataOutputStream to manage the actual writing of data byte for byte.
		 */
		try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(dataFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
			RandomNumberEntry newEntry = RandomNumberEntry.fromRandom(new Random());
			newEntry.write(out);
			
			System.out.println("The data was written successfully.");
		} catch (IOException e) {
			System.out.println("There was a problem writing the data.");
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
}
