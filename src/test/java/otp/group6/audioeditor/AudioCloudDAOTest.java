package otp.group6.audioeditor;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import otp.group6.audioeditor.AudioCloudDAO;
import otp.group6.audioeditor.AudioCloudDAO.MixerSetting;
import otp.group6.audioeditor.AudioCloudDAO.User;

import org.junit.jupiter.api.Disabled;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
/**
 * Test class for AudioCloudDAO to check for different methods functionality with the database 
 * 
 * @author Joonas Soininen
 *
 */
@Disabled
class AudioCloudDAOTest {

	/**
	 * Instance of AudioCloudDAO and variables for thestin
	 */
	private AudioCloudDAO dao = new AudioCloudDAO();
	private final String user1 = "test1";
	private final String user2 = "Joonas123";
	private final String pw1 = "test";
	private final String pw2 = "Password1!";

	/**
	 * Database query checking for the username availability
	 */
	@Test
	@DisplayName("Cheking the database for availability in the username")
	@Order(1)
	void testChekcforUser() {
		assertTrue(dao.chekcforUser(user1), "chekforUser(user): true, when the username is in the database");
		assertFalse(dao.chekcforUser(user2), "chekforUser(user): false, when the username is not in the database");
	}

	/**
	 * Database query for adding a new username.
	 * @throws SQLException if there is a problem with the database connection
	 */
	@Test
	@DisplayName("Registering a new user")
	@Order(2)
	void testCreateUser() throws SQLException {
		assertFalse(AudioCloudDAO.isValid(pw1),
				"isValid(String): false, when the password does not match the requirements");
		assertTrue(AudioCloudDAO.isValid(pw2), "isValid(String): true, when the password is in a correct format");
		assertTrue(dao.createUser(user2, pw2), "createUser(user): true when there is a problem creatin the user");
	}

	/**
	 * Database query for logging in the new user.
	 */
	@Test
	@DisplayName("User login")
	@Order(3)
	void testLoginUser() {
		assertEquals("Welcome Joonas123", dao.loginUser(user2, pw2), "loginUser(String, String): ");
	}

	/**
	 * Database query to add new mixer setting.
	 * @throws SQLException if there is a problem with the database connection.
	 */
	@Test
	@DisplayName("Creating a new mixer setting")
	@Order(7)
	void testCreateMix() throws SQLException {
		assertFalse(dao.createMix("testi", "Filtteriä kuvaava teksti", 1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, (float) 8.8),
				"createMix(Mixersetting): false, User needs to be logged in");
		dao.loginUser("test6", "Example1!");
		assertTrue(dao.createMix("testi", "Filtteriä kuvaava teksti", 1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, (float) 8.8),
				"createMix(Mixersetting): true, when setting was created");
	}

	/**
	 * Logging out the previously logged in user.
	 */
	@Test
	@DisplayName("Logout test")
	@Order(4)
	void testLogout() {
		assertTrue(dao.logoutUser(), "logoutUser(): true, if the logout was a success");
	}

	/**
	 * Query to list all registered users from the database.
	 */
	@Test
	@DisplayName("Getting all registered users")
	void testGetUsers() {
		int expected = 15;
		int actual = dao.getUsers().length;
		assertEquals(expected, actual, "Length is set for the testing, must be cheked if tested later on");
	}

	/**
	 * Query to get all the mixer settings from the database.
	 */
	@Test
	@DisplayName("Getting all mixer settings")
	void testGetAllMixArray() {
		int expected = dao.getAllMixArray().length;
		int actual = dao.getAllMixArray().length;
		assertEquals(expected, actual, "Length is set for the testing, must be cheked if tested later on");
	}

	/**
	 * Query to get a specific mixer setting from the database using different variables.
	 */
	@Test
	@DisplayName("Getting specific mixer setting")
	void testGetCertainMixesArray() {
		assertEquals(dao.getCertainMixesArray(1, "Joonas").length, 15,
				"Checks for the amount of entries including specified symbols");
		assertEquals(dao.getCertainMixesArray(2, "7").length, 0,
				"Checks for the amount of entries including specified symbols");
		assertEquals(dao.getCertainMixesArray(3, "usko").length, 1,
				"Checks for the amount of entries including specified symbols");
	}

	/**
	 * Query to delete previously created mixer setting from the database.
	 */
	@Test
	@DisplayName("Deleting mixer setting")
	@Order(8)
	void testDeleteMix() {
		MixerSetting[] mlist = dao.getCertainMixesArray(1, "test6");
		int id = 0;
		for (MixerSetting mix : mlist) {
			id = mix.getMixID();
		}
		assertTrue(dao.deleteMix("test6", id), "deleteMix(String, int): true, if the specified mix was deleted");
	}

	/**
	 * Query to delete previously created user.
	 */
	@Test
	@DisplayName("Deleting the just created user")
	@Order(6)
	void testDeleteUser() {
		// dao.loginUser(user2, pw2);
		assertTrue(dao.deleteUser(), "deleteUser(): true, when user deletion is ok");
	}

	/**
	 * Checking for password validity.
	 */
	@Test
	@DisplayName("Password is valid")
	void testIsValid() {
		assertTrue(AudioCloudDAO.isValid(pw2),
				"isValid(String): true, when the inserted password matches requirements");
	}
	
	/**
	 * Query for changing password.
	 */
	@Test
	@DisplayName("Change password")
	@Order(5)
	void testChangePassword() {
		dao.loginUser(user2, pw2);
		assertFalse(AudioCloudDAO.isValid(pw1),
				"isValid(String): false, when the password does not match the requirements");
		assertTrue(AudioCloudDAO.isValid(pw2), "isValid(String): true, when the password is in a correct format");
		assertTrue(dao.changePassword(user2, pw2, pw2),
				"changePassword(String,String,String): true if the change was succesfull ");
	}

}
