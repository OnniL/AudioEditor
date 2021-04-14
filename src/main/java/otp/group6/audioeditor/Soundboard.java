package otp.group6.audioeditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineListener;

/**
 * Soundboard class for storing and playing sampled audio
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 * @version 0.1
 */
public class Soundboard {
	/**
	 * Class for handling playable audio files as objects
	 * 
	 * @author Kevin Akkoyun, Joonas Soininen
	 * @version 0.1
	 */
	public class Sample {

		private AudioInputStream file;
		private String filepath;
		private String name;

		/**
		 * 
		 * TODO lisää error handling
		 */
		public Sample(String filepath) {
			this.filepath = filepath;
			this.name = DEFAULT_NAME + "(" + sampleArray.size() + ")";
		}

		public Sample(String name, String filepath) {
			this.filepath = filepath;
			this.name = name;
		}

		public void setSamplePath(String filepath) {
			this.filepath = filepath;
		}

		public String getSamplePath() {
			return this.filepath;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		/**
		 * Uses AudioFileHandler to open a new audio file into AudioInputStream Closes
		 * existing AudioInputStream
		 * 
		 * @return New AudioInputStream with samples specified file path
		 */
		public AudioInputStream getSample() {
			try {
				if (file != null) {
					file.close();
				}
				file = AudioFileHandler.OpenFile(filepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return file;
		}
	}
	/**
	 * Localization name variable
	 */
	private String DEFAULT_NAME;

	private String sampleData = "";

	private SoundboardPlayer player;
	/**
	 * an <b>ArrayList</b> that contains all Sample data <br>
	 * @see {@link Sample}
	 */
	private ArrayList<Sample> sampleArray = new ArrayList<Sample>();

	/**
	 * Checks if sampleArray contains specified sample
	 * 
	 * @param sampleIndex index of the sample
	 * @return returns true if array contains a sample with given index
	 */
	public boolean checkSampleArray(int sampleIndex) {
		if (sampleArray.get(sampleIndex) != null) {
			return true;
		}
		return false;
	}

	public boolean checkSampleArray(Sample sample) {
		return sampleArray.contains(sample);
	}

	/**
	 * Returns the length of sample array
	 * 
	 * @return length of the array as integer
	 */
	public int getSampleArrayLength() {
		return sampleArray.size();
	}

	public Soundboard() {
		player = new SoundboardPlayer();
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("properties/Soundboard", new Locale("fi","FI"));
			DEFAULT_NAME = bundle.getString("DEFAULT_NAME");
		}catch(MissingResourceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a sample to {@link #sampleArray}
	 * 
	 * @param path - file path as String
	 * @return index of the sample
	 * @see Sample
	 */
	public int addSample(String path) {
		Sample newSample = new Sample(path);
		sampleArray.add(newSample);
		return sampleArray.indexOf(newSample);
	}

	/**
	 * Adds a sample to {@link #sampleArray}
	 * 
	 * @param sample - sample to be added
	 * @return index of the sample
	 * @see Sample
	 */
	public int addSample(Sample sample) {
		sampleArray.add(sample);
		return sampleArray.indexOf(sample);
	}

	/**
	 * Adds a sample to {@link #sampleArray}
	 * 
	 * @param file - audio file
	 * @return index of the sample
	 * @see Sample
	 */
	public int addSample(File file) {
		Sample newSample = new Sample(file.getAbsolutePath());
		sampleArray.add(newSample);
		return sampleArray.indexOf(newSample);
	}

	public Boolean setSampleFilepath(String path, int index) {
		try {
			sampleArray.get(index).setSamplePath(path);
			return true;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getSampleFilepath(int index) {
		return sampleArray.get(index).getSamplePath();
	}

	public String getSampleName(int index) {
		return sampleArray.get(index).getName();
	}

	public Boolean setSampleName(String name, int index) {
		try {
			sampleArray.get(index).setName(name);
			return true;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Removes {@link Sample} with a given index value from the {@link sampleArray}
	 * @param sampleIndex - index of the Sample
	 * @return returns removed <b>Sample</b>
	 */
	public Sample removeSample(int sampleIndex) {
		return sampleArray.remove(sampleIndex);
		// update button positions
	}
	/**
	 * Removes referenced {@link Sample} if it exists in the {@link sampleArray}
	 * @param sample - Sample to be <b>removed</b>
	 * @return returns <b>true</b> if operation was successful
	 */
	public boolean removeSample(Sample sample) {
		return sampleArray.remove(sample);
	}

	/**
	 * Plays a sample with SoundboardPlayer
	 * 
	 * @param sampleIndex
	 */
	public void playSample(int sampleIndex) {
		try {
			player.playAudio(sampleArray.get(sampleIndex).getSample());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops the sample output and closes audio
	 * 
	 * @author Kevin Akkoyun
	 */
	public void stopSample() {
		if (isPlaying()) {
			player.closeAudio();
		}
	}

	/**
	 * Method to check if player is active
	 * 
	 * @author Kevin Akkoyun
	 * @return returns true if player is active; otherwise returns false if player
	 *         is null or not playing
	 */
	public boolean isPlaying() {
		if (player == null || !player.isAlive()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Shortens {@link sampleArray} until its size is <b>20</b>
	 * 
	 * @author Kevin Akkoyun
	 */
	public void validateSampleArray() {
		while (sampleArray.size() > 20) {
			sampleArray.remove(sampleArray.size() - 1);
		}
	}

	/**
	 * Saves sample data to a text file<br>
	 * Uses <b>;</b> to separate sample name and path.
	 * 
	 * @author Kevin Akkoyun
	 */
	public void saveSampleData() {

		try {
			File targetFile = new File("sampledata.txt");
			targetFile.createNewFile();

			FileWriter dataWriter = new FileWriter(targetFile);
			sampleArray.forEach((sample) -> {
				sampleData = sampleData.concat(sample.getName() + ";" + sample.getSamplePath() + "\n");
			});
			dataWriter.write(sampleData);
			dataWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Reads sample data from text file and adds valid lines to sample array <br>
	 * Checks if sampleArray is <b>oversized</b> and reduces it to 20 if needed
	 * TODO FIX ERROR WHEN DATA DOES NOT EXIST
	 * @author Kevin Akkoyun
	 * @return Returns the size of {@link #sampleArray}
	 */
	public int readSampleData() {
		try {
			File targetFile = new File("sampledata.txt");
			Scanner fileReader = new Scanner(targetFile);
			
			while (fileReader.hasNextLine()) {
				try {
					String temp = fileReader.nextLine();
					String[] sampleParts = temp.split(";");
					File tester = new File(sampleParts[1]);
					if (tester.exists()) {
						sampleArray.add(new Sample(sampleParts[0], sampleParts[1]));
					}
					validateSampleArray();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sampleArray.size();
	}

	/**
	 * Clears sampleData and sampleArray <br>
	 * Writes blank to sampledata.txt
	 * 
	 * @author Kevin Akkoyun
	 */
	public void clearSampleData() {
		try {
			File targetFile = new File("sampledata.txt");
			FileWriter writer = new FileWriter(targetFile);
			writer.write("");
			writer.close();
			sampleArray = new ArrayList<Sample>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setListener(LineListener listener) {
		player.setOnClose(listener);
	}

	public void removeListener(LineListener listener) {
		player.removeListeners(listener);
	}
}