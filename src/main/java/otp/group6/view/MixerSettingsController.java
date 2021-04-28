package otp.group6.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import otp.group6.audioeditor.AudioCloudDAO.MixerSetting;
import otp.group6.controller.Controller;

/**
 * Class handles database stored mixer settings, displaying them, adding them to
 * favorites and storing the favorites list locally.
 * Classes main functionality is to provide mixer settings from the database for the user to select from.
 * Mixer settings are fetched from the database and passed trough lists into MixerSettingsView as a list.
 * User can select a mixer setting there to use in the mixer, add mixer settings to a favorites list and 
 * delete settings from the database if the user is logged in as the creator of those mixes.
 * 
 * @author Joonas Soininen
 *
	 * TODO Metodi suosikkien tallentamiseen tietokantaan puuttuu	 
 */
public class MixerSettingsController implements Initializable {
	/** Object of the MainController.java class */
	MainController mc;
	/** Object of the Controller.java class */
	Controller controller;
	
	/**
	 * Variables for the different JavaFX elements
	 * mS in front refers to this class and then the variable name
	 * to its function in the visible view.
	 */
	@FXML
	AnchorPane mainContainer;
	@FXML
	private Tab mSTabCloud;
	@FXML
	private Tab mSTabFavorites;
	@FXML
	private TextField mSSearchField;
	@FXML
	private RadioButton mSRadioCreator;
	@FXML
	private RadioButton mSRadioName;
	@FXML
	private RadioButton mSRadioDescription;
	@FXML
	private Button mSSearchButton;
	@FXML
	private Button mSUseSettingCloud;
	@FXML
	private Button mSCancelButtonCloud;
	@FXML
	private Button mSUseSettingFav;
	@FXML
	private Button mSCancelButtonFav;
	@FXML
	private Button mSRemoveFav;
	@FXML
	private Button mSDeleteMix;
	@FXML
	/**
	 * Variables for the lists shown for user
	 */
	private ListView<String> favoritesListView;
	@FXML
	private ObservableList<String> mixerSettings = FXCollections.observableArrayList();
	@FXML
	private ListView<HBoxCell> cloudListView;
	@FXML
	private ObservableList<HBoxCell> settingsListWithButton = FXCollections.observableArrayList();;
	
	/**
	 * Variables for alert messages
	 */
	private String mSGeneralOKBT;
	private String mSGeneralCancelBT;
	private String mSSaveAlertTitle;
	private String mSSaveAlertHeader;
	private String mSSaveAlertContent;
	private String mSDeleteAlert1Title;
	private String mSDeleteAlert1Header;
	private String mSDeleteAlert1Content;
	private String mSDelteAlert2Title;
	private String mSDeleteAlert2Header;
	
	/** 
	 * Variables for different information to store.
	 */
	private int mixerIndetification;	
	private String mixerCreatorName;	
	private List<String> localList = new ArrayList<>();


	/**
	 * Inner class to handle create buttons on the listView elements
	 * Button is used to favorite specific mixer settings.
	 * 
	 * @author Joonas Soininen
	 *
	 */
	public class HBoxCell extends HBox {
		Label label = new Label();
		Button button = new Button();

		/**
		 * Method to create buttons into the ListView.
		 * It gets all necessary parameters when called to set the correct response for the button
		 * @param labelText, general information about the mixer setting
		 * @param buttonText, the text or image that is shown to the user
		 * @param id, mixer setting id
		 * Each button has their own label and id that is connected to the favorites list
		 */
		HBoxCell(String labelText, String buttonText, int id) {
			super();

			label.setText(labelText);
			label.setMaxWidth(Double.MAX_VALUE);
			HBox.setHgrow(label, Priority.ALWAYS);

			button.setText(buttonText);
			button.setId(String.valueOf(id));

			if (localList.contains(String.valueOf(id))) {
				button.setDisable(true);
			}
			button.setOnAction(e -> {
				try {
					favoriteButton(button.getId(), labelText);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				button.setDisable(true);
			});

			this.getChildren().addAll(label, button);
		}
	}
	
