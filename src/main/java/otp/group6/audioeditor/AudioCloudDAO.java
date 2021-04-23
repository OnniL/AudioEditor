package otp.group6.audioeditor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.time.LocalDate;

/**
 * AudioCloudDAO is a class that handles the database connection, user object and mixer settings object. 
 * Main functionality is to read from the database and write into the database. 
 * Database holds user information including name, encrypted password and encrypted salt for the password.
 * Also stored are mixer settings that can be used in the main applications mixer. 
 * 
 * @author Joonas Soininen
 *
 */
public class AudioCloudDAO {

	/**
	 * Class Users is used as a object for the user while logged in.
	 * 
	 * @author Joonas Soininen
	 *
	 */
	public static class User {
		/** Static string for the users user name */
		private static String user;

		/**
		 * Method to set the user name for the variable
		 * @param user is the user name from the database
		 */
		private void setUser(String user) {
			User.user = user;
		}

		/**
		 * Method to get the set user name.
		 * @return Returns set user name or null if none is set.
		 */
		public String getUser() {
			return User.user;
		}

	}

	/**
	 * MixerSettings class is used as an object to access the database mixer settings data in the main
	 * application.
	 * 
	 * @author Joonas Soininen
	 *
	 */
	public static class MixerSetting {
		/** Variables for the mixer string types */
		private String mixName, description, dateDAO, creatorName;
		/** Variables for the double values in the mixer settings */
		private double pitch, echo, decay, gain, flangerLenght, wetness, lfoFrequency;
		/** Variable for the float value of low pass in the mixer settings */
		private float lowPass;
		/** Variable for the unique mixer id */
		private int mixID;

		/**
		 * Method to set the id from the database
		 * @param mixID unique id-number from the database
		 */
		private void setMixID(int mixID) {
			this.mixID = mixID;
		}

		/**
		 * Method to set the name from the database
		 * @param mixName specific name form the database
		 */
		private void setMixName(String mixName) {
			this.mixName = mixName;
		}

		/**
		 * Method to set the mix description form the database
		 * @param description specific description from the database
		 */
		private void setDescription(String description) {
			this.description = description;
		}
		
		/**
		 * Method to set the creator name from the database
		 * @param creatorName specific name from the database
		 */
		private void setCreatorName(String creatorName) {
			this.creatorName = creatorName;
		}

		/**
		 * Method to set the pitch value from the database
		 * @param pitch specific value from the database
		 */
		private void setPitch(double pitch) {
			this.pitch = pitch;
		}

		/**
		 * Method to set the echo value from the database
		 * @param echo specific value from the database
		 */
		private void setEcho(double echo) {
			this.echo = echo;
		}

		/**
		 * Method to set the decay value from the database
		 * @param decay specific value from the database
		 */
		private void setDecay(double decay) {
			this.decay = decay;
		}

		/**
		 * Method to set the gain value from the database
		 * @param gain specific value from the database
		 */
		private void setGain(double gain) {
			this.gain = gain;
		}

		/**
		 * Method to set the flanger length from the database
		 * @param flangerLenght specific valuea from the database
		 */
		private void setFlangerLenght(double flangerLenght) {
			this.flangerLenght = flangerLenght;
		}

		/**
		 * Method to set the wetness value from the database
		 * @param wetness specific value from the database
		 */
		private void setWetness(double wetness) {
			this.wetness = wetness;
		}
		
		/**
		 * Method to set the lfo frequency from the database
		 * @param lfoFrequency specific value from the database
		 */
		private void setLfoFrequency(double lfoFrequency) {
			this.lfoFrequency = lfoFrequency;
		}

		/**
		 * Method to set the low pass value from the database
		 * @param lowPass specific value from the database
		 */
		private void setLowPass(float lowPass) {
			this.lowPass = lowPass;
		}

		/**
		 * Method to set the date from the database for the mixer setting
		 * @param daoDate specific date from the database
		 */
		private void setDateDAO(String daoDate) {
			this.dateDAO = daoDate;
		}

