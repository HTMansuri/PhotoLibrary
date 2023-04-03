package photos.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photos.control.LoginController;

public class Photos extends Application 
{
	Stage mainStage;	
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		// TODO Auto-generated method stub
		mainStage = primaryStage;
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("../design/Login.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);
			
			LoginController loginController = loader.getController();
			loginController.start();
			
			mainStage.setScene(scene);
			mainStage.setTitle("Calculator");
			mainStage.setResizable(false);
			mainStage.show();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		launch(args);
	}
}