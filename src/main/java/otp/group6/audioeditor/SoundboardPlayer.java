package otp.group6.audioeditor;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

/**
 * Audio player class for soundboard
 * 
 * @author Kevin Akkoyun
 *
 */
public class SoundboardPlayer {
	/**
	 * a {@link Clip} object for audio playback
	 */
	private Clip clip;

	public SoundboardPlayer() {
		try {
			clip = AudioSystem.getClip();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Plays audio with given {@link AudioInputStream}
	 * 
	 * @param sample AudioInputStream of file to be played
	 */
	public void playAudio(AudioInputStream sample) {
		closeAudio();
		try {
			clip.open(sample);
			clip.loop(0);
			clip.start();
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes {@link #clip} if its open
	 */
	public void closeAudio() {
		if (clip.isOpen()) {
			clip.close();
		}
	}

	/**
	 * Checks if the clip is active
	 * 
	 * @return Returns <b>true</b> if {@link #clip} is active
	 */
	public boolean isActive() {
		return clip.isActive();
	}

	/**
	 * Adds a {@link LineListener} to the {@link #clip}
	 * 
	 * @param listener - <b>LineListener</b>
	 */
	public void setListener(LineListener listener) {
		clip.addLineListener(listener);
	}

	/**
	 * Removes a {@link LineListener} from the {@link #clip}
	 * 
	 * @param listener - <b>LineListener</b>
	 */
	public void removeListeners(LineListener listener) {
		clip.removeLineListener(listener);
	}
}