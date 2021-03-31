package otp.group6.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import otp.group6.controller.SoundboardController;

/**
 * Controller for soundboard view TODO changes to make this class control the
 * view and standalone controller for soundboard in controller package
 * 
 * @author kevin
 *
 */
public class SoundboardViewController implements Initializable {
	private SoundboardController controller;

	/**
	 * Fetches an instance of SoundboardController
	 */
	public SoundboardViewController() {
		controller = SoundboardController.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
