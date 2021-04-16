package otp.group6.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import otp.group6.audioeditor.AudioFileHandler;
import otp.group6.audioeditor.Soundboard;
import otp.group6.audioeditor.Soundboard.Sample;
import otp.group6.controller.SoundboardController;
import otp.group6.controller.SoundboardController.INPUT_TYPE;

/**
 * Controller class for the Soundboard view
 * 
 * @author Kevin Akkoyun
 */
public class SoundboardViewController implements Initializable {

	/**
	 * Used to determine type of operation necessary when adding a new Soundboard
	 * button
	 * 
	 * @author Kevin Akkoyun
	 */
	private enum OP_TYPE {
		NEW, EXISTING
	}

	/**
	 * Controller object for executing
	 */
	private SoundboardController controller;

	private FXMLLoader newSoundLoader = null, buttonLoader = null;

	// Keeps track of all soundboard buttons and their indexes
	private ArrayList<Node> buttonList = null;

	// Keeps track of all containers
	private HashMap<Integer, Pane> containerMap = null;

	// Localization name variables
	private String CSOUND_BTN, DELETE_BTN, RENAME_BTN, CONFIRM_BTN, CANCEL_BTN, WARNING_MSG, WARNING_TITLE, CLEAR_BTN;

	// FXML variables
	/**
	 * Main GridPane that contains every single soundboard button
	 */
	@FXML
	private GridPane mainGrid;

	@FXML
	private Button clearAll;

	private Button lastButton;

	/**
	 * Fetches an instance of {@link SoundboardController}
	 */
	public SoundboardViewController() {
		controller = SoundboardController.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setLocalization(resources);
		createStorageVariables();
		getViewResources();
		loadSavedSamples();
		setIconChanger();
	}

	// *********************UTILITY METHODS*******************************//
	public void setIconChanger() {
		controller.setListener(new LineListener() {

			@Override
			public void update(LineEvent arg0) {
				if (arg0.getType() == Type.STOP) {
					switchIconVisibility(lastButton, true);
				} else if (arg0.getType() == Type.START) {
					switchIconVisibility(lastButton, false);
				}
			}
		});
	}

	/**
	 * Initializes storage variables that contain runtime view components
	 */
	public void createStorageVariables() {
		if (buttonList == null) {
			buttonList = new ArrayList<Node>();
		}
		if (containerMap == null) {
			containerMap = new HashMap<Integer, Pane>();
			getContainers();
		}
	}

	/**
	 * TODO javadoc
	 */
	public void setLocalization(ResourceBundle bundle) {
		CSOUND_BTN = bundle.getString("sBCSound_btn");
		DELETE_BTN = bundle.getString("sBDelete_btn");
		RENAME_BTN = bundle.getString("sBRename_btn");
		CONFIRM_BTN = bundle.getString("sBConfirm_btn");
		CANCEL_BTN = bundle.getString("sBCancel_btn");
		WARNING_MSG = bundle.getString("sBWarning_msg");
		WARNING_TITLE = bundle.getString("sBWarning_title");
		CLEAR_BTN = bundle.getString("sBClear_btn");
		controller.setSampleDefaultName(bundle.getString("sBDefault_name"));
	}

	/**
	 * Controller method for saving Sample data
	 */
	public void saveSampleData() {
		controller.saveSampleData();
	}

