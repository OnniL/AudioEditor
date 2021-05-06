package otp.group6.audioeditor;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.GainProcessor;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.effects.FlangerEffect;
import be.tarsos.dsp.filters.LowPassSP;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import be.tarsos.dsp.resample.RateTransposer;
import otp.group6.controller.Controller;

/**
 * AudioManipulator is a class that is used to create and operate the mixer.
 * Class includes methods for manipulating WAV files with five different
 * effects. Class also includes general audio player methods for audio playback.
 * 
 * @author Roosa Laukkanen, Onni Lukkarila
 */
public class AudioManipulator {

	private Controller controller;

	private File file;
	private TargetDataLine line;
	private AudioDispatcher adp;
	private AudioDispatcher liveDispatcher;
	private AudioInputStream audioInputStream;
	private JVMAudioInputStream audioInputStreamForTarsos;
	private AudioFormat format;
	private TarsosDSPAudioFormat tarsosFormat;
	private AudioPlayer audioPlayer;
	private WaveformWriter writer;
	private WaveformSimilarityBasedOverlapAdd wsola;

	private GainProcessor gainProcessor;
	private RateTransposer rateTransposer;
	private FlangerEffect flangerEffect;
	private DelayEffect delayEffect;
	private LowPassSP lowPassSP;

	private Timer timer;
	private TimerTask task;

	// Starting values for variables that change when user changes mixer values
	private float sampleRate;
	private double pitchFactor = 1;
	private double gain = 1;
	private double echoLength = 1;
	private double decay = 0;
	private double flangerLength = 0.01;
	private double wetness = 0;
	private double lfo = 5;
	private float lowPass = 44100;

	// Original values causing no effect to audio
	private final double ogPitchFactor = 1;
	private final double ogGain = 1;
	private final double ogEchoLength = 1;
	private final double ogDecay = 0;
	private final double ogFlangerLength = 0.01;
	private final double ogWetness = 0;
	private final double ogLfo = 5;
	private final float ogLowPass = 44100;

	private double audioFileLengthInSec;
	private float playbackStartingPoint = (float) 0.0;
	private float currentProgress;
	private boolean isPlaying = false;
	private boolean isTestingFilter = false;

	private boolean isPitchWanted = true;
	private boolean isDelayWanted = true;
	private boolean isGainWanted = true;
	private boolean isFlangerWanted = true;
	private boolean isLowPassWanted = true;

	private boolean isSaved = true;
	private boolean isRecordedFile = false;

