package otp.group6.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import otp.group6.controller.Controller;

/**
 * Class handles user registering and logging in
 * 
 * @author Joonas Soininen
 *
 */
public class RegisterLoginController implements Initializable {
	Controller controller;
	MainController mc;

	public RegisterLoginController() {

	}
	@FXML
	private Label rLWelcomeLabel;
	@FXML
	private Label rLPleaseLabel;
	@FXML
	private Label rLUsernameLabel;
	@FXML
	private TextField rLUserTextField;
	@FXML
	private Label rLPasswdLabel;
	@FXML
	private TextField rLPasswdField;
	@FXML
	private TextField rLPasswdTextField;
	@FXML
	private ToggleButton rLShowPwToggle;
	@FXML
	private Button rLRegisterButton;
	@FXML
	private Label rLLorLabel;
	@FXML
	private Button rLLoginButton;
	@FXML
	private Label rLForgotLabel;
	@FXML
	private Button rLCloseButton;
	
	private Image imageShow;
	
	private Image imageHide;
	
	private FileInputStream imageinputshow;
	
	private FileInputStream imageinputhide;

	private String lastPW;
	
	final Tooltip rLpwtooltip = new Tooltip();

	final Tooltip rLwuntooltip = new Tooltip();

	final Tooltip rLlogintip = new Tooltip();
	
