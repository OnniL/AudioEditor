package otp.group6.view;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Pattern;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import otp.group6.AudioEditor.AudioFileHandler;
import otp.group6.AudioEditor.AudioRecorder;
import otp.group6.controller.*;

/**
 * Main controller for the view
 * 
 * @author Kevin Akkoyun
 * @version 0.1
 */
public class MainController {
	Controller controller;

	public MainController() {
		controller = new Controller(this);
	}

	/**
	 * FXML element variables
	 */
	@FXML
	AnchorPane mainContainer, soundboardContainer;
	@FXML
	Button newSoundButton;
	@FXML
	GridPane buttonGrid;

////// MIXER //////////////////////////////////////////////////////////////////////////////////////////////////////////
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
	private Slider sliderRecordedFileDuration;
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
	private Text textSelectedFile;
	@FXML
	private Text textAudioFileDuration;
	@FXML
	private Text textRecordFileDuration;
	@FXML
	private Text textRecordingDuration;
	@FXML
	private GridPane paneMixerSliders;
	@FXML
	private AnchorPane paneMixerAudioPlayer;
	@FXML
	private Button buttonPlay;
	@FXML
	private Button buttonPause;
	@FXML
	private Button buttonStop;
	@FXML
	private Button recorderButtonPlay;
	@FXML
	private Button recorderButtonPause;
	@FXML
	private Button recorderButtonStop;
	@FXML
	private Button recorderButtonSave;
	@FXML
	private ToggleButton recorderButtonPauseRecord;
	@FXML
	private Button buttonInfoPitch;
	@FXML
	private Button buttonInfoGain;
	@FXML
	private Button buttonInfoEchoLength;
	@FXML
	private Button buttonInfoDecay;
	@FXML
	private Button buttonInfoFlangerLength;
	@FXML
	private Button buttonInfoWetness;
	@FXML
	private Button buttonInfoLfo;
	@FXML
	private Button buttonInfoLowPass;

	// Muuttujat tiedoston kokonaiskestolle ja toistetulle ajalle
	private String audioFileDurationString = "0:00";
	private String audioFileProcessedTimeString = "0:00";
	private DecimalFormat decimalFormat = new DecimalFormat("#0.00"); // kaikki luvut kahden desimaalin tarkkuuteen

	private Boolean isRecording = false;
	private Timer timer;
	private float recordedFileProcessed;

	public void initializeMixer() {
		initializeSlidersAndTextFields();
		setTooltips();
		initializeRecorderListener();
	}

	// Methods for buttons
	@FXML
	public void soundManipulatorPlayAudio() {
		controller.soundManipulatorPlayAudio();
		buttonPlay.setDisable(true);
		buttonPause.setDisable(false);
		buttonStop.setDisable(false);
	}

	@FXML
	public void soundManipulatorStopAudio() {
		controller.soundManipulatorStopAudio();
		buttonPlay.setDisable(false);
		buttonPause.setDisable(true);
		buttonStop.setDisable(true);
	}

	@FXML
	public void soundManipulatorPauseAudio() {
		controller.soundManipulatorPauseAudio();
		buttonPlay.setDisable(false);
		buttonPause.setDisable(true);
		buttonStop.setDisable(false);
	}

	@FXML
	public void soundManipulatorTestFilter() {
		controller.testFilter();
	}

