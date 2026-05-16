/*
 * CSD420: Advanced Java Programming
 * Module 10: Java Database Programming Part II
 *   Assignment 2: Programming Assignment
 * 
 * Isaac Ellingson
 * 5/15/2026
 * 
 * Creates the specified UI frontend to a 'fans' table in the 'databasedb' mysql database on localhost.
 * 
 * In this UI, there is a field for entering in a user ID and a button for retrieving that fan. There is an area for
 * displaying / editing the retrieved user. And there's a button for updating that fan's data.
 * 
 * I got clearance in writing to use Swing for this, so that's what we're doing!
 */

package blue.endless.module10_2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class App {
	// Database link and login credentials: Better to move these all into an externally-provided Properties
	public static final String CONNECTION_URI = "jdbc:mysql://localhost/databasedb";
	public static final String USERNAME = "student1";
	public static final String PASSWORD = "pass";
	
	// Query which creates the fans table. We don't use this, but it helps document the requirements
	public static final String CREATE_FANS_TABLE =
		"CREATE TABLE IF NOT EXISTS fans (ID INT AUTO_INCREMENT PRIMARY KEY, firstname VARCHAR(25), lastname VARCHAR(25), favoriteteam VARCHAR(25));";
	
	// General queries
	public static final String GET_FANS = "SELECT * FROM fans;";
	public static final String GET_FAN = "SELECT * FROM fans WHERE `ID` = ?;";
	public static final String UPDATE_FAN = "UPDATE fans SET `firstname` = ?, `lastname` = ?, `favoriteteam` = ? WHERE `ID` = ?;";
	
	// Controls we need runtime access to
	private static JFrame window = new JFrame("CSD420 Module 10.2 - Isaac Ellingson");
	private static JTextField searchBox = new JTextField(40);
	private static JLabel idBox = new JLabel("?");
	private static JTextField firstNameBox = new JTextField(20);
	private static JTextField lastNameBox = new JTextField(20);
	private static JTextField favoriteTeamBox = new JTextField(20);
	private static JButton updateButton = new JButton("Update");
	
	/**
	 * Creates a connection to the databasedb database on localhost.
	 * @return The Connection
	 * @throws SQLException if there was a problem creating the Connection
	 */
	public static Connection createConnection() throws SQLException {
		return DriverManager.getConnection(CONNECTION_URI, USERNAME, PASSWORD);
	}
	
	/**
	 * UNUSED: Creates the fans table.
	 * @throws SQLException if an error was created when trying to create the table
	 */
	public static void createTable() throws SQLException {
		try (
				Connection conn = createConnection();
				Statement statement = conn.createStatement();
		) {
			statement.execute(CREATE_FANS_TABLE);
		}
	}
	
	/**
	 * UNUSED: Gets a List of all Fans in the fans table, as domain objects
	 * @return A List of all Fans
	 * @throws SQLException if there was a problem obtaining the list of Fans
	 */
	public static List<Fan> getAllFans() throws SQLException {
		List<Fan> fans = new ArrayList<>();
		
		try (
				Connection conn = createConnection();
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(GET_FANS);
		) {
			while(result.next()) {
				fans.add(Fan.ofRow(result));
			}
		}
		
		return fans;
	}
	
	/**
	 * Gets a Fan from the database by their ID and returns it as a domain object.
	 * @param id the Fan's ID
	 * @return either an Optional containing the Fan, or empty if there is no Fan with that ID
	 * @throws SQLException if there was an error encountered trying to retrieve the Fan from the database
	 */
	public static Optional<Fan> getFan(int id) throws SQLException {
		try (
				Connection conn = createConnection();
				PreparedStatement statement = conn.prepareStatement(GET_FAN);
		) {
			statement.setInt(1, id);
			
			try (ResultSet result = statement.executeQuery()) {
				if(result.next()) {
					return Optional.of(Fan.ofRow(result));
				} else {
					return Optional.empty();
				}
			}
		}
	}
	
	/**
	 * Transmits the provided Fan to the database as an update - if there is a Fan in the database with this ID, it will
	 * be updated to reflect the provided data.
	 * @param fan the Fan to send as an update
	 * @throws SQLException if there was no Fan with that ID, or if the update failed for some reason
	 */
	public static void updateFan(Fan fan) throws SQLException {
		try (
				Connection conn = createConnection();
				PreparedStatement statement = conn.prepareStatement(UPDATE_FAN);
		) {
			statement.setString(1, fan.firstName());
			statement.setString(2, fan.lastName());
			statement.setString(3, fan.favoriteTeam());
			statement.setInt(4, fan.id());
			
			int rows = statement.executeUpdate();
			if (rows == PreparedStatement.EXECUTE_FAILED) throw new SQLException("Failed to update.");
			if (rows == 0) throw new SQLException("Expected 1 row affected");
		}
	}
	
	/**
	 * ActionEvent listener which responds to the "Display" button being clicked. This will attempt to display the fan
	 * with the ID number entered into the searchBox.
	 * @param evt the ActionEvent sent by the Display button
	 */
	public static void onDisplayClicked(ActionEvent evt) {
		try {
			int id = Integer.parseInt(searchBox.getText());
			getFan(id).ifPresentOrElse(
					(fan) -> {
						idBox.setText(Integer.toString(fan.id()));
						firstNameBox.setText(fan.firstName());
						lastNameBox.setText(fan.lastName());
						favoriteTeamBox.setText(fan.favoriteTeam());
						updateButton.setEnabled(true);
					},
					() -> {
						searchBox.setText("");
						JOptionPane.showMessageDialog(window, "There is no Fan with that ID");
					});
		} catch (NumberFormatException ex) {
			searchBox.setText("");
			JOptionPane.showMessageDialog(window, "Please enter a number to bring up a record.");
		} catch (SQLException e) {
			searchBox.setText("");
			JOptionPane.showMessageDialog(window, "There was an error retrieving that Fan.");
		}
	}
	
	/**
	 * ActionEvent listener which responds to the "Update" button being clicked. This will create a Fan out of
	 * information entered in the fields and transmit it to the database as an UPDATE query.
	 * @param evt the ActionEvent sent by the Update button
	 */
	public static void onUpdateClicked(ActionEvent evt) {
		if (idBox.getText().equals("?")) return; // Nothing selected yet.
		
		try {
			int id = Integer.parseInt(idBox.getText());
			Fan fan = new Fan(id, firstNameBox.getText(), lastNameBox.getText(), favoriteTeamBox.getText());
			updateFan(fan);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(window, "An unexpected error occurred trying to update the Fan");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(window, "A database error was encountered trying to update the Fan");
		}
	}
	
	public static void main(String... args) {
		// This part just populates and stitches the UI
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setMinimumSize(new Dimension(300, 300));
		window.setPreferredSize(new Dimension(800, 300));
		{
			JPanel searchPanel = new JPanel();
			searchPanel.setLayout(new BorderLayout());
			{
				searchPanel.add(searchBox, BorderLayout.CENTER);
				JButton displayButton = new JButton("Display");
				displayButton.addActionListener(App::onDisplayClicked);
				searchPanel.add(displayButton, BorderLayout.EAST);
			}
			window.add(searchPanel, BorderLayout.NORTH);
			
			
			JPanel displayPanel = new JPanel();
			displayPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			displayPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 16, 16));
			{
				// Labeled text fields are kind of a pain - ideally the label goes *above* the field. So:
				
				JPanel idPanel = new JPanel();
				idPanel.setLayout(new BorderLayout());
				{
					JLabel idLabel = new JLabel("ID");
					idPanel.add(idLabel, BorderLayout.NORTH);
					idPanel.add(idBox, BorderLayout.SOUTH);
				}
				displayPanel.add(idPanel);
				
				JPanel firstNamePanel = new JPanel();
				firstNamePanel.setLayout(new BorderLayout());
				{
					JLabel firstNameLabel = new JLabel("First Name");
					firstNamePanel.add(firstNameLabel, BorderLayout.NORTH);
					firstNamePanel.add(firstNameBox, BorderLayout.SOUTH);
				}
				displayPanel.add(firstNamePanel);
				
				JPanel lastNamePanel = new JPanel();
				lastNamePanel.setLayout(new BorderLayout());
				{
					JLabel lastNameLabel = new JLabel("Last Name");
					lastNamePanel.add(lastNameLabel, BorderLayout.NORTH);
					lastNamePanel.add(lastNameBox, BorderLayout.SOUTH);
				}
				displayPanel.add(lastNamePanel);
				
				JPanel favoriteTeamPanel = new JPanel();
				favoriteTeamPanel.setLayout(new BorderLayout());
				{
					JLabel favoriteTeamLabel = new JLabel("Favorite Team");
					favoriteTeamPanel.add(favoriteTeamLabel, BorderLayout.NORTH);
					favoriteTeamPanel.add(favoriteTeamBox, BorderLayout.SOUTH);
				}
				displayPanel.add(favoriteTeamPanel);
			}
			window.add(displayPanel, BorderLayout.CENTER);
			
			JPanel savePanel = new JPanel();
			{
				updateButton.setEnabled(false);
				updateButton.addActionListener(App::onUpdateClicked);
				savePanel.add(updateButton, BorderLayout.CENTER);
			}
			window.add(savePanel, BorderLayout.SOUTH);
		}
		
		window.setVisible(true);
	}
}
