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

/**
 * This class is the controller for the Login Scene. Once the user clicks Login Button this controller is triggered.
 * 
 * @author Pavitra Patel, Huzaif Mansuri
 */
public class LoginController
{
    @FXML
    private TextField userName;
    
    /**
     * The start method doesn't perform any initializing tasks for this class
     */
    public void start()
    {
    	
	}
    
    /**
     * A user can type their username and click Login button. If the username is "admin" the admin is redirected to
     * Admin scene. If the username is not admin then we get the list of users from the UserDataController and check to
     * see if the user exists. If the user exists then we open AllAlbums Scene for that user else we show them a warning.
     * 
     * @param event		The ActionEvent that is triggered upon selecting "Login" Button
     */
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