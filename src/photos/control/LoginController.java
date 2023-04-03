package photos.control;

import java.io.*;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController 
{
    @FXML
    private TextField userName;
    private static File admin = new File("admin.txt");
    private static File user = new File("user.txt");
    
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
    	try(Scanner sc = new Scanner(admin))
    	{
			while(sc.hasNext())
			{
				String user = sc.nextLine();
				if(user.equals(userCred))
				{
					System.out.println("Logged In: " + userCred);
				}
			}
			sc.close();
		}
    	catch(FileNotFoundException e)
    	{
			e.printStackTrace();
		}
    }
}