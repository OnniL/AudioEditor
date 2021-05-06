package otp.group6.view;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
/**
 * This test class tests basic functions in this class
 * 
 * @author Joonas Soininen
 *
 */
class MixerSettingsControllerTest {
	MixerSettingsController msc = new MixerSettingsController();

	/**
	 * Tests for correct identification for the mixer setting
	 */
	@Test
	@DisplayName("Cheking for mix identifiaction to be correct")
	@Order(1)
	final void testSetMixerIndetification() {
		msc.setMixerIndetification(15);
		assertEquals(15, msc.getMixerIndetification());
	}
	
	/**
	 * Tests for correct identification for the mixer setting
	 */	
	@Test
	@DisplayName("Cheking for mix identifiaction to be correct")
	@Order(2)
	final void testGetMixerIndetification() {
		msc.setMixerIndetification(155);
		assertEquals(155, msc.getMixerIndetification());
	}

	/**
	 * Tests for the mix creator to be correct
	 */
	@Test
	@DisplayName("Cheking for mix creator to be correct")
	@Order(3)
	final void testSetMixerCreatorName() {
		msc.setMixerCreatorName("TESTIKÄYTTÄJÄ");
		assertEquals("TESTIKÄYTTÄJÄ", msc.getMixerCreatorName(), "Returns the assigned mix creator");
	}

	/**
	 * Tests for the mix creator to be correct
	 */
	@Test
	@DisplayName("Cheking for mix creator to be correct")
	@Order(4)
	final void testGetMixerCreatorName() {
		msc.setMixerCreatorName("TESTIKÄYTTÄJÄ");
		assertEquals("TESTIKÄYTTÄJÄ", msc.getMixerCreatorName(), "Returns the assigned mix creator");
	}

}
