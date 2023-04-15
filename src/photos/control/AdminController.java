package photos.control;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
/**
 * When the admin login, they are redirected to AdminController class. Here they can list, add, and delete users.
 * 
 * @author Pavitra Patel, Huzaif Mansuri
 *
 */
public class AdminController
{
    @FXML
    private ListView<String> adminList;
    private final ObservableList<String> userList = FXCollections.observableArrayList();
    @FXML
    private TextField userName;
    @FXML
    private Button deleteB;
    @FXML
    
	/**
	 * Creates a new user with the username entered in the userName TextField. If the username already exists, 
	 * displays an error message. If the username is blank, displays an error message and clears the userName TextField.
	 * If the username is valid and not already in the user list, adds it to the user list, selects it in the adminList, 
	 * adds it to the list of users in the UserDataController, and clears the userName TextField.
	 * 
	 * @param event		the ActionEvent triggered by clicking the "Create" button
	 */
    public void create(ActionEvent event)
    {
    	String username = userName.getText();
    	User user = new User(username);
    	if(UserDataController.getInstance().containsUser(username))
    	{
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Invalid Username!!!");
    		alert.setHeaderText("A User with provided username already exists!!!");
    		alert.showAndWait();
    	}
    	else if(username.isBlank())
    	{
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Invalid Username!!!");
    		alert.setHeaderText("Please enter a valid Username!!!");
    		alert.showAndWait();
    		userName.clear();
    	}
    	else 
    	{
	    	userList.add(username);
	    	adminList.getSelectionModel().select(username);
	    	UserDataController.getInstance().addUser(user);
	    	deleteB.setDisable(false);
    		userName.clear();
    	}
    }

    /**
     * Deletes the user with the username specified in the userName field, if it exists.
     * If the specified user does not exist, an error message is displayed.
     * If the deletion is confirmed by the user, the user is removed from the userList and the list of users in 
     * UserDataController. The userName field is cleared, and if there are no more users in the userList, the 
     * delete button is disabled.
     * 
     * @param event 	the ActionEvent triggered by clicking the "Delete" button
     */
    @FXML
    public void delete(ActionEvent event)
    {
    	String username = userName.getText();
    	if(UserDataController.getInstance().containsUser(username)) {
    		Alert confirm = new Alert(Alert.AlertType.WARNING);
			confirm.setTitle("WARNING!!!");
			confirm.setContentText("Are you sure you want to delete \"" + username + "\" User?");
			confirm.setHeaderText(null);
			confirm.setResizable(false);
			confirm.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
			Optional<ButtonType> result = confirm.showAndWait();
			if(result.get() == ButtonType.OK)
			{
				userList.remove(username);
				UserDataController.getInstance().deleteUser(username);
				userName.clear();
	    		
				if(userList.isEmpty())
				{
					deleteB.setDisable(true);			        
				}
			}
    	}
    	else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Invalid Username!!!");
    		alert.setHeaderText("No User with provided username exists!!!");
    		alert.showAndWait();
    	}
    }

    /**
     * The admin logs out and returns to the Login Page upon clicking the LogOut button.
     * 
     * @param event 	the ActionEvent triggered by clicking the "LogOut" button
     */
    @FXML
    public void logout(ActionEvent event)
    {
    	try
    	{
    		Parent login = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
            Scene loginScene = new Scene(login);
            Stage mainStage = (Stage) adminList.getScene().getWindow();
            mainStage.setScene(loginScene);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    /**
     * Initializes the controller by setting the adminList to display the userList, loading the initial list of users
     * from the list of users from the UserDataController, disabling the delete button if the userList is empty, and 
     * setting an event handler to fill the userName TextField when an item is clicked in the adminList.
    */
    public void initialize()
    {
        // Set the adminList to display the userList
        adminList.setItems(userList);

        // Load the initial list of users from the UserManager
        userList.addAll(UserDataController.getInstance().getUsernames());
        
        if(userList.isEmpty())
        {
        	deleteB.setDisable(true);
        }
        // Set an event handler to fill the userName TextField when an item is clicked
        adminList.setOnMouseClicked(new EventHandler<MouseEvent>() 
        {
            @Override
            public void handle(MouseEvent event)
            {
                // Get the selected item from the ListView
                String selectedItem = adminList.getSelectionModel().getSelectedItem();

                // Set the selected item as the text of the userName TextField
                userName.setText(selectedItem);
            }
        });
    }
}