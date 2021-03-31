package otp.group6.controller;

import javax.sound.sampled.LineListener;

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

	public void playSound(int index) {
		if (soundboard.checkSampleArray(index)) {
			soundboard.playSample(index);
		} else {
			System.out.println("index not found - sample array: " + index);
		}

	}

	public void stopSound() {
		soundboard.stopSample();
	}

	public boolean isPlaying() {
		return soundboard.isPlaying();
	}

	/**
	 * Adds sample to Soundboard SampleArray
	 * 
	 * @param path filepath
	 */
	public void addSample(String path) {
		try {
			soundboard.addSample(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editSample(String path, int index) {
		soundboard.editSample(path, index);
	}

	public void removeSample(int index) {
		if (soundboard.checkSampleArray(index)) {
			soundboard.removeSample(index);
		} else {
			System.out.println("index not found - sample array: " + index);
		}
	}

	public String getSampleName(int index) {
		return soundboard.getSampleName(index);
	}

	public void setSampleName(int index, String name) {
		soundboard.setSampleName(index, name);
	}

	public int getSampleArrayLength() {
		return soundboard.getSampleArrayLength();
	}

	public void saveSampleData() {
		soundboard.saveSampleData();
	}

	public void readSampleData() {
		soundboard.readSampleData();
	}

	public void clearSampleData() {
		soundboard.clearSampleData();
	}

	public void setListener(LineListener listener) {
		soundboard.setListener(listener);
	}

	public void removeListener(LineListener listener) {
		soundboard.removeListener(listener);
	}
}
