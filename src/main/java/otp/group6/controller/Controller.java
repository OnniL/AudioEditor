package otp.group6.controller;

import java.io.File;
import java.sql.SQLException;

import otp.group6.audioeditor.AudioCloudDAO;
import otp.group6.audioeditor.AudioCloudDAO.MixerSetting;
import otp.group6.audioeditor.AudioManipulator;
import otp.group6.audioeditor.AudioRecorder;
import otp.group6.view.MainController;

/**
 * Controller of MVC pattern
 * 
 * @author Kevin Akkoyun, Joonas Soininen, Roosa Laukkanen, Onni Lukkarila
 *
 */
public class Controller {

	private AudioRecorder recorder;
	private AudioManipulator audioManipulator;
	private AudioCloudDAO audioDAO;
	private MainController mainController;

	/**
	 * Constructor for Controller. Gets MainController as its parameter.
	 * 
	 * @param mainController
	 */
	public Controller(MainController mainController) {
		this.mainController = mainController;
		initialConfig();
	}

	public Controller() {
		initialConfig();
	}

	/**
	 * Method is called when the Controller class is created
	 */
	public void initialConfig() {
		recorder = new AudioRecorder(this);
		audioManipulator = new AudioManipulator(this);
	}

	/**
	 * @author Roosa Laukkanen
	 */
	/**** AUDIOMANIPULATOR METHODS ******/

	/** FROM VIEW TO AUDIOMANIPULATOR **/

	/**
	 * Calls checkIfUnsavedMixedFile() method in {@link AudioManipulator}
	 * 
	 * @return true or false
	 */
	public boolean checkIfUnsavedMixedFile() {
		return audioManipulator.checkIfUnsavedMixedFile();
	}

	/**
	 * Forwards pitch value from view to AudioManipulator
	 * 
	 * @param pitch
	 */
	public void audioManipulatorSetPitchFactor(double pitch) {
		audioManipulator.setPitchFactor(pitch);
	}

	/**
	 * Forwards gain value from view to AudioManipulator
	 * 
	 * @param gain
	 */
	public void audioManipulatorSetGain(double gain) {
		audioManipulator.setGain(gain);
	}

	/**
	 * Forwards echo length value from view to AudioManipulator
	 * 
	 * @param echoLength
	 */
	public void audioManipulatorSetEchoLength(double echoLength) {
		audioManipulator.setEchoLength(echoLength);
	}

	/**
	 * Forwards decay value from view to AudioManipulator
	 * 
	 * @param decay
	 */
	public void audioManipulatorSetDecay(double decay) {
		audioManipulator.setDecay(decay);
	}

	/**
	 * Forwards flanger length value from view to AudioManipulator
	 * 
	 * @param flangerLength
	 */
	public void audioManipulatorSetFlangerLength(double flangerLength) {
		audioManipulator.setFlangerLength(flangerLength);
	}

	/**
	 * Forwards wetness value from view to AudioManipulator
	 * 
	 * @param wetness
	 */
	public void audioManipulatorSetWetness(double wetness) {
		audioManipulator.setWetness(wetness);
	}

	/**
	 * Forwards lfo value from view to AudioManipulator
	 * 
	 * @param lfo
	 */
	public void audioManipulatorSetLFO(double lfo) {
		audioManipulator.setLFO(lfo);
	}

	/**
	 * Forwards low pass value from view to AudioManipulator
	 * 
	 * @param lowPass
	 */
	public void audioManipulatorSetLowPass(float lowPass) {
		audioManipulator.setLowPass(lowPass);
	}

	/**
	 * Forwards file from view to AudioManipulator setAudioSourceFile(file) method
	 * 
	 * @param file
	 */
	public void audioManipulatorOpenFile(File file) {
		audioManipulator.setAudioSourceFile(file);
	}

	/**
	 * Calls recordAudio() method in {@link AudioManipulator}
	 */
	public void audioManipulatorStartRecord() {
		audioManipulator.recordAudio();
	}

	/**
	 * Calls stopRecord() method in {@link AudioManipulator}
	 */
	public void audioManipulatorStopRecord() {
		audioManipulator.stopRecord();
	}

	/**
	 * Calls testFilter() method in {@link AudioManipulator}
	 */
	public void testFilter() {
		audioManipulator.testFilter();
	}

	/**
	 * Forwards path to {@link AudioManipulator} audioManipulatorSaveFile(String
	 * path)
	 * 
	 * @param path
	 * @return
	 */
	public boolean audioManipulatorSaveFile(String path) {
		return audioManipulator.saveFile(path);
	}

