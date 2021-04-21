package otp.group6.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import otp.group6.controller.Controller;

/**
 * Class controls the UserSettingsView.fxml and it main function is to 
 * make user able to change their password or to delete their account.
 * 
 * @author Joonas Soininen
 *
 */
public class UserSettingsController implements Initializable{
	/** Object of the MainController.java class */
	MainController mc;
	/** Object of the Controller.java class */
	Controller controller;

	/**
	 * Variables for the different JavaFX elements
	 * uS in front refers to this class and then the variable name
	 * to its function in the visible view.
	 */
	@FXML
	private Label uSHeaderLabel;
	@FXML
	private Button uSXButton;
	@FXML
	private Label uSChangePWLabel;
	@FXML
	private Label uSOldPWLabel;
	@FXML
	private ToggleButton uSShowPW1Toggle;
	@FXML
	private Label uSNewPWLabel;
	@FXML
	private ToggleButton uSShowPW2Toggle;
	@FXML
	private Button uSChangeButton;
	@FXML
	private Label uSUserDeleLabel;
	@FXML
	private Button uSDeleteUserButton;
	@FXML
	private Button uSCancelButton;
	@FXML
	private Label userName;
	@FXML
	private TextField password;
	@FXML
	private TextField showOldPW;
	@FXML
	private TextField npassword;
	@FXML
	private TextField showNewPW;
	private String uSGeneralOKBT;
	private String uSGeneralCancelBT;
	private String uSDeleteAlert1Title;
	private String uSDeleteAlert1Header;
	private String uSDeleteAlert1Content;
	private String uSDeleteAlert2Title;
	private String uSDeleteAlert2Header;
	private String uSDeleteAlert2Content;
	private String uSChangePWAlert1Header;
	private String uSChangePWAlert2Title;
	private String uSChangePWAlert2Header;
	private String uSChangePWAlert2Content;
	final Tooltip uSpwtooltip = new Tooltip();
	
	/**
	 *Variables for the images 
	 */
	private Image imageShow;	
	private Image imageHide;	
	private FileInputStream imageinputshow;	
	private FileInputStream imageinputhide;

