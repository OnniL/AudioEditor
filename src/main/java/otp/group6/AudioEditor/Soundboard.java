package otp.group6.AudioEditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineListener;

import otp.group6.AudioEditor.Soundboard.Sample;

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
			this.name = "New Sound(" + sampleArray.size() + ")";
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

	private String sampleData = "";

	private SoundboardPlayer player;

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
	 * Removes sample with a given index value from the sample array
	 * 
	 * @return if sample index is given, returns sample; else returns boolean if
	 *         given sample is removed
	 * @param sampleIndex
	 */
	public Sample removeSample(int sampleIndex) {
		return sampleArray.remove(sampleIndex);
		// update button positions
	}

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
	 * Shortens sampleArray until its size is 20
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
	 * Checks if sampleArray is oversized and reduces it to 20 if needed
	 * 
	 * @author Kevin Akkoyun
	 */
	public void readSampleData() {
		try {
			File targetFile = new File("sampledata.txt");
			Scanner fileReader = new Scanner(targetFile);

			while (fileReader.hasNextLine()) {
				String temp = fileReader.nextLine();
				String[] sampleParts = temp.split(";");
				File tester = new File(sampleParts[1]);
				if (tester.exists()) {
					sampleArray.add(new Sample(sampleParts[0], sampleParts[1]));
				}
				validateSampleArray();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
