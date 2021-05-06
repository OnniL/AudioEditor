package otp.group6.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import otp.group6.audioeditor.AudioFileHandler;
import otp.group6.controller.Controller;

/**
 * Class functions as a controller for the MixerSettingsView.fxml view and is
 * used to load mixer settings either locally or fromt he database
 * 
 * @author Joonas Soininen
 *
 */
public class LoadSelectionController implements Initializable {
	/** Object of the MainController.java class */
	MainController mc;
	/** Object of the Controller.java class */
	Controller controller;

	/**
	 * Variables for the different JavaFX elements lSB in front refers to this class
	 * and then the variable name to its function in the visible view.
	 */
	@FXML
	AnchorPane mainContainer;
	@FXML
	private Button lSBcancel;
	// @FXML
	// private Button lSBX;
	@FXML
	private Label lSTitle;
	@FXML
	private Label lSmaintext;
	@FXML
	private Button lSBlocal;
	@FXML
	private Button lSBcloud;

	/** Array list for the slider values that are fetched from the database */
	private List<String> sliderValues = new ArrayList<String>();

	/**
	 * Method initializes this class when loaded, calls
	 * {@link #setLocalization(ResourceBundle)} to set certain variables passing the
	 * ResourceBundle to it.
	 * 
	 * @param arg1, resource bundle from the MainController.java
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setLocalization(arg1);
	}

	/**
	 * Method sets all visible text according to the localization settings with the
	 * provided parameter.
	 * 
	 * @param bundle. is localization data passed trough from the
	 *                MainController.java
	 */
	private void setLocalization(ResourceBundle bundle) {
		lSBcancel.setText(bundle.getString("lSBcancel"));
		// lSBX.setText(bundle.getString("lSBX"));
		lSTitle.setText(bundle.getString("lSTitle"));
		lSmaintext.setText(bundle.getString("lSmaintext"));
		lSBlocal.setText(bundle.getString("lSBlocal"));
		lSBcloud.setText(bundle.getString("lSBcloud"));
	}

	/**
	 * Method sets variable to the parameter provided from MainController.java
	 * 
	 * @param mainController, is the instance of MainController.java that is in the
	 *                        current thread running.
	 */
	public void setMainController(MainController mainController) {
		this.mc = mainController;
		this.controller = mc.getController();
	}

	/**
	 * Method to close the view when button is pressed
	 * 
	 * @param event, handles the on push events of binded buttons
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) lSBcancel.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method opens MixerSettingsView.fxml by calling its method from the
	 * MainController.java and closes currently visible stage.
	 */
	public void loadFromCloud() {
		mc.openMixerSettings();
		Stage stage = (Stage) lSBcancel.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method to load mixer settings from the users own computers file system. When
	 * loaded it will set the slider values by calling MainController.java method
	 * setSliderValues. Also will close the currently open stage.
	 */
	public void loadFromLocal() {
			try {
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();

				sliderValues.add(data);
			}
			myReader.close();
			mc.setSliderValues(Double.parseDouble(sliderValues.get(0)), Double.parseDouble(sliderValues.get(1)),
					Double.parseDouble(sliderValues.get(2)), Double.parseDouble(sliderValues.get(3)),
					Double.parseDouble(sliderValues.get(4)), Double.parseDouble(sliderValues.get(5)),
					Double.parseDouble(sliderValues.get(6)), Float.parseFloat(sliderValues.get(7)));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		Stage stage = (Stage) lSBcancel.getScene().getWindow();
		stage.close();
	}

}
