// This controller will be used to store user objects to a file. We will have methods writeFromAFile and readFromAFile
// which will be called before application opens and closes respectively to store and get data.
// implements Singelton design pattern
// all classes to serialize implements Serializable

package photos.control;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class UserDataController implements Serializable
{
	private static UserDataController instance = null;
	private ArrayList<User> users;
	
	private UserDataController()
	{
		users = new ArrayList<>();
	}
	
	public static UserDataController getInstance()
	{
		if(instance == null)
		{
			instance = new UserDataController();
		}
		return instance;
	}
	
	public static UserDataController readFromAFile()
	{
		UserDataController loadUsers = UserDataController.getInstance();
		
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("../data/users.ser")))
		{
	        ArrayList<User> userList = (ArrayList<User>)ois.readObject();
	        loadUsers.users = userList;
	    }
		catch(Exception e)
		{
	        e.printStackTrace();
	    }
		
		return loadUsers;
	}
	
	public void writeToAFile()
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("../data/users.ser")))
		{
            oos.writeObject(users);
        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
	}
}