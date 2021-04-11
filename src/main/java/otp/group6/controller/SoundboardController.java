package otp.group6.controller;

import java.io.File;

import javax.sound.sampled.LineListener;

import otp.group6.AudioEditor.Soundboard;
import otp.group6.AudioEditor.Soundboard.Sample;

/**
 * Controller for soundboard features
 * 
 * @author Kevin Akkoyun
 * @Version 1.1 - in progress TODO
 *
 */
public class SoundboardController {
	
	/**
	 * Used to determine the type of the string input
	 * @author Kevin Akkoyun
	 *
	 */
	public enum INPUT_TYPE {
		FILEPATH, SAMPLE_NAME
	}

	private static Soundboard soundboard;

	public static SoundboardController instance = null;

	private SoundboardController() {
	}

	/**
	 * Returns an instance of {@link SoundboardController}
	 * 
	 * @return instance of SoundboardController
	 */
	public static SoundboardController getInstance() {
		if (instance == null) {
			soundboard = new Soundboard();
			instance = new SoundboardController();
		}
		return instance;
	}

	/**
	 * Uses {@link Soundboard} to play Sample with given index. <br>
	 * If audio is already playing, calls <b>Soundboard</b> to stop sample.
	 * 
	 * @param index - index of the Sample
	 */
	public void playSample(int index) {
		if (soundboard.isPlaying()) {
			soundboard.stopSample();
		} else {
			soundboard.playSample(index);
		}

	}
	/**
	 * Uses {@link Soundboard} to play Sample with given index. <br>
	 * Plays audio <b>instantly</b>, closing already playing sample in the process
	 * .
	 * @param index - index of the Sample
	 */
	public void playSampleInstantly(int index) {
		soundboard.stopSample();
		soundboard.playSample(index);
	}

	// ######################################### Soundboard data manipulation
	// ##################################################

	/**
	 * Controller method for adding samples to the {@link Soundboard}
	 * 
	 * @param path - filepath for audio file
	 * @return index of the added sample
	 */
	public int addSample(String filepath) {
		return soundboard.addSample(filepath);
	}

	/**
	 * Controller method for removing samples
	 * 
	 * @param index - index of the <b>sample</b>
	 * @return if successful, returns removed sample, otherwise returns null
	 * @see {@link Sample}
	 */
	public Sample removeSample(int index) {
		return soundboard.removeSample(index);
	}

	/**
	 * Edits the sample with given index. <br>
	 * Depending on input <b>type</b>, changes the samples <u>filepath or name</u>
	 * @param index - index of the sample.
	 * @param input - either name or filepath for the sample
	 * @param type - type of the given string input. Either SAMPLE_NAME or FILEPATH
	 * @return Returns true if operation is successful, otherwise returns false.
	 */
	public Boolean editSample(int index, String input, INPUT_TYPE type) {
		switch(type) {
		case FILEPATH:
			return soundboard.setSampleFilepath(input, index);
		case SAMPLE_NAME:
			return soundboard.setSampleName(input, index);
		}
		return false;
	}
	/**
	 * Compares Sample with given index and input<br>
	 * <b>INPUT_TYPE</b> determines compare type <br>
	 * <b>INPUT_TYPE.FILEPATH</b> or <b>INPUT_TYPE.SAMPLE_NAME</b>
	 * @param index - index of the target sample
	 * @param input - input in string form
	 * @param type - determines compare operation
	 * @return returns <b>true</b> if the sample and input match
	 */
	public Boolean compareSamples(int index, String input, INPUT_TYPE type) {
		switch(type) {
		case FILEPATH:
			return input.equals(soundboard.getSampleFilepath(index));
		case SAMPLE_NAME:
			return input.equals(soundboard.getSampleName(index));
		}
		return false;
	}
	/**
	 * Gets the sample name with given index
	 * @param index - index of the sample
	 * @return returns sample name if it exists, otherwise returns <b>null</b>
	 */
	public String getSampleName(int index) {
		return soundboard.getSampleName(index);
	}
	/**
	 * Clears sampledata.txt and {@link Soundboard#sampleArray}
	 */
	public void clearSampleArray() {
		soundboard.clearSampleData();
	}
	/**
	 * Writes all {@link Soundboard#sampleArray} contents to sampledata.txt
	 */
	public void saveSampleData() {
		soundboard.saveSampleData();
	}
	/**
	 * Reads all data from sampledata.txt and inserts valid samples to {@link Soundboard#sampleArray}
	 */
	public int initializeSoundboard() {
		return soundboard.readSampleData();
	}
	public void setListener(LineListener listener) {
		soundboard.setListener(listener);
	}
}
