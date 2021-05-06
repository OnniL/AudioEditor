package otp.group6.view;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
/**
 * This test class is used to test RegisterLoginController.
 * 
 * @author Joonas Soininen
 *
 */
class RegisterLoginControllerTest {

	/**
	 * Tests for the password formatting to be correct
	 */
	@Test
	@DisplayName("Cheking for the password format to be correct")
	final void testPwIsValid() {
		assertFalse(RegisterLoginController.pwIsValid("keijo"),
				"Returns false if the password is not the right format");
		assertTrue(RegisterLoginController.pwIsValid("Keijo123"),
				"Returns true when the password is in the right format");
		assertFalse(RegisterLoginController.pwIsValid("keijo123"),
				"Returns false if the password is not in the right format");
		assertTrue(RegisterLoginController.pwIsValid("Kekkonen1"),
				"Returns true when the password is the right format");
	}

	/**
	 * Tests for the username formatting to be correct
	 */
	@Test
	@DisplayName("Cheking for the username format to be correct")
	final void testUnisValid() {
		assertFalse(RegisterLoginController.unisValid("@keijo"),
				"Returns false if the username contains uncommon symbols such as @");
		assertTrue(RegisterLoginController.unisValid("Seppo"), "Returns true when the name is just letters or numbers");
		assertFalse(RegisterLoginController.unisValid("Petteri@sähköposti.fi"),
				"Returns false if the username is a email address");
		assertTrue(RegisterLoginController.unisValid("123456"), "Returns true when the username is letters or numbers");
	}

}