	/**
	 * Method initializes this class when loaded, calls {@link #setLocalization(ResourceBundle)} to set certain variables passing the ResourceBundle to it.
	 * @param arg1, is the resource bundle provided from the MainControler.java containing the language settings
	 * Calls {@link #pwReminder()} to remind the user of the correct way to input the password.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pwReminder();
		setLocalization(arg1);

	}
	
	/**
	 * Method sets all visible texts and button labels to chosen language. Sets specific images to specific buttons.
	 * @param bundle transfers the localization data to the method so the right language is set
	 */
	private void setLocalization(ResourceBundle bundle) {
		uSHeaderLabel.setText(bundle.getString("uSHeaderLabel"));
		uSXButton.setText(bundle.getString("uSXButton"));
		uSChangePWLabel.setText(bundle.getString("uSChangePWLabel"));
		uSOldPWLabel.setText(bundle.getString("uSOldPWLabel"));
		uSNewPWLabel.setText(bundle.getString("uSNewPWLabel"));
		uSChangeButton.setText(bundle.getString("uSChangeButton"));
		uSUserDeleLabel.setText(bundle.getString("uSUserDeleLabel"));
		uSDeleteUserButton.setText(bundle.getString("uSDeleteUserButton"));
		uSCancelButton.setText(bundle.getString("uSCancelButton"));
		uSpwtooltip.setText(bundle.getString("uSpwtooltip"));
		uSGeneralOKBT=bundle.getString("uSGeneralOKBT");
		uSGeneralCancelBT=bundle.getString("uSGeneralCancelBT");
		uSDeleteAlert1Title=bundle.getString("uSDeleteAlert2Title");
		uSDeleteAlert1Header=bundle.getString("uSDeleteAlert1Header");
		uSDeleteAlert1Content=bundle.getString("uSDeleteAlert1Content");
		uSDeleteAlert2Title=bundle.getString("uSDeleteAlert2Title");
		uSDeleteAlert2Header=bundle.getString("uSDeleteAlert2Header");
		uSDeleteAlert2Content=bundle.getString("uSDeleteAlert2Content");
		uSChangePWAlert1Header=bundle.getString("uSChangePWAlert1Header");
		uSChangePWAlert2Title=bundle.getString("uSChangePWAlert2Title");
		uSChangePWAlert2Header=bundle.getString("uSChangePWAlert2Header");
		uSChangePWAlert2Content=bundle.getString("uSChangePWAlert2Content");
		try {
			imageinputshow = new FileInputStream("src/main/resources/images/showPW.jpg");
			imageinputhide = new FileInputStream("src/main/resources/images/hidePW.jpg");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		imageShow = new Image(imageinputshow,20,20, true, true);
		imageHide = new Image(imageinputhide,20,20, true, true);
		uSShowPW1Toggle.setGraphic(new ImageView(imageHide));
		uSShowPW1Toggle.setText("");
		uSShowPW2Toggle.setGraphic(new ImageView(imageHide));
		uSShowPW2Toggle.setText("");
	}
	
	/**
	 * Method to show and hide users old password using the eye-icon
	 */
	@FXML
	public void showPW1() {		
		if (uSShowPW1Toggle.isSelected()) {
			password.setVisible(false);
			showOldPW.setVisible(true);
			showOldPW.setEditable(true);
			showOldPW.setText(password.getText());
			uSShowPW1Toggle.setGraphic(new ImageView(imageShow));
			uSShowPW1Toggle.setText("");
		} else if (!uSShowPW1Toggle.isSelected()) {
			password.setText(showOldPW.getText());
			password.setVisible(true);			
			showOldPW.setVisible(false);
			uSShowPW1Toggle.setGraphic(new ImageView(imageHide));
			uSShowPW1Toggle.setText("");
		}

	}
	
	/*
	 * Method to show and hide users new password using the eye-icon
	 */
	@FXML
	public void showPW2() {
		if (uSShowPW2Toggle.isSelected()) {
			npassword.setVisible(false);
			showNewPW.setVisible(true);
			showNewPW.setEditable(true);
			showNewPW.setText(npassword.getText());
			uSShowPW2Toggle.setGraphic(new ImageView(imageShow));
			uSShowPW2Toggle.setText("");
		} else if (!uSShowPW2Toggle.isSelected()) {
			npassword.setText(showNewPW.getText());
			npassword.setVisible(true);
			showNewPW.setVisible(false);
			uSShowPW2Toggle.setGraphic(new ImageView(imageHide));
			uSShowPW2Toggle.setText("");
		}

	}
	
	/**
	 * Method sets variable to the parameter provided from MainController.java 
	 * @param mainController, is the instance of MainController.java that is in the
	 * current thread running.
	 */
	public void setMainController(MainController mainController) {
		this.mc = mainController;
		this.controller = mc.getController();
		userName.setText(controller.loggedIn());
	}

	/**
	 * Method to close the view when button is pressed
	 * @param event, handles the on push events of binded buttons
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) uSCancelButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method to delete the users account from the database and make sure user is logged out.
	 * Alerts are used to give feedback to the user.
	 */
	@FXML
	public void deleteUser() {
		Alert alert1 = new Alert(AlertType.CONFIRMATION);
		alert1.setTitle(uSDeleteAlert1Title);
		alert1.setHeaderText(uSDeleteAlert1Header);
		alert1.setContentText(uSDeleteAlert1Content);
		ButtonType ok = new ButtonType(uSGeneralOKBT, ButtonData.OK_DONE);
		ButtonType cancel = new ButtonType(uSGeneralCancelBT, ButtonData.CANCEL_CLOSE);
		alert1.getButtonTypes().setAll(ok, cancel);
		Optional<ButtonType> result = alert1.showAndWait();
		if (result.get() == ok) {
			controller.deleteUser();
			mc.setlogUserOut();
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle(uSDeleteAlert2Title);
			alert2.setHeaderText(uSDeleteAlert2Header);
			alert2.setContentText(uSDeleteAlert2Content);
			alert2.showAndWait();
			Stage stage = (Stage) uSCancelButton.getScene().getWindow();
			stage.close();
		} else {

		}
	}

	/**
	 * Method used to change the users password.
	 * Alerts provide feedback for the user.
	 */
	@FXML
	public void changePassword() {
		if (isValid(npassword.getText())) {
			controller.changePW(controller.loggedIn(), password.getText(), npassword.getText());
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setTitle(uSDeleteAlert2Title);
			alert1.setHeaderText(uSChangePWAlert1Header);
			alert1.showAndWait();
		} else {
			Alert alert2 = new Alert(AlertType.ERROR);
			alert2.setTitle(uSChangePWAlert2Title);
			alert2.setHeaderText(uSChangePWAlert2Header);
			alert2.setContentText(uSChangePWAlert2Content);
			alert2.showAndWait();
		}

	}

	/**
	 * Method reminds the user for the correct way of choosing a password.
	 */
	@FXML
	public void pwReminder() {
		uSpwtooltip.setWrapText(true);
		uSpwtooltip.setTextOverrun(OverrunStyle.ELLIPSIS);

		npassword.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					uSpwtooltip.show(npassword, //
							// popup tooltip on the right, you can adjust these values for different
							// positions
							npassword.getScene().getWindow().getX() + npassword.getLayoutX() + npassword.getWidth(), //
							npassword.getScene().getWindow().getY() + npassword.getLayoutY() + npassword.getHeight()
									+ 0);
				} else {
					uSpwtooltip.hide();
				}
			}
		});
		showNewPW.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					uSpwtooltip.show(showNewPW, //
							// popup tooltip on the right, you can adjust these values for different
							// positions
							showNewPW.getScene().getWindow().getX() + showNewPW.getLayoutX() + showNewPW.getWidth(), //
							showNewPW.getScene().getWindow().getY() + showNewPW.getLayoutY() + showNewPW.getHeight()
									+ 0);
				} else {
					uSpwtooltip.hide();
				}
			}
		});
	}

	/**
	 * Variables for the correct password pattern and a compiler for the password pattern
	 */
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
	private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

	/**
	 * Used to check for password security
	 * 
	 * @param password is the inputed password
	 * @return true if it matches requirements
	 */
	public static boolean isValid(final String password) {
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
	
	/**
	 * Method to set the password to different variables depending on if the password is visible to the user or not.
	 */
	@FXML
	private void setText() {
		if (uSShowPW1Toggle.isSelected()) {
			showOldPW.setText(password.getText());
		} else if (!uSShowPW1Toggle.isSelected()) {
			password.setText(showOldPW.getText());
		}
		if (uSShowPW2Toggle.isSelected()) {
			showNewPW.setText(npassword.getText());
		} else if (!uSShowPW2Toggle.isSelected()) {
			npassword.setText(showNewPW.getText());
		}
	}


}
