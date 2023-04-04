package photos.control;

import java.io.*;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController 
{
    @FXML
    private TextField userName;
    
    @FXML
    private Button loginButton;
    
    private static File admin = new File("src/photos/data/admin.txt");
    private static File user = new File("src/photos/data/user.txt");
    
    public void start()
    {
		try 
		{
			if(admin.createNewFile())
			{
				FileWriter writer = new FileWriter(admin);
				writer.write("admin\nstock\n");
				writer.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			if(user.createNewFile())
			{
				FileWriter w = new FileWriter(user);
				w.write("stock\n");
				//add photos
				w.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
    
    @FXML
    void Login(ActionEvent event)
    {
    	String userCred = userName.getText();
    	boolean flag = false;
    	
    	try(Scanner sc = new Scanner(admin))
    	{
			while(sc.hasNext())
			{
				String user = sc.nextLine();
				if(user.equals(userCred))
				{
					flag = true;
					//admin
					Stage mainStage = (Stage)loginButton.getScene().getWindow();
					if(userCred.equals("admin"))
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
			            break;
			    	}
					else //user
					{
						try
			            {
							Parent allAlbums = FXMLLoader.load(getClass().getResource("../design/AllAlbums.fxml"));
					        Scene allAlbumsScene = new Scene(allAlbums);
					        mainStage.setScene(allAlbumsScene);
					        mainStage.show();
			            }
			            catch (IOException e) 
			            {
							e.printStackTrace();
						}
						break;
					}
				}
			}
			
			if(!flag)
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Invalid Credentials!!!");
				alert.setHeaderText("Either the user doesn't exist or the credentails are incorrect!!!");
				alert.showAndWait();
			}
			
			sc.close();
		}
    	catch(FileNotFoundException e)
    	{
			e.printStackTrace();
		}
    }
}