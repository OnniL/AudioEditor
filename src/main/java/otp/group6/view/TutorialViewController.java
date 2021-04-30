package otp.group6.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class TutorialViewController implements Initializable {

	public TutorialViewController() {

	}

	@FXML
	private ComboBox<String> comboBoxlanguageOptions;

	ObservableList<String> languageOptions = FXCollections.observableArrayList("English", "Suomi");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboBoxlanguageOptions.setPromptText("Language");
		comboBoxlanguageOptions.setItems(languageOptions);

	}

}
