package otp.group6.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Class that starts the application
 * 
 * @author Roosa Laukkanen, Kevin Akkoyun
 */
public class MainApplication extends Application {

	private Stage primaryStage;
	private AnchorPane rootLayout;
	private MainController mainController;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	/**
	 * Starts the application
	 */
	public void start(Stage primaryStage) throws Exception {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AudioEditor");

		initializeRootLayout();

		Platform.setImplicitExit(false);

		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				// Checks if there are any unsaved files in mixer and closes the application if
				// ok to do so
				if (mainController.isMixerOkToExit() == true) {
					mainController.exitRoutine();
					Platform.exit();
					System.exit(0);
				} else {
					event.consume();
				}
			}
		});
	}

	/**
	 * Initializes the root layout.
	 */
	public void initializeRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("/MainView.fxml"));
			loader.setResources(getLocalization());

			rootLayout = (AnchorPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			mainController = loader.getController();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets app localization settings from AudioEditor.properties. <br>
	 * Creates a new {@link Locale} from .properties and finds appropriate
	 * localization file
	 * 
	 * @return Returns a {@link ResourceBundle} with set localization
	 */
	public ResourceBundle getLocalization() {
		Locale locale = new Locale("en","US");
		Locale.setDefault(locale);
		
		String appConfigPath = "src/main/resources/properties/AudioEditor.properties";
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(appConfigPath).getAbsoluteFile()));
			String language = properties.getProperty("language");
			String country = properties.getProperty("country");
			
			Locale curLocale = new Locale(language, country);
			return ResourceBundle.getBundle("properties/ApplicationResources", curLocale);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Locale.getDefault());
			return ResourceBundle.getBundle("properties/ApplicationResources", Locale.getDefault());
		}
	}
}
