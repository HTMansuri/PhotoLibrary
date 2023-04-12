package photos.control;

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
import javafx.stage.Stage;
import photos.app.UserDataController;

public class AdminController
{
    @FXML
    private ListView<String> adminList;
    
    // Use an ObservableList to store the list of users
    private final ObservableList<String> userList = FXCollections.observableArrayList();


    @FXML
    private TextField userName;

    @FXML
    void create(ActionEvent event)
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
    	else 
    	{
	    	userList.add(username);
	    	UserDataController.getInstance().addUser(user);
    		userName.clear();
    	}
    }

    @FXML
    void delete(ActionEvent event)
    {
    	String username = userName.getText();
    	if(UserDataController.getInstance().deleteUser(username)) {
    		userList.remove(username);
    		userName.clear();
    	}
    	else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Invalid Username!!!");
    		alert.setHeaderText("No User with provided username exists!!!");
    		alert.showAndWait();
    	}
    }

    @FXML
    void logout(ActionEvent event)
    {
    	try
    	{
    		Parent login = FXMLLoader.load(getClass().getResource("../design/Login.fxml"));
            Scene loginScene = new Scene(login);
            Stage mainStage = (Stage) adminList.getScene().getWindow();
            mainStage.setScene(loginScene);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    // Initialize the controller
    public void initialize() {
        // Set the adminList to display the userList
        adminList.setItems(userList);

        // Load the initial list of users from the UserManager
        userList.addAll(UserDataController.getInstance().getUsernames());
        
        // Set an event handler to fill the userName TextField when an item is clicked
        adminList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Get the selected item from the ListView
                String selectedItem = adminList.getSelectionModel().getSelectedItem();

                // Set the selected item as the text of the userName TextField
                userName.setText(selectedItem);
            }
        });
    }
}