	/**
	 * Method initializes this class when loaded, calls {@link #setLocalization(ResourceBundle)} to set certain variables passing the ResourceBundle to it.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setLocalization(arg1);
	}
	
	/**
	 * Method sets all visible texts and button labels to chosen language. Sets specific images to specific buttons.
	 * @param bundle transfers the localization data to the method so the right language is set
	 */
	private void setLocalization(ResourceBundle bundle) {
		rLWelcomeLabel.setText(bundle.getString("rLWelcomeLabel"));
		rLPleaseLabel.setText(bundle.getString("rLPleaseLabel"));
		rLUsernameLabel.setText(bundle.getString("rLUsernameLabel"));
		rLUserTextField.setPromptText(bundle.getString("rLUserTextField"));
		rLPasswdLabel.setText(bundle.getString("rLPasswdLabel"));
		//rLShowPwToggle.setText(bundle.getString("rLShowPwToggle"));
		rLRegisterButton.setText(bundle.getString("rLRegisterButton"));
		rLLorLabel.setText(bundle.getString("rLLorLabel"));
		rLLoginButton.setText(bundle.getString("rLLoginButton"));
		rLForgotLabel.setText(bundle.getString("rLForgotLabel"));
		rLCloseButton.setText(bundle.getString("rLCloseButton"));
		rLpwtooltip.setText(bundle.getString("rLpwtooltip"));
		rLwuntooltip.setText(bundle.getString("rLwuntooltip"));
		rLlogintip.setText(bundle.getString("rLlogintip"));
		
		try {
			imageinputshow = new FileInputStream("src/main/resources/images/showPW.jpg");
			imageinputhide = new FileInputStream("src/main/resources/images/hidePW.jpg");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageShow = new Image(imageinputshow,20,20, true, true);
		imageHide = new Image(imageinputhide,20,20, true, true);
		rLShowPwToggle.setGraphic(new ImageView(imageHide));
		rLShowPwToggle.setText("");
	}
	
	
	/**
	 * Method for user to see their password on toggleButton click
	 */
	@FXML
	public void showPW() {
		if (rLShowPwToggle.isSelected()) {
			rLPasswdField.setVisible(false);
			rLPasswdTextField.setVisible(true);
			rLPasswdTextField.setEditable(true);
			rLPasswdTextField.setText(rLPasswdField.getText());
			rLShowPwToggle.setGraphic(new ImageView(imageShow));
			rLShowPwToggle.setText("");
			
		} else if (!rLShowPwToggle.isSelected()) {
			rLPasswdTextField.setVisible(false);
			rLPasswdField.setText(rLPasswdTextField.getText());
			rLPasswdField.setVisible(true);
			rLShowPwToggle.setGraphic(new ImageView(imageHide));
			rLShowPwToggle.setText("");
		}

	}
	
	/**
	 * Method to set either the PasswordText or TextField text as the password
	 */
	@FXML
	public void setFinalPW() {
		if (rLShowPwToggle.isSelected()) {
			setLastPW(rLPasswdTextField.getText());
		} else {
			setLastPW(rLPasswdField.getText());	
		}		
	}
	
	/**
	 * Method to close opened scenes
	 * 
	 * @param event
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) rLCloseButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method to get mainController
	 * 
	 * @param mainController
	 */
	public void setMainController(MainController mainController) {
		this.mc = mainController;
		this.controller = mc.getController();
		controller.intializeDatabaseConnection();
		pwReminder();
		rLUserTextField.requestFocus();
	}

	/**
	 * Method called to remind of correct password format. Only called form username
	 * textfield.
	 */
	@FXML
	public void pwReminder() {
		rLpwtooltip.setWrapText(true);
		rLpwtooltip.setTextOverrun(OverrunStyle.ELLIPSIS);

		rLPasswdField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					rLpwtooltip.show(rLPasswdField, //
							// popup tooltip on the right, you can adjust these values for different
							// positions
							rLPasswdField.getScene().getWindow().getX() + rLPasswdField.getLayoutX() + rLPasswdField.getWidth() + 0, //
							rLPasswdField.getScene().getWindow().getY() + rLPasswdField.getLayoutY() + rLPasswdField.getHeight());
				} else {
					rLpwtooltip.hide();
				}
			}
		});
	}

	/**
	 * Method registers new user to the database
	 * 
	 * @param event
	 * @throws SQLException
	 */
	public void registerUser(ActionEvent event) throws SQLException {
		// System.out.println(username.getText().toString().length());// Poistettava
		// System.out.println(password.getText());// Poistettava

		if ((unisValid(rLUserTextField.getText()))&&!(controller.chekcforUser(rLUserTextField.getText()))) {
			// System.out.println("VAPAA"); // Poistettava
			rLUserTextField.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
			rLwuntooltip.hide();
			if (pwIsValid(getLastPW())) {
				controller.createUser(rLUserTextField.getText(), getLastPW());
				loginUser();
			} else {
				// System.out.println("UUS PASSU"); // Poistettava
				rLPasswdField.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				rLPasswdTextField.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				// password.setStyle("-fx-control-inner-background:#000000; -fx-font-family:
				// Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000;
				// -fx-text-fill: #00ff00; ");
				rLwuntooltip.hide();
				rLPasswdField.requestFocus();
				rLPasswdTextField.requestFocus();
			}

		} else {
			rLUserTextField.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			rLwuntooltip.setWrapText(true);
			rLwuntooltip.setTextOverrun(OverrunStyle.ELLIPSIS);
			// System.out.println("Varattu!"); // Poistettava
			rLUserTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						rLwuntooltip.show(rLUserTextField, //
								// popup tooltip on the right, you can adjust these values for different
								// positions
								rLUserTextField.getScene().getWindow().getX() + rLUserTextField.getLayoutX() + rLUserTextField.getWidth()
										+ 35, //
								rLUserTextField.getScene().getWindow().getY() + rLUserTextField.getLayoutY() + rLUserTextField.getHeight());

					} else {
						rLwuntooltip.hide();
					}
				}
			});
			rLUserTextField.requestFocus();
		}

	}

	/**
	 * Method logs user in to access the database save function
	 */
	public void loginUser() {
		rLlogintip.setWrapText(true);
		rLlogintip.setTextOverrun(OverrunStyle.ELLIPSIS);
		if (controller.loginUser(rLUserTextField.getText(), getLastPW()) == "No user") {
			rLUserTextField.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			rLPasswdField.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			rLPasswdTextField.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			rLUserTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						rLlogintip.show(rLUserTextField, //
								// popup tooltip on the right, you can adjust these values for different
								// positions
								rLUserTextField.getScene().getWindow().getX() + rLUserTextField.getLayoutX() + rLUserTextField.getWidth()
										+ 35, //
								rLUserTextField.getScene().getWindow().getY() + rLUserTextField.getLayoutY() + rLUserTextField.getHeight());

					} else {
						rLlogintip.hide();
					}
				}
			});
			rLUserTextField.requestFocus();
		} else {
			if (controller.loginUser(rLUserTextField.getText(), getLastPW()) == "Incorrect user or pw") {
				rLUserTextField.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				rLPasswdField.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				rLPasswdTextField.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				rLUserTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						if (newValue) {
							rLlogintip.show(rLUserTextField, //
									// popup tooltip on the right, you can adjust these values for different
									// positions
									rLUserTextField.getScene().getWindow().getX() + rLUserTextField.getLayoutX() + rLUserTextField.getWidth()
											+ 35, //
									rLUserTextField.getScene().getWindow().getY() + rLUserTextField.getLayoutY()
											+ rLUserTextField.getHeight());

						} else {
							rLlogintip.hide();
						}
					}
				});
				rLUserTextField.requestFocus();
			} else {
				controller.loginUser(rLUserTextField.getText(), getLastPW());
				Stage stage = (Stage) rLCloseButton.getScene().getWindow();
				stage.close();
				// mc.openMixerSave();
				mc.setlogUserIn();
			}

		}

	}

	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
	private static final Pattern pwPattern = Pattern.compile(PASSWORD_PATTERN);

	/**
	 * Used to check for password security
	 * 
	 * @param password is the inputed password
	 * @return true if it matches requirements
	 */
	public static boolean pwIsValid(final String password) {
		Matcher matcher = pwPattern.matcher(password);
		return matcher.matches();
	}
	
	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
	private static final Pattern unPattern = Pattern.compile(USERNAME_PATTERN);
	
	/**
	 * Method validates inputed user name, in this case checks for whitespace usage.
	 * @param username users inputed user name.
	 * @return true if user name meet the standards.
	 */
	public static boolean unisValid(final String username) {
		Matcher matcher = unPattern.matcher(username);
		return matcher.matches();
	}

	/**
	 * Method to make sure the right password is returned to the database
	 * @return last set password.
	 */
	private String getLastPW() {
		return lastPW;
	}

	/**
	 * Sets the right password to be sent to the database.
	 * @param lastPW 
	 */
	private void setLastPW(String lastPW) {
		this.lastPW = lastPW;
	}

}
