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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Serializable, Initializable
{
    @FXML
    private TextField userName;
    
    @FXML
    private Button loginButton;
    
    public void start()
    {
    	User stockUser = new User("stock");
    	Album stockAlbum = new Album("stock");
    	stockUser.addAlbum(stockAlbum);
    	
    	Photo p1 = new Photo("stockphoto1", "../data/stockphoto1");
    	Photo p2 = new Photo("stockphoto2", "../data/stockphoto1");
    	Photo p3 = new Photo("stockphoto3", "../data/stockphoto1");
    	Photo p4 = new Photo("stockphoto4", "../data/stockphoto1");
    	Photo p5 = new Photo("stockphoto5", "../data/stockphoto1");
    	
    	stockAlbum.addPhotos(p1);
    	stockAlbum.addPhotos(p2);
    	stockAlbum.addPhotos(p3);
    	stockAlbum.addPhotos(p4);
    	stockAlbum.addPhotos(p5);
	}
    
    @FXML
    public void Login(ActionEvent event)
    {
    	Stage mainStage = null;
    	String user = userName.getText();
    	if(user.equals("admin"))
    	{
            try
            {
				Parent admin = FXMLLoader.load(getClass().getResource("../design/admin.fxml"));
		        Scene adminScene = new Scene(admin);
		        mainStage.setScene(adminScene);
		        mainStage.show();
            }
            catch (IOException e) 
            {
				e.printStackTrace();
			}
    	}
    	else
    	{
    		
    	}
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		
	}
}