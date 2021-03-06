package otp.group6.audioeditor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import otp.group6.controller.Controller;
/**
 * This test class is for testing the AudioManipulator.
 * 
 * @author Onni Lukkarila 
 *
 */

public class AudioManipulatorTest {

	private static Controller controller = new Controller();
	private static AudioManipulator manipulator = new AudioManipulator(controller);

	@BeforeAll
	public static void setUp() {
		manipulator.setAudioSourceFile(new File("src/audio/testiaani.wav"));
	}

	/**
	 * Test for setting gain
	 */
	@Test
	@DisplayName("Test for setting gain")
	@Order(1)
	void testSetGain() {
		manipulator.setGain(2.0);
		assertEquals(2.0, manipulator.getEffectValue("gain"), "Gain wasn't set succesfully");
	}

	/**
	 * Test for setting pitch
	 */
	@Test
	@DisplayName("Test for setting pitch")
	@Order(2)
	void testSetPitch() {
		manipulator.setPitchFactor(2.4);
		assertEquals(2.4, manipulator.getEffectValue("pitch"), "Pitch wasn't set succesfully");
	}

	/**
	 * Test for setting length
	 */
	@Test
	@DisplayName("Test for setting echo length")
	@Order(3)
	void testSetEcho() {
		manipulator.setEchoLength(1.4);
		assertEquals(1.4, manipulator.getEffectValue("echoLength"), "Echo lenght wasn't set succesfully");
	}

	/**
	 * Test for setting decay
	 */
	@Test
	@DisplayName("Test for setting decay")
	@Order(4)
	void testSetDecay() {
		manipulator.setDecay(3.1);
		assertEquals(3.1, manipulator.getEffectValue("decay"), "Decay wasn't set succesfully");
	}

	/**
	 * Test for setting flanger effect
	 */
	@Test
	@DisplayName("Test for setting flanger effect")
	@Order(5)
	void testSetFlanger() {
		manipulator.setFlangerLength(0.2);
		assertEquals(0.2, manipulator.getEffectValue("flangerLength"), "flanger effect wasn't set succesfully");
	}

	/**
	 * Test for setting wetness effect
	 */
	@Test
	@DisplayName("Test for setting wetness effect")
	@Order(6)
	void testSetWetness() {
		manipulator.setWetness(0.5);
		assertEquals(0.5, manipulator.getEffectValue("wetness"), "wetness wasn't set succesfully");
	}

	/**
	 * Test for setting LFO
	 */
	@Test
	@DisplayName("Test for setting LFO")
	@Order(7)
	void testSetLFO() {
		manipulator.setLFO(0.7);
		assertEquals(0.7, manipulator.getEffectValue("lfo"), "LFO wasn't set succesfully");
	}

	/**
	 * Test for setting LowPass
	 */
	@Test
	@DisplayName("Test for setting LowPass")
	@Order(8)
	void testSetLowPass() {
		manipulator.setLowPass((float) 0.7);
		assertEquals((float) 0.7, manipulator.getEffectValue("lowPass"), "LowPass wasn't set succesfully");
	}
	
	/**
	 * Test for disabling pitch
	 */
	@Test
	@DisplayName("Test for disabling pitch")
	@Order(9)
	void testDisablePitch() {
		manipulator.disablePitchEffect();
		assertEquals(1, manipulator.getEffectValue("pitch"), "Pitch wasn't disabled succesfully");
	}
	
	/**
	 * Test for disabling gain effect
	 */
	@Test
	@DisplayName("Test for disabling gain effect")
	@Order(10)
	void testDisableGain() {
		manipulator.disableGainEffect();
		assertEquals(1, manipulator.getEffectValue("gain"), "Gain wasn't disabled succesfully");
	}
	
	/**
	 * Test for disabling delay effect
	 */
	@Test
	@DisplayName("Test for disabling delay effect")
	@Order(11)
	void testDisableDelay() {
		manipulator.disableDelayEffect();
		assertEquals(0, manipulator.getEffectValue("decay"), "Decay wasn't disabled succesfully");
		assertEquals(1, manipulator.getEffectValue("echoLength"), "Echo length wasn't disabled succesfully");
	}
	
	/**
	 * Test for disabling flanger effect
	 */
	@Test
	@DisplayName("Test for disabling flanger effect")
	@Order(12)
	void testDisableFlanger() {
		manipulator.disableFlangerEffect();
		assertEquals(0.01, manipulator.getEffectValue("flangerLength"), "Flanger lenght wasn't disabled succesfully");
		assertEquals(0, manipulator.getEffectValue("wetness"), "Wetness wasn't disabled succesfully");
		assertEquals(5, manipulator.getEffectValue("lfo"), "Lfo wasn't disabled succesfully");
	}
	
	/**
	 * Test for disabling lowpass effect
	 */
	@Test
	@DisplayName("Test for disabling lowpass effect")
	@Order(13)
	void testDisableLowPass() {
		manipulator.disableLowPassEffect();
		assertEquals(44100, manipulator.getEffectValue("lowPass"), "Low pass wasn't disabled succesfully");
	}
}
