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
 * Class handles selection for saving mixer settings locally or into database
 * 
 * @author Joonas Soininen
 *
 */
public class SaveSelectionController implements Initializable{
	Controller controller;
	MainController mc;

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
	@FXML
	private Button sSXButton;
	@FXML
	AnchorPane mainContainer;

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
		sSHeaderLabel.setText(bundle.getString("sSHeaderLabel"));
		sSTextLable.setText(bundle.getString("sSTextLable"));
		sSLocalButton.setText(bundle.getString("sSLocalButton"));
		sSCloudButton.setText(bundle.getString("sSCloudButton"));
		sSCancelButton.setText(bundle.getString("sSCancelButton"));
		sSXButton.setText(bundle.getString("sSXButton"));
	}
	
	/**
	 * Method to get mainController
	 * 
	 * @param mainController
	 */
	public void setMainController(MainController mainController) {
		this.mc = mainController;
		this.controller = mc.getController();
	}

	/**
	 * Method to close opened scenes
	 * 
	 * @param event
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) sSCancelButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Does a query to the database class to check for a logged in user
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
		} else  {
			Stage stage = (Stage) sSCancelButton.getScene().getWindow();
			stage.close();
		}
		
	}

	/**
	 * Method for storing settings on users computer
	 */
	public void saveLocal() {
		mc.saveMixerSettingsLocally();
		Stage stage = (Stage) sSCancelButton.getScene().getWindow();
		stage.close();
	}


}
