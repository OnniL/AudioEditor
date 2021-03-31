package otp.group6.controller;

import otp.group6.AudioEditor.Soundboard;

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
}
