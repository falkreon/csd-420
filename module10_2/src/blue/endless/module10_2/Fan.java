package blue.endless.module10_2;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Immutable class representing the "Fan" database record. We're still sticking to Java 10 for this, otherwise this
 * would just be a record type.
 */
public class Fan {
	private final int id;
	private final String firstName;
	private final String lastName;
	private final String favoriteTeam;
	
	/**
	 * Creates a new fan with the specified values
	 * @param id The Fan's ID
	 * @param firstName The Fan's first name
	 * @param lastName  The Fan's last name
	 * @param favoriteTeam The Fan's favorite team
	 */
	public Fan(int id, String firstName, String lastName, String favoriteTeam) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.favoriteTeam = favoriteTeam;
	}
	
	// Getters
	
	/** Gets this Fan's ID */
	public int id() { return this.id; }
	/** Gets this Fan's first name */
	public String firstName() { return this.firstName; }
	/** Gets this Fan's last name */
	public String lastName() { return this.lastName; }
	/** Gets this Fan's favorite team */
	public String favoriteTeam() { return this.favoriteTeam; }
	
	
	// Withers - creates a new immutable object with the specified changes.
	
	/**
	 * Gets a copy of this Fan, but with a different firstName
	 * @param value the new firstName
	 * @return A copy of this Fan with the changed field
	 */
	public Fan withFirstName(String value) {
		return new Fan(id, value, lastName, favoriteTeam);
	}
	
	/**
	 * Gets a copy of this Fan, but with a different lastName
	 * @param value the new lastName
	 * @return A copy of this Fan with the changed field
	 */
	public Fan withLastName(String value) {
		return new Fan(id, firstName, value, favoriteTeam);
	}
	
	/**
	 * Gets a copy of this Fan, but with a different favoriteTeam
	 * @param value the new favoriteTeam
	 * @return A copy of this Fan with the changed field
	 */
	public Fan withFavoriteTeam(String value) {
		return new Fan(id, firstName, lastName, value);
	}
	
	/**
	 * Gets a Fan from the current row of a ResultSet.
	 * @param result The ResultSet from a query on the "fans" table
	 * @return A Fan representing the currently-selected row of the ResultSet
	 * @throws SQLException if there was an error retrieving the Fan
	 */
	public static Fan ofRow(ResultSet result) throws SQLException {
		return new Fan(
			result.getInt("ID"),
			result.getString("firstname"),
			result.getString("lastname"),
			result.getString("favoriteteam")
			);
	}
	
	@Override
	public String toString() {
		return "{ id: " + id +
				", firstName: " + firstName +
				", lastName: " + lastName +
				", favoriteTeam: " + favoriteTeam +
				" }";
	}
}
