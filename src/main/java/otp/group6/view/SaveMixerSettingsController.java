package otp.group6.view;

import javafx.scene.control.TextArea;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import otp.group6.controller.Controller;

/**
 * 
 * Class handles saving mixer settings locally and to the database
 * 
 * @author Joonas Soininen
 *
 */
public class SaveMixerSettingsController implements Initializable{
	Controller controller;
	MainController mc;
	//Properties-muuttujat alkaa
	@FXML
	private Label sMSPitchLabel;
	@FXML
	private Label sMSEchoLLabel;
	@FXML
	private Label sMSEchoDLabel;
	@FXML
	private Label sMSGainLabel;
	@FXML
	private Label sMSFlangerLabel;
	@FXML
	private Label sMSWetnessLabel;
	@FXML
	private Label sMSLFOLabel;
	@FXML
	private Label sMSLowLabel;
	@FXML
	private Label sMSMixCreatorLabel;
	@FXML
	private Label sMSMixNameLabel;
	@FXML
	private TextField sMSMixNTextField;
	@FXML
	private Label sMSRequiredLable;
	@FXML
	private Label sMSDescLabel;
	@FXML
	private Button sMSSaveButton;
	@FXML
	private Button sMSCancelButton;
	private String sMSsaveAlert1Title;
	private String sMSsaveAlert1Header;
	private String sMSsaveAlert1Content;
	private String sMSsaveAlert2Title;
	private String sMSsaveAlert2Header;
	private String sMSsaveAlert2Content;
	private String sMSsaveAlert3Title;
	private String sMSsaveAlert3Header;
	private String sMSsaveAlert3Content;
	//Properties-muuttujat päättyy
	@FXML
	private Label pitchValue;
	@FXML
	private Label echoValue;
	@FXML
	private Label decayValue;
	@FXML
	private Label gainValue;
	@FXML
	private Label flangerLenghtValue;
	@FXML
	private Label wetnessValue;
	@FXML
	private Label lfoFrequencyValue;
	@FXML
	private Label lowPassValue;
	@FXML
	private Label name;
	@FXML
	private TextArea description;

	private double pitch, echo, decay, gain, flangerLenght, wetness, lfoFrequency;
	private float lowPass;

	final Tooltip sMSmixNameField = new Tooltip();
	
	/**
	 * Method initializes this class when loaded, calls {@link #setLocalization(ResourceBundle)} to set certain variables passing the ResourceBundle to it.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setLocalization(arg1);
	}
	
	/**
	 * Method sets all visible texts and button labels to chosen language.
	 * @param bundle transfers the localization data to the method so the right language is set
	 */
	private void setLocalization(ResourceBundle bundle) {
		sMSPitchLabel.setText(bundle.getString("sMSPitchLabel"));
		sMSEchoLLabel.setText(bundle.getString("sMSEchoLLabel"));
		sMSEchoDLabel.setText(bundle.getString("sMSEchoDLabel"));
		sMSGainLabel.setText(bundle.getString("sMSGainLabel"));
		sMSFlangerLabel.setText(bundle.getString("sMSFlangerLabel"));
		sMSWetnessLabel.setText(bundle.getString("sMSWetnessLabel"));
		sMSLFOLabel.setText(bundle.getString("sMSLFOLabel"));
		sMSLowLabel.setText(bundle.getString("sMSLowLabel"));
		sMSMixCreatorLabel.setText(bundle.getString("sMSMixCreatorLabel"));
		sMSMixNameLabel.setText(bundle.getString("sMSMixNameLabel"));
		sMSMixNTextField.setPromptText(bundle.getString("sMSMixNTextField"));
		sMSRequiredLable.setText(bundle.getString("sMSRequiredLable"));
		sMSDescLabel.setText(bundle.getString("sMSDescLabel"));
		sMSSaveButton.setText(bundle.getString("sMSSaveButton"));
		sMSCancelButton.setText(bundle.getString("sMSCancelButton"));
		sMSmixNameField.setText(bundle.getString("sMSmixNameField"));
		sMSMixNameLabel.requestFocus();
		sMSsaveAlert1Title=bundle.getString("sMSsaveAlert1Title");
		sMSsaveAlert1Header=bundle.getString("sMSsaveAlert1Header");
		sMSsaveAlert1Content=bundle.getString("sMSsaveAlert1Content");
		sMSsaveAlert2Title=bundle.getString("sMSsaveAlert2Title");
		sMSsaveAlert2Header=bundle.getString("sMSsaveAlert2Header");
		sMSsaveAlert2Content=bundle.getString("sMSsaveAlert2Content");
		sMSsaveAlert3Title=bundle.getString("sMSsaveAlert3Title");
		sMSsaveAlert3Header=bundle.getString("sMSsaveAlert3Header");
		sMSsaveAlert3Content=bundle.getString("sMSsaveAlert3Content");
	}

