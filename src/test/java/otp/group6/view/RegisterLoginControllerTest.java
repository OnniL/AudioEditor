package otp.group6.view;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterLoginControllerTest {

	private RegisterLoginController rlc;

	@BeforeEach
	void setUp() throws Exception {
		rlc = new RegisterLoginController();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testRegisterLoginController() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testShowPW() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testSetFinalPW() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testHandleCloseButtonAction() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testSetMainController() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testPwReminder() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testRegisterUser() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testLoginUser() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@DisplayName("Cheking for the password format to be correct")
	final void testPwIsValid() {
		assertFalse(RegisterLoginController.pwIsValid("keijo"), "Returns false if the password is not the right format");
		assertTrue(RegisterLoginController.pwIsValid("Keijo123"), "Returns true when the password is in the right format");
		assertFalse(RegisterLoginController.pwIsValid("keijo123"), "Returns false if the password is not in the right format");
		assertTrue(RegisterLoginController.pwIsValid("Kekkonen1"), "Returns true when the password is the right format");
	}

	@Test
	@DisplayName("Cheking for the username format to be correct")
	final void testUnisValid() {
		assertFalse(RegisterLoginController.unisValid("@keijo"), "Returns false if the username contains uncommon symbols such as @");
		assertTrue(RegisterLoginController.unisValid("Seppo"), "Returns true when the name is just letters or numbers");
		assertFalse(RegisterLoginController.unisValid("Petteri@sähköposti.fi"), "Returns false if the username is a email address");
		assertTrue(RegisterLoginController.unisValid("123456"), "Returns true when the username is letters or numbers");
	}

	@Test
	final void testInitialize() {
		fail("Not yet implemented"); // TODO
	}

}
