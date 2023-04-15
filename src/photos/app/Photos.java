package photos.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photos.control.LoginController;
import photos.control.UserDataController;

/**
 * This class is the entry point for the Photos application.
 * 
 * @author Pavitra Patel, Huzaif Mansuri
 */
public class Photos extends Application 
{
	Stage mainStage;	
	
	/**
	 * The start method is called when the application is launched. It loads the Login.fxml file and sets up the UI, 
	 * sets up the stage, and displays it. It also sets up an event handler for the close button to prompt the user 
	 * to confirm the exit operation. It also takes care of serializing and de-serializing data before the start and
	 * and end of this method.
	 *
	 * @param primaryStage    the primary stage of the application
	 * @throws Exception      if an error occurs while initializing the application
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		mainStage = primaryStage;
		try
		{
			UserDataController.readFromAFile();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("../view/Login.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);
			
			LoginController loginController = loader.getController();
			loginController.start();
			
			mainStage.setScene(scene);
			mainStage.setTitle("Photos");
			mainStage.setResizable(false);
			mainStage.show();
			
			primaryStage.setOnCloseRequest(event -> {
	            event.consume();
	            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.CANCEL);
	            alert.showAndWait();
	            if(alert.getResult() == ButtonType.YES)
	            {
	    			UserDataController userData = UserDataController.getInstance();
	    			userData.writeToAFile();
	                Platform.exit();
	            }
	        });
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * The main method is the entry point for the application. It launches the application by calling the launch method
	 * from the Application class.
	 *
	 * @param args 		command line arguments (not used)
	 */
	public static void main(String[] args)
	{
		launch(args);
	}
}