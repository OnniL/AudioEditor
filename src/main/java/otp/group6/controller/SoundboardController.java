package otp.group6.controller;

import java.io.File;

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

	private static Soundboard soundboard;

	public static SoundboardController instance = null;

	private SoundboardController() {
	}

	/**
	 * Returns an instance of this class
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

	public void playSample(int index) {

	}

	/**
	 * Controller method for addding samples to the soundboard
	 * 
	 * @param path - filepath for auddio file
	 * @return index of the added sample
	 */
	public int addSample(String filepath) {
		return soundboard.addSample(filepath);
	}

	/**
	 * Controller method for removing samples
	 * 
	 * @param index - index of the sample
	 * @return if successful, returns removed sample, otherwise returns null
	 */
	public Sample removeSample(int index) {
		return soundboard.removeSample(index);
	}
}
