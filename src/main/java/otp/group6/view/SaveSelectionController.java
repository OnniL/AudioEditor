package otp.group6.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import otp.group6.controller.Controller;

/**
 * 
 * Class is used for the SaveSelectionView.fxml to control its actions. Main
 * functionality is for the user to select save location for the mixer settings
 * either locally or to the database.
 * 
 * @author Joonas Soininen
 *
 */
public class SaveSelectionController implements Initializable {
	/** Object of the MainController.java class */
	MainController mc;
	/** Object of the Controller.java class */
	Controller controller;

	/**
	 * Variables for the different JavaFX elements sS in front refers to this class
	 * and then the variable name to its function in the visible view.
	 */
	@FXML
	private Label sSHeaderLabel;
	@FXML
	private Label sSTextLable;
	@FXML
	private Button sSLocalButton;
	@FXML
	private Button sSCloudButton;
	@FXML
	private Button sSCancelButton;
	// @FXML
	// private Button sSXButton;
	@FXML
	AnchorPane mainContainer;

	/**
	 * Method initializes this class when loaded, calls
	 * {@link #setLocalization(ResourceBundle)} to set certain variables passing the
	 * ResourceBundle to it.
	 * 
	 * @param arg1, is the resource bundle provided from the MainControler.java
	 *              containing the language settings
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setLocalization(arg1);
	}

	/**
	 * Method sets all visible texts and button labels to chosen language.
	 * 
	 * @param bundle transfers the localization data to the method so the right
	 *               language is set
	 */
	private void setLocalization(ResourceBundle bundle) {
		sSHeaderLabel.setText(bundle.getString("sSHeaderLabel"));
		sSTextLable.setText(bundle.getString("sSTextLable"));
		sSLocalButton.setText(bundle.getString("sSLocalButton"));
		sSCloudButton.setText(bundle.getString("sSCloudButton"));
		sSCancelButton.setText(bundle.getString("sSCancelButton"));
		// sSXButton.setText(bundle.getString("sSXButton"));
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
		Stage stage = (Stage) sSCancelButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method does a query to the database and checks for logged in user If there is
	 * a user logged in the SaveMixerSettingsView.fxml is opened trough the
	 * MainController.java or the RegisterLoginView.fxml is opened. Closes this
	 * stage automatically.
	 */
	public void checkForloggedin() {
		controller.intializeDatabaseConnection();
		if (controller.isConnected()) {
			if (!(controller.loggedIn() == " ")) {
				mc.openMixerSave();
				Stage stage = (Stage) sSCancelButton.getScene().getWindow();
				stage.close();
			} else {
				mc.openLoginRegister();
				Stage stage = (Stage) sSCancelButton.getScene().getWindow();
				stage.close();
			}
		} else {
			Stage stage = (Stage) sSCancelButton.getScene().getWindow();
			stage.close();
		}

	}

	/**
	 * Method calls for local save method that is in MainController.java.
	 */
	public void saveLocal() {
		mc.saveMixerSettingsLocally();
		Stage stage = (Stage) sSCancelButton.getScene().getWindow();
		stage.close();
	}

}
