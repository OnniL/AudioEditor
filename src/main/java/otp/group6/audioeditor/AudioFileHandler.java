package otp.group6.audioeditor;

import java.io.File;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 * @version 0.1
 *
 */

public class AudioFileHandler {

	// Constructor
	public AudioFileHandler() {

	}

	public static AudioInputStream OpenFile(String name) {
		AudioInputStream a = null;
		try {
			a = AudioSystem.getAudioInputStream(new File(name).getAbsoluteFile());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;

	}

	public static void CloseFile(AudioInputStream file) {
		try {
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void SaveFile(AudioInputStream audio, File targetFile, Type fileFormat) {
		try {
			AudioSystem.write(audio, fileFormat, targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// TODO Tiedostonimi tai Inputstreami!
	// Muista varmitaa lopullinen versio!
	// Muista varmistaa käyttäjältä ennen tiedoston poistoa!
	public static void DeleteFile(String name) {
		try {
			File tiedosto = new File(name).getAbsoluteFile();
			tiedosto.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// TODO Jokaiselle oikea tiedostomuoto, eli useampi metodi, yks per
	// tiedostomuoto.
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
