package blue.endless.module9_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class App {
	// This is our Java entity, representing the same data as one line in the database table.
	public static record Address(
			int id,
			String lastName,
			String firstName,
			String street,
			String city,
			String state,
			String zip
			) {
		
		// records give us a built-in toString, but this one is slightly more readable.
		@Override
		public String toString() {
			return "  " +
				lastName() + ", " +
				firstName() + ": " +
				street() + " " +
				city() + ", " +
				state + " " +
				zip;
		}
	}
	
	/*
	 * In this program, we're downloading the whole table into an "address book",
	 * but that's mainly just to show you that you can move SQL data into *Java*
	 * domain objects. You don't have to work with the data "raw". Sometimes
	 * you'll want to use the cursor behavior of ResultSet to handle less data at
	 * a time, instead of saving out the whole query result.
	 */
	public static void main(String... args) {
		
		// Remember, no Class.forName() -- that's for the old pre-Java-6 way of
		// loading drivers!!!
		
		List<Address> addressBook = new ArrayList<>();
		
		// Try-with-resources on Connection!
		try (
				Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost/databasedb",
						"student33",
						"pass")
			) {
			
			// If you have *any* parameters, make sure to make a PreparedStatement instead!
			// It's super easy to avoid SQL injection in JDBC that way!
			Statement statement = conn.createStatement();
			
			
			// Try-with-resources on ResultSet!
			try (ResultSet results = statement.executeQuery("SELECT * FROM Address33;")) {
				while(results.next()) {
					// Pour the row into an Address domain object
					Address address = new Address(
							results.getInt("Id"),
							results.getString("LastName"),
							results.getString("FirstName"),
							results.getString("Street"),
							results.getString("City"),
							results.getString("State"),
							results.getString("Zip")
							);
					
					// Stuff the Address into our address book
					addressBook.add(address);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		// My policy is to get in and get out fast on a database transaction. The
		// SQL server has limited resources to serve queries, and we don't need
		// to linger longer than we have to.
		
		
		// Now that we're free of the database entirely, we can do whatever we want
		// with our loaded Java objects (if any).
		System.out.println("Addresses Loaded:");
		System.out.println();
		for(Address address : addressBook) {
			System.out.println(address);
		}
	}
}
