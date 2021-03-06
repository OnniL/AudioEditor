package otp.group6.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import otp.group6.audioeditor.AudioFileHandler;
import otp.group6.audioeditor.AudioManipulator;
import otp.group6.controller.Controller;

/**
 * Controller for the MainView.fxml
 * 
 * @author Kevin Akkouyn, Roosa Laukkanen, Onni Lukkarila, Joonas Soininen
 */
public class MainController implements Initializable {

	Controller controller;
	Locale curLocale;
	ResourceBundle bundle;
	SoundboardViewController boardController;

	@FXML
	private Tab mixerTab;
	@FXML
	private Tab recorderTab;
	public static AnchorPane sharedMain;

	/**
	 * Constructor for class MainController. Creates a new instance of Controller class and gives its own instance as a parameter 
	 */
	public MainController() {
		controller = new Controller(this);
	}

	@Override
	/**
	 * Method is called when the class is created
	 */
	public void initialize(URL location, ResourceBundle resources) {
		bundle = resources;
		loadSoundboard();
		initializeMixer();
		sharedMain = mainContainer;
		initializeMenuItems();
		setApplicationLanguage();
	}

	/**
	 * Method that is called when the program is exit
	 */
	public void exitRoutine() {
		boardController.saveSampleData();
	}

	/********* MENU BAR **********/

	/**
	 * @author Roosa Laukkanen
	 */

	@FXML
	private Menu menuSettings;
	@FXML
	private MenuItem menuItemPreferences;
	// MenuItem userSettings (defined in different section)
	// MenuItem loginoption (defined in different section)

	@FXML
	private Menu menuHelp;
	@FXML
	private MenuItem menuItemAbout;
	@FXML
	private MenuItem menuItemUserguide;

	@FXML
	private Menu menuLanguage;
	@FXML
	private MenuItem languageEnglish;
	@FXML
	private ImageView checkmarkEnglish;
	@FXML
	private MenuItem languageFinnish;
	@FXML
	private ImageView checkmarkFinnish;

	/**
	 * Localization for every text element in the menu bar
	 */
	public void initializeMenuBarLocalization() {
		menuSettings.setText(bundle.getString("mVSettings"));
		menuItemPreferences.setText(bundle.getString("mVPreferences"));
		userSettings.setText(bundle.getString("mVUsersettings"));
		loginoption.setText(bundle.getString("mVLogin"));
		menuHelp.setText(bundle.getString("mVHelp"));
		menuItemAbout.setText(bundle.getString("mVAbout"));
		menuItemUserguide.setText(bundle.getString("mVUserguide"));
		menuLanguage.setText(bundle.getString("mVLanguage"));
	}

