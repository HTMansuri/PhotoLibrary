//why initializable?
//user login not working

package photos.control;

import java.io.*;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable
{
    @FXML
    private TextField userName;
    
    public void start()
    {
    	User stock = new User("stock");
    	UserDataController userData = UserDataController.getInstance();
    	userData.addUser(stock);
    	//Album stockAlbum = new Album("stock");
    	//stock.addAlbum(stockAlbum); //this won't find
    	
//    	Photo p1 = new Photo("stockphoto1", "../data/stockphoto1");
//    	Photo p2 = new Photo("stockphoto2", "../data/stockphoto1");
//    	Photo p3 = new Photo("stockphoto3", "../data/stockphoto1");
//    	Photo p4 = new Photo("stockphoto4", "../data/stockphoto1");
//    	Photo p5 = new Photo("stockphoto5", "../data/stockphoto1");
//    	
//    	stockAlbum.addPhotos(p1);
//    	stockAlbum.addPhotos(p2);
//    	stockAlbum.addPhotos(p3);
//    	stockAlbum.addPhotos(p4);
//    	stockAlbum.addPhotos(p5);
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
				Parent admin = FXMLLoader.load(getClass().getResource("../design/admin.fxml"));
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
    				Parent allAlbums = FXMLLoader.load(getClass().getResource("../design/AllAlbums.fxml"));
    		        Scene allAlbumsScene = new Scene(allAlbums);
    		        Stage mainStage = (Stage) userName.getScene().getWindow();
    		        mainStage.setScene(allAlbumsScene);
                }
                catch (IOException e) 
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
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		
	}
}