	/**
	 * Calls playAudio() method in {@link AudioManipulator}
	 */
	public void audioManipulatorPlayAudio() {
		audioManipulator.playAudio();
	}

	/**
	 * Calls stopAudio() method in {@link AudioManipulator}
	 */
	public void audioManipulatorStopAudio() {
		audioManipulator.stopAudio();
	}

	/**
	 * Calls pauseAudio() method in {@link AudioManipulator}
	 */
	public void audioManipulatorPauseAudio() {
		audioManipulator.pauseAudio();
	}

	/**
	 * Forwards seconds to {@link AudioManipulator} playFromDesiredSec(seconds)
	 * 
	 * @param seconds
	 */
	public void audioManipulatorPlayFromDesiredSec(double seconds) {
		audioManipulator.playFromDesiredSec(seconds);
	}

	/**
	 * Forwards audio file's length in seconds to {@link AudioManipulator}
	 * setAudioFileLengthInSec(audioFileLengthInSec)
	 * 
	 * @param audioFileLengthInSec
	 */
	public void setAudioFileLengthInSec(double audioFileLengthInSec) {
		audioManipulator.setAudioFileLengthInSec(audioFileLengthInSec);

	}

	/**
	 * Calls resetMediaPlayer() method in {@link AudioManipulator}
	 */
	public void audioManipulatorResetMediaPlayer() {
		audioManipulator.resetMediaPlayer();
	}

	/**
	 * Calls timerCancel() method in {@link AudioManipulator}
	 */
	public void timerCancel() {
		audioManipulator.timerCancel();
	}

	/**
	 * Forwards boolean value to {@link AudioManipulator}
	 * usePitchProcessor(trueOrFalse)
	 * 
	 * @param trueOrFalse
	 */
	public void audioManipulatorUsePitchProcessor(boolean trueOrFalse) {
		audioManipulator.usePitchProcessor(trueOrFalse);

	}

	/**
	 * Forwards boolean value to {@link AudioManipulator}
	 * useDelayProcessor(trueOrFalse)
	 * 
	 * @param trueOrFalse
	 */
	public void audioManipulatorUseDelayProcessor(boolean trueOrFalse) {
		audioManipulator.useDelayProcessor(trueOrFalse);

	}

	/**
	 * Forwards boolean value to {@link AudioManipulator}
	 * useGainProcessor(trueOrFalse)
	 * 
	 * @param trueOrFalse
	 */
	public void audioManipulatorUseGainProcessor(boolean trueOrFalse) {
		audioManipulator.useGainProcessor(trueOrFalse);

	}

	/**
	 * Forwards boolean value to {@link AudioManipulator}
	 * useFlangerProcessor(trueOrFalse)
	 * 
	 * @param trueOrFalse
	 */
	public void audioManipulatorUseFlangerProcessor(boolean trueOrFalse) {
		audioManipulator.useFlangerProcessor(trueOrFalse);

	}

	/**
	 * Forwards boolean value to {@link AudioManipulator}
	 * useLowPassProcessor(trueOrFalse)
	 * 
	 * @param trueOrFalse
	 */
	public void audioManipulatorUseLowPassProcessor(boolean trueOrFalse) {
		audioManipulator.useLowPassProcessor(trueOrFalse);

	}

	/** FROM AUDIOMANIPULATOR TO VIEW **/

	/**
	 * Calls audioManipulatorOpenRecordedFile() method in {@link MainController}
	 */
	public void audioManipulatorOpenRecordedFile() {
		mainController.audioManipulatorOpenRecordedFile();
	}

	/**
	 * Calls audioManipulatorAudioFileReachedEnd() method in {@link MainController}
	 */
	public void audioManipulatorAudioFileReachedEnd() {
		mainController.audioManipulatorAudioFileReachedEnd();
	}

	/**
	 * Sets max value to audio duration slider in MainController
	 * 
	 * @param maxLenghthInSeconds
	 */
	public void setMaxValueToAudioDurationSlider(double maxLenghthInSeconds) {
		mainController.setCurrentValueToAudioDurationSlider(maxLenghthInSeconds);
	}

	/**
	 * Sets current position to the audio duration slider
	 * 
	 * @param currentSeconds
	 */
	public void setCurrentValueToAudioDurationSlider(double currentSeconds) {
		mainController.setCurrentValueToAudioDurationSlider(currentSeconds);
	}