	/**
	 * Loads samples with {@link SoundboardController} from sampledata.txt <br>
	 * Creates buttons equal to the amount of Samples loaded. <br>
	 * <b>WARNING</b> size of {@link Soundboard#sampleArray} cannot exceed <b>20</b>
	 */
	public void loadSavedSamples() {
		int sampleAmount = controller.initializeSoundboard();
		for (int i = 0; i < sampleAmount; i++) {
			addSoundboardButton(containerMap.get(i), i, OP_TYPE.NEW);
		}
		if (sampleAmount < 20) {
			addNewSoundButton(containerMap.get(sampleAmount));
		}
		configureClearAllButton();
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
	 * Invoked in {@link #createStorageVariables()}
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
	 * Configures the given Soundboard button <br>
	 * Adds functionality to all child nodes
	 * 
	 * @param buttonRoot - root element of the Soundboard button
	 */
	private void configureButton(Pane buttonRoot, int sampleIndex) {
		buttonRoot.getChildren().forEach(e -> {
			String temp = e.getClass().getSimpleName();
			switch (temp) {
			case "Button":
				configurePlayButton((Button) e, sampleIndex);
				break;
			case "Text":
				configureRenameOnClick((Text) e, sampleIndex);
				break;
			case "MenuButton":
				configureMenuButton((MenuButton) e, sampleIndex);
				break;
			}
		});
	}

	/**
	 * Configures button to play sample with given index on click
	 * 
	 * @param button - button to be configured
	 * @param index  - index of the sample
	 */
	private void configurePlayButton(Button button, int index) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playButton(index, button);
			}
		});
	}

	/**
	 * Configures text element to handle renaming functionality
	 * 
	 * @param text  - text element
	 * @param index - index of the {@link Soundboard.Sample}
	 */
	private void configureRenameOnClick(Text text, int index) {
		text.setText(controller.getSampleName(index));
		text.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				renameButton(index);
			}

		});

	}

	/**
	 * Configures all MenuItems contained by MenuButton
	 * 
	 * @param menuButton - parent element for MenuItems
	 * @param index      - index of the {@link Sample}
	 */
	private void configureMenuButton(MenuButton menuButton, int index) {
		List<MenuItem> list = menuButton.getItems();
		list.forEach(e -> {
			String eName = e.getText();
			switch (eName) {
			case "Rename":
				e.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						renameButton(index);
					}

				});
				e.setText(RENAME_BTN);
				break;
			case "Change sound":
				e.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						editButton(index);
					}

				});
				e.setText(CSOUND_BTN);
				break;
			case "Delete":
				e.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						deleteButton(index);
					}

				});
				e.setText(DELETE_BTN);
				break;
			}
		});
	}

	/**
	 * Configures <b>Clear all</b> button
	 */
	private void configureClearAllButton() {
		clearAll.setText(CLEAR_BTN);
	}

	/**
	 * Checks if the string is empty
	 * 
	 * @param input - input in String format
	 * @return Returns <b>true</b> if the string is empty, otherwise returns
	 *         <b>false</b>
	 */
	public Boolean checkEmpty(String input) {
		input = input.trim();
		return input.isEmpty();
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
	 * in {@link #buttonList} to its length.
	 * 
	 * @param container   - parent element for the button
	 * @param sampleIndex - index of the sample related to the button
	 * @param type        - either <u>NEW or EXISTING</u>. Defines if button should
	 *                    be loaded as new from <b>fxml</b> or loaded from existing
	 *                    list of buttons.
	 * @return if successful, returns created buttons root, otherwise returns null
	 */
	private Pane addSoundboardButton(Pane container, int sampleIndex, OP_TYPE type) {
		switch (type) {
		case NEW:
			clearContainer(container);
			buttonLoader = new FXMLLoader();
			buttonLoader.setLocation(MainApplication.class.getResource("/SoundBoardButton.fxml"));
			try {
				AnchorPane root = buttonLoader.load();
				container.getChildren().add(root);
				configureButton(root, sampleIndex);
				buttonList.add(root);
				return root;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		case EXISTING:
			clearContainer(container);
			try {
				Node root = buttonList.get(sampleIndex);
				container.getChildren().add(root);
				configureButton((Pane) root, sampleIndex);
				return (Pane) root;
			} catch (NullPointerException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
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

	/**
	 * Refreshes
	 * 
	 * @param container - root element of the button
	 */
	private void refreshContainerText(Pane container, int index) {
		List<Node> nList = container.getChildren();
		nList.forEach(e -> {
			if (e.getClass().getSimpleName().equals("Text")) {
				Text text = (Text) e;
				text.setText(controller.getSampleName(index));
			}
		});
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
			Pane currentContainer = containerMap.get(buttonList.size());
			clearContainer(currentContainer);
			int sampleIndex = controller.addSample(newFile.getAbsolutePath());
			Pane newButton = addSoundboardButton(currentContainer, sampleIndex, OP_TYPE.NEW);

			if (newButton != null) {
				// checks if a next container exists
				Pane nextContainer = containerMap.get(buttonList.size());
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
	 * Functionality for the play button
	 * 
	 * @param index - index of the <b>Sample</b>
	 * @see {@link Sample}
	 */
	@SuppressWarnings("unused")
	private void playButton(int index) {
		controller.playSample(index);
	}

	/**
	 * Functionality for the play button. <br>
	 * Compares current and last pressed button to determine operation
	 * 
	 * @param index  - index of the <b>Sample</b>
	 * @param button - current button
	 */
	private void playButton(int index, Button button) {
		if (button == lastButton) {
			controller.playSample(index);
		} else {
			controller.playSampleInstantly(index);
			lastButton = button;
		}
	}

	/**
	 * Functionality for the edit button
	 * 
	 * @param index - index of the <b>Sample</b>
	 * @see {@link Sample}
	 */
	private void editButton(int index) {
		File newFile = openFileExplorer();
		if (newFile != null) {
			controller.editSample(index, newFile.getAbsolutePath(), INPUT_TYPE.FILEPATH);
		}
	}

	/**
	 * Creates a new TextField and brings it to the front. <br>
	 * The created TextField will take its input upon {@link KeyCode#ENTER} or focus
	 * loss. Checks if the input is valid and changes sample name.
	 * 
	 * @param index - index of the <b>Sample</b>
	 * @see {@link Sample}
	 */
	@SuppressWarnings("unchecked")
	private void renameButton(int index) {
		try {
			Pane buttonRoot = (Pane) buttonList.get(index);
			TextField iField = new TextField();
			iField.setText(controller.getSampleName(index));
			@SuppressWarnings("rawtypes")
			ChangeListener listener = new ChangeListener() {

				@Override
				public void changed(ObservableValue observable, Object oldValue, Object newValue) {
					if (!iField.isFocused()) {
						String iText = iField.getText();
						// Process input
						if (!controller.compareSamples(index, iText, INPUT_TYPE.SAMPLE_NAME) && !checkEmpty(iText)) {
							controller.editSample(index, iText, INPUT_TYPE.SAMPLE_NAME);
							refreshContainerText(buttonRoot, index);
						}
						buttonRoot.getChildren().remove(buttonRoot.getChildren().indexOf(iField));
					}

				}

			};
			iField.focusedProperty().addListener(listener);
			iField.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.ENTER) {
						String iText = iField.getText();

						if (!controller.compareSamples(index, iText, INPUT_TYPE.SAMPLE_NAME) && !checkEmpty(iText)) {
							controller.editSample(index, iText, INPUT_TYPE.SAMPLE_NAME);
							refreshContainerText(buttonRoot, index);
						}
						iField.focusedProperty().removeListener(listener);
						buttonRoot.getChildren().remove(buttonRoot.getChildren().indexOf(iField));
					}
				}

			});
			buttonRoot.getChildren().add(iField);
			iField.forward();
			iField.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes specified sample with given index<br>
	 * Deletes last button on the screen then refreshes all button names
	 * 
	 * @param index - index of the sample
	 */
	private void deleteButton(int index) {
		controller.removeSample(index);
		clearContainer(containerMap.get(buttonList.size() - 1));
		if (buttonList.size() < 20) {
			clearContainer(containerMap.get(buttonList.size()));
			addNewSoundButton(containerMap.get(buttonList.size() - 1));
		}
		for (int i = index; i < buttonList.size() - 1; i++) {
			refreshContainerText((Pane) buttonList.get(i), i);
		}
		buttonList.remove(index);

		// remove specified sample from soundboard -> delete last button from container
		// -> refresh affected button texts

	}

	/**
	 * Clears everything and recreates storage variables
	 */
	@FXML
	private void clearAllButton() {
		if (alertCheck(WARNING_MSG)) {
			containerMap.forEach((i, e) -> {
				clearContainer(e);
			});
			createStorageVariables();
			addNewSoundButton(containerMap.get(0));
			controller.clearSampleArray();
		}
	}

	/**
	 * Creates an alert to confirm if the user wants to complete certain action <br>
	 * 
	 * @param msg - warning message to be displayed.
	 * @return returns the selected choice, either <b>true</b> or <b>false</b>
	 */
	private boolean alertCheck(String msg) {
		Alert alert = new Alert(Alert.AlertType.NONE);
		alert.setContentText(msg);

		ButtonType type = new ButtonType(CONFIRM_BTN, ButtonData.OK_DONE);
		ButtonType ntype = new ButtonType(CANCEL_BTN, ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().add(type);
		alert.getButtonTypes().add(ntype);

		alert.setTitle(WARNING_TITLE);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get().getButtonData() == ButtonData.OK_DONE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method that switches the visibility of button icons if <b>true</b> is given,
	 * sets the visibility of "play" icon to true and "stop" to false<br>
	 * if <b>false</b> is given, the operation is reverse
	 * 
	 * @param button - Soundboard <b>play</b> - button
	 * @param bool   - Wether or not to display the play icon
	 */
	private void switchIconVisibility(Button button, boolean bool) {
		Pane imgRoot = (Pane) button.getGraphic();
		ImageView playImg = (ImageView) imgRoot.getChildren().get(0);
		ImageView stopImg = (ImageView) imgRoot.getChildren().get(1);
		playImg.setVisible(bool);
		stopImg.setVisible(!bool);
	}
}
