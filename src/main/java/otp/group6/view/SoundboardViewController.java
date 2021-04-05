package otp.group6.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
 * Controller for soundboard view
 * 
 * @author Kevin Akkoyun
 *TODO nappien toiminnot. Tallennus + tiedoston luku (kontrollerissa)
 *
 */
public class SoundboardViewController implements Initializable {

	private SoundboardController controller;

	private FXMLLoader newSoundLoader = null, buttonLoader = null;

	// Keeps track of all soundboard buttons and their indexes
	private HashMap<Integer, Node> buttonMap = null;

	// Keeps track of all containers
	private HashMap<Integer, Pane> containerMap = null;
	/**
	 * FXML variables
	 */
	@FXML
	private GridPane mainGrid; // Main GridPane that contains every single soundboard button

	/**
	 * Fetches an instance of SoundboardController
	 */
	public SoundboardViewController() {
		controller = SoundboardController.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		createMaps();
		getViewResources();
		devTestMethod();
	}

	// *********************UTILITY METHODS*******************************//
	/**
	 * TODO REMOVE
	 */
	private void devTestMethod() {
		containerMap.forEach((e, p) -> {
			if (e != 19) {
				addSoundboardButton(p, e);
			} else {
				addNewSoundButton(p);
			}

		});

	}

	/**
	 * Initializes maps that keep track of view components.
	 */
	public void createMaps() {
		if (buttonMap == null) {
			buttonMap = new HashMap<Integer, Node>();
		}
		if (containerMap == null) {
			containerMap = new HashMap<Integer, Pane>();
			getContainers();
		}
	}

	/**
	 * Opens system file explorer and returns selected file if it is .wav <br>
	 * Performs a regex check to see if file is .wav
	 * 
	 * @return .wav file or null
	 */
	private File openFileExplorer() {

		Pattern pattern = Pattern.compile("(\\.wav)$", Pattern.CASE_INSENSITIVE);

		File file = AudioFileHandler.openFileExplorer(MainController.sharedMain.getScene().getWindow());
		if (file != null) {
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
	 * HashMap<br>
	 * Invoked in {@link #createMaps()}
	 */
	private void getContainers() {

		Object[] temp = mainGrid.getChildren().toArray();
		if (containerMap == null) {
			containerMap = new HashMap<Integer, Pane>();
		}
		int i = 0;
		for (Object o : temp) {
			containerMap.put(i, (Pane) o);
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
	 * 
	 * @param root of the soundboard button
	 */
	private void configureButton(Pane buttonRoot) {
		buttonRoot.getChildren().forEach(e -> {
			String temp = e.getClass().getSimpleName();
			switch (temp) {
			case "Button":
				System.out.println("button");
				break;
			case "Text":
				System.out.println("text");
				break;
			case "MenuButton":
				System.out.println("menu button");
				break;
			}
		});
	}

	/**
	 * TODO localization Configures the button for playing samples
	 * 
	 * @param button
	 */
	private void configurePlayButton(Button button) {

	}

	/**
	 * TODO localization Configures the text for renaming functionality
	 * 
	 * @param text
	 */
	private void configureRenameOnClick(Text button) {

	}

	/**
	 * TODO localization Configures the menu button
	 * 
	 * @param button
	 */
	private void configureMenuButton(MenuButton button) {

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
	 * Adds a new Soundboard button loaded from an FXML template to given container.
	 * <br>
	 * Clears all children from given container By default sets create buttons index
	 * in {@link #buttonMap} to its length.
	 * 
	 * @param container - parent element for the button
	 * @return if successful, returns created buttons root, otherwise returns null
	 */
	private Pane addSoundboardButton(Pane container, int sampleIndex) {
		clearContainer(container);
		buttonLoader = new FXMLLoader();
		buttonLoader.setLocation(MainApplication.class.getResource("/SoundBoardButton.fxml"));
		try {
			AnchorPane root = buttonLoader.load();
			container.getChildren().add(root);
			configureButton(root);
			return root;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Adds an existing Soundboard button from the {@link #buttonMap} with given
	 * index <br>
	 * Clears all children from given container
	 * 
	 * @param container - parent element
	 * @param index     - index of the button
	 * @return if successful, returns added buttons root element, otherwise returns
	 *         null.
	 */
	private Pane adSoundboardButton(Pane container, int index) {
		clearContainer(container);
		try {
			Node root = buttonMap.get(index);
			container.getChildren().add(root);
			configureButton((Pane) root);
			return (Pane) root;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Removes children from the given container.
	 * 
	 * @param container - container to be cleared
	 * @see Pane
	 */
	private void clearContainer(Pane container) {
		if (!container.getChildren().isEmpty()) {
			container.getChildren().clear();
		}
	}
	// *****************************BUTTON
	// FUNCTIONALITY*************************************//

	/**
	 * Functionality for button created by {@link #addNewSoundButton(Pane)} <br>
	 * <br>
	 * Replaces itself with a button created by
	 * {@link #addSoundboardButton(Pane)}<br>
	 * Creates another one of itself to the next container in {@link #containerMap}
	 */
	private void newSoundButton() {
		File newFile = openFileExplorer();
		if (newFile != null) {
			// The next container pane
			Pane currentContainer = containerMap.get(buttonMap.size());
			clearContainer(currentContainer);
			int sampleIndex = controller.addSample(newFile.getAbsolutePath());
			Pane newButton = addSoundboardButton(currentContainer, sampleIndex);

			if (newButton != null) {
				//checks if a next container exists
				Pane nextContainer = containerMap.get(buttonMap.size());
				if (nextContainer != null) {
					addNewSoundButton(nextContainer);
				}

			} else if (newButton == null) {
				// If operation fails, removes added samples and reverts back to state before
				clearContainer(currentContainer);
				controller.removeSample(sampleIndex);
				addNewSoundButton(currentContainer);
			}
		}
	}

	/**
	 * Functionality for play button
	 * 
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
