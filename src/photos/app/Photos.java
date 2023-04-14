//add support for enter and key pressed in listview

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

public class Photos extends Application 
{
	Stage mainStage;	
	
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

	public static void main(String[] args)
	{
		launch(args);
	}
}