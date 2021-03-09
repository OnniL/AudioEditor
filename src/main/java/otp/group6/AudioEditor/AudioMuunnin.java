package otp.group6.AudioEditor;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
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

public class AudioMuunnin {

	private Controller controller;

	private File file;
	private AudioDispatcher adp;
	private AudioDispatcher liveDispatcher;
	private WaveformSimilarityBasedOverlapAdd wsola;
	private GainProcessor gainProcessor;
	private AudioPlayer audioPlayer;
	private RateTransposer rateTransposer;
	private TarsosDSPAudioFormat format;
	private WaveformWriter writer;
	private FlangerEffect flangerEffect;
	private DelayEffect delayEffect;
	private LowPassSP lowPassSP;
	private Timer timer;
	private TimerTask task;
	private JVMAudioInputStream audioInputStreamForTarsos;
	private AudioInputStream audioInputStream;
	private TargetDataLine line = null;

	// Original values
	private float sampleRate;
	private double pitchFactor = 1;
	private double gain = 1;
	private double echoLength = 0.0001; // Cannot be zero
	private double decay = 0;
	private double flangerLength = 0.0001; // Cannot be zero
	private double wetness = 0;
	private double lfo = 0;
	private float lowPass = 44100;

	private float playbackStartingPoint = (float) 0.0;// pause-nappia varten
	private float kokonaiskesto;
	private boolean isPlaying = false;

	// Konstruktori
	public AudioMuunnin(Controller controller) {
		this.controller = controller;
	}

