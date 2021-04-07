package otp.group6.view;

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
 * Class is used to change users password or delete users account from our
 * database
 * 
 * @author Joonas Soininen
 *
 */
public class UserSettingsController implements Initializable{
	Controller controller;
	MainController mc;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pwReminder();
		//TODO Nämä properties tiedostosta haettavaksi
		uSHeaderLabel.setText("Käyttäjäasetukset");
		uSXButton.setText("X");
		uSChangePWLabel.setText("Vaihda salasana");
		uSOldPWLabel.setText("Vanha salasana");
		uSShowPW1Toggle.setText("SILMÄ");
		uSNewPWLabel.setText("Uusi salasana");
		uSShowPW2Toggle.setText("SILMÄ");
		uSChangeButton.setText("Vaihda");
		uSUserDeleLabel.setText("Poista käyttäjätunnus");
		uSDeleteUserButton.setText("Poista");
		uSCancelButton.setText("Peruuta");
		uSpwtooltip.setText("Salasanan pitää sisältää 8-20 merkkiä,\nvähintään yksi iso kirjain ja vähintään yksi numero");
		uSGeneralOKBT="Kyllä";
		uSGeneralCancelBT="Ei";
		uSDeleteAlert1Title="Poistatko tunnuksen?";
		uSDeleteAlert1Header="Olet poistamassa käyttäjätunnusta pysyvästi.\nTunnuksen poiston jälkeen mitään tietoja ei pystytä palauttamaan.";
		uSDeleteAlert1Content="Oletko varma, että haluat poistaa käyttäjätunnuksen?";
		uSDeleteAlert2Title="Onnistui!";
		uSDeleteAlert2Header="Käyttäjätunnus poistettu";
		uSDeleteAlert2Content="Kiitos, kun käytit ohjelmaamme!";
		uSChangePWAlert1Header="Salasanan vaihto onnistui!";
		uSChangePWAlert2Title="Virhe!";
		uSChangePWAlert2Header="Jotain meni vikaan, ole hyvä ja koita uudelleen";
		uSChangePWAlert2Content="Jos virhe toistuu ota yhteyttä ylläpitoon";

	}
	
	@FXML
	public void showPW1() {		
		if (uSShowPW1Toggle.isSelected()) {
			password.setVisible(false);
			showOldPW.setVisible(true);
			showOldPW.setEditable(true);
			showOldPW.setText(password.getText());
		} else if (!uSShowPW1Toggle.isSelected()) {
			password.setText(showOldPW.getText());
			password.setVisible(true);			
			showOldPW.setVisible(false);
		}

	}
	
	@FXML
	public void showPW2() {
		if (uSShowPW2Toggle.isSelected()) {
			npassword.setVisible(false);
			showNewPW.setVisible(true);
			showNewPW.setEditable(true);
			showNewPW.setText(npassword.getText());
		} else if (!uSShowPW2Toggle.isSelected()) {
			npassword.setText(showNewPW.getText());
			npassword.setVisible(true);
			showNewPW.setVisible(false);
		}

	}
	
	/**
	 * Method to get mainController
	 * 
	 * @param mainController
	 */
	public void setMainController(MainController mainController) {
		this.mc = mainController;
		this.controller = mc.getController();
		userName.setText(controller.loggedIn());
	}

	/**
	 * Method to close opened scenes
	 * 
	 * @param event
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) uSCancelButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method deletes user from the database.
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
	 * Method for changing password
	 */
	@FXML
	public void changePassword() {
		if (isValid(npassword.getText())) {
			controller.changePW(controller.loggedIn(), password.getText(), npassword.getText());
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setTitle(uSDeleteAlert2Title);
			alert1.setHeaderText(uSChangePWAlert1Header);
			alert1.showAndWait();
			//Stage stage = (Stage) uSCancelButton.getScene().getWindow();
			//stage.close();
		} else {
			Alert alert2 = new Alert(AlertType.ERROR);
			alert2.setTitle(uSChangePWAlert2Title);
			alert2.setHeaderText(uSChangePWAlert2Header);
			alert2.setContentText(uSChangePWAlert2Content);
			alert2.showAndWait();
		}

	}

	/**
	 * Method called to remind of correct password format. Only called form username
	 * textfield.
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
	}

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
