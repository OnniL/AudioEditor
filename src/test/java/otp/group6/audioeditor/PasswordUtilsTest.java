package otp.group6.audioeditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import otp.group6.audioeditor.PasswordUtils;

/**
 * Test class for passwordUtils to check the functionality of the class
 * 
 * @author Joonas Soininen
 *
 */
class PasswordUtilsTest {
	
	/**
	 * Variables for testing
	 */
	//private PasswordUtils pwtest = new PasswordUtils();
	private String pw = "Password";
	private String salt = PasswordUtils.getSalt(30);
	private String securepw = PasswordUtils.generateSecurePassword(pw, salt);

	/**
	 * Testing that the password salt is correct length.
	 */
	@Test
	@DisplayName("Cheking the correct lenght of the salt")
	final void testGetSalt() {
		assertEquals(salt.length(), PasswordUtils.getSalt(30).length(), "Comparison of lenghts in salt");
	}

	/**
	 * Testing that the password length is correct.
	 */
	@Test
	@DisplayName("Cheking the correct lenghts of the password")
	final void testGenerateSecurePassword() {
		assertEquals(securepw.length(), PasswordUtils.generateSecurePassword(pw, salt).length(),
				"Comparison of lenghts with passwords");
	}
	
	/**
	 * Testing that the user password and secure password match using the user unique salt.
	 */
	@Test
	@DisplayName("Cheking that the user pw and secure pw are correct with the unique salt")
	final void testVerifyUserPassword() {
		assertTrue(PasswordUtils.verifyUserPassword(pw, securepw, salt), "True if the password is the secured one");
	}

}