	/**
	 * Method initializes this class when loaded, calls {@link #setLocalization(ResourceBundle)} to set certain variables passing the ResourceBundle to it.
	 * @param arg1, is the resource bundle provided from the MainControler.java containing the language settings
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
		mSTabCloud.setText(bundle.getString("mSTabCloud"));
		mSTabFavorites.setText(bundle.getString("mSTabFavorites"));
		mSSearchField.setPromptText(bundle.getString("mSSearchField"));
		mSRadioCreator.setText(bundle.getString("mSRadioCreator"));
		mSRadioName.setText(bundle.getString("mSRadioName"));
		mSRadioDescription.setText(bundle.getString("mSRadioDescription"));
		mSSearchButton.setText(bundle.getString("mSSearchButton"));
		mSUseSettingCloud.setText(bundle.getString("mSUseSettingCloud"));
		mSCancelButtonCloud.setText(bundle.getString("mSCancelButtonCloud"));
		mSUseSettingFav.setText(bundle.getString("mSUseSettingFav"));
		mSCancelButtonFav.setText(bundle.getString("mSCancelButtonFav"));
		mSRemoveFav.setText(bundle.getString("mSRemoveFav"));
		mSDeleteMix.setText(bundle.getString("mSDeleteMix"));
		mSGeneralOKBT=bundle.getString("mSGeneralOKBT");
		mSGeneralCancelBT=bundle.getString("mSGeneralCancelBT");
		mSSaveAlertTitle=bundle.getString("mSSaveAlertTitle");
		mSSaveAlertHeader=bundle.getString("mSSaveAlertHeader");
		mSSaveAlertContent=bundle.getString("mSSaveAlertContent");
		mSDeleteAlert1Title=bundle.getString("mSDeleteAlert1Title");
		mSDeleteAlert1Header=bundle.getString("mSDeleteAlert1Header");
		mSDeleteAlert1Content=bundle.getString("mSDeleteAlert1Content");
		mSDelteAlert2Title=bundle.getString("mSDelteAlert2Title");
		mSDeleteAlert2Header=bundle.getString("mSDeleteAlert2Header");
	}
	
	/**
	 * Method adds mixer setting assigned to the specific button to the favorites list 
	 * @param id, mixer setting id
	 * @param title, mixer setting title
	 * @throws IOException, exception is thrown if the local {@link #save()} or {@link #read()} fails
	 */
	public void favoriteButton(String id, String title) throws IOException {		
		mixerSettings.add(title);
		localList.add(id);
		save();
		localList.clear();
		read();
	}

	/**
	 * Method to store the favorite list data locally so it can be automatically loaded when this view opens
	 * @throws IOException, exception is thrown if the local save fails.
	 */
	public void save() throws IOException {
		//Automatic save method
		File file1 = new File("src/localfav/Fav1.txt");
		try {
			FileOutputStream fout = new FileOutputStream(file1);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(localList);
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(mSSaveAlertTitle);
			alert.setHeaderText(mSSaveAlertHeader);
			alert.setContentText(mSSaveAlertContent);
			alert.showAndWait();
		}
		//Manual save method
		/*
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
				FileOutputStream fout = new FileOutputStream(fullPath);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(localList);
				fout.close();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Setting saved succesfully!");
				alert.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText("Something went wrong!");
				alert.setContentText("If this keeps happening, contact support! :)");
				alert.showAndWait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	/**
	 * Method to read from local storage automatically when the view is opened.
	 * Loads favorite mixer settings automatically.
	 * @throws IOException, exception thrown if the read fails.
	 */
	@SuppressWarnings({ "unchecked" })
	public void read() throws IOException {		
		favoritesListView.getItems().clear();
		localList.clear();
		mixerSettings.clear();
		File autofile= new File("src/localfav/Fav1.txt");
		FileInputStream fin = new FileInputStream(autofile);
		ObjectInputStream ois = new ObjectInputStream(fin);
		try {
			localList = (ArrayList<String>) ois.readObject();
			//System.out.println(localList); //POISTETTAVA
			ObservableList<Object> mixerID = FXCollections.observableArrayList();
			MixerSetting[] setlist = controller.getAllMixArray();
			for (MixerSetting mix : setlist) {
				for (int i = 0; i < localList.size(); i++) {
					if (mix.getMixID() == Integer.valueOf(localList.get(i))) {
						mixerSettings.add("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
								+ "\nMix Description: " + mix.getDescription());
						mixerID.add(mix.getMixID());
					}
				}

			}			
			favoritesListView.setItems(mixerSettings);
			favoritesListView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
						int index = favoritesListView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						setMixerIndetification(identification);
						//String selectedItem = mixListView.getSelectionModel().getSelectedItem(); //POISTETTAVA
						//System.out.println(index); //POISTETTAVA
						mSRemoveFav.setDisable(false);
					});
			getMixes();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		fin.close();
	}
	
	/**
	 * Method to remove favorite settings from the shown list
	 * @throws IOException, exception is thrown if the local {@link #save()} or {@link #read()} fails 
	 */
	public void removeFav() throws IOException {
		localList.remove(localList.lastIndexOf(String.valueOf(getMixerIndetification())));
		save();
		mixerSettings.clear();
		read();
		mSRemoveFav.setDisable(true);
	}

