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
 * @author Kevin Akkoyun
 * @version 0.1
 */
public class Soundboard {
	/**
	 * Class for handling playable audio files as objects
	 * 
	 * @author Kevin Akkoyun
	 * @version 0.1
	 */
	public class Sample {
		/**
		 * {@link AudioInputStream} object for audio playback
		 */
		private AudioInputStream file;
		/**
		 * Filepath of the <b>Sample</b>
		 */
		private String filepath;
		/**
		 * Name of the <b>Sample</b>
		 */
		private String name;

		/**
		 * Creates a new {@link Sample} with given <b>Filepath</b><br>
		 * if no <b>Name</b> is given, sets <u>New Sample(*)</u> as default
		 * 
		 * @param filepath - Filepath in <b>String</b> format
		 */
		public Sample(String filepath) {
			this.filepath = filepath;
			this.name = DEFAULT_NAME + "(" + sampleArray.size() + ")";
		}

		/**
		 * Creates a new {@link Sample} with given <b>Filepath</b><br>
		 * if no <b>Name</b> is given, sets <u>New Sample(*)</u> as default
		 * 
		 * @param filepath - Filepath in <b>String</b> format
		 * @param name     - Name in <b>String</b> format
		 */
		public Sample(String name, String filepath) {
			this.filepath = filepath;
			this.name = name;
		}

		/**
		 * Sets the sample <b>Filepath</b>
		 * 
		 * @param filepath - Filepath in <b>String</b> format
		 */
		public void setSamplePath(String filepath) {
			this.filepath = filepath;
		}

		/**
		 * Returns <b>Sample</b> filepath
		 * 
		 * @return sample <b>Filepath</b>
		 */
		public String getSamplePath() {
			return this.filepath;
		}

		/**
		 * Sets the sample <b>Name</b>
		 * 
		 * @param name - new name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Returns <b>Sample</b> name
		 * 
		 * @return sample name in <b>String</b> format
		 */
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
	private String DEFAULT_NAME = "New Sample";
	/**
	 * Sample data string for saving
	 */
	private String sampleData = "";
	/**
	 * Audio player object
	 */
	private SoundboardPlayer player;
	/**
	 * an <b>ArrayList</b> that contains all Sample data <br>
	 * 
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

	/**
	 * Checks if {@link #sampleArray} contains given <b>Sample</b><br>
	 * 
	 * @param sample - a {@link Sample} object
	 * @return Returns true if <b>sampleArray</b> contains given sample
	 */
	public boolean checkSampleArray(Sample sample) {
		return sampleArray.contains(sample);
	}

	/**
	 * Returns the length of {@link #sampleArray}
	 * 
	 * @return length of the array as integer
	 */
	public int getSampleArrayLength() {
		return sampleArray.size();
	}

	/**
	 * Initializes {@link Soundboard}
	 */
	public Soundboard() {
		player = new SoundboardPlayer();
	}
	
	/**
	 * Sets the <b>default</b> Sample name
	 * @param dName - name in <b>String</b> format
	 */
	public void setDefaultSampleName(String dName) {
		DEFAULT_NAME = dName;
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

	/**
	 * Sets a new filepath to the {@link Sample} with a given <b>index</b>
	 * 
	 * @param path  - a new filepath in <b>String</b> format.
	 * @param index - index of the <b>Sample</b>
	 * @return Returns true if the operation was successful.
	 */
	public Boolean setSampleFilepath(String path, int index) {
		try {
			sampleArray.get(index).setSamplePath(path);
			return true;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Gets a {@link Sample} filepath from the {@link #sampleArray}
	 * 
	 * @param index - index of the <b>Sample</b>
	 * @return Returns the <b>filepath</b> of the sample if it exists
	 */
	public String getSampleFilepath(int index) {
		return sampleArray.get(index).getSamplePath();
	}

	/**
	 * Gets a {@link Sample} name from the {@link #sampleArray}
	 * 
	 * @param index - index of the <b>Sample</b>
	 * @return Returns the <b>name</b> of the sample if it exists
	 */
	public String getSampleName(int index) {
		return sampleArray.get(index).getName();
	}

	/**
	 * Sets a new name to the {@link Sample} with a given <b>index</b>
	 * 
	 * @param name  - a new name in <b>String</b> format.
	 * @param index - index of the <b>Sample</b>
	 * @return Returns true if the operation was successful.
	 */
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
	 * Removes {@link Sample} with a given index value from the {@link #sampleArray}
	 * 
	 * @param sampleIndex - index of the Sample
	 * @return returns removed <b>Sample</b>
	 */
	public Sample removeSample(int sampleIndex) {
		return sampleArray.remove(sampleIndex);
		// update button positions
	}

	/**
	 * Removes referenced {@link Sample} if it exists in the {@link #sampleArray}
	 * 
	 * @param sample - Sample to be <b>removed</b>
	 * @return returns <b>true</b> if operation was successful
	 */
	public boolean removeSample(Sample sample) {
		return sampleArray.remove(sample);
	}

	/**
	 * Plays a sample with {@link SoundboardPlayer}
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
	 * Stops the {@link SoundboardPlayer} output and closes audio
	 * 
	 */
	public void stopSample() {
		if (isPlaying()) {
			player.closeAudio();
		}
	}

	/**
	 * Method to check if {@link SoundboardPlayer} is active
	 * 
	 * @return returns true if player is active; otherwise returns false if player
	 *         is null or not playing
	 */
	public boolean isPlaying() {
		if (player == null || !player.isActive()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Shortens {@link #sampleArray} until its size is <b>20</b>
	 */
	public void validateSampleArray() {
		while (sampleArray.size() > 20) {
			sampleArray.remove(sampleArray.size() - 1);
		}
	}

	/**
	 * Saves {@link Sample} data to a <b>text file</b><br>
	 * Uses <b>;</b> to separate sample name and path.
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
	 * Reads {@link Sample} data from text file and adds valid lines to
	 * {@link #sampleArray} <br>
	 * Checks if <b>sampleArray</b> is <u>oversized</u> and reduces it to 20 if
	 * needed <br>
	 * 
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
					File tester;
					if(sampleParts.length == 2) {
						tester = new File(sampleParts[1]);
					}else {
						tester = null;
					}
					if (tester != null && tester.exists()) {
						sampleArray.add(new Sample(sampleParts[0], sampleParts[1]));
					}
					validateSampleArray();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sampleArray.size();
	}

	/**
	 * Clears {@link #sampleData} and {@link #sampleArray} <br>
	 * Writes blank to <b>sampledata.txt</b>
	 */
	public void clearSampleData() {
		try {
			File targetFile = new File("sampledata.txt");
			FileWriter writer = new FileWriter(targetFile);
			writer.write("");
			writer.close();
			sampleArray = new ArrayList<Sample>();
			sampleData = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets a {@link LineListener} to the {@link SounboardPlayer}
	 * 
	 * @param listener - <b>LineListener</b>
	 */
	public void setListener(LineListener listener) {
		player.setListener(listener);
	}

	/**
	 * Removes a {@link LineListener} from the {@link SounboardPlayer}
	 * 
	 * @param listener - <b>LineListener</b>
	 */
	public void removeListener(LineListener listener) {
		player.removeListeners(listener);
	}
}
