package otp.group6.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Window;
import otp.group6.AudioEditor.AudioFileHandler;
import otp.group6.controller.SoundboardController;

/**
 * Controller for soundboard view TODO changes to make this class control the
 * view and standalone controller for soundboard in controller package
 * 
 * @author Kevin Akkoyun
 *
 */
public class SoundboardViewController implements Initializable {

	private SoundboardController controller;

	/**
	 * FXML variables
	 */
	@FXML
	private GridPane mainGrid;

	private AnchorPane[] containerArray;

	private FXMLLoader newSoundLoader = null, buttonLoader = null;

	/**
	 * Fetches an instance of SoundboardController
	 */
	public SoundboardViewController() {
		controller = SoundboardController.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getContainers();
		getViewResources();
		// TODO REMOVE
		devTestMethod();
	}

	// *********************UTILITY METHODS*******************************//
	/**
	 * TODO REMOVE
	 */
	private void devTestMethod() {
		addSoundboardButton(containerArray[0]);
	}
	
	/**
	 * Opens system file explorer and returns selected file if it is .wav <br>
	 * Performs a regex check to see if file is .wav
	 * @return .wav file or null
	 */
	private File openFileExplorer() {

		Pattern pattern = Pattern.compile("(\\.wav)$", Pattern.CASE_INSENSITIVE);

		File file = AudioFileHandler.openFileExplorer(MainController.sharedMain.getScene().getWindow());
		if(file != null) {
			Matcher matcher = pattern.matcher(file.getName());
			if (matcher.find()) {
				return file;
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Wrong file format");
				alert.setContentText("Please select only WAV files");
				alert.showAndWait();
				return null;
			}
		}
		return file;
	}

	/**
	 * Fetches all container AnchorPanes from main GridPane and places them to an
	 * array
	 */
	private void getContainers() {

		Object[] temp = mainGrid.getChildren().toArray();
		containerArray = new AnchorPane[temp.length];
		int i = 0;
		for (Object o : temp) {
			containerArray[i] = (AnchorPane) o;
			i++;
		}
	}

	/**
	 * Initializes FXMLLoaders for later use and checks if resources are available
	 */
	private void getViewResources() {
		if (newSoundLoader == null)
			newSoundLoader = new FXMLLoader();
		if (buttonLoader == null)
			buttonLoader = new FXMLLoader();
		newSoundLoader.setLocation(MainApplication.class.getResource("/NewSoundButton.fxml"));
		buttonLoader.setLocation(MainApplication.class.getResource("/SoundBoardButton.fxml"));
		try {
			newSoundLoader.load();
			buttonLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Configures the given SoundboardButton <br>
	 * Adds functionality to all child nodes
	 * @param root of the soundboard button
	 */
	private void configureButton(AnchorPane buttonRoot) {
		buttonRoot.getChildren().forEach(e ->{
			switch(e.getClass().getTypeName()) {
			case "javafx.scene.control.Button":
				System.out.println("button");
				break;
			case "javafx.scene.text.Text":
				System.out.println("text");
				break;
			case "javafx.scene.control.MenuButton":
				System.out.println("menu button");
				break;
			}
		});
	}

	// *********************VIEW MANIPULATION METHODS*********************//

	/**
	 * Adds a newSoundButton loaded from an FXML template to given Pane.<br>
	 * Clears all chilren from given Pane
	 * 
	 * @param container for the button {@link #clearContainer(Pane)}
	 */
	private void addNewSoundButton(Pane container) {
		clearContainer(container);
		newSoundLoader = new FXMLLoader();
		newSoundLoader.setLocation(MainApplication.class.getResource("/NewSoundButton.fxml"));
		try {
			AnchorPane root = newSoundLoader.load();
			root.getChildren().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					newSoundButton();
				}
			});
			container.getChildren().add(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a new Soundboard button loaded from an FXML template to given
	 * container.<br>
	 * Clears all chilren from given Pane
	 * 
	 * @param container for the button {@link #clearContainer(Pane)}
	 */
	private void addSoundboardButton(Pane container) {
		clearContainer(container);
		buttonLoader = new FXMLLoader();
		buttonLoader.setLocation(MainApplication.class.getResource("/SoundBoardButton.fxml"));
		try {
			AnchorPane root = buttonLoader.load();
			container.getChildren().add(root);
			configureButton(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes children of the given Pane
	 * 
	 * @param container
	 * @see Pane
	 */
	private void clearContainer(Pane container) {
		if (!container.getChildren().isEmpty()) {
			container.getChildren().clear();
		}
	}
	//*****************************BUTTON FUNCTIONALITY*************************************//
	
	/**
	 * Functionality for newSoundButton
	 */
	private void newSoundButton() {
		File newFile = openFileExplorer();
		if(newFile != null) {
			//TODO STUFF
		}
	}
	
	/**
	 * Functionality for play button
	 * @param index of the sample
	 */
	private void playButton(int index) {
		
	}
	
	/**
	 * Functionality for edit button
	 */
	private void editButton() {
		
	}
	
	/**
	 * Functionality for rename Button
	 */
	private void renameButton() {
		
	}
	
	/**
	 * Functionality for delete button
	 */
	private void deleteButton() {
		
	}
	/**
	 * Functionality for clear all button
	 */
	private void clearAllButton() {
		
	}
}
