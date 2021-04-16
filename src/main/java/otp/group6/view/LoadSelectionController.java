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
 * Class is used for loading slider values from our database or locally from the
 * users computer,
 * 
 * @author Joonas Soininen
 *
 */
public class LoadSelectionController implements Initializable{
	Controller controller;
	MainController mc;

	@FXML
	AnchorPane mainContainer;

	@FXML
	private Button lSBcancel;
	@FXML
	private Button lSBX;
	@FXML
	private Label lSTitle;
	@FXML
	private Label lSmaintext;
	@FXML
	private Button lSBlocal;
	@FXML
	private Button lSBcloud;
	
	private List<String> lines = new ArrayList<String>();
	
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
		lSBcancel.setText(bundle.getString("lSBcancel"));
		lSBX.setText(bundle.getString("lSBX"));
		lSTitle.setText(bundle.getString("lSTitle"));
		lSmaintext.setText(bundle.getString("lSmaintext"));
		lSBlocal.setText(bundle.getString("lSBlocal"));
		lSBcloud.setText(bundle.getString("lSBcloud"));
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
		Stage stage = (Stage) lSBcancel.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method to open mixer settings from the database
	 */
	public void loadFromCloud() {
		mc.openMixerSettings();
		Stage stage = (Stage) lSBcancel.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method to load mixer settings from local storage
	 */
	public void loadFromLocal() {
		// TODO Varmista oikea tiedostomuoto
		try {
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();

				lines.add(data);
			}
			myReader.close();
			mc.setSliderValues(Double.parseDouble(lines.get(0)), Double.parseDouble(lines.get(1)),
					Double.parseDouble(lines.get(2)), Double.parseDouble(lines.get(3)),
					Double.parseDouble(lines.get(4)), Double.parseDouble(lines.get(5)),
					Double.parseDouble(lines.get(6)), Float.parseFloat(lines.get(7)));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		Stage stage = (Stage) lSBcancel.getScene().getWindow();
		stage.close();
	}


}
