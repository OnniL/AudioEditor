package otp.group6.view;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UserSettingsControllerTest {
	
	@Test
	final void testIsValid() {
		assertFalse(UserSettingsController.isValid("keijo"), "Returns false if the password is not the right format");
		assertTrue(UserSettingsController.isValid("Keijo123"), "Returns true when the password is in the right format");
		assertFalse(UserSettingsController.isValid("keijo123"), "Returns false if the password is not in the right format");
		assertTrue(UserSettingsController.isValid("Kekkonen1"), "Returns true when the password is the right format");
	}
	
}