		/**
		 * Method to get the id for the mixer setting
		 * @return id from the database or null
		 */
		public int getMixID() {
			return mixID;
		}
		
		/**
		 * Method to get the date for the mixer setting
		 * @return date from the databse or null
		 */
		public String getDateDAO() {
			return dateDAO;
		}

		/**
		 * Method to get the mixer setting name
		 * @return name from the database or null
		 */
		public String getMixName() {
			return mixName;
		}

		/**
		 * Method to get the mixer setting description
		 * @return description from the database or null
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * Method to get the mixer setting creator name
		 * @return name from the database or null
		 */
		public String getCreatorName() {
			return creatorName;
		}

		/**
		 * Method to get the pitch value for the mixer setting
		 * @return value from database or null
		 */
		public double getPitch() {
			return pitch;
		}

		/**
		 * Method to get the echo value for the mixer setting
		 * @return value from database or null
		 */
		public double getEcho() {
			return echo;
		}

		/**
		 * Method to get the decay value for the mixer setting
		 * @return value from database or null
		 */
		public double getDecay() {
			return decay;
		}

		/**
		 * Method to get the gain value for the mixer setting
		 * @return value from database or null
		 */
		public double getGain() {
			return gain;
		}

		/**
		 * Method to get the flanger length value for the mixer setting
		 * @return value from database or null
		 */
		public double getFlangerLenght() {
			return flangerLenght;
		}

		/**
		 * Method to get the wetness value for the mixer setting
		 * @return value from database or null
		 */ 
		public double getWetness() {
			return wetness;
		}

		/**
		 * Method to get the lfo frequency for the mixer setting
		 * @return value from database or null
		 */
		public double getLfoFrequency() {
			return lfoFrequency;
		}

		/**
		 * Method to get the low pass value for the mixer setting
		 * @return value from database or null
		 */
		public float getLowPass() {
			return lowPass;
		}

		/**
		 * Returns a string of the demanded values
		 */
		@Override
		public String toString() {
			return "MixerSetting\n[mixName=" + mixName + "\ndescription=" + description + "\ndateDAO=" + dateDAO
					+ "\ncreatorName=" + creatorName + "\npitch=" + pitch + "\necho=" + echo + "\ndecay=" + decay
					+ "\ngain=" + gain + "\nflangerLenght=" + flangerLenght + "\nwetness=" + wetness + "\nlfoFrequency="
					+ lfoFrequency + "\nlowPass=" + lowPass + "\nmixID=" + mixID + "]";
		}

	}

	/** Database connection variable */
	private Connection databaseConnection;
	/** User object to store the user name */
	private User userclass = new User();
	/** Boolean for checking the database connection */
	private boolean hasconnected = true;

