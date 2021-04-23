package otp.group6.audioeditor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import otp.group6.controller.Controller;

public class AudioManipulatorTest {

	private Controller controller = new Controller();
	private AudioManipulator manipulator = new AudioManipulator(controller);
	
	
	@BeforeEach
	public void setUp() {
		manipulator.setAudioSourceFile(new File("src/audio/mixer_default.wav"));
	}
	
	
	@Test
	@DisplayName ("Test for setting gain")
	@Order(1)
	void testSetGain() {
		manipulator.setGain(2.0);
		assertEquals(2.0, manipulator.getEffectValue("gain"), "Gain wasn't set succesfully");
	}
	
	@Test
	@DisplayName ("Test for setting pitch")
	@Order(2)
	void testSetPitch() {
		manipulator.setPitchFactor(2.4);
		assertEquals(2.4, manipulator.getEffectValue("pitch"), "Pitch wasn't set succesfully");
	}
	
	@Test
	@DisplayName ("Test for setting echo length")
	@Order(3)
	void testSetEcho() {
		manipulator.setEchoLength(1.4);
		assertEquals(1.4, manipulator.getEffectValue("echoLength"), "Pitch wasn't set succesfully");
	}
	
	@Test
	@DisplayName ("Test for setting decay")
	@Order(4)
	void testSetDecay() {
		manipulator.setDecay(3.1);
		assertEquals(3.1, manipulator.getEffectValue("decay"), "Decay wasn't set succesfully");
	}
	
	@Test
	@DisplayName ("Test for setting flanger effect")
	@Order(5)
	void testSetFlanger() {
		manipulator.setFlangerLength(0.2);
		assertEquals(0.2, manipulator.getEffectValue("flangerLength"), "flanger effect wasn't set succesfully");
	}
	
	@Test
	@DisplayName ("Test for setting wetness effect")
	@Order(6)
	void testSetWetness() {
		manipulator.setWetness(0.5);
		assertEquals(0.5, manipulator.getEffectValue("wetness"), "wetness wasn't set succesfully");
	}
	
	@Test
	@DisplayName ("Test for setting LFO")
	@Order(7)
	void testSetLFO() {
		manipulator.setLFO(0.7);
		assertEquals(0.7, manipulator.getEffectValue("lfo"), "LFO wasn't set succesfully");
	}
	
	@Test
	@DisplayName ("Test for setting LowPass")
	@Order(8)
	void testSetLowPass() {
		manipulator.setLowPass((float) 0.7);
		assertEquals((float) 0.7, manipulator.getEffectValue("lowPass"), "LowPass wasn't set succesfully");
	}
}
