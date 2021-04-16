package otp.group6.audioeditor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import otp.group6.controller.Controller;

/**
 * 
 * @author Onni Lukkarila
 *
 */
public class AudioRecorderTest {

	
	private Controller controller;
	private AudioRecorder recorder = new AudioRecorder(controller);
	private final static double DELTA = 0.001;
	File defaultAudioFile;
	
	
	
	@BeforeEach
	public void setUp() {
		recorder.setAudioFileDuration((float) 2.1);
		defaultAudioFile = new File("src/audio/recorder_default.wav").getAbsoluteFile();
	}
	
	@Test
	@DisplayName ("Test for getting the target file")
	@Order(1)
	void testGetTargetFile() {
		recorder.setTargetFile(new File("src/audio/default.wav").getAbsoluteFile());
		assertEquals(new File("src/audio/default.wav").getAbsoluteFile(), recorder.getTargetFile(), "Wrong target file");
	}
	
	@Test
	@DisplayName ("Test for getting duration of audio file")
	@Order(2)
	void testGetAudioFileDuration() {
		assertEquals(2.1, recorder.getAudioFileDuration(), DELTA,  "Setting audio file's duration wasn't succesful");
	}
	
	@Test
	@DisplayName ("Test for setting duration of audio file")
	@Order(3)
	void testSetAudioFileDuration() {
		recorder.setAudioFileDuration((float) 2.7);
		assertEquals(2.7, recorder.getAudioFileDuration(), DELTA,  "Setting audio file's duration wasn't succesful");
	}
	
	@Test
	@DisplayName ("Test for saving recorded file")
	@Order(4)
	void testSaveRecordedFile() {
		recorder.recordAudio();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		recorder.stopRecord();
		recorder.saveAudioFile("src/audio/testRecording.wav");
		assertEquals(defaultAudioFile.length(), new File("src/audio/testRecording.wav").getAbsoluteFile().length(), "Saving of audio file wasn't succesful");
	}
	
}