	/**
	 * Sets the current progress of the song in audio duration text
	 * 
	 * @param currentSeconds
	 */
	public void setCurrentPositionToAudioDurationText(double currentSeconds) {
		mainController.setCurrentValueToAudioDurationText(currentSeconds);
	}

	/**
	 * Forwards boolean value to {@link MainController} method
	 * setDisableMixerSliders(trueOrFalse)
	 * 
	 * @param trueOrFalse
	 */
	public void setDisableMixerSliders(boolean trueOrFalse) {
		mainController.setDisableMixerSliders(trueOrFalse);
	}

	/*** AUDIOMANIPULATOR METHODS END ***/

	// AudioRecorder methods start
	
	/**
	 * Starts recording audio
	 */
	public void recordAudio() {
		recorder.recordAudio();
	}

	/**
	 * Stops recording audio
	 */
	public void stopRecord() {
		recorder.stopRecord();
	}

	/**
	 * Plays the recorded audio on recorder tab
	 */
	public void audioRecorderPlayAudio() {
		recorder.playAudio();
	}

	/**
	 * Stops playing audio on recorder tab
	 */
	public void audioRecorderStopAudio() {
		recorder.stopAudio();
	}

	/**
	 * Pauses playing audio on recorder tab
	 */
	public void audioRecorderPauseAudio() {
		recorder.pauseAudio();
	}

	/**
	 * Starts playing the recorded file from a certain position
	 * @param seconds
	 */
	public void recorderPlayFromDesiredSec(double seconds) {
		recorder.playFromDesiredSec(seconds);
	}

	/**
	 * Cancels timer that is used in showing the recorder duration
	 */
	public void recorderTimerCancel() {
		recorder.timerCancel();
	}

	/**
	 * Communicates AudioRecorder class that the mediaplayerslider has been pressed
	 */
	public void recorderSliderPressed() {
		recorder.recorderSliderPressed();
	}

	/**
	 * Used to save the recorded file
	 * @param path
	 */
	public void saveAudioFile(String path) {
		recorder.saveAudioFile(path);
	}
	
	/**
	 * Sets correct value to mediaplayerslider
	 * @param currentSeconds
	 */
	public void setCurrentValueToRecordFileDurationSlider(Double currentSeconds) {
		mainController.setCurrentValueToRecordDurationSlider(currentSeconds);
	}

	/**
	 * Sets the correct duration of audio file
	 * @param audioFileDuration
	 */
	public void recorderSetAudioFileDuration(float audioFileDuration) {
		recorder.setAudioFileDuration(audioFileDuration);
	}

	/**
	 * Communicates that audio file has reached end
	 */
	public void recorderAudioFileReachedEnd() {
		mainController.recorderAudioFileReachedEnd();
	}

	// AudioRecorder methods stop

	// AudioCloudDAO methods start
	public void intializeDatabaseConnection() {
		audioDAO = new AudioCloudDAO();
	}

	public boolean chekcforUser(String user) {
		return audioDAO.chekcforUser(user);
	}

	public boolean createUser(String user, String pw) throws SQLException {
		return audioDAO.createUser(user, pw);
	}

	public String loginUser(String u, String p) {
		return audioDAO.loginUser(u, p);
	}

	public boolean logoutUser() {
		return audioDAO.logoutUser();
	}

	public boolean createMix(String mixName, String description, double pitch, double echo, double decay, double gain,
			double flangerLenght, double wetness, double lfoFrequency, float lowPass) throws SQLException {
		return audioDAO.createMix(mixName, description, pitch, echo, decay, gain, flangerLenght, wetness, lfoFrequency,
				lowPass);
	}

	public MixerSetting[] getAllMixArray() {
		return audioDAO.getAllMixArray();
	}

	public MixerSetting[] getCertainMixesArray(int select, String specify) {
		return audioDAO.getCertainMixesArray(select, specify);
	}

	public boolean deleteMix(String name, int id) {
		return audioDAO.deleteMix(name, id);
	}

	public boolean deleteUser() {
		return audioDAO.deleteUser();
	}

	public String toString() {
		return audioDAO.toString();
	}

	public String loggedIn() {
		return audioDAO.loggedIn();
	}

	public boolean changePW(String u, String p, String np) {
		return audioDAO.changePassword(u, p, np);
	}

	public boolean isConnected() {
		return audioDAO.isHasconnected();
	}
	// AudioCloudDAO methods stop

}