	/**
	 * Method sets the maincontroller used in the main view Opens the database
	 * connection
	 * 
	 * @param mainController
	 */
	public void setMainController(MainController mainController) {
		this.mc = mainController;
		this.controller = mc.getController();
		controller.intializeDatabaseConnection();
	}

	/**
	 * Method is used to set the values from the main views sliders
	 * 
	 * @param mix1
	 * @param mix2
	 * @param mix3
	 * @param mix4
	 * @param mix5
	 * @param mix6
	 */
	public void getSettings(double sliderPitch, double sliderEchoLength, double sliderDecay, double sliderGain,
			double sliderFlangerLength, double sliderWetness, double sliderLfoFrequency, float sliderLowPass) {
		pitchValue.setText(String.valueOf(sliderPitch));
		echoValue.setText(String.valueOf(sliderEchoLength));
		decayValue.setText(String.valueOf(sliderDecay));
		gainValue.setText(String.valueOf(sliderGain));
		flangerLenghtValue.setText(String.valueOf(sliderFlangerLength));
		wetnessValue.setText(String.valueOf(sliderWetness));
		lfoFrequencyValue.setText(String.valueOf(sliderLfoFrequency));
		lowPassValue.setText(String.valueOf(sliderLowPass));
		name.setText(controller.loggedIn());

		this.pitch = sliderPitch;
		this.echo = sliderEchoLength;
		this.decay = sliderDecay;
		this.gain = sliderGain;
		this.flangerLenght = sliderFlangerLength;
		this.wetness = sliderWetness;
		this.lfoFrequency = sliderLfoFrequency;
		this.lowPass = sliderLowPass;

		sMSmixNameField.setWrapText(true);
		sMSmixNameField.setTextOverrun(OverrunStyle.ELLIPSIS);
		sMSMixNTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					sMSmixNameField.show(sMSMixNTextField, //
							// popup tooltip on the right, you can adjust these values for different
							// positions
							sMSMixNTextField.getScene().getWindow().getX() + sMSMixNTextField.getLayoutX() + sMSMixNTextField.getWidth() - 220, //
							sMSMixNTextField.getScene().getWindow().getY() + sMSMixNTextField.getLayoutY() + sMSMixNTextField.getHeight() + 200);
				} else {
					sMSmixNameField.hide();
				}
			}
		});

	}

	/**
	 * Method stores mixer settings to the database
	 * 
	 * @throws SQLException
	 */
	public void saveMix() throws SQLException {

		if (!(checkEmpty(sMSMixNTextField.getText()))) {
			if (sMSMixNTextField.getText().length() < 1) {
				sMSmixNameField.setWrapText(true);
				sMSmixNameField.setTextOverrun(OverrunStyle.ELLIPSIS);
				sMSMixNTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
						if (newValue) {
							sMSmixNameField.show(sMSMixNTextField, //
									// popup tooltip on the right, you can adjust these values for different
									// positions
									sMSMixNTextField.getScene().getWindow().getX() + sMSMixNTextField.getLayoutX() + sMSMixNTextField.getWidth() - 220, //
									sMSMixNTextField.getScene().getWindow().getY() + sMSMixNTextField.getLayoutY() + sMSMixNTextField.getHeight()
											+ 200);
						} else {
							sMSmixNameField.hide();
						}
					}
				});

			} else {
				if (controller.createMix(sMSMixNTextField.getText(), description.getText(), pitch, echo, decay, gain, flangerLenght,
						wetness, lfoFrequency, lowPass)) {
					Alert alert1 = new Alert(AlertType.INFORMATION);
					alert1.setTitle(sMSsaveAlert1Title);
					alert1.setHeaderText(sMSsaveAlert1Header);
					alert1.setContentText(sMSsaveAlert1Content);
					alert1.showAndWait();
					Stage stage = (Stage) sMSCancelButton.getScene().getWindow();
					stage.close();
				} else {
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle(sMSsaveAlert2Title);
					alert2.setHeaderText(sMSsaveAlert2Header);
					alert2.setContentText(sMSsaveAlert2Content); 
					alert2.showAndWait();
				}

			}
		} else {
			Alert alert3 = new Alert(AlertType.ERROR);
			alert3.setTitle(sMSsaveAlert3Title);
			alert3.setHeaderText(sMSsaveAlert3Header);
			alert3.setContentText(sMSsaveAlert3Content);
			alert3.showAndWait();
		}
		
	}
	
	
	/**
	 * Checks if string contains only whitespaces
	 * 
	 * @author Kevin Akkoyun 
	 * @param input -- String to be checked
	 * @return returns true if string contains only whitespaces, otherwise returns
	 *         false
	 */
	public boolean checkEmpty(String input) {
		input = input.trim();
		return input.isEmpty();
	}


	/**
	 * Method handles closing scene windows.
	 * 
	 * @param event
	 */
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) sMSCancelButton.getScene().getWindow();
		stage.close();
	}

}