	/**
	 * Constructor that accesses the database every time this instance is created.
	 * Try clause tires the connection
	 * Catch clause returns an error for the user
	 */
	public AudioCloudDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:2280/audiocloud", "yleinen",
					"J0k3OnR0");

		} catch (Exception e) {
			setHasconnected(false);
			System.err.println("Virhe tietokantayhteyden muodostamisessa. " + e);
			// System.exit(-1);
		}
	}

	/**
	 * Method closes the database connection
	 */
	@Override
	protected void finalize() {
		try {
			if (databaseConnection != null) {
				databaseConnection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to check for user name availability from the database.
	 * 
	 * @param user is the inputed user name from the user
	 * @return statement return true if the user name exist and false if it is
	 *         available
	 */
	public boolean chekcforUser(String user) {
		// TODO lopullisesta tietokannasta tippuu TEST pois
		try (PreparedStatement myStatement = databaseConnection
				.prepareStatement("SELECT * FROM accountsTEST WHERE username = ? ");) {
			myStatement.setString(1, user);
			ResultSet rset = myStatement.executeQuery();
			if (!rset.next()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}

	}

	/**
	 * Method is used to deliver a new user name in to the database.
	 * @param user, new user name to be inputed into the database
	 * @param pw,   hashed password
	 * @return true if the new user was inputed in to the database, false if it was not successful
	 * @throws SQLException if there is a problem with the database
	 */
	public boolean createUser(String user, String pw) throws SQLException {
		String salt = PasswordUtils.getSalt(30);
		String securePW = PasswordUtils.generateSecurePassword(pw, salt);

		// TODO lopullisesta tietokannasta tippuu TEST pois
		try (PreparedStatement query = databaseConnection
				.prepareStatement("INSERT INTO accountsTEST values (?,?,?,?)")) {
			query.setString(1, null);
			query.setString(2, user);
			query.setString(3, securePW);
			query.setString(4, salt);
			query.executeUpdate();
			return true;
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}
		return false;

	}

	/**
	 * Method logs in user if the credentials are correct and the user is found in the database.
	 * @param u, inputted user name
	 * @param p, inputted password
	 * @return Welcome "user" if user name and password were correct, No user if the user name does not exist,
	 * Incorrect user or pw if either is not correct, Unexpected error logging in, please try again! if there
	 * is any sort of problem with the SQL database.
	 */
	public String loginUser(String u, String p) {
		// TODO lopullisesta tietokannasta tippuu TEST pois
		try (PreparedStatement myStatement = databaseConnection
				.prepareStatement("SELECT username, password, salt FROM accountsTEST WHERE username = ?");) {
			myStatement.setString(1, u);
			ResultSet rset = myStatement.executeQuery();

			if (!rset.next()) {
				return "No user";
			}

			String pw = rset.getString("password");
			String salt = rset.getString("salt");

			if (p == null) {
				return "Incorrect user or pw";
			} else {
				boolean pwMatch = PasswordUtils.verifyUserPassword(p, pw, salt);

				if (pwMatch) {
					userclass.setUser((rset.getString("username")));
					return "Welcome " + rset.getString("username");
				} else {
					return "Incorrect user or pw";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Unexpected error logging in, please try again!";
	}
	
	/**
	 * Method to change users password
	 * @param u, user name given by the user
	 * @param p, old password of the user
	 * @param np, new password that user wants to be set
	 * @return true if password was changed correctly, false if old password is not a match,
	 * if there is a problem with the database connection, if the user name does not match.
	 */
	public boolean changePassword(String u, String p, String np) {
		// TODO lopullisesta tietokannasta tippuu TEST pois
		try (PreparedStatement myStatement = databaseConnection
				.prepareStatement("SELECT username, password, salt FROM accountsTEST WHERE username = ?");) {
			myStatement.setString(1, u);
			ResultSet rset = myStatement.executeQuery();

			if (!rset.next()) {
				return false;
			}

			String pw = rset.getString("password");
			String salt = rset.getString("salt");

			boolean pwMatch = PasswordUtils.verifyUserPassword(p, pw, salt);

			if (pwMatch) {
				String newsalt = PasswordUtils.getSalt(30);
				String securePW = PasswordUtils.generateSecurePassword(np, newsalt);
				// TODO lopullisesta tietokannasta tippuu TEST pois
				try (PreparedStatement myStatement1 = databaseConnection
						.prepareStatement("UPDATE accountsTEST set password=?, salt=? where username=?");) {
					myStatement1.setString(1, securePW);
					myStatement1.setString(2, newsalt);
					myStatement1.setString(3, loggedIn());
					myStatement1.executeUpdate();
					userclass.setUser((rset.getString("username")));
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Method checks the user-class for logged in user.
	 * 
	 * @return empty string if the user has not been set and returns user name if it is set
	 */
	public String loggedIn() {
		if ((userclass.getUser() == null)) {
			return " ";
		} else {
			return userclass.getUser();
		}
	}

	/**
	 * Method to logout the user
	 * 
	 * @return true when logged out, false when not
	 */
	public boolean logoutUser() {
		if (!(userclass.getUser() == " ")) {
			userclass.setUser(" ");
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Method to add mixer setting to the database with given parameters
	 * 
	 * @param mixName, compulsory has to have one
	 * @param description, voluntary input
	 * @param pitch, double value preset
	 * @param echo, double value preset
	 * @param decay, double value preset
	 * @param gain, double value preset
	 * @param flangerLenght, double value preset
	 * @param wetness, double value preset
	 * @param lfoFrequency, double value preset
	 * @param lowPass, float value preset
	 * @return true when mixer setting was successfully inserted to the database, false if there is anything wrong.
	 * @throws SQLException
	 */
	public boolean createMix(String mixName, String description, double pitch, double echo, double decay, double gain,
			double flangerLenght, double wetness, double lfoFrequency, float lowPass) throws SQLException {

		LocalDate date = LocalDate.now();

		if (!(userclass.getUser() == " ")) {
			// TODO lopullisesta tietokannasta tippuu TEST pois
			try (PreparedStatement query = databaseConnection
					.prepareStatement("INSERT INTO mixerSETTINGSTEST values (?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
				query.setString(1, null);
				query.setString(2, mixName);
				query.setString(3, description);
				query.setString(4, userclass.getUser());
				query.setString(5, date.toString());
				query.setDouble(6, pitch);
				query.setDouble(7, echo);
				query.setDouble(8, decay);
				query.setDouble(9, gain);
				query.setDouble(10, flangerLenght);
				query.setDouble(11, wetness);
				query.setDouble(12, lfoFrequency);
				query.setFloat(13, lowPass);
				query.executeUpdate();
				return true;
			} catch (SQLException e) {
				do {
					System.err.println("Viesti: " + e.getMessage());
					System.err.println("Virhekoodi: " + e.getErrorCode());
					System.err.println("SQL-tilakoodi: " + e.getSQLState());
				} while (e.getNextException() != null);
			}
			return false;
		} else {
			return false;
		}

	}


	/**
	 * Method to get all users listed from the database. This method is only used for development.
	 * @return Arraylist of all the users listed in the database
	 */
	public String[] getUsers() {
		Statement statement = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		// TODO lopullisesta tietokannasta tippuu TEST pois
		try {
			statement = databaseConnection.createStatement();
			rs = statement.executeQuery("SELECT * FROM accountsTEST");
			while (rs.next()) {
				String user = (rs.getString("username"));
				list.add(user);
			}
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}
		String[] returnArray = new String[list.size()];
		return (String[]) list.toArray(returnArray);
	}

	/**
	 * Method lists all the mixer settings from the database into an array list that can be iterated later.
	 * @return Array list of Mixer setting objects
	 */
	public MixerSetting[] getAllMixArray() {

		Statement statement = null;
		ResultSet rs = null;
		ArrayList<MixerSetting> list = new ArrayList<MixerSetting>();
		// TODO lopullisesta tietokannasta tippuu TEST pois
		try {
			statement = databaseConnection.createStatement();
			rs = statement.executeQuery("SELECT * FROM mixerSETTINGSTEST");

			while (rs.next()) {
				MixerSetting ms = new MixerSetting();
				ms.setMixID(rs.getInt("id"));
				ms.setMixName(rs.getString("mixName"));
				ms.setDescription(rs.getString("mixDescription"));
				ms.setDateDAO(rs.getString("dateAdded"));
				ms.setCreatorName(rs.getString("mixCreator"));
				ms.setPitch(rs.getDouble("pitch"));
				ms.setEcho(rs.getDouble("echo"));
				ms.setDecay(rs.getDouble("decay"));
				ms.setGain(rs.getDouble("gain"));
				ms.setFlangerLenght(rs.getDouble("flangerLenght"));
				ms.setWetness(rs.getDouble("wetness"));
				ms.setLfoFrequency(rs.getDouble("lfoFrequency"));
				ms.setLowPass(rs.getFloat("lowPass"));
				list.add(ms);
			}

		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}

		MixerSetting[] returnArray = new MixerSetting[list.size()];
		return (MixerSetting[]) list.toArray(returnArray);

	}

	/**
	 * Method is used to get specific mixer setting from a user, including a certain
	 * name or something in it's description.
	 * 
	 * @param select  is a variable used to specify what are being searched.
	 * @param specify is a variable that is searched for.
	 * @return returns the search in an array list containing mixer settings objects.
	 */
	public MixerSetting[] getCertainMixesArray(int select, String specify) {
		ArrayList<MixerSetting> list = new ArrayList<MixerSetting>();
		String statement = null;
		// TODO lopullisesta tietokannasta tippuu TEST pois
		if (select == 1) {
			statement = "SELECT * FROM mixerSETTINGSTEST where mixCreator LIKE '%" + specify + "%'";
		} else if (select == 2) {
			statement = "SELECT * FROM mixerSETTINGSTEST where mixName LIKE '%" + specify + "%'";
		} else if (select == 3) {
			statement = "SELECT * FROM mixerSETTINGSTEST where mixDescription LIKE '%" + specify + "%'";
		}

		try (PreparedStatement query = databaseConnection.prepareStatement(statement)) {
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				MixerSetting ms = new MixerSetting();
				ms.setMixID(rs.getInt("id"));
				ms.setMixName(rs.getString("mixName"));
				ms.setDescription(rs.getString("mixDescription"));
				ms.setDateDAO(rs.getString("dateAdded"));
				ms.setCreatorName(rs.getString("mixCreator"));
				ms.setPitch(rs.getDouble("pitch"));
				ms.setEcho(rs.getDouble("echo"));
				ms.setDecay(rs.getDouble("decay"));
				ms.setGain(rs.getDouble("gain"));
				ms.setFlangerLenght(rs.getDouble("flangerLenght"));
				ms.setWetness(rs.getDouble("wetness"));
				ms.setLfoFrequency(rs.getDouble("lfoFrequency"));
				ms.setLowPass(rs.getFloat("lowPass"));
				list.add(ms);
			}
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}
		MixerSetting[] returnArray = new MixerSetting[list.size()];
		return (MixerSetting[]) list.toArray(returnArray);
	}


	/**
	 * Method is used to delete mixer settings from the database using the creators name and the settings id.
	 * @param name, creators name
	 * @param id, mixer settings id
	 * @return true if deletion was successful, false if there was anything wrong.
	 */
	public boolean deleteMix(String name, int id) {
		// TODO lopullisesta tietokannasta tippuu TEST pois
		if (!(userclass.getUser() == " ")) {
			try (PreparedStatement statement = databaseConnection
					.prepareStatement("DELETE FROM mixerSETTINGSTEST WHERE mixCreator = ? AND id = ?")) {
				statement.setString(1, name);
				statement.setInt(2, id);
				int affected = statement.executeUpdate();
				if (affected < 1) {
					return false;
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * Used to delete a user from the database. 
	 * Only functions for logged users.
	 * 
	 * @return true when deletion was successful and false if anything went wrong.
	 */
	public boolean deleteUser() {
		// TODO lopullisesta tietokannasta tippuu TEST pois
		try (PreparedStatement statement = databaseConnection
				.prepareStatement("DELETE FROM accountsTEST WHERE username = ?")) {
			statement.setString(1, userclass.getUser());
			statement.executeUpdate();
			userclass.setUser(" ");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Used here only for JUnit testing
	 * 
	 * @param password is the inputed password
	 * @return true if it matches requirements
	 */
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
	private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

	public static boolean isValid(final String password) {
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public boolean isHasconnected() {
		return hasconnected;
	}

	public void setHasconnected(boolean hasconnected) {
		this.hasconnected = hasconnected;
	}

}