	/**
	 * Method fetches all mixer settings from the database and shows them to the user in a ListView.
	 */
	@FXML
	public void getMixes() {
		controller.intializeDatabaseConnection();
		MixerSetting[] setlist = controller.getAllMixArray();
		ObservableList<Object> mixerID = FXCollections.observableArrayList(); //List to save specific mixer id
		ObservableList<Object> mixCretor = FXCollections.observableArrayList(); //List to save specific mixer creator name
		for (MixerSetting mix : setlist) {
			settingsListWithButton.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
					+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
			mixerID.add(mix.getMixID());
			mixCretor.add(mix.getCreatorName());
		}
		cloudListView.setItems(settingsListWithButton);
		cloudListView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
					// String selectedItem = mixListView.getSelectionModel().getSelectedItem();
					int index = cloudListView.getSelectionModel().getSelectedIndex();
					int identification = (int) mixerID.get(index);
					String name = (String) mixCretor.get(index);
					setMixerIndetification(identification);
					setMixerCreatorName(name);
					checkUp();
				});

	}

	/**
	 * Method is used to select any mixer setting and pass it to the MainController.view mixer.
	 */
	@FXML
	private void selectMIX() {
		controller.intializeDatabaseConnection();
		if (getMixerIndetification() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No input detected!");
			alert.setContentText("Please select one setting from the list.");
			alert.showAndWait();
		} else {
			MixerSetting[] setlist = controller.getAllMixArray();
			double pitch = 0, echo = 0, decay = 0, gain = 0, flangerLenght = 0, wetness = 0, lfoFrequency = 0;
			float lowPass = 0;
			for (MixerSetting mix : setlist) {

				if (mix.getMixID() == getMixerIndetification()) {
					pitch = mix.getPitch();
					echo = mix.getEcho();
					decay = mix.getDecay();
					gain = mix.getGain();
					flangerLenght = mix.getFlangerLenght();
					wetness = mix.getWetness();
					lfoFrequency = mix.getLfoFrequency();
					lowPass = mix.getLowPass();
				}
			}
			mc.setSliderValues(pitch, echo, decay, gain, flangerLenght, wetness, lfoFrequency, lowPass);
			Stage stage = (Stage) mSCancelButtonCloud.getScene().getWindow();
			stage.close();
		}

	}

	/**
	 * Method is user to search the database for specific mixer settings according to the creator, description or name.
	 * It fetches only the specific searched and shows them to the user in a ListView.
	 */
	@FXML
	public void searchMix() {
		controller.intializeDatabaseConnection();
		if (mSRadioCreator.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(1, mSSearchField.getText());
			ObservableList<Object> mixerID = FXCollections.observableArrayList(); //List to save specific mixer id
			ObservableList<Object> mixCretor = FXCollections.observableArrayList(); //List to save specific mixer creator name
			List<HBoxCell> list = new ArrayList<>();
			for (MixerSetting mix : setlist) {
				list.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
						+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
				mixerID.add(mix.getMixID());
				mixCretor.add(mix.getCreatorName());
			}
			settingsListWithButton = FXCollections.observableArrayList(list);
			cloudListView.setItems(settingsListWithButton);

			cloudListView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
						int index = cloudListView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						String name = (String) mixCretor.get(index);
						setMixerIndetification(identification);
						setMixerCreatorName(name);
						checkUp();
					});
		} else if (mSRadioName.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(2, mSSearchField.getText());
			ObservableList<Object> mixerID = FXCollections.observableArrayList(); //List to save specific mixer id
			ObservableList<Object> mixCretor = FXCollections.observableArrayList(); //List to save specific mixer creator name
			List<HBoxCell> list = new ArrayList<>();
			for (MixerSetting mix : setlist) {
				list.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
						+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
				mixerID.add(mix.getMixID());
				mixCretor.add(mix.getCreatorName());
			}
			settingsListWithButton = FXCollections.observableArrayList(list);
			cloudListView.setItems(settingsListWithButton);

			cloudListView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
						int index = cloudListView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						String name = (String) mixCretor.get(index);
						setMixerIndetification(identification);
						setMixerCreatorName(name);
						checkUp();
					});
		} else if (mSRadioDescription.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(3, mSSearchField.getText());
			ObservableList<Object> mixerID = FXCollections.observableArrayList(); //List to save specific mixer id
			ObservableList<Object> mixCretor = FXCollections.observableArrayList(); //List to save specific mixer creator name
			List<HBoxCell> list = new ArrayList<>();
			for (MixerSetting mix : setlist) {
				list.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
						+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
				mixerID.add(mix.getMixID());
				mixCretor.add(mix.getCreatorName());
			}
			settingsListWithButton = FXCollections.observableArrayList(list);
			cloudListView.setItems(settingsListWithButton);

			cloudListView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
						int index = cloudListView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						String name = (String) mixCretor.get(index);
						setMixerIndetification(identification);
						setMixerCreatorName(name);
						checkUp();
					});
		}
	}
	
	/**
	 * Method is used to check for logged in user, if there is a logged in user
	 * it will show the delete button for the user on those mixer settings where
	 * the user is the creator.
	 */
	public void checkUp() {
	
		if (controller.loggedIn().equals(getMixerCreatorName())) {
			mSDeleteMix.setVisible(true);
			mSDeleteMix.setDisable(false);
		} else {
			mSDeleteMix.setDisable(true);
			mSDeleteMix.setVisible(false);
		}
	}
	
	/**
	 * Method to delete user created mixer setting from the database.
	 * Alerts are used to give feedback for the user.
	 * @throws IOException, exception is thrown if the local {@link #save()} or {@link #read()} fails
	 */
	@FXML
	public void deleteMix() throws IOException {		
		Alert alert1 = new Alert(AlertType.CONFIRMATION);
		alert1.setTitle(mSDeleteAlert1Title);
		alert1.setHeaderText(mSDeleteAlert1Header);
		alert1.setContentText(mSDeleteAlert1Content); 
		ButtonType ok = new ButtonType(mSGeneralOKBT, ButtonData.OK_DONE);
		ButtonType cancel = new ButtonType(mSGeneralCancelBT, ButtonData.CANCEL_CLOSE);
		alert1.getButtonTypes().setAll(ok, cancel);
		Optional<ButtonType> result = alert1.showAndWait();
		if (result.get() == ok) {
			controller.deleteMix(getMixerCreatorName(), getMixerIndetification());
			Alert alert2 = new Alert(AlertType.INFORMATION); 
			alert2.setTitle(mSDelteAlert2Title);
			alert2.setHeaderText(mSDeleteAlert2Header);
			alert2.showAndWait();
			if (localList.contains(String.valueOf(getMixerIndetification()))) {
				localList.remove(localList.lastIndexOf(String.valueOf(getMixerIndetification())));
			}			
			save();
			getMixes();
			mixerSettings.clear();
			read();
			mSRemoveFav.setDisable(true);
			mSDeleteMix.setDisable(true);
			mSDeleteMix.setVisible(false);
		} else {
			Alert alert3 = new Alert(AlertType.ERROR);
			alert3.setTitle(mSSaveAlertTitle);
			alert3.setHeaderText(mSSaveAlertHeader);
			alert3.setContentText(mSSaveAlertContent);
			alert3.showAndWait();
		}
	}


	/**
	 * Method sets variable to the parameter provided from MainController.java 
	 * @param mainController, is the instance of MainController.java that is in the
	 * current thread running.
	 * @throws IOException, exception is thrown if the local {@link #read()} fails
	 */
	public void setMainController(MainController mainController) throws IOException {
		this.mc = mainController;
		this.controller = mc.getController();
		controller.intializeDatabaseConnection();
		read();
	}
	
	/**
	 * Method to close the view when button is pressed
	 * @param event, handles the on push events of binded buttons
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) mSCancelButtonCloud.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method to set the variable mixerIdentification a specific value 
	 * @param mixerIndetification, value of the id.
	 */
	public void setMixerIndetification(int mixerIndetification) {
		this.mixerIndetification = mixerIndetification;
	}

	/**
	 * Method to get the mixerIdentification.
	 * @return the set id or null
	 */
	public int getMixerIndetification() {
		return mixerIndetification;
	}
	/**
	 * Method to set the mixerCreator variable the correct value
	 * @param name, user name for the variable.
	 */
	public void setMixerCreatorName(String name) {
		this.mixerCreatorName=name;
	}
	
	/**
	 * Method to get the previously set mixerCreatorName
	 * @return current value or null
	 */
	public String getMixerCreatorName() {
		return mixerCreatorName;
	}
	
	/**
	 * Method to reload the scene, possibly will be put into use in the future development.
	 * @throws IOException
	 */
	public void reload() throws IOException {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MixerSettingsView.fxml")); 
	    Parent root = fxmlLoader.load();
	    Stage stage = (Stage) mainContainer.getScene().getWindow();
	    stage.getScene().setRoot(root);
	    setMainController(mc);
	}

}
