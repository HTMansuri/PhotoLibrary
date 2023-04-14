package photos.control;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController
{
    @FXML
    private TextField userName;
    
    public void start()
    {
    	
	}
    
	@FXML
    public void Login(ActionEvent event)
    {
    	boolean userNotFound = false;
    	String user = userName.getText();
    	if(user.equals("admin"))
    	{
            try
            {
				Parent admin = FXMLLoader.load(getClass().getResource("../view/admin.fxml"));
		        Scene adminScene = new Scene(admin);
		        Stage mainStage = (Stage) userName.getScene().getWindow();
		        mainStage.setScene(adminScene);
            }
            catch (IOException e) 
            {
				e.printStackTrace();
			}
    	}
    	else
    	{
    		UserDataController userData = UserDataController.getInstance();
    		if(userData.containsUser(user))
    		{
    			try
                {
//    				Parent allAlbums = FXMLLoader.load(getClass().getResource("../view/AllAlbums.fxml"));
//    		        Scene allAlbumsScene = new Scene(allAlbums);
//    		        Stage mainStage = (Stage) userName.getScene().getWindow();
//    		        mainStage.setScene(allAlbumsScene);
    				FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AllAlbums.fxml"));
    				Parent allAlbums = loader.load();
    				AllAlbumsController controller = loader.getController();
    				Scene allAlbumsScene = new Scene(allAlbums);
    				
    				userData.setCurrentSessionUser(user);
    				
    				controller.start(); // Call the start method on the controller instance
    				Stage mainStage = (Stage) userName.getScene().getWindow();
    				mainStage.setScene(allAlbumsScene);
                }
                catch (Exception e) 
                {
    				e.printStackTrace();
    			}
    		}
    		else
    		{
    			userNotFound = true;
    		}
    	}
    	if(userNotFound)
    	{
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Invalid Credentails!!!");
    		alert.setHeaderText("The username entered is invalid!!!");
    		alert.showAndWait();
    	}
    }
}