	/**
	 * Changes the language of the application. Gets the wanted language from the
	 * source that triggers this method
	 * 
	 * @param event - event that triggers the method
	 */
	public void changeApplicationLanguage(ActionEvent event) {
		String appConfigPath = "src/main/resources/properties/AudioEditor.properties";
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(appConfigPath).getAbsoluteFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (event.getSource() == languageEnglish) {
			curLocale = new Locale("en", "US");
			properties.setProperty("language", "en");
			properties.setProperty("country", "US");
		} else if (event.getSource() == languageFinnish) {
			curLocale = new Locale("fi", "FI");
			properties.setProperty("country", "FI");
			properties.setProperty("language", "fi");
		}

		try (FileOutputStream fos = new FileOutputStream(appConfigPath);) {
			properties.store(fos, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		bundle = ResourceBundle.getBundle("properties/ApplicationResources", curLocale);

		setApplicationLanguage();
	}

	/**
	 * Calls every localization method in AudioEditor to update
	 */
	public void setApplicationLanguage() {
		initializeMixerLocalization();
		initializeRecorderLocalization();
		initializeMenuBarLocalization();
		setLocalizedLanguageMenuItems();
		boardController.setLocalization(bundle);
		boardController.refreshSoundboard();
	}

	/********* MIXER **********/

	/**
	 * @author Roosa Laukkanen
	 */

	@FXML
	private Label labelSelectFile;
	@FXML
	private Label labelOr;
	@FXML
	private Label labelRecordFile;
	@FXML
	private Label labelSelectedFile;
	@FXML
	private Label labelTryMixer;
	@FXML
	private Label labelPitch;
	@FXML
	private Label labelEcho;
	@FXML
	private Label labelDecay;
	@FXML
	private Label labelEchoLength;
	@FXML
	private Label labelLowPass;
	@FXML
	private Label labelFlanger;
	@FXML
	private Label labelWetness;
	@FXML
	private Label labelFlangerLength;
	@FXML
	private Label labelLfo;
	@FXML
	private Label labelGain;
	@FXML
	private Text textAudioFileDuration;

	@FXML
	private Slider sliderPitch;
	@FXML
	private Slider sliderEchoLength;
	@FXML
	private Slider sliderDecay;
	@FXML
	private Slider sliderGain;
	@FXML
	private Slider sliderFlangerLength;
	@FXML
	private Slider sliderWetness;
	@FXML
	private Slider sliderLfoFrequency;
	@FXML
	private Slider sliderLowPass;
	@FXML
	private Slider sliderAudioFileDuration;

	@FXML
	private TextField textFieldPitch;
	@FXML
	private TextField textFieldGain;
	@FXML
	private TextField textFieldEchoLength;
	@FXML
	private TextField textFieldDecay;
	@FXML
	private TextField textFieldFlangerLength;
	@FXML
	private TextField textFieldWetness;
	@FXML
	private TextField textFieldLfo;
	@FXML
	private TextField textFieldLowPass;

	@FXML
	private Button buttonPlay;
	@FXML
	private Button buttonPause;
	@FXML
	private Button buttonStop;
	@FXML
	private Button buttonInfoPitch;
	@FXML
	private Button buttonInfoGain;
	@FXML
	private Button buttonInfoEcho;
	@FXML
	private Button buttonInfoFlanger;
	@FXML
	private Button buttonInfoLowPass;
	@FXML
	private Button buttonSaveSettings;
	@FXML
	private Button buttonLoadSettings;
	@FXML
	private Button buttonMixerFileOpener;
	@FXML
	private Button buttonMixerResetSliders;
	@FXML
	private Button buttonSaveMixedFile;

	@FXML
	private ToggleButton toggleButtonMixerStartRecording;
	@FXML
	private ToggleButton toggleButtonTestFilter;
	@FXML
	private ToggleButton toggleButtonPitch;
	@FXML
	private ToggleButton toggleButtonGain;
	@FXML
	private ToggleButton toggleButtonEcho;
	@FXML
	private ToggleButton toggleButtonFlanger;
	@FXML
	private ToggleButton toggleButtonLowPass;

	@FXML
	private ButtonBar buttonBarResetSaveLoad;

	@FXML
	private AnchorPane paneMixerSliders;
	@FXML
	private AnchorPane paneMixerAudioPlayer;
	@FXML
	private AnchorPane paneMixerMainControls;
	@FXML
	private AnchorPane panePitch;
	@FXML
	private AnchorPane paneEcho;
	@FXML
	private AnchorPane paneLowPass;
	@FXML
	private AnchorPane paneFlanger;
	@FXML
	private AnchorPane paneGain;

	private Tooltip tooltipPitch;
	private Tooltip tooltipGain;
	private Tooltip tooltipEcho;
	private Tooltip tooltipFlanger;
	private Tooltip tooltipLowPass;

	@FXML
	private AnchorPane mainContainer;

	private File mixerSelectedFile;

	@FXML
	AnchorPane soundboardRoot;

	private String audioFileDurationString = "0:00";
	private String audioFileProcessedTimeString = "0:00";

	private DecimalFormatSymbols symbols;
	private DecimalFormat decimalFormat;

	/**
	 * Initializes Mixer by creating event listeners and tooltips
	 */
	public void initializeMixer() {
		initializeSlidersAndTextFields();
		initializeTooltips();
		initializeRecorderListener();
	}

	// METHODS FOR BUTTONS

	/**
	 * Method is called when user clicks the file button. Opens a file chooser and
	 * transfers the file to {@link AudioManipulator} if the file is valid.
	 */
	@FXML
	public void handleAudioManipulatorOpenFileButton() {
		try {
			Pattern pattern = Pattern.compile("(\\.wav)$", Pattern.CASE_INSENSITIVE);

			mixerSelectedFile = AudioFileHandler.openWavFileExplorer(mainContainer.getScene().getWindow());

			Matcher matcher = pattern.matcher(mixerSelectedFile.getName());
			if (matcher.find()) {
				audioManipulatorResetMediaPlayer();
				controller.audioManipulatorOpenFile(mixerSelectedFile);
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(bundle.getString("mixerErrorTitle"));
				alert.setHeaderText(bundle.getString("mixerErrorReasonWrongFormat"));
				alert.setContentText(bundle.getString("mixerErrorContentWrongFormat"));
				alert.showAndWait();
			}
			// Length of the audio file in seconds (file.length / (format.frameSize *
			// format.frameRate))
			AudioFormat format = AudioSystem.getAudioFileFormat(mixerSelectedFile.getAbsoluteFile()).getFormat();
			double audioFileLengthInSec = mixerSelectedFile.length() / (format.getFrameSize() * format.getFrameRate());
			controller.setAudioFileLengthInSec(audioFileLengthInSec);
			audioFileDurationString = secondsToMinutesAndSeconds(audioFileLengthInSec);

			setMaxValueToAudioDurationSlider(audioFileLengthInSec);
			textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);

			// Shows the name of the file in textSelectedFile element
			labelSelectedFile.setText(bundle.getString("mixerFileSelectedText") + " " + mixerSelectedFile.getName());

			// Enables all sliders and audio player
			enableMixerSlidersAndAudioPlayer();
			buttonPlay.setDisable(false);

		} catch (Exception e) {
		}
	}

	/**
	 * Method is called when user clicks the record button in Mixer. If toggle
	 * button is selected recording is active, else recording is inactive.
	 */
	@FXML
	public void handleMixerRecordButton() {
		if (toggleButtonMixerStartRecording.isSelected() == true) {
			controller.audioManipulatorStartRecord();
			paneMixerAudioPlayer.setDisable(true);
			paneMixerSliders.setDisable(true);
			buttonMixerFileOpener.setDisable(true);
			toggleButtonTestFilter.setDisable(true);
			toggleButtonMixerStartRecording.setText(bundle.getString("mixerStopRecordButton"));
		} else {
			controller.audioManipulatorStopRecord();
			paneMixerAudioPlayer.setDisable(false);
			paneMixerSliders.setDisable(false);
			buttonMixerFileOpener.setDisable(false);
			toggleButtonTestFilter.setDisable(false);
			buttonPlay.setDisable(false);
			toggleButtonMixerStartRecording.setText(bundle.getString("mixerStartRecordButton"));
		}
	}

	/**
	 * Sets the recorded file as {@link AudioManipulator}'s source
	 */
	public void audioManipulatorOpenRecordedFile() {
		try {
			mixerSelectedFile = new File("src/audio/mixer_default.wav").getAbsoluteFile();
			mixerSelectedFile.deleteOnExit();
			audioManipulatorResetMediaPlayer();
			controller.audioManipulatorOpenFile(mixerSelectedFile);

			// Length of the audio file in seconds (file.length / (format.frameSize *
			// format.frameRate))
			AudioFormat format = AudioSystem.getAudioFileFormat(mixerSelectedFile.getAbsoluteFile()).getFormat();
			double audioFileLengthInSec = mixerSelectedFile.length() / (format.getFrameSize() * format.getFrameRate());
			controller.setAudioFileLengthInSec(audioFileLengthInSec);
			audioFileDurationString = secondsToMinutesAndSeconds(audioFileLengthInSec);

			setMaxValueToAudioDurationSlider(audioFileLengthInSec);
			textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);

			// Shows the name of the file in textSelectedFile element
			labelSelectedFile.setText(
					bundle.getString("mixerFileSelectedText") + " " + bundle.getString("mixerSelectedRecordingText"));

			// Enables all sliders and audio player
			enableMixerSlidersAndAudioPlayer();

		} catch (Exception e) {
		}
	}

	/**
	 * Method is called when user clicks "test filter" button in Mixer. When toggle
	 * button is selected filter testing is active. When toggle button is not
	 * selected filter is inactive.
	 */
	@FXML
	public void handleAudioManipulatorTestFilterButton() {
		if (toggleButtonTestFilter.isSelected() == true) {
			controller.testFilter();
			buttonMixerFileOpener.setDisable(true);
			toggleButtonMixerStartRecording.setDisable(true);
			paneMixerAudioPlayer.setDisable(true);
			sliderLowPass.setDisable(true);
			textFieldLowPass.setDisable(true);
			toggleButtonTestFilter.setText(bundle.getString("mixerButtonTryMixerStopText"));
		} else {
			controller.testFilter();
			buttonMixerFileOpener.setDisable(false);
			toggleButtonMixerStartRecording.setDisable(false);
			paneMixerAudioPlayer.setDisable(false);
			sliderLowPass.setDisable(false);
			textFieldLowPass.setDisable(false);
			toggleButtonTestFilter.setText(bundle.getString("mixerButtonTryMixerStartText"));
		}
	}

