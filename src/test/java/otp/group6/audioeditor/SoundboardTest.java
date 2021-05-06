package otp.group6.audioeditor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Disabled;

import otp.group6.audioeditor.Soundboard.Sample;


/**
 * Test class for soundboard
 * 
 * @author Kevin Akkoyun
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SoundboardTest {
	private Soundboard soundboard = new Soundboard();
	
	/**
	 * Adds a file path to a fixed test audio into the {@link Soundboard} <br>
	 * Checks if the given file path exists as a {@link Sample} in the <b>sampleArray</b>
	 */
	@DisplayName("Trying to add a sample to sampleArray via file path")
	@Order(1)
	@Test
	final void testCreateSampleFPath() {

		int testSampleIndex = soundboard.addSample("src/audio/testiaani.wav");

		assertTrue(soundboard.checkSampleArray(testSampleIndex), "Checks if sample array contains added sample");
	}
	
	/**
	 * Creates a {@link Sample} from a fixed test audio file <br>
	 * Checks if the created <b>Sample</b> is added to the <b>sampleArray</b>
	 */
	@DisplayName("Trying to add a sample to sampleArray via creating a new sample")
	@Order(2)
	@Test
	final void testCreateSampleNew() {

		Sample testSample = soundboard.new Sample("src/audio/testiaani.wav");

		int testSampleIndex = soundboard.addSample(testSample);

		assertTrue(soundboard.checkSampleArray(testSampleIndex), "Checks if sample array contains added sample");
	}
	/**
	 * Creates a new {@link Sample} from a file path. <br>
	 * Attempts to remove it from the <b>sampleArray</b> with given index
	 */
	@DisplayName("Removing a sample from sampleArray with an index")
	@Order(3)
	@Test
	final void testSampleRemovalWithIndex() {

		Sample newSample = soundboard.new Sample("src/audio/testiaani.wav");
		int testSampleIndex = soundboard.addSample(newSample);

		Sample removedSample = soundboard.removeSample(testSampleIndex);

		assertTrue(newSample == removedSample, "Comparing removed sample");
	}
	/**
	 *  Creates a new {@link Sample} from a file path. <br>
	 *  Attempt to delete created <b>Sample</b> with reference to itself.
	 */
	@DisplayName("Removing a Sample from sampleArray with a reference to a Sample")
	@Order(4)
	@Test
	final void testSampleRemovalWithSample() {

		Sample newSample = soundboard.new Sample("src/audio/testiaani.wav");
		soundboard.addSample(newSample);

		assertTrue(soundboard.removeSample(newSample), "Removing sample");
	}
	
	/**
	 * Creates 10 identical <b>Samples</b> and writes them to sampledata.txt <br>
	 * Checks if the file contains written data.
	 */
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
	/**
	 * Creates a new {@link Sample} and adds it to <b>sampleArray</b>. <br>
	 * Checks if <b>Samples</b> initial file path matches the one from <b>sampleArray</b>
	 */
	@DisplayName("Check if sample file exits")
	@Order(6)
	@Test
	final void testIfSampleExist() {
		int index = soundboard.addSample("src/audio/testiaani.wav");
		String filepath = soundboard.getSampleFilepath(index);
		
		assertEquals("src/audio/testiaani.wav", filepath, "Sample filename and default filename didn't match");
		
		File sampleFile = new File(filepath);
		assertTrue(sampleFile.exists(), "Sample file does not exist");
		
	}
	/**
	 * Writes a single line to <b>sampledata.txt</b> and reads it. <br>
	 * Attempts to create {@link Sample} from the read data.
	 */
	@DisplayName("Test sample data reading")
	@Order(7)
	@Test
	final void testSampleDataReading() {
		File saveFile = new File("sampledata.txt");
		assertTrue(saveFile.exists(), "sampledata.txt does not exist or has been moved from root directory");
		try {
			FileWriter fw = new FileWriter(saveFile);
			fw.write("NewSound(0);src/audio/testiaani.wav");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Scanner sc = new Scanner(saveFile);
			String filepath = sc.nextLine();
			sc.close();
			filepath = filepath.split(";")[1];
			assertEquals("src/audio/testiaani.wav", filepath, "filepath was different from default");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Writes 25 lines to <b>sampledata.txt</b> and reads it. <br>
	 * Creates <b>Samples</b> from read data. Checks if <b>sampleArray</b> handles overflow
	 */
	@DisplayName("Test overflowing sampleArray and error handling")
	@Order(8)
	@Test
	final void testSampleArrayOverflow() {
		for(int i = 0; i < 25; i++) {
			soundboard.addSample("src/audio/testiaani.wav");
		}
		assertNotEquals(20, soundboard.getSampleArrayLength(), "sample array should not trim to 20 without specific call");
		soundboard.validateSampleArray();
		assertEquals(20, soundboard.getSampleArrayLength(), "sample array failed to trim to 20");
	}
	/**
	 * Checks if <b>sampleArray</b> is empty.
	 */
	@BeforeEach
	final void checkIfArrayEmpty() {
		assertEquals(0, soundboard.getSampleArrayLength(), "sampleArray is not empty");
	}
}
