package otp.group6.audioeditor;

import java.io.File;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 * Class for handling audio files and audio input streams
 * 
 * @author Kevin Akkoyun, Joonas Soininen, Roosa Laukkanen
 */
public class AudioFileHandler {

	public AudioFileHandler() {

	}

	/**
	 * Creates a new AudioInputStream using the given file path
	 * 
	 * @param name - file path
	 * @return audio input stream
	 */
	public static AudioInputStream OpenFile(String name) {
		AudioInputStream a = null;
		try {
			a = AudioSystem.getAudioInputStream(new File(name).getAbsoluteFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;

	}

	/**
	 * Closes the specified file
	 * 
	 * @param file to be closed
	 */
	public static void CloseFile(AudioInputStream file) {
		try {
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves audio input stream to specified file
	 * 
	 * @param audio      - audio input stream to be saved
	 * @param targetFile - file where the audio input stream will be saved
	 * @param fileFormat - format in which the audio input stream will be saved
	 */
	public static void SaveFile(AudioInputStream audio, File targetFile, Type fileFormat) {
		try {
			AudioSystem.write(audio, fileFormat, targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Deletes the specified file
	 * 
	 * @param path to the file to be deleted
	 */
	public static void DeleteFile(String name) {
		try {
			File tiedosto = new File(name).getAbsoluteFile();
			tiedosto.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates and opens a file chooser
	 * 
	 * @param window - window where the file chooser will be opened in
	 * @return chosen file
	 */
	public static File openFileExplorer(Window window) {
		FileChooser fc = new FileChooser();
		File file = fc.showOpenDialog(window);
		return file;

	}

	/*
	 * @author Roosa Laukkanen Opens the file choosers and shows all WAV files in
	 * user's music folder
	 * 
	 * @return returns the chosen WAV file
	 */
	public static File openWavFileExplorer(Window window) {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("Wav files", "*.wav");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + File.separator + "Music"));
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showOpenDialog(window);
		return file;

	}

}