	/**
	 * Method is called when user clicks "save mixed file" button. Opens a file
	 * chooser and forwards the selected path to {@link Controller}
	 */
	@FXML
	public boolean handleAudioManipulatorSaveMixedFileButton() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("WAV files (*.wav)", "*.wav");
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showSaveDialog(mainContainer.getScene().getWindow());
		if (file == null) { // returns false if user clicks cancel or close buttons
			return false;
		}
		String fullPath;
		try {
			fullPath = file.getAbsolutePath();
			if (!fullPath.endsWith(".wav")) {
				fullPath = fullPath + ".wav";
			}
			return controller.audioManipulatorSaveFile(fullPath);
		} catch (Exception e) {
			return false;
		}
	}

	// AUDIO PLAYER

	/**
	 * Method is called when user clicks the play button in Mixer. Starts playing
	 * audio.
	 */
	@FXML
	public void handleAudioManipulatorPlayButton() {
		controller.audioManipulatorPlayAudio();
		buttonPlay.setDisable(true);
		buttonPause.setDisable(false);
		buttonStop.setDisable(false);
		paneMixerMainControls.setDisable(true);
		sliderLowPass.setDisable(true);
		textFieldLowPass.setDisable(true);
	}

	/**
	 * Method is called when user clicks the pause button in Mixer. Pauses audio
	 * playback.
	 */
	@FXML
	public void handleAudioManipulatorPauseButton() {
		controller.audioManipulatorPauseAudio();
		buttonPlay.setDisable(false);
		buttonPause.setDisable(true);
		buttonStop.setDisable(false);
		paneMixerMainControls.setDisable(false);
		sliderLowPass.setDisable(false);
		textFieldLowPass.setDisable(false);
	}

	/**
	 * Method is called when user clicks the stop button in Mixer. Stops audio
	 * playback.
	 */
	@FXML
	public void handleAudioManipulatorStopButton() {
		controller.audioManipulatorStopAudio();
		buttonPlay.setDisable(false);
		buttonPause.setDisable(true);
		buttonStop.setDisable(true);
		paneMixerMainControls.setDisable(false);
		sliderLowPass.setDisable(false);
		textFieldLowPass.setDisable(false);
	}

	/**
	 * Method is called when user clicks the audio progress bar in media player.
	 * Forwards the value of the click to {@link Controller}
	 */
	@FXML
	public void handleAudioFileDurationSliderClick() {
		controller.timerCancel();
		controller.audioManipulatorPlayFromDesiredSec(sliderAudioFileDuration.getValue());

		// Nyk kesto tekstin??
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderAudioFileDuration.getValue());
		textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
	}

	/**
	 * Resets all effect sliders
	 */
	@FXML
	public void audioManipulatorResetAllSliders() {
		sliderPitch.setValue(1);
		sliderGain.setValue(1);
		sliderEchoLength.setValue(1);
		sliderDecay.setValue(0);
		sliderFlangerLength.setValue(0.01);
		sliderWetness.setValue(0);
		sliderLfoFrequency.setValue(5);
		sliderLowPass.setValue(44100);
	}

	// Methods for toggle buttons in Mixer

	/**
	 * Enables/disables pitch effect
	 */
	public void handleToggleButtonPitch() {
		if (toggleButtonPitch.isSelected()) {
			controller.audioManipulatorUsePitchProcessor(true);
			sliderPitch.setDisable(false);
			textFieldPitch.setDisable(false);
			toggleButtonPitch.setText(bundle.getString("mixerToggleButtonOn"));

		} else if (!toggleButtonPitch.isSelected()) {
			controller.audioManipulatorUsePitchProcessor(false);
			sliderPitch.setDisable(true);
			textFieldPitch.setDisable(true);
			toggleButtonPitch.setText(bundle.getString("mixerToggleButtonOff"));
		}
	}

	/**
	 * Enables/disables echo effect (aka delay)
	 */
	public void handleToggleButtonEcho() {
		if (toggleButtonEcho.isSelected() == true) {
			controller.audioManipulatorUseDelayProcessor(true);
			sliderEchoLength.setDisable(false);
			sliderDecay.setDisable(false);
			textFieldEchoLength.setDisable(false);
			textFieldDecay.setDisable(false);
			toggleButtonEcho.setText(bundle.getString("mixerToggleButtonOn"));
		} else {
			controller.audioManipulatorUseDelayProcessor(false);
			sliderEchoLength.setDisable(true);
			sliderDecay.setDisable(true);
			textFieldEchoLength.setDisable(true);
			textFieldDecay.setDisable(true);
			toggleButtonEcho.setText(bundle.getString("mixerToggleButtonOff"));
		}

	}

	/**
	 * Enables/disables gain effect
	 */
	public void handleToggleButtonGain() {
		if (toggleButtonGain.isSelected() == true) {
			controller.audioManipulatorUseGainProcessor(true);
			sliderGain.setDisable(false);
			textFieldGain.setDisable(false);
			toggleButtonGain.setText(bundle.getString("mixerToggleButtonOn"));
		} else {
			controller.audioManipulatorUseGainProcessor(false);
			sliderGain.setDisable(true);
			textFieldGain.setDisable(true);
			toggleButtonGain.setText(bundle.getString("mixerToggleButtonOff"));
		}

	}

	/**
	 * Enables/disables flanger effect
	 */
	public void handleToggleButtonFlanger() {
		if (toggleButtonFlanger.isSelected() == true) {
			controller.audioManipulatorUseFlangerProcessor(true);
			sliderWetness.setDisable(false);
			sliderFlangerLength.setDisable(false);
			sliderLfoFrequency.setDisable(false);
			textFieldWetness.setDisable(false);
			textFieldFlangerLength.setDisable(false);
			textFieldLfo.setDisable(false);
			toggleButtonFlanger.setText(bundle.getString("mixerToggleButtonOn"));
		} else {
			controller.audioManipulatorUseFlangerProcessor(false);
			sliderWetness.setDisable(true);
			sliderFlangerLength.setDisable(true);
			sliderLfoFrequency.setDisable(true);
			textFieldWetness.setDisable(true);
			textFieldFlangerLength.setDisable(true);
			textFieldLfo.setDisable(true);
			toggleButtonFlanger.setText(bundle.getString("mixerToggleButtonOff"));
		}

	}

	/**
	 * Enables/disables low pass effect
	 */
	public void handleToggleButtonLowPass() {
		if (toggleButtonLowPass.isSelected() == true) {
			controller.audioManipulatorUseLowPassProcessor(true);
			sliderLowPass.setDisable(false);
			textFieldLowPass.setDisable(false);
			toggleButtonLowPass.setText(bundle.getString("mixerToggleButtonOn"));
		} else {
			controller.audioManipulatorUseLowPassProcessor(false);
			sliderLowPass.setDisable(true);
			textFieldLowPass.setDisable(true);
			toggleButtonLowPass.setText(bundle.getString("mixerToggleButtonOff"));
		}

	}

	// Methods for getting TextField input values

	/**
	 * Gets pitch value from the corresponding text field and forwards it to
	 * {@link Controller}
	 */
	@FXML
	private void getTextFieldPitch() {
		textFieldPitch.setStyle("-fx-text-fill: black");
		String text = textFieldPitch.getText().replace(',', '.');
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderPitch.getMin() && number <= sliderPitch.getMax()) {
				controller.audioManipulatorSetPitchFactor(number);
				sliderPitch.setValue(number);
			} else {
				textFieldPitch.setStyle("-fx-text-fill: red");
			}
		} catch (Exception e) {
			textFieldPitch.setStyle("-fx-text-fill: red");
		}
	}

	/**
	 * Gets gain value from the corresponding text field and forwards it to
	 * {@link Controller}
	 */
	@FXML
	private void getTextFieldGain() {
		textFieldGain.setStyle("-fx-text-fill: black");
		String text = textFieldGain.getText().replace(',', '.');
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderGain.getMin() && number <= sliderGain.getMax()) {
				controller.audioManipulatorSetGain(number);
				sliderGain.setValue(number);
			} else {
				textFieldGain.setStyle("-fx-text-fill: red");
			}
		} catch (Exception e) {
			textFieldGain.setStyle("-fx-text-fill: red");
		}
	}

	/**
	 * Gets echo length value from the corresponding text field and forwards it to
	 * {@link Controller}
	 */
	@FXML
	private void getTextFieldEchoLength() {
		textFieldEchoLength.setStyle("-fx-text-fill: black");
		String text = textFieldEchoLength.getText().replace(',', '.');
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderEchoLength.getMin() && number <= sliderEchoLength.getMax()) {
				controller.audioManipulatorSetEchoLength(number);
				sliderEchoLength.setValue(number);
			} else {
				textFieldEchoLength.setStyle("-fx-text-fill: red");
			}
		} catch (Exception e) {
			textFieldEchoLength.setStyle("-fx-text-fill: red");
		}
	}

	/**
	 * Gets decay value from the corresponding text field and forwards it to
	 * {@link Controller}
	 */
	@FXML
	private void getTextFieldDecay() {
		textFieldDecay.setStyle("-fx-text-fill: black");
		String text = textFieldDecay.getText().replace(',', '.');
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderDecay.getMin() && number <= sliderDecay.getMax()) {
				controller.audioManipulatorSetDecay(number);
				sliderDecay.setValue(number);
			} else {
				textFieldDecay.setStyle("-fx-text-fill: red");
			}
		} catch (Exception e) {
			textFieldDecay.setStyle("-fx-text-fill: red");
		}
	}

	/**
	 * Gets flanger length value from the corresponding text field and forwards it
	 * to {@link Controller}
	 */
	@FXML
	private void getTextFieldFlangerLength() {
		textFieldFlangerLength.setStyle("-fx-text-fill: black");
		String text = textFieldFlangerLength.getText().replace(',', '.');
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderFlangerLength.getMin() && number <= sliderFlangerLength.getMax()) {
				controller.audioManipulatorSetFlangerLength(number);
				sliderFlangerLength.setValue(number);
			} else {
				textFieldFlangerLength.setStyle("-fx-text-fill: red");
			}
		} catch (Exception e) {
			textFieldFlangerLength.setStyle("-fx-text-fill: red");
		}
	}

	/**
	 * Gets wetness value from the corresponding text field and forwards it to
	 * {@link Controller}
	 */
	@FXML
	private void getTextFieldWetness() {
		textFieldWetness.setStyle("-fx-text-fill: black");
		String text = textFieldWetness.getText().replace(',', '.');
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderWetness.getMin() && number <= sliderWetness.getMax()) {
				controller.audioManipulatorSetWetness(number);
				sliderWetness.setValue(number);
			} else {
				textFieldWetness.setStyle("-fx-text-fill: red");
			}
		} catch (Exception e) {
			textFieldWetness.setStyle("-fx-text-fill: red");
		}
	}

	/**
	 * Gets lfo value from the corresponding text field and forwards it to
	 * {@link Controller}
	 */
	@FXML
	private void getTextFieldLfo() {
		textFieldLfo.setStyle("-fx-text-fill: black");
		String text = textFieldLfo.getText().replace(',', '.');
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderLfoFrequency.getMin() && number <= sliderLfoFrequency.getMax()) {
				controller.audioManipulatorSetLFO(number);
				sliderLfoFrequency.setValue(number);
			} else {
				textFieldLfo.setStyle("-fx-text-fill: red");
			}
		} catch (Exception e) {
			textFieldLfo.setStyle("-fx-text-fill: red");
		}
	}

	/**
	 * Gets low pass value from the corresponding text field and forwards it to
	 * {@link Controller}
	 */
	@FXML
	private void getTextFieldLowPass() {
		textFieldLowPass.setStyle("-fx-text-fill: black");
		String text = textFieldLowPass.getText().replace(',', '.');
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderLowPass.getMin() && number <= sliderLowPass.getMax()) {
				controller.audioManipulatorSetLowPass((float) number);
				sliderLowPass.setValue(number);
			} else {
				textFieldLowPass.setStyle("-fx-text-fill: red");
			}
		} catch (Exception e) {
			textFieldLowPass.setStyle("-fx-text-fill: red");
		}
	}

	/**
	 * Checks if there is an unsaved file in {@link AudioManipulator}. If there is
	 * it creates an alert to check if user wants to save the file.
	 * 
	 * @return returns true if ready to exit, false if not
	 */
	public boolean isMixerOkToExit() {
		if (controller.checkIfUnsavedMixedFile() == true) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle(bundle.getString("mixerUnsavedAlertTitle"));
			alert.setHeaderText(bundle.getString("mixerUnsavedAlertHeader"));
			alert.setContentText(bundle.getString("mixerUnsavedAlertContent"));

			ButtonType buttonSave = new ButtonType(bundle.getString("mixerUnsavedAlertSaveButton"));
			ButtonType buttonExit = new ButtonType(bundle.getString("mixerUnsavedAlertExitButton"));
			ButtonType buttonCancel = new ButtonType(bundle.getString("mixerUnsavedAlertCancelButton"),
					ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(buttonSave, buttonExit, buttonCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonSave) {
				return handleAudioManipulatorSaveMixedFileButton();
			} else if (result.get() == buttonExit) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Is called by controller when audio file has reached its end. Resets the audio
	 * player view elements.
	 */
	public void audioManipulatorAudioFileReachedEnd() {
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(0);
		textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
		sliderAudioFileDuration.setValue(0);
		buttonPlay.setDisable(false);
		buttonPause.setDisable(true);
		buttonStop.setDisable(true);
		paneMixerMainControls.setDisable(false);
		paneLowPass.setDisable(false);
	}

	/**
	 * Calls controller to reset media player
	 */
	private void audioManipulatorResetMediaPlayer() {
		controller.audioManipulatorResetMediaPlayer();
	}

	/**
	 * Sets the maximum value to audio duration slider (progress bar)
	 * 
	 * @param audioFileLengthInSec
	 */
	public void setMaxValueToAudioDurationSlider(double audioFileLengthInSec) {
		sliderAudioFileDuration.setMax(audioFileLengthInSec);
	}

	/**
	 * Sets the current progress in seconds to audio duration slider (progress bar)
	 * 
	 * @param currentSeconds
	 */
	public void setCurrentValueToAudioDurationSlider(double currentSeconds) {
		sliderAudioFileDuration.setValue(currentSeconds);
	}

	/**
	 * Sets the current progress in seconds to textAudioFileDuration element
	 * 
	 * @param currentSeconds
	 */
	public void setCurrentValueToAudioDurationText(double currentSeconds) {
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(currentSeconds);
		textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
	}

	/**
	 * Converts seconds to minutes and seconds.
	 * 
	 * @param seconds
	 * @return Returns String in XX:XX format
	 */
	private String secondsToMinutesAndSeconds(double seconds) {
		int numberOfminutes = (int) (seconds / 60);
		int numberOfSeconds = (int) (seconds % 60);
		if (numberOfSeconds < 10) {
			return numberOfminutes + ":0" + numberOfSeconds;
		} else {
			return numberOfminutes + ":" + numberOfSeconds;
		}
	}

	/**
	 * Enables all mixer sliders and audio player
	 */
	public void enableMixerSlidersAndAudioPlayer() {
		paneMixerAudioPlayer.setDisable(false);
		paneMixerSliders.setDisable(false);
		buttonBarResetSaveLoad.setDisable(false);
	}

	/**
	 * Disables all mixer sliders
	 * 
	 * @param trueOrFalse - to be or not to be (disabled)
	 */
	public void setDisableMixerSliders(boolean trueOrFalse) {
		paneMixerSliders.setDisable(trueOrFalse);
	}

	/**
	 * Sets value property listeners to every slider and sets event listeners to
	 * input text fields
	 */
	private void initializeSlidersAndTextFields() {
		sliderPitch.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetPitchFactor(newValue.doubleValue());
				textFieldPitch.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});

		sliderGain.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetGain(newValue.doubleValue());
				textFieldGain.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});

		sliderEchoLength.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetEchoLength(newValue.doubleValue());
				textFieldEchoLength.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});

		sliderDecay.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetDecay(newValue.doubleValue());
				textFieldDecay.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});

		sliderFlangerLength.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetFlangerLength(newValue.doubleValue());
				textFieldFlangerLength.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});

		sliderWetness.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetWetness(newValue.doubleValue());
				textFieldWetness.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});

		sliderLfoFrequency.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetLFO(newValue.doubleValue());
				textFieldLfo.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});

		sliderLowPass.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetLowPass(newValue.floatValue());
				textFieldLowPass.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});

		sliderAudioFileDuration.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				if (sliderAudioFileDuration.isPressed()) {
					controller.timerCancel();

					controller.audioManipulatorPlayFromDesiredSec(sliderAudioFileDuration.getValue());

					audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderAudioFileDuration.getValue());
					textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
				}
			}
		});

		// On key methods for every text field

		textFieldPitch.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldPitch();
			}
		});

		textFieldGain.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldGain();
			}
		});

		textFieldEchoLength.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldEchoLength();
			}
		});

		textFieldDecay.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldDecay();
			}
		});

		textFieldFlangerLength.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldFlangerLength();
			}
		});

		textFieldWetness.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldWetness();
			}
		});

		textFieldLfo.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldLfo();
			}
		});

		textFieldLowPass.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldLowPass();
			}
		});
	}

	/**
	 * Sets a tooltip to every info button
	 */
	private void initializeTooltips() {
		tooltipPitch = new Tooltip();
		buttonInfoPitch.setTooltip(tooltipPitch);

		tooltipGain = new Tooltip();
		buttonInfoGain.setTooltip(tooltipGain);

		tooltipEcho = new Tooltip();
		buttonInfoEcho.setTooltip(tooltipEcho);

		tooltipFlanger = new Tooltip();
		buttonInfoFlanger.setTooltip(tooltipFlanger);

		tooltipLowPass = new Tooltip();
		buttonInfoLowPass.setTooltip(tooltipLowPass);
	}

	/**
	 * Localization for every text element and decimal formatter in Mixer
	 */
	private void initializeMixerLocalization() {
		try {
			mixerTab.setText(bundle.getString("mixerTab"));

			labelSelectFile.setText(bundle.getString("mixerSelectFileText"));
			labelOr.setText(bundle.getString("mixerOrText"));
			labelRecordFile.setText(bundle.getString("mixerRecordFileText"));

			if (mixerSelectedFile != null) {
				if (mixerSelectedFile.getName().equals("mixer_default.wav")) {
					labelSelectedFile.setText(bundle.getString("mixerFileSelectedText") + " "
							+ bundle.getString("mixerSelectedRecordingText"));
			
				} else {
					labelSelectedFile
							.setText(bundle.getString("mixerFileSelectedText") + " " + mixerSelectedFile.getName());
				
				}
			} else {
				labelSelectedFile.setText(bundle.getString("mixerFileNotSelectedText"));
			}

			labelTryMixer.setText(bundle.getString("mixerTryMixerText"));

			labelPitch.setText(bundle.getString("mixerPitchText"));
			labelEcho.setText(bundle.getString("mixerEchoText"));
			labelDecay.setText(bundle.getString("mixerDecayText"));
			labelEchoLength.setText(bundle.getString("mixerEchoLengthText"));
			labelLowPass.setText(bundle.getString("mixerLowPassText"));
			labelFlanger.setText(bundle.getString("mixerFlangerText"));
			labelWetness.setText(bundle.getString("mixerWetnessText"));
			labelFlangerLength.setText(bundle.getString("mixerFlangerLengthText"));
			labelLfo.setText(bundle.getString("mixerLfoText"));
			labelGain.setText(bundle.getString("mixerGainText"));

			buttonMixerFileOpener.setText(bundle.getString("mixerFileButton"));
			if (toggleButtonMixerStartRecording.isSelected()) {
				toggleButtonMixerStartRecording.setText(bundle.getString("mixerStopRecordButton"));
			} else {
				toggleButtonMixerStartRecording.setText(bundle.getString("mixerStartRecordButton"));
			}

			if (toggleButtonTestFilter.isSelected()) {
				toggleButtonTestFilter.setText(bundle.getString("mixerButtonTryMixerStopText"));
			} else {
				toggleButtonTestFilter.setText(bundle.getString("mixerButtonTryMixerStartText"));
			}

			buttonMixerResetSliders.setText(bundle.getString("mixerResetSlidersButton"));
			buttonSaveSettings.setText(bundle.getString("mixerSaveSettingsButton"));
			buttonLoadSettings.setText(bundle.getString("mixerLoadSettingsButton"));
			buttonSaveMixedFile.setText(bundle.getString("mixerSaveFileButton"));

			tooltipPitch.setText(bundle.getString("mixerPitchTooltip"));
			tooltipGain.setText(bundle.getString("mixerGainTooltip"));
			tooltipEcho.setText(bundle.getString("mixerEchoTooltip"));
			tooltipFlanger.setText(bundle.getString("mixerFlangerTooltip"));
			tooltipLowPass.setText(bundle.getString("mixerLowPassTooltip"));

			symbols = new DecimalFormatSymbols(bundle.getLocale());
			decimalFormat = new DecimalFormat("#0.00", symbols);

			textFieldPitch
					.setText(decimalFormat.format(Double.parseDouble(textFieldPitch.getText().replace(',', '.'))));
			textFieldEchoLength
					.setText(decimalFormat.format(Double.parseDouble(textFieldEchoLength.getText().replace(',', '.'))));
			textFieldDecay
					.setText(decimalFormat.format(Double.parseDouble(textFieldDecay.getText().replace(',', '.'))));
			textFieldLowPass
					.setText(decimalFormat.format(Double.parseDouble(textFieldLowPass.getText().replace(',', '.'))));
			textFieldWetness
					.setText(decimalFormat.format(Double.parseDouble(textFieldWetness.getText().replace(',', '.'))));
			textFieldFlangerLength.setText(
					decimalFormat.format(Double.parseDouble(textFieldFlangerLength.getText().replace(',', '.'))));
			textFieldLfo.setText(decimalFormat.format(Double.parseDouble(textFieldLfo.getText().replace(',', '.'))));
			textFieldGain.setText(decimalFormat.format(Double.parseDouble(textFieldGain.getText().replace(',', '.'))));

			if (bundle.getLocale().toString().equals("fi_FI")) {
				checkmarkFinnish.setVisible(true);
				checkmarkEnglish.setVisible(false);
			} else if (bundle.getLocale().toString().equals("en_US")) {
				checkmarkEnglish.setVisible(true);
				checkmarkFinnish.setVisible(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/******* MIXER METHODS END HERE ************/

	// RECORDER METHODS START HERE/////////////////////////////////////

	/**
	 * @author Onni Lukkarila
	 */

	@FXML
	private Button recorderButtonPlay;
	@FXML
	private Button recorderButtonPause;
	@FXML
	private Button recorderButtonStop;
	@FXML
	private Button recorderButtonSave;
	@FXML
	private ToggleButton recorderToggleButtonStartRecording;
	@FXML
	private ToggleButton recorderButtonPauseRecord;
	@FXML
	private Text textRecordFileDuration;
	@FXML
	private Text textRecordingDuration;
	@FXML
	private Slider sliderRecordedFileDuration;

	private Timer timer;

	/**
	 * Method to start and stop recording your voice
	 */
	@FXML
	public void recordAudioToggle() {
		if (recorderToggleButtonStartRecording.isSelected()) {

			textRecordFileDuration.setDisable(true);
			sliderRecordedFileDuration.setDisable(true);
			recorderButtonPause.setDisable(true);
			recorderButtonPlay.setDisable(true);
			recorderButtonStop.setDisable(true);
			recorderButtonSave.setDisable(true);

			controller.recordAudio();
			timer = new Timer();
			TimerTask task = new TimerTask() {
				int i = 0;

				@Override
				public void run() {
					textRecordingDuration.setText("" + i);
					i++;
				}
			};
			timer.schedule(task, 0, 1000);
		} else {
			timer.cancel();
			textRecordingDuration.setText("0");
			controller.stopRecord();
			enableRecorderPlayer();
			File file = null;
			file = new File("src/audio/recorder_default.wav").getAbsoluteFile();
			file.deleteOnExit();

			if (file != null) {
				try {
					AudioFormat format = AudioSystem.getAudioFileFormat(file.getAbsoluteFile()).getFormat();
					float audioFileLengthInSec = file.length() / (format.getFrameSize() * format.getFrameRate());
					controller.recorderSetAudioFileDuration(audioFileLengthInSec);
					setMaxValueToRecordDurationSlider(audioFileLengthInSec);
					audioFileDurationString = secondsToMinutesAndSeconds(audioFileLengthInSec);
					textRecordFileDuration.setText("0:00 / " + audioFileDurationString);
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * Method for disabling/enabling correct buttons
	 */
	@FXML
	public void recorderPlayAudio() {
		controller.audioRecorderPlayAudio();
		recorderButtonPlay.setDisable(true);
		recorderButtonPause.setDisable(false);
		recorderButtonStop.setDisable(false);
	}

	/**
	 * Method for disabling/enabling correct buttons
	 */
	@FXML
	public void recorderStopAudio() {
		controller.recorderTimerCancel();
		recorderButtonPlay.setDisable(false);
		recorderButtonPause.setDisable(true);
		recorderButtonStop.setDisable(true);
		controller.audioRecorderStopAudio();
	}

	/**
	 * Method for disabling/enabling correct buttons
	 */
	@FXML
	public void recorderPauseAudio() {
		controller.recorderTimerCancel();
		recorderButtonPlay.setDisable(false);
		recorderButtonPause.setDisable(true);
		recorderButtonStop.setDisable(false);
		controller.audioRecorderPauseAudio();
	}

	/**
	 * Method for disabling/enabling correct buttons
	 */
	private void enableRecorderPlayer() {
		sliderRecordedFileDuration.setDisable(false);
		textRecordFileDuration.setDisable(false);
		recorderButtonPlay.setDisable(false);
		recorderButtonSave.setDisable(false);
	}

	/**
	 * Method that tells AudioRecorder class that the slider has been pressed
	 */
	public void recorderSliderPressed() {
		controller.recorderSliderPressed();
	}

	/**
	 * Method that initializes a listener for mediaplayerslider
	 */
	public void initializeRecorderListener() {
		sliderRecordedFileDuration.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {

				if (sliderRecordedFileDuration.isPressed()) {
					recorderSliderPressed();
					controller.recorderTimerCancel();
					double currentPoint = sliderRecordedFileDuration.getValue();
					sliderRecordedFileDuration.setValue(currentPoint);
					controller.recorderPlayFromDesiredSec(currentPoint);
					audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderRecordedFileDuration.getValue());
					textRecordFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
				}

			}
		});
	}

	/**
	 * Method for saving a recorded file locally, opens a file chooser window
	 */
	public void saveRecordedFile() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("WAV files (*.wav)", ".wav");
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showSaveDialog(mainContainer.getScene().getWindow());
		String fullPath;
		try {
			fullPath = file.getAbsolutePath();
			if (!fullPath.endsWith(".wav")) {
				fullPath = fullPath + ".wav";
			}
			controller.saveAudioFile(fullPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method that sets the correct maximum value to mediaplayerslider
	 * 
	 * @param audioFileLengthInSec
	 */
	public void setMaxValueToRecordDurationSlider(double audioFileLengthInSec) {
		sliderRecordedFileDuration.setMax(audioFileLengthInSec);
	}

	/**
	 * Method that sets the current time to the mediaplayerslider
	 * 
	 * @param currentSeconds
	 */
	public void setCurrentValueToRecordDurationSlider(double currentSeconds) {
		sliderRecordedFileDuration.setValue(currentSeconds);
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderRecordedFileDuration.getValue());
		textRecordFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
	}

	/**
	 * Method that makes mandatory changes when playback has reached end
	 */
	public void recorderAudioFileReachedEnd() {
		setCurrentValueToRecordDurationSlider(0);
		recorderButtonPlay.setDisable(false);
		recorderButtonPause.setDisable(true);
		recorderButtonStop.setDisable(true);
	}

	/**
	 * Method that initializes recorder localization
	 */
	private void initializeRecorderLocalization() {

		try {
			recorderTab.setText(bundle.getString("recorderTab"));

			// Following methods have been commented out because the strings were replaced
			// by images
			/*
			 * recorderToggleButtonStartRecording.setText(bundle.getString("recorderRecord")
			 * ); recorderButtonPause.setText(bundle.getString("recorderPause"));
			 * recorderButtonPlay.setText(bundle.getString("recorderPlay"));
			 * recorderButtonStop.setText(bundle.getString("recorderStop"));
			 */

			recorderButtonSave.setText(bundle.getString("recorderSave"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//// RECORDER METHODS END
	//// HERE////////////////////////////////////////////////////////////////

	/**
	 * @author Joonas Soininen
	 */

	/**
	 * Following variables and methods mostly open new stages and handle slider
	 * values back and forth from files. More specific explanations with each
	 * method.
	 */
	@FXML
	private Label loggedinuser;
	@FXML
	private MenuItem userSettings;
	private MenuButton userMenuButton;
	private MenuItem menu1;
	private MenuItem menu2;
	@FXML
	private MenuItem loginoption;
	@FXML
	private Button closeButton;

	/**
	 * Method sets localization parameters for the variables.
	 */
	public void initializeMenuItems() {
		userMenuButton = new MenuButton();
		menu1 = new MenuItem(bundle.getString("mVmenu1")); //
		menu2 = new MenuItem(bundle.getString("mVmenu2")); //
	}

	/*
	 * Method changes localization parameters for the variables.
	 */
	public void setLocalizedLanguageMenuItems() {
		if (!userMenuButton.getText().equals("")) {
			loggedinuser.setText(bundle.getString("mVloggedin"));
			menu1.setText(bundle.getString("mVmenu1"));
			menu2.setText(bundle.getString("mVmenu2"));
		}
	}

	/**
	 * Method opens a new stage of the login and register view.
	 */
	public void openLoginRegister() {
		controller.intializeDatabaseConnection();
		if (controller.isConnected()) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RegisterLoginView.fxml"));
				fxmlLoader.setResources(bundle);
				Parent root1 = (Parent) fxmlLoader.load();
				Stage stage = new Stage();
				RegisterLoginController rlc = fxmlLoader.getController();
				rlc.setMainController(this);
				stage.initModality(Modality.APPLICATION_MODAL);
				// stage.initStyle(StageStyle.UNDECORATED);
				stage.setTitle(bundle.getString("rLPleaseLabel"));
				stage.setScene(new Scene(root1));
				stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Alert alert1 = new Alert(AlertType.ERROR);
			alert1.setTitle(bundle.getString("mVDataConAlertTitle"));
			alert1.setHeaderText(bundle.getString("mVDataConAlertHeader"));
			alert1.setContentText(bundle.getString("mVDataConAlertContent"));
			alert1.showAndWait();
		}

	}

	/**
	 * Method opens a new stage of the mixer settings lists.
	 */
	public void openMixerSettings() {
		controller.intializeDatabaseConnection();
		if (controller.isConnected()) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MixerSettingsView.fxml"));
				fxmlLoader.setResources(bundle);
				Parent root1 = (Parent) fxmlLoader.load();
				Stage stage = new Stage();
				MixerSettingsController msc = fxmlLoader.getController();
				msc.setMainController(this);
				stage.initModality(Modality.APPLICATION_MODAL);
				// stage.initStyle(StageStyle.UNDECORATED);
				stage.setTitle("Mixer Settings Loader");
				stage.setScene(new Scene(root1));
				stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Alert alert1 = new Alert(AlertType.ERROR);
			alert1.setTitle(bundle.getString("mVDataConAlertTitle"));
			alert1.setHeaderText(bundle.getString("mVDataConAlertHeader"));
			alert1.setContentText(bundle.getString("mVDataConAlertContent"));
			alert1.showAndWait();
		}

	}

	/**
	 * Method to get current controller object.
	 * 
	 * @return the current set controller or null
	 */
	public Controller getController() {
		return this.controller;
	}

	/**
	 * Method opens a new scene where the mixer settings can be saved to the
	 * database
	 */
	public void openMixerSave() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SaveMixerSettings.fxml"));
			fxmlLoader.setResources(bundle);
			Parent root1 = (Parent) fxmlLoader.load();
			SaveMixerSettingsController smsc = fxmlLoader.getController();
			smsc.setMainController(this);
			smsc.getSettings(sliderPitch.getValue(), sliderEchoLength.getValue(), sliderDecay.getValue(),
					sliderGain.getValue(), sliderFlangerLength.getValue(), sliderWetness.getValue(),
					sliderLfoFrequency.getValue(), (float) sliderLowPass.getValue());
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			// stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle(bundle.getString("mSTitle"));
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method opens the save selection scene where user can decide to save settings
	 * locally or to the database.
	 */
	public void openSaveSelection() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SaveSelectionView.fxml"));
			fxmlLoader.setResources(bundle);
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			SaveSelectionController ssc = fxmlLoader.getController();
			ssc.setMainController(this);
			stage.initModality(Modality.APPLICATION_MODAL);
			// stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle(bundle.getString("sSHeaderLabel"));
			stage.setScene(new Scene(root1));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method opens a new scene where user can select where to load mixer settings,
	 * locally or from the database.
	 */
	public void openLoadSelection() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoadSelectionView.fxml"));
			fxmlLoader.setResources(bundle);
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			LoadSelectionController lsc = fxmlLoader.getController();
			lsc.setMainController(this);
			stage.initModality(Modality.APPLICATION_MODAL);
			// stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle(bundle.getString("lSTitle"));
			stage.setScene(new Scene(root1));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method opens user settings view where password can be changed or the account
	 * deleted.
	 */
	public void openUserSettings() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UserSettingsView.fxml"));
			fxmlLoader.setResources(bundle);
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			UserSettingsController usc = fxmlLoader.getController();
			usc.setMainController(this);
			stage.initModality(Modality.APPLICATION_MODAL);
			// stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle(bundle.getString("uSHeaderLabel"));
			stage.setScene(new Scene(root1));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to close the view when button is pressed
	 * 
	 * @param event, handles the on push events of binded buttons
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method to set the slider values with data from the database
	 * 
	 * @param pitch
	 * @param echo
	 * @param decay
	 * @param gain
	 * @param flangerLenght
	 * @param wetness
	 * @param lfoFrequency
	 * @param lowPass
	 */
	public void setSliderValues(double pitch, double echo, double decay, double gain, double flangerLenght,
			double wetness, double lfoFrequency, float lowPass) {
		sliderPitch.setValue(pitch);
		sliderEchoLength.setValue(echo);
		sliderDecay.setValue(decay);
		sliderGain.setValue(gain);
		sliderFlangerLength.setValue(flangerLenght);
		sliderWetness.setValue(wetness);
		sliderLfoFrequency.setValue(lfoFrequency);
		sliderLowPass.setValue(lowPass);
	}

	/**
	 * Method to store mixer settings locally
	 */
	public void saveMixerSettingsLocally() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showSaveDialog(mainContainer.getScene().getWindow());
		String fullPath;
		try {
			fullPath = file.getAbsolutePath();
			if (!fullPath.endsWith(".txt")) {
				fullPath = fullPath + ".txt";
			}
			try {
				FileWriter writeFile = new FileWriter(fullPath);
				writeFile.write(Double.toString(sliderPitch.getValue()) + "\n");
				writeFile.write(Double.toString(sliderEchoLength.getValue()) + "\n");
				writeFile.write(Double.toString(sliderDecay.getValue()) + "\n");
				writeFile.write(Double.toString(sliderGain.getValue()) + "\n");
				writeFile.write(Double.toString(sliderFlangerLength.getValue()) + "\n");
				writeFile.write(Double.toString(sliderWetness.getValue()) + "\n");
				writeFile.write(Double.toString(sliderLfoFrequency.getValue()) + "\n");
				writeFile.write(Float.toString((float) sliderLowPass.getValue()) + "\n");
				writeFile.close();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(bundle.getString("sMSsaveAlert1Title"));
				alert.setHeaderText(bundle.getString("sMSsaveAlert1Header"));
				alert.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(bundle.getString("mSSaveAlertTitle"));
				alert.setHeaderText(bundle.getString("mSSaveAlertHeader"));
				alert.setContentText(bundle.getString("mSSaveAlertContent"));
				alert.showAndWait();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Method to set visible user into main view and also set up different functions
	 * for logging out
	 */
	public void setlogUserIn() {
		menu1.setOnAction(event -> {
			openUserSettings();
		});
		menu2.setOnAction(event -> {
			setlogUserOut();
		});
		userMenuButton.setText(controller.loggedIn());
		userMenuButton.setStyle("-fx-font-size: 10pt; -fx-text-fill:black;"); // MUOTOILU CSSSSS????????N
		userMenuButton.getItems().addAll(menu1, menu2);
		loggedinuser.setVisible(true);
		loggedinuser.setText(bundle.getString("mVloggedin"));
		loggedinuser.setGraphic(userMenuButton);
		loggedinuser.setContentDisplay(ContentDisplay.RIGHT);
		userSettings.setVisible(true);
		loginoption.setVisible(false);
	}

	/**
	 * Method to change visibility of certain labels and menu items.
	 */
	public void setlogUserOut() {
		controller.logoutUser();
		userMenuButton.setText("");
		userMenuButton.setStyle("");
		userMenuButton.getItems().removeAll(menu1, menu2);
		loggedinuser.setVisible(false);
		userSettings.setVisible(false);
		loginoption.setVisible(true);

	}

	/**
	 * Loads soundboard from fxml and sets boardController as soundboards controller
	 * 
	 * @author Kevin Akkoyun
	 */
	@SuppressWarnings("static-access")
	public void loadSoundboard() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("/SoundboardView.fxml"));
			loader.setResources(bundle);
			AnchorPane temp = (AnchorPane) loader.load();
			boardController = loader.getController();
			soundboardRoot.getChildren().add(temp);
			soundboardRoot.setBottomAnchor(temp, 0.0);
			soundboardRoot.setTopAnchor(temp, 0.0);
			soundboardRoot.setLeftAnchor(temp, 0.0);
			soundboardRoot.setRightAnchor(temp, 0.0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