	/**
	 * Creates a new AudioManipulator. Constructor requires the controller from MVC
	 * pattern as its parameter.
	 * 
	 * @param controller
	 */
	public AudioManipulator(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Checks if there is an unsaved file in AudioManipulator.
	 * 
	 * @return <b>true</b> if there is an unsaved file and <b>false</b> if there
	 *         isn't an unsaved file
	 * 
	 */
	public boolean checkIfUnsavedMixedFile() {
		// If any of the effect values differ from the original values and the file is
		// not
		// saved -> true
		if (pitchFactor != ogPitchFactor && isSaved == false || echoLength != ogEchoLength && isSaved == false
				|| decay != ogDecay && isSaved == false || lowPass != ogLowPass && isSaved == false
				|| wetness != ogWetness && isSaved == false || flangerLength != ogFlangerLength && isSaved == false
				|| lfo != ogLfo && isSaved == false || gain != ogGain && isSaved == false) {
			return true;
		} else if (isRecordedFile == true && isSaved == false) { // If unsaved recorded file -> true
			return true;
		} else {
			return false;
		}
	}

	/******* AUDIOMANIPULATOR GENERAL METHODS ********/

	/**
	 * Creates an {@link AudioDispatcher} that uses given file as its audio source.
	 * Creates {@link WaveformSimilarityBasedOverlapAdd}, {@link RateTransposer},
	 * {@link DelayEffect}, {@link GainProcessor}, {@link FlangerEffect} and
	 * {@link LowPassSP} and connects them to AudioDispatcher.
	 * 
	 * @param file - WAV file
	 */
	public void setAudioSourceFile(File file) {
		try {
			this.file = file.getAbsoluteFile();
			audioInputStream = AudioSystem.getAudioInputStream(file);

			// Converts AudioInputStream to Tarsos compatible JVMAudioInputSteam
			audioInputStreamForTarsos = new JVMAudioInputStream(
					AudioSystem.getAudioInputStream(getAudioFormat(), audioInputStream));

			this.tarsosFormat = audioInputStreamForTarsos.getFormat();
			this.sampleRate = tarsosFormat.getSampleRate();

			wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(pitchFactor, sampleRate));
			adp = new AudioDispatcher(audioInputStreamForTarsos, wsola.getInputBufferSize(), wsola.getOverlap());

			wsola.setDispatcher(adp);
			adp.addAudioProcessor(wsola);

			rateTransposer = new RateTransposer(pitchFactor);
			adp.addAudioProcessor(rateTransposer);

			delayEffect = new DelayEffect(echoLength, decay, sampleRate);
			adp.addAudioProcessor(delayEffect);

			gainProcessor = new GainProcessor(gain);
			adp.addAudioProcessor(gainProcessor);

			flangerEffect = new FlangerEffect(flangerLength, wetness, sampleRate, (lfo));
			adp.addAudioProcessor(flangerEffect);

			lowPassSP = new LowPassSP(lowPass, sampleRate);
			adp.addAudioProcessor(lowPassSP);

			/*
			 * Only if the file being used is a recording, isSaved is false. This is to
			 * prevent the user from exiting the application without saving the recorded
			 * file
			 */
			if (file.getAbsoluteFile().getName().equals("mixer_default.wav")) {
				// isSaved = false;
				isRecordedFile = true;
				isSaved = false;
			} else {
				isSaved = true;
				isRecordedFile = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Records audio file using default input data line
	 */
	public void recordAudio() {
		try {
			format = getAudioFormat();
			writer = new WaveformWriter(format, "src/audio/mixer_default.wav");

			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();

			audioInputStream = new AudioInputStream(line);
			audioInputStreamForTarsos = new JVMAudioInputStream(audioInputStream);

			wsola = new WaveformSimilarityBasedOverlapAdd(
					Parameters.musicDefaults(pitchFactor, format.getSampleRate()));
			adp = new AudioDispatcher(audioInputStreamForTarsos, wsola.getInputBufferSize(), wsola.getOverlap());
			adp.addAudioProcessor(writer);
			Thread t = new Thread(adp);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Stops recording
	 */
	public void stopRecord() {
		adp.stop();
		controller.audioManipulatorOpenRecordedFile();
	}

	/**
	 * Method for testing AudioManipulator effects without audio file source.
	 * Creates a new {@link AudioDispatcher} and audio effects using default input
	 * data line as its source.
	 */
	public void testFilter() {
		// Stop liveDispatcher if playing and disable mixer sliders
		if (isTestingFilter == true) {
			liveDispatcher.stop();
			isTestingFilter = false;
			if (file == null) {
				controller.setDisableMixerSliders(true);
			}

		} else { // Start liveDispatcher and enable mixer sliders
			isTestingFilter = true;
			AudioFormat format2 = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format2);
			TargetDataLine line = null;
			try {
				line = (TargetDataLine) AudioSystem.getLine(info);
				line.open(format2);
				line.start();
				AudioInputStream ais = new AudioInputStream(line);
				JVMAudioInputStream audioStream = new JVMAudioInputStream(ais);
				//
				// formaatti ja sampleRate instanssimuuttujiin
				this.tarsosFormat = audioStream.getFormat();
				this.sampleRate = tarsosFormat.getSampleRate();

				wsola = new WaveformSimilarityBasedOverlapAdd(
						Parameters.musicDefaults(pitchFactor, audioStream.getFormat().getSampleRate()));

				liveDispatcher = new AudioDispatcher(audioStream, wsola.getInputBufferSize(), wsola.getOverlap());
				wsola.setDispatcher(liveDispatcher);
				liveDispatcher.addAudioProcessor(wsola);

				rateTransposer = new RateTransposer(pitchFactor);
				delayEffect = new DelayEffect(echoLength, decay, sampleRate);
				gainProcessor = new GainProcessor(gain);
				flangerEffect = new FlangerEffect(flangerLength, wetness, sampleRate, lfo);
				lowPassSP = new LowPassSP(lowPass, sampleRate);
				audioPlayer = new AudioPlayer(tarsosFormat);

				liveDispatcher.addAudioProcessor(rateTransposer);
				liveDispatcher.addAudioProcessor(delayEffect);
				liveDispatcher.addAudioProcessor(gainProcessor);
				liveDispatcher.addAudioProcessor(flangerEffect);
				liveDispatcher.addAudioProcessor(lowPassSP);
				liveDispatcher.addAudioProcessor(audioPlayer);
				controller.setDisableMixerSliders(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread t = new Thread(liveDispatcher);
				t.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Saves AudioManipulator's mixed file to the path given as a parameter
	 * 
	 * @param path - Path where the file is to be saved
	 * @return true if saving was succesful, false if not
	 */
	public boolean saveFile(String path) {
		createAudioProcessors();
		writer = new WaveformWriter(tarsosFormat, path);
		adp.removeAudioProcessor(audioPlayer);
		adp.addAudioProcessor(writer);
		try {
			Thread t = new Thread(adp);
			t.start();
			while (t.isAlive()) {
				Thread.sleep(100);
			}
			isSaved = true;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Creates following audio processors:
	 * {@link WaveformSimilarityBasedOverlapAdd}, {@link RateTransposer},
	 * {@link DelayEffect}, {@link GainProcessor}, {@link FlangerEffect},
	 * {@link LowPassSP} and {@link AudioPlayer}
	 */
	private void createAudioProcessors() {
		try {
			if (isPitchWanted == true) {
				wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(pitchFactor, sampleRate));
			} else {
				wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(ogPitchFactor, sampleRate));
			}

			audioInputStream = AudioSystem.getAudioInputStream(file);
			audioInputStreamForTarsos = new JVMAudioInputStream(
					AudioSystem.getAudioInputStream(getAudioFormat(), audioInputStream));
			adp = new AudioDispatcher(audioInputStreamForTarsos, wsola.getInputBufferSize(), wsola.getOverlap());
			wsola.setDispatcher(adp);
			adp.addAudioProcessor(wsola);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (isPitchWanted == true) {
			rateTransposer = new RateTransposer(pitchFactor);
		} else {
			rateTransposer = new RateTransposer(ogPitchFactor);
		}
		adp.addAudioProcessor(rateTransposer);

		if (isDelayWanted == true) {
			delayEffect = new DelayEffect(echoLength, decay, sampleRate);
		} else {
			delayEffect = new DelayEffect(ogEchoLength, ogDecay, sampleRate);
		}
		adp.addAudioProcessor(delayEffect);

		if (isGainWanted == true) {
			gainProcessor = new GainProcessor(gain);
		} else {
			gainProcessor = new GainProcessor(ogGain);
		}
		adp.addAudioProcessor(gainProcessor);

		if (isFlangerWanted == true) {
			flangerEffect = new FlangerEffect(flangerLength, wetness, sampleRate, lfo);
		} else {
			flangerEffect = new FlangerEffect(ogFlangerLength, ogWetness, sampleRate, ogLfo);
		}
		adp.addAudioProcessor(flangerEffect);

		if (isLowPassWanted == true) {
			lowPassSP = new LowPassSP(lowPass, sampleRate);
		} else {
			lowPassSP = new LowPassSP(ogLowPass, sampleRate);
		}
		adp.addAudioProcessor(lowPassSP);

		try {
			audioPlayer = new AudioPlayer(tarsosFormat);
			adp.addAudioProcessor(audioPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Default AudioFormat suitable with {@link AudioDispatcher}
	 * 
	 * @return
	 */
	private AudioFormat getAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
		return format;
	}

	/**
	 * Sets the length of the audio file to AudioManipulator's instance variable
	 * 
	 * @param audioFileLengthInSec
	 */
	public void setAudioFileLengthInSec(double audioFileLengthInSec) {
		this.audioFileLengthInSec = audioFileLengthInSec;

	}

	/****** SOUND EFFECT METHODS *********/

	// Setters for sound effect values

	/**
	 * Changes the current value of {@link RateTransposer}
	 * 
	 * @param pitchFactor - new value for pitchFactor in RateTransposer
	 */
	public void setPitchFactor(double pitchFactor) {
		this.pitchFactor = pitchFactor;
		rateTransposer.setFactor(pitchFactor);
		wsola.setParameters(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(pitchFactor, sampleRate));
		isSaved = false;
	}

	/**
	 * Changes the current value of {@link GainProcessor}
	 * 
	 * @param gain - new value for GainProcessor
	 */
	public void setGain(double gain) {
		this.gain = gain;
		gainProcessor.setGain(gain);
		isSaved = false;
	}

	/**
	 * Changes the current value of echoLength in {@link DelayEffect}
	 * 
	 * @param echoLength - new value for echoLength in DelayEffect
	 */
	public void setEchoLength(double echoLength) {
		// Echo length can't be zero
		if (echoLength == 0) {
			echoLength = 0.0001;
			this.echoLength = echoLength;
			delayEffect.setEchoLength(echoLength);
		} else {
			this.echoLength = echoLength;
			delayEffect.setEchoLength(echoLength);
		}
		isSaved = false;
	}

	/**
	 * Changes the current value of decay in {@link DelayEffect}
	 * 
	 * @param decay - new value for decay in DelayEffect
	 */
	public void setDecay(double decay) {
		this.decay = decay;
		delayEffect.setDecay(decay);
		isSaved = false;
	}

	/**
	 * Changes the current value of flangerLength in {@link FlangerEffect}
	 * 
	 * @param flangerLength - new value for flangerLength in FlangerEffect
	 */
	public void setFlangerLength(double flangerLength) {
		// Flanger length cannot be zero
		if (flangerLength == 0) {
			flangerLength = 0.001;
			this.flangerLength = flangerLength;
			flangerEffect.setFlangerLength(flangerLength);
		}
		this.flangerLength = flangerLength;
		flangerEffect.setFlangerLength(flangerLength);
		isSaved = false;
	}

	/**
	 * Changes the current value of wet in {@link FlangerEffect}
	 * 
	 * @param wetness - new value for wet in FlangerEffect
	 */
	public void setWetness(double wetness) {
		this.wetness = wetness;
		flangerEffect.setWet(wetness);
		isSaved = false;
	}

	/**
	 * Changes the current value of lfoFrequency in {@link FlangerEffect}
	 * 
	 * @param lfo - new value for lfoFrequency in FlangerEffect
	 */
	public void setLFO(double lfo) {
		this.lfo = lfo;
		flangerEffect.setLFOFrequency(lfo);
		isSaved = false;
	}

	/**
	 * Changes the current value of freq in {@link LowPassSP}
	 * 
	 * @param lowPass - new value for freq in LowPassSP
	 */
	public void setLowPass(float lowPass) {
		this.lowPass = lowPass;
		lowPassSP = new LowPassSP(lowPass, sampleRate);
		if (adp != null) {
			adp.addAudioProcessor(lowPassSP);
		}
		if (liveDispatcher != null) {
			liveDispatcher.addAudioProcessor(lowPassSP);
		}
		isSaved = false;
	}

	// Methods for making effects inactive, in other words restoring their values to
	// original ones so they cause no effect

	/**
	 * Sets pitch to 1.0 (original value)
	 */
	public void disablePitchEffect() {
		rateTransposer.setFactor(ogPitchFactor);
		wsola.setParameters(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(ogPitchFactor, sampleRate));
	}

	/**
	 * Sets gain to 1.0 (original value)
	 */
	public void disableGainEffect() {
		gainProcessor.setGain(ogGain);
	}

	/**
	 * Sets echo length to 1 and decay to 0 (original values)
	 */
	public void disableDelayEffect() {
		delayEffect.setEchoLength(ogEchoLength);
		delayEffect.setDecay(ogDecay);
	}

	/**
	 * Sets flanger length to 0.01, wetness to 0 and LFO to 5 (original values)
	 */
	public void disableFlangerEffect() {
		flangerEffect.setFlangerLength(ogFlangerLength);
		flangerEffect.setWet(ogWetness);
		flangerEffect.setLFOFrequency(ogLfo);
		wetness = ogWetness;
	}

	/**
	 * Sets low pass to 44100 (original value)
	 */
	public void disableLowPassEffect() {
		lowPassSP = new LowPassSP(ogLowPass, sampleRate);
		adp.addAudioProcessor(lowPassSP);
		lowPass = ogLowPass;
	}

	// Methods for enabling/disabling effects

	/**
	 * Enables/disables pitch effect
	 * 
	 * @param trueOrFalse
	 */
	public void usePitchProcessor(boolean trueOrFalse) {
		if (trueOrFalse == true) {
			setPitchFactor(this.pitchFactor);
			isPitchWanted = true;
		} else if (trueOrFalse == false) {
			disablePitchEffect();
			isPitchWanted = false;
		}
	}

	/**
	 * Enables/disables gain effect
	 * 
	 * @param trueOrFalse
	 */
	public void useGainProcessor(boolean trueOrFalse) {
		if (trueOrFalse == true) {
			setGain(gain);
			isGainWanted = true;
		} else {
			disableGainEffect();
			isGainWanted = false;
		}

	}

	/**
	 * Enables/disables delay effect
	 * 
	 * @param trueOrFalse
	 */
	public void useDelayProcessor(boolean trueOrFalse) {
		if (trueOrFalse == true) {
			setDecay(decay);
			setEchoLength(echoLength);
			isDelayWanted = true;
		} else {
			disableDelayEffect();
			isDelayWanted = false;
		}

	}

	/**
	 * Enables/disables flanger effect
	 * 
	 * @param trueOrFalse
	 */
	public void useFlangerProcessor(boolean trueOrFalse) {
		if (trueOrFalse == true) {
			setWetness(wetness);
			setFlangerLength(flangerLength);
			setLFO(lfo);
			isFlangerWanted = true;
		} else {
			disableFlangerEffect();
			isFlangerWanted = false;
		}

	}

	/**
	 * Enables/disables low pass effect
	 * 
	 * @param trueOrFalse
	 */
	public void useLowPassProcessor(boolean trueOrFalse) {
		if (trueOrFalse == true) {
			lowPassSP = new LowPassSP(lowPass, sampleRate);
			adp.addAudioProcessor(lowPassSP);
			isLowPassWanted = true;
		} else {
			disableLowPassEffect();
			isLowPassWanted = false;
		}

	}

	/******* MEDIA PLAYER *********/

	/**
	 * Plays the selected audio file and creates a timer that updates the audio
	 * file's progress bar
	 */
	public void playAudio() {
		// Stops audio dispatcher if already playing
		if (adp != null) {
			adp.stop();
		}

		isPlaying = true;
		createAudioProcessors();

		adp.addAudioProcessor(new AudioProcessor() {
			@Override
			public void processingFinished() {
				if ((audioFileLengthInSec - currentProgress) < 0.15) {
					audioFileReachedEnd();
				}
				task.cancel();
				timer.cancel();
			}

			@Override
			public boolean process(AudioEvent audioEvent) {
				currentProgress = adp.secondsProcessed();
				return false;
			}
		});

		Thread t = new Thread(adp);
		t.start();

		if (playbackStartingPoint != (float) 0.0) {
			try {
				adp.skip(playbackStartingPoint);
			} catch (Exception e) {
			}
		}

		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				controller.setCurrentValueToAudioDurationSlider(currentProgress);
				controller.setCurrentPositionToAudioDurationText(currentProgress);
			}
		};
		timer.schedule(task, 100, 500);

	}

	/**
	 * Starts playback from desired part of the file
	 * 
	 * @param seconds - playback starting point in seconds
	 */
	public void playFromDesiredSec(double seconds) {
		playbackStartingPoint = (float) seconds;
		if (isPlaying == true) {
			playAudio();
		}
	}

	/**
	 * Stops playing audio and cancels the timer that updates the progress bar
	 */
	public void stopAudio() {
		if (task != null) {
			task.cancel();
			timer.cancel();
		}
		if (adp != null) {
			adp.stop();
			isPlaying = false;
			playbackStartingPoint = (float) 0.0;

			controller.setCurrentValueToAudioDurationSlider(0.0);
			controller.setCurrentPositionToAudioDurationText(0.0);
		}
	}

	/**
	 * Pauses audio playback and saves the current playback time
	 */
	public void pauseAudio() {
		if (task != null) {
			task.cancel();
			timer.cancel();
		}
		if (adp != null) {
			isPlaying = false;
			playbackStartingPoint = adp.secondsProcessed();
			adp.stop();
		}

	}

	/**
	 * Informs controller that playing audio has finished because audio file has
	 * reached its end
	 */
	public void audioFileReachedEnd() {
		controller.audioManipulatorAudioFileReachedEnd();
		playbackStartingPoint = 0;
		currentProgress = 0;
		isPlaying = false;
	}

	/**
	 * Resets the media player
	 */
	public void resetMediaPlayer() {
		playbackStartingPoint = 0;
		currentProgress = 0;
		isPlaying = false;
	}

	/****** TIMER METHODS ******/

	/**
	 * Stops the timer
	 */
	public void timerCancel() {
		task.cancel();
		timer.cancel();
		timer.purge();
	}

	/****** FOR JUNIT TESTING ******/

	/**
	 * Returns the current value of one of the following effects depending on the
	 * parameter
	 * 
	 * @param effectName can be any of the following:
	 *                   <ul>
	 *                   <li>"pitch" - method returns the current value of
	 *                   pitch</li>
	 *                   <li>"gain"</li>
	 *                   <li>"echoLength"</li>
	 *                   <li>"decay"</li>
	 *                   <li>"flangerLength"</li>
	 *                   <li>"wetness"</li>
	 *                   <li>"lfo"</li>
	 *                   <li>"lowPass"</li>
	 *                   </ul>
	 * 
	 *                   if the parameter given is none of the following, method
	 *                   returns 0
	 */
	public double getEffectValue(String effectName) {
		switch (effectName) {
		case "pitch":
			return pitchFactor;
		case "gain":
			return gain;
		case "echoLength":
			return echoLength;
		case "decay":
			return decay;
		case "flangerLength":
			return flangerLength;
		case "wetness":
			return wetness;
		case "lfo":
			return lfo;
		case "lowPass":
			return lowPass;
		default:
			return 0;
		}
	}

}