	public void setAudioSourceFile(File file) {
		try {// Haetaan tiedosto parametrin perusteella
			this.file = file.getAbsoluteFile();

			// Converts AudioInputStream to Tarsos compatible JVMAudioInputSteam
			audioInputStream = AudioSystem.getAudioInputStream(file);
			audioInputStreamForTarsos = new JVMAudioInputStream(
					AudioSystem.getAudioInputStream(getAudioFormat(), audioInputStream));

			// formaatti ja sampleRate instanssimuuttujiin
			this.format = audioInputStreamForTarsos.getFormat();
			this.sampleRate = format.getSampleRate();

			wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(pitchFactor, sampleRate));
			adp = new AudioDispatcher(audioInputStreamForTarsos, wsola.getInputBufferSize(), wsola.getOverlap());

			wsola.setDispatcher(adp);
			adp.addAudioProcessor(wsola);

			// Pitch-arvon muuttaja
			rateTransposer = new RateTransposer(pitchFactor);
			adp.addAudioProcessor(rateTransposer);

			// Kaikuefekti
			delayEffect = new DelayEffect(echoLength, decay, sampleRate);
			adp.addAudioProcessor(delayEffect);

			// Gain
			gainProcessor = new GainProcessor(gain);
			adp.addAudioProcessor(gainProcessor);

			// Flangerefekti
			flangerEffect = new FlangerEffect(flangerLength, wetness, sampleRate, (lfo));
			adp.addAudioProcessor(flangerEffect);

			// LowPass
			lowPassSP = new LowPassSP(lowPass, sampleRate);
			adp.addAudioProcessor(lowPassSP);

			audioPlayer = new AudioPlayer(format);

		} catch (LineUnavailableException e) {
		} catch (NullPointerException e) {
			System.out.println("Tiedostoa ei valittu");
		} catch (Exception e) {
		}
	}

	// SOUND EFFECT METHODS
	public void setPitchFactor(double pitchFactor) {
		// Vaihdetaan arvot Rate Transposeriin..
		this.pitchFactor = pitchFactor;
		rateTransposer.setFactor(pitchFactor);
		// ..ja WaveFormSimilarityOverlappAddiin
		wsola.setParameters(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(pitchFactor, sampleRate));
	}

	public void setGain(double gain) {
		this.gain = gain;
		gainProcessor.setGain(gain);
	}

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
	}

	public void setDecay(double decay) {
		this.decay = decay;
		delayEffect.setDecay(decay);
	}

	public void setFlangerLength(double flangerLength) {
		// Flanger length cannot be zero
		if (flangerLength == 0) {
			flangerLength = 0.001;
			this.flangerLength = flangerLength;
			flangerEffect.setFlangerLength(flangerLength);
		}
		this.flangerLength = flangerLength;
		flangerEffect.setFlangerLength(flangerLength);
	}

	public void setWetness(double wetness) {
		this.wetness = wetness;
		flangerEffect.setWet(wetness);
	}

	public void setLFO(double lfo) {
		this.lfo = lfo;
		flangerEffect.setLFOFrequency(lfo);
	}

	public void setLowPass(float lowPass) {
		this.lowPass = lowPass;
		lowPassSP = new LowPassSP(lowPass, sampleRate);
		adp.addAudioProcessor(lowPassSP);

	}

	public void testFilter() {
		AudioFormat format2 = getAudioFormat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format2);
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format2);
			line.start();
			AudioInputStream ais = new AudioInputStream(line);
			JVMAudioInputStream audioStream = new JVMAudioInputStream(ais);
			liveDispatcher = new AudioDispatcher(audioStream, wsola.getInputBufferSize(),
					wsola.getOverlap());
			audioPlayer = new AudioPlayer(format2);
			wsola.setDispatcher(liveDispatcher);
			liveDispatcher.addAudioProcessor(wsola);
			liveDispatcher.addAudioProcessor(rateTransposer);
			liveDispatcher.addAudioProcessor(delayEffect);
			liveDispatcher.addAudioProcessor(gainProcessor);
			liveDispatcher.addAudioProcessor(flangerEffect);
			liveDispatcher.addAudioProcessor(audioPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Thread t = new Thread(liveDispatcher);
			t.start();
			System.out.println("test filter started");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopTestFilter() {
		liveDispatcher.stop();
		liveDispatcher.removeAudioProcessor(wsola);
		liveDispatcher.removeAudioProcessor(rateTransposer);
		liveDispatcher.removeAudioProcessor(delayEffect);
		liveDispatcher.removeAudioProcessor(gainProcessor);
		liveDispatcher.removeAudioProcessor(flangerEffect);
		liveDispatcher.removeAudioProcessor(audioPlayer);
		line.close();
		line.stop();
		System.out.println("Test filter stopped");
	}

	// MEDIAPLAYER METHODS
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
				task.cancel();
				timer.cancel();
			}

			@Override
			public boolean process(AudioEvent audioEvent) {
				return false;
			}
		});

		Thread t = new Thread(adp);
		t.start();

		if (playbackStartingPoint != (float) 0.0) {
			adp.skip(playbackStartingPoint);
			System.out.println("aloitetaan toisto kohdasta " + playbackStartingPoint);
		}

		// Luodaan uusi Timer, joka kuuntelee adp:n toistokohtaa ja välittää sen
		// kontrollerin kautta näkymään oikeaan slideriin
		timer = new Timer();

		task = new TimerTask() {
			@Override
			public void run() {
				kokonaiskesto = playbackStartingPoint;
				kokonaiskesto += (float) (audioPlayer.getMicroSecondPosition() / 1000000.0);
				System.out.println(" timer" + kokonaiskesto);
				setCurrentPositionToAudioFileDurationSlider(kokonaiskesto);
				controller.setCurrentPositionToAudioDurationText(kokonaiskesto);
			}
		};
		timer.schedule(task, 100, 500); // käynnistää timerin, joka suorittaa taskin 0,5 sekunnin välein alkaen kohdasta
										// 0s

	}

	public void playFromDesiredSec(double seconds) {
		playbackStartingPoint = (float) seconds;
		System.out.println("asetetaan muuttujaan " + playbackStartingPoint);
		if (isPlaying == true) {
			playAudio();
		}
	}

	public void stopAudio() {
		if (task != null) {
			task.cancel();
			timer.cancel();
		}
		if (adp != null) {
			adp.stop();
			isPlaying = false;
			playbackStartingPoint = (float) 0.0;
			// Asettaa kestosliderin nollaan
			setCurrentPositionToAudioFileDurationSlider(0);
		}
	}

	public void pauseAudio() {
		if (task != null) {
			task.cancel();
			timer.cancel();
		}
		if (adp != null) {
			// kohta mihin jäätiin
			isPlaying = false;
			// secondsProcessed = adp.secondsProcessed();
			System.out.println(kokonaiskesto);
			playbackStartingPoint = adp.secondsProcessed();
			adp.stop();
		}

	}

	private void setCurrentPositionToAudioFileDurationSlider(double seconds) {
		controller.setCurrentValueToAudioDuratinSlider(seconds);
		controller.setCurrentPositionToAudioDurationText(seconds);
	}

	// Timer metodit
	public void timerCancel() {
		task.cancel();
		timer.cancel();
		timer.purge();
	}

	public void timerWait() {
		try {
			task.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void timerNotify() {
		timer.notify();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void saveFile(String path) { 
		
		writer = new WaveformWriter(format, path);
		adp.addAudioProcessor(writer);
		try {
			Thread t = new Thread(adp);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// PRIVATE METHODS
	private void createAudioProcessors() {
		try {
			// WaveformSimilarityBasedOverlapAdd pitää tiedoston samanmittaisena
			// pitch-arvosta riippumatta
			wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(pitchFactor, sampleRate));
			audioInputStream = AudioSystem.getAudioInputStream(file);
			// audioInputStreamForTarsos = getMonoAudioInputStream(audioInputStream);
			audioInputStreamForTarsos = new JVMAudioInputStream(
					AudioSystem.getAudioInputStream(getAudioFormat(), audioInputStream));
			adp = new AudioDispatcher(audioInputStreamForTarsos, wsola.getInputBufferSize(), wsola.getOverlap());
			// adp = AudioDispatcherFactory.fromFile(wavFile, wsola.getInputBufferSize(),
			// wsola.getOverlap());

			wsola.setDispatcher(adp);
			adp.addAudioProcessor(wsola);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Pitch-arvon muuttaja
		rateTransposer = new RateTransposer(pitchFactor);
		adp.addAudioProcessor(rateTransposer);

		// Kaikuefekti
		delayEffect = new DelayEffect(echoLength, decay, sampleRate);
		adp.addAudioProcessor(delayEffect);

		// Gain
		gainProcessor = new GainProcessor(gain);
		adp.addAudioProcessor(gainProcessor);

		// Flangerefekti
		flangerEffect = new FlangerEffect(flangerLength, wetness, sampleRate, (lfo));
		adp.addAudioProcessor(flangerEffect);

		// LowPass
		lowPassSP = new LowPassSP(lowPass, sampleRate);
		adp.addAudioProcessor(lowPassSP);

		try {
			audioPlayer = new AudioPlayer(format);
			adp.addAudioProcessor(audioPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
		return format;
	}
}