	@FXML
	public void soundManipulatorSaveMixedFile() {
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
			controller.soundManipulatorSaveFile(fullPath);
		} catch (Exception e) {

		}
	}

	@FXML
	public void soundManipulatorSaveMixerSettings() {
		// TODO Tämä tekemättä!!!
	}

	@FXML
	public void soundManipulatorOpenFile() {
		try {
			// Avataan file AudioFileHandlerilla ja välitetään file kontrollerille
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
			controller.soundManipulatorOpenFile(file);

			// Length of the audio file in seconds (file.length / (format.frameSize *
			// format.frameRate))
			AudioFormat format = AudioSystem.getAudioFileFormat(file.getAbsoluteFile()).getFormat();
			double audioFileLengthInSec = file.length() / (format.getFrameSize() * format.getFrameRate());
			audioFileDurationString = secondsToMinutesAndSeconds(audioFileLengthInSec);

			setMaxValueToAudioDurationSlider(audioFileLengthInSec);
			textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);

			// Shows the name of the file in textSelectedFile element
			textSelectedFile.setText("Selected file: " + file.getName());

			// Enables all sliders and audio player
			enableMixerSlidersAndAudioPlayer();

		} catch (UnsupportedAudioFileException e) {
			System.out.println("Väärä tiedostomuoto");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Virhe!");
			alert.setHeaderText("Väärä tiedostomuoto");
			alert.setContentText("Valitse vain WAV-tyyppisiä tiedostoja");
			alert.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Methods for getting TextField input values
	@FXML
	public void getTextFieldPitch() {
		String text = textFieldPitch.getText();
		try {
			double number = Double.parseDouble(text);
			if (number <= 4.0 && number >= 0.1) {
				controller.soundManipulatorSetPitchFactor(number);
				sliderPitch.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldGain() {
		String text = textFieldGain.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 5) {
				controller.soundManipulatorSetGain(number);
				sliderGain.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldEchoLength() {
		String text = textFieldEchoLength.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 5) {
				controller.soundManipulatorSetEchoLength(number);
				sliderEchoLength.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldDecay() {
		String text = textFieldDecay.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 1) {
				controller.soundManipulatorSetDecay(number);
				sliderDecay.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldFlangerLength() {
		String text = textFieldFlangerLength.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 1) {
				controller.soundManipulatorSetFlangerLength(number);
				sliderFlangerLength.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldWetness() {
		String text = textFieldWetness.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 1) {
				controller.soundManipulatorSetWetness(number);
				sliderWetness.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldLfo() {
		String text = textFieldLfo.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 100) {
				controller.soundManipulatorSetLFO(number);
				sliderLfoFrequency.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldLowPass() {
		String text = textFieldLowPass.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 44100) {
				controller.soundManipulatorSetLowPass((float) number);
				sliderLowPass.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void handleAudioFileDurationSliderClick() {
		controller.timerCancel();

		System.out.println("slideria klikattu " + sliderAudioFileDuration.getValue());
		controller.soundManipulatorPlayFromDesiredSec(sliderAudioFileDuration.getValue());

		// Nyk kesto tekstinä
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderAudioFileDuration.getValue());
		textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
	}

	@FXML
	public void soundManipulatorResetAllSliders() {
		sliderPitch.setValue(1);
		sliderGain.setValue(1);
		sliderEchoLength.setValue(0);
		sliderDecay.setValue(0);
		sliderFlangerLength.setValue(0);
		sliderWetness.setValue(0);
		sliderLfoFrequency.setValue(0);
		sliderLowPass.setValue(44100);
	}

	// Audio file sliderin metodit (controller kutsuu)
	public void setMaxValueToAudioDurationSlider(double audioFileLengthInSec) {
		sliderAudioFileDuration.setMax(audioFileLengthInSec);
	}

	public void setCurrentValueToAudioDurationSlider(double currentSeconds) {
		sliderAudioFileDuration.setValue(currentSeconds);
		if (currentSeconds == sliderAudioFileDuration.getMax()) {
			buttonPlay.setDisable(false);
			buttonStop.setDisable(true);
			buttonPause.setDisable(true);
		}
	}

	public void setCurrentPositionToAudioDurationText(double currentSeconds) {
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(currentSeconds);
		textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
	}

	// Apumetodeja
	private String secondsToMinutesAndSeconds(double seconds) {
		int numberOfminutes = (int) (seconds / 60);
		int numberOfSeconds = (int) (seconds % 60);
		if (numberOfSeconds < 10) {
			return numberOfminutes + ":0" + numberOfSeconds;
		} else {
			return numberOfminutes + ":" + numberOfSeconds;
		}
	}

	private void enableMixerSlidersAndAudioPlayer() {
		paneMixerAudioPlayer.setDisable(false);
		paneMixerSliders.setDisable(false);
	}

	private void initializeSlidersAndTextFields() {
		// Pitch slider
		sliderPitch.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetPitchFactor(newValue.doubleValue());
				textFieldPitch.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		// Gain slider
		sliderGain.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetGain(newValue.doubleValue());
				textFieldGain.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		// Echo length slider
		sliderEchoLength.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetEchoLength(newValue.doubleValue());
				textFieldEchoLength.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		// Echo decay slider
		sliderDecay.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetDecay(newValue.doubleValue());
				textFieldDecay.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		// Flanger length slider
		sliderFlangerLength.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetFlangerLength(newValue.doubleValue());
				textFieldFlangerLength.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		// Wetness slider
		sliderWetness.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetWetness(newValue.doubleValue());
				textFieldWetness.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		//Lfo slider
		sliderLfoFrequency.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetLFO(newValue.doubleValue());
				textFieldLfo.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		//LowPass slider
		sliderLowPass.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetLowPass(newValue.floatValue());
				textFieldLowPass.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});

		//AudioFileDuration slider
		sliderAudioFileDuration.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				if (sliderAudioFileDuration.isPressed()) {
					controller.timerCancel();

					System.out.println("slideria klikattu " + sliderAudioFileDuration.getValue());
					controller.soundManipulatorPlayFromDesiredSec(sliderAudioFileDuration.getValue());

					// Nyk kesto tekstinä
					audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderAudioFileDuration.getValue());
					textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
				}
			}
		});
	}


	/*
	 * Sets a tooltip to every info button
	 */
	private void setTooltips() {
		final Tooltip tooltipPitch = new Tooltip();
		tooltipPitch.setText("Pitch");

		final Tooltip tooltipGain = new Tooltip();
		tooltipGain.setText("Gain");

		final Tooltip tooltipEchoLength = new Tooltip();
		tooltipEchoLength.setText("Echo");

		final Tooltip tooltipDecay = new Tooltip();
		tooltipDecay.setText("Decay");

		final Tooltip tooltipFlangerLength = new Tooltip();
		tooltipFlangerLength.setText("Flanger Length");

		final Tooltip tooltipWetness = new Tooltip();
		tooltipWetness.setText("Wet");

		final Tooltip tooltipLfo = new Tooltip();
		tooltipLfo.setText("LFO");

		final Tooltip tooltipLowPass = new Tooltip();
		tooltipLowPass.setText("Low Pass");

		buttonInfoPitch.setTooltip(tooltipPitch);
		buttonInfoGain.setTooltip(tooltipGain);
		buttonInfoEchoLength.setTooltip(tooltipEchoLength);
		buttonInfoDecay.setTooltip(tooltipDecay);
		buttonInfoFlangerLength.setTooltip(tooltipFlangerLength);
		buttonInfoWetness.setTooltip(tooltipWetness);
		buttonInfoLfo.setTooltip(tooltipLfo);
		buttonInfoLowPass.setTooltip(tooltipLowPass);
	}



	//// MIXER METHODS END HERE
	//// /////////////////////////////////////////////////////////////////////////////////////

	// RECORDER METHODS START HERE/////////////////////////////////////

	@FXML
	public void recordAudioToggle() {

		if (!isRecording) {
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
			isRecording = true;

		} else {
			timer.cancel();
			textRecordingDuration.setText("0");
			controller.stopRecord();
			isRecording = false;
			enableRecorderPlayer();
			File file = null;
			file = new File("src/audio/default.wav").getAbsoluteFile();
			file.deleteOnExit();

			if (file != null) {
				try {
					AudioFormat format = AudioSystem.getAudioFileFormat(file.getAbsoluteFile()).getFormat();
					double audioFileLengthInSec = file.length() / (format.getFrameSize() * format.getFrameRate());
					setMaxValueToRecordDurationSlider(audioFileLengthInSec);
					audioFileDurationString = secondsToMinutesAndSeconds(audioFileLengthInSec);
					textAudioFileDuration.setText(": / " + audioFileDurationString);

					textRecordFileDuration.setText("0:00 / " + audioFileDurationString);
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		}

	}

	public void recorderPauseRecord() {
		if (recorderButtonPauseRecord.isPressed() == false) {
			controller.pauseRecord();
		} else {
			controller.resumeRecord();
		}
	}

	@FXML
	public void recorderPlayAudio() {
		controller.audioRecorderPlayAudio();
		recorderButtonPlay.setDisable(true);
		recorderButtonPause.setDisable(false);
		recorderButtonStop.setDisable(false);
	}

	@FXML
	public void recorderStopAudio() {
		controller.recorderTimerCancel();
		recorderButtonPlay.setDisable(false);
		recorderButtonPause.setDisable(true);
		recorderButtonStop.setDisable(true);
		controller.audioRecorderStopAudio();
	}

	@FXML
	public void recorderPauseAudio() {
		controller.recorderTimerCancel();
		recorderButtonPlay.setDisable(false);
		recorderButtonPause.setDisable(true);
		recorderButtonStop.setDisable(false);
		controller.audioRecorderPauseAudio();
	}

	private void enableRecorderPlayer() {
		sliderRecordedFileDuration.setDisable(false);
		textRecordFileDuration.setDisable(false);
		recorderButtonPlay.setDisable(false);
		recorderButtonSave.setDisable(false);
	}

	private void updateRecorderSlider() {
		sliderRecordedFileDuration.setValue(controller.getRecorderSecondsProcessed());
	}

	public void recorderSliderPressed() {
		controller.recorderSliderPressed();
	}

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

	public void setMaxValueToRecordDurationSlider(double audioFileLengthInSec) {
		sliderRecordedFileDuration.setMax(audioFileLengthInSec);
	}

	public void setCurrentValueToRecordDuratinSlider(double currentSeconds) {
		sliderRecordedFileDuration.setValue(currentSeconds);
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderRecordedFileDuration.getValue());
		textRecordFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
	}

	//// RECORDER METHODS END
	//// HERE////////////////////////////////////////////////////////////////

	public void openFile(int index) {
		Pattern pattern = Pattern.compile("(\\.wav)$", Pattern.CASE_INSENSITIVE);
		try {
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());

			// File must be not null to add button
			if (file != null) {
				Matcher matcher = pattern.matcher(file.getName());
				if (matcher.find()) {
					controller.editSample(file.getAbsolutePath(), index);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openSample() {
		// REGEX pattern for ".wav"
		Pattern pattern = Pattern.compile("(\\.wav)$", Pattern.CASE_INSENSITIVE);
		try {
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());

			// File must be not null to add button
			if (file != null) {
				Matcher matcher = pattern.matcher(file.getName());
				if (matcher.find()) {
					controller.addSample(file.getAbsolutePath());
					addButton(controller.getSampleArrayLength() - 1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addButton(int index) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApplication.class.getResource("SoundBoardButton.fxml"));

		AnchorPane gridRoot = (AnchorPane) buttonGrid.getChildren().get(index);
		Node soundButtonRoot;

		try {
			soundButtonRoot = (Node) loader.load();

			Button temp = (Button) gridRoot.getChildren().remove(0);

			if (index < buttonGrid.getChildren().size() - 1) {
				AnchorPane ap = (AnchorPane) buttonGrid.getChildren().get(index + 1);
				ap.getChildren().add(temp);
			}

			gridRoot.getChildren().add(soundButtonRoot);
			configureSoundButton((AnchorPane) soundButtonRoot, index);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	public void configureSoundButton(AnchorPane ap, int index) {
		Button play = (Button) ap.getChildren().get(0);
		Text description = (Text) ap.getChildren().get(1);
		MenuButton mp = (MenuButton) ap.getChildren().get(2);

		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.playSound(index);
			}
		});
		description.setText(controller.getSampleName(index));
		description.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				renameButton(description, ap, index);
			}

		});

		MenuItem editButton = (MenuItem) mp.getItems().get(0);
		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				openFile(index);
			}

		});

		MenuItem deleteButton = mp.getItems().get(1);
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				controller.removeSample(index);
				refreshButtons();
				removeLast();
			}

		});

		// add edit and delete functionality
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void renameButton(Text text, AnchorPane ap, int index) {

		TextField tf = new TextField();
		tf.setText(text.getText());
		tf.forward();
		ChangeListener cl = new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {

				if (!tf.isFocused()) {
					String temp = tf.getText();
					if (temp.length() > 20) {
						temp = temp.substring(0, 20);
					}
					text.setText(temp);
					controller.setSampleName(index, temp);
					ap.getChildren().remove(ap.getChildren().indexOf(tf));
				}
			}
		};
		tf.focusedProperty().addListener(cl);
		tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					String temp = tf.getText();
					if (temp.length() > 20) {
						temp = temp.substring(0, 20);
					}
					text.setText(temp);
					controller.setSampleName(index, temp);
					tf.focusedProperty().removeListener(cl);
					ap.getChildren().remove(ap.getChildren().indexOf(tf));
				}
			}

		});
		ap.getChildren().add(tf);
		tf.layoutXProperty().set(text.getLayoutX());
		tf.layoutYProperty().set(text.getLayoutY() - 20);
		tf.requestFocus();
	}

	public void refreshButtons() {
		int length = controller.getSampleArrayLength();
		for (int i = 0; i < length; i++) {
			AnchorPane gridRoot = (AnchorPane) buttonGrid.getChildren().get(i);
			AnchorPane root = (AnchorPane) gridRoot.getChildren().get(0);
			Text description = (Text) root.getChildren().get(1);
			description.setText(controller.getSampleName(i));
		}
	}

	public void removeLast() {
		int length = controller.getSampleArrayLength();
		AnchorPane gridRoot = (AnchorPane) buttonGrid.getChildren().get(length);
		if (length + 1 < buttonGrid.getChildren().size()) {
			AnchorPane newSoundRoot = (AnchorPane) buttonGrid.getChildren().get(length + 1);
			Button temp = (Button) newSoundRoot.getChildren().remove(0);

			gridRoot.getChildren().remove(0);
			gridRoot.getChildren().add(temp);
		} else {
			gridRoot.getChildren().remove(0);
			gridRoot.getChildren().add(newSoundButton);
		}
	}

	/**
	 * Joonaksen tekemiä lisäyksiä
	 */

	@FXML
	private Slider slider1;
	@FXML
	private Slider slider2;
	@FXML
	private Slider slider3;
	@FXML
	private Slider slider4;
	@FXML
	private Slider slider5;
	@FXML
	private Slider slider6;

	/**
	 * Method opens a new scene Login and Register form
	 */
	public void openLoginRegister() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("\\RegisterLoginView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Login or Register");
			stage.setScene(new Scene(root1));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method opens a new scene, the mixer settings from the database
	 */
	public void openMixerSettings() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("\\MixerSettingsView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Mixer Settings Loader");
			stage.setScene(new Scene(root1));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open a new scene where the mixer settings can be saved to the database
	 */
	public void openMixerSave() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("\\SaveMixerSettings.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			SaveMixerSettingsController smsc = fxmlLoader.getController();
			smsc.getSettings(slider1.getValue(), slider2.getValue(), slider3.getValue(), slider4.getValue(),
					slider5.getValue(), slider6.getValue());

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Save Mixer Settings");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Does a query to the database class to check for a logged in user
	 */
	public void checkForloggedin() {
		if (!(controller.loggedIn() == " ")) {
			openMixerSave();
		} else {
			openLoginRegister();
		}
	}

	@FXML
	private Button closeButton;

	/**
	 * Method to close open scenes
	 * 
	 * @param event
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}

	// TODO tarvitaan arvojen asettamiselle tapa!
	/**
	 * Method to set the slider values from the database stored settings
	 * 
	 * @param set1
	 * @param set2
	 * @param set3
	 * @param set4
	 * @param set5
	 * @param set6
	 */
	public void setSliderValues(double set1, double set2, double set3, double set4, double set5, double set6) {

	}
}
