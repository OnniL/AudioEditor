package otp.group6.audioeditor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import otp.group6.audioeditor.Soundboard.Sample;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
/**
 * Test class for soundboard
 * 
 * @author Kevin Akkoyun
 *
 */
class SoundboardTest {
	private Soundboard soundboard = new Soundboard();

	@DisplayName("Trying to add a sample to sampleArray via file path")
	@Order(1)
	@Test
	final void testCreateSampleFPath() {

		int testSampleIndex = soundboard.addSample("src/audio/testiaani.wav");

		assertTrue(soundboard.checkSampleArray(testSampleIndex), "Checks if sample array contains added sample");
	}

	@DisplayName("Trying to add a sample to sampleArray via creating a new sample")
	@Order(2)
	@Test
	final void testCreateSampleNew() {

		Sample testSample = soundboard.new Sample("src/audio/testiaani.wav");

		int testSampleIndex = soundboard.addSample(testSample);

		assertTrue(soundboard.checkSampleArray(testSampleIndex), "Checks if sample array contains added sample");
	}

	@DisplayName("Removing a sample from sampleArray with an index")
	@Order(3)
	@Test
	final void testSampleRemovalWithIndex() {

		Sample newSample = soundboard.new Sample("src/audio/testiaani.wav");
		int testSampleIndex = soundboard.addSample(newSample);

		Sample removedSample = soundboard.removeSample(testSampleIndex);

		assertTrue(newSample == removedSample, "Comparing removed sample");
	}

	@DisplayName("Removing a Sample from sampleArray with a reference to a Sample")
	@Order(4)
	@Test
	final void testSampleRemovalWithSample() {

		Sample newSample = soundboard.new Sample("src/audio/testiaani.wav");
		soundboard.addSample(newSample);

		assertTrue(soundboard.removeSample(newSample), "Removing sample");
	}

	@DisplayName("Save sample data and read it")
	@Order(5)
	@Test

	final void testSaveSampleData() {
		for (int i = 0; i < 10; i++) {
			soundboard.addSample("src/audio/testiaani.wav");
		}
		assertEquals(10, soundboard.getSampleArrayLength(), "sampleArray didnt recieve all samples");
		soundboard.saveSampleData();
		
		for (int i = 9; i >= 0; i--) {
			soundboard.removeSample(i);
		}
		assertEquals(0, soundboard.getSampleArrayLength(), "sampleArray is not empty");
		assertEquals(10, soundboard.readSampleData(), "sampleArray didnt recieve as many samples as needed");
		
	}

	@BeforeEach
	final void checkIfArrayEmpty() {
		assertEquals(0, soundboard.getSampleArrayLength(), "sampleArray is not empty");
	}
}
