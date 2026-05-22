/**
 * CSD-420: Advanced Java
 * Module 11: JSON
 *   Assignment 2: Programming Assignment
 * Isaac Ellingson
 * 5/22/2026
 * 
 * This is a companion piece to a paper for this assignment, describing the Jankson stable 1.2.x API's history and
 * features.
 * 
 * The objective of this program is to take a large array of Student records as JSON, sort them by major, and write the
 * result to an output JSON. In other words, we're transforming the data into a shape that's more usable for us.
 * 
 * The root element of the input student_data.json is an array. Each array element is an object in the form,
 * { "first_name": "...", "last_name": "...", "student_id": ..., "major": "..." }
 * 
 * However, we only count on the presence of a "major" field, and commute any additional data unharmed to the output
 * file.
 * 
 * The output file will look something like,
 * {
 *   "art": [ ... ],
 *   "architecture": [ ... ],
 *   "math": [ ... ],
 *   ...
 * }
 */


package blue.endless.module11_2;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;

public class App {
	public static void main(String... args) {
		// Configure the parser and serializer. In this case, default settings are perfect.
		Jankson jankson = Jankson.builder().build();
		
		// Create the output object. This is what will be written out to the output file.
		JsonObject output = new JsonObject();
		
		// Open the input file. This file was created by Mockaroo!
		// A comment was added to the front of the file to illustrate that we do in fact support those!
		try(InputStream in = Files.newInputStream(Path.of("student_data.json"))) {
			
			// Load the whole object tree into memory. It's only 1000 records, we'll be fine.
			JsonElement rootElem = jankson.loadElement(in);
			
			// Make sure the root element is a Json Array - this is uncommon for JSON files!
			if (rootElem instanceof JsonArray array) {
				
				// Since JsonArray implements List, we can use it just like a List, including enhanced-for / foreach
				for(JsonElement elem : array) {
					if (elem instanceof JsonObject studentObj) {
						
						// Since JsonObject implements Map, we can use get and put like normal.
						String subject = studentObj.get(String.class, "major");
						
						// Get or create the student-list for this subject
						JsonArray studentList = output.get(JsonArray.class, subject);
						if (studentList == null) {
							studentList = new JsonArray();
							output.put(subject, studentList);
						}
						
						studentList.add(studentObj);
					}
				}
			} else {
				System.out.println("Cannot process the input file. Root element of the file MUST be an array.");
				System.exit(-1);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(-1);
		}
		
		// We're now done reading the file, and all the resources associated with that have been released.
		// Only "output" remains, our computed result.
		
		// Write the result to the output file, creating or overwriting it as needed.
		try(BufferedWriter out = Files.newBufferedWriter(Path.of("student_output.json"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			// We could alternatively use JsonGrammar.COMPACT to automatically minify the output.
			output.toJson(out, JsonGrammar.STRICT, 0);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}
}
