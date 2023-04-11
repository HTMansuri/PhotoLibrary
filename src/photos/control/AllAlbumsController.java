package photos.control;

import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import photos.app.UserDataController;

public class AllAlbumsController
{
    @FXML
    private ListView<String> allAlbumsList;
    private ObservableList<String> albums;
    
    @FXML
    private VBox displayAlbumDetails;
    
    public void start()
    {
    	albums = FXCollections.observableArrayList();
    	allAlbumsList.setItems(albums);
    	
    	User currentUser = UserDataController.getCurrentSessionUser();
    	
    	ArrayList<Album> currentAlbums = currentUser.getAlbumList();
    	for(Album a: currentAlbums)
    	{
    		albums.add(a.getAlbumName());
    	}
    	if(!albums.isEmpty())
    	{
    		allAlbumsList.getSelectionModel().select(0);
    	}
    	displayalbum();
    	
    	allAlbumsList.setCellFactory(lv ->
    	{
    	    ListCell<String> cell = new ListCell<>()
    	    {
    	    	protected void updateItem(String item, boolean empty)
    	    	{
    	            super.updateItem(item, empty);
    	            if(empty || item == null)
    	            {
    	                setText(null);
    	            }
    	            else
    	            {
    	                setText(item);
    	                setGraphic(null);
    	            }
    	        }
    	    };
    	    		
    	    cell.setOnMouseClicked(event ->
    	    {
    	        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
    	        {
    	            try
    	            {
    	                FXMLLoader loader = new FXMLLoader(getClass().getResource("../design/OpenAlbum.fxml"));
    	                Parent openAlbum = loader.load();
    	                OpenAlbumController controller = loader.getController();
    	                Scene allAlbumsScene = new Scene(openAlbum);
    	                controller.start();
    	                Stage mainStage = (Stage) allAlbumsList.getScene().getWindow();
    	                mainStage.setScene(allAlbumsScene);
    	            }
    	            catch (Exception e)
    	            {
    	                e.printStackTrace();
    	            }
    	        }
    	    });
    	    return cell;
    	});
	}

    @FXML
    public void create(ActionEvent event)
    {
    	TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Album: ");
        dialog.setHeaderText("Enter the name of new album: ");
        
        dialog.showAndWait().ifPresent(str ->
        {
            if(!albums.contains(str))
            {
            	Album newAlbum = new Album(str);
            	User currentUser = UserDataController.getCurrentSessionUser();
                currentUser.addAlbum(newAlbum);
            	albums.add(str);
            	int index = albums.indexOf(str);
                allAlbumsList.getSelectionModel().select(index);
            	displayalbum();
            }
            else
            {
            	Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Duplicate Album Name");
                alert.setHeaderText(null);
                alert.setContentText("An album with the name \"" + str + "\" already exists.");
                alert.showAndWait();
                create(event);
            }
        });
    }

    @FXML
    public void delete(ActionEvent event)
    {
    	int selectedID = allAlbumsList.getSelectionModel().getSelectedIndex();
		if(selectedID != -1)
		{
			Alert confirm = new Alert(Alert.AlertType.WARNING);
			confirm.setTitle("WARNING!!!");
			confirm.setContentText("Are you sure you want to delete selected Album?");
			confirm.setHeaderText(null);
			confirm.setResizable(false);
			confirm.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
			Optional<ButtonType> result = confirm.showAndWait();
			if(result.get() == ButtonType.OK)
			{
				User currentUser = UserDataController.getCurrentSessionUser();
				String selectedAlbum = allAlbumsList.getSelectionModel().getSelectedItem();
				currentUser.removeAlbum(selectedAlbum);
				albums.remove(selectedID);
				
				if(selectedID >= albums.size())
					selectedID -= 1;
				
				if(!albums.isEmpty())
				{
					allAlbumsList.getSelectionModel().select(selectedID);
				}
			}
			displayalbum();
		}
    }

    @FXML
    public void logOut(ActionEvent event)
    {
    	try
    	{
    		Parent login = FXMLLoader.load(getClass().getResource("../design/Login.fxml"));
            Scene loginScene = new Scene(login);
            Stage mainStage = (Stage) allAlbumsList.getScene().getWindow();
            mainStage.setScene(loginScene);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    @FXML
    public void rename(ActionEvent event)
    {	
    	String selectedItem = allAlbumsList.getSelectionModel().getSelectedItem();
    	if (selectedItem != null)
    	{
    	    TextInputDialog dialog = new TextInputDialog();
    	    dialog.setTitle("Rename Album: ");
    	    dialog.setHeaderText("Enter a new name:");

    	    Optional<String> result = dialog.showAndWait();
    	    if(result.isPresent())
    	    {
    	        String newName = result.get();
    	        if(!newName.equals(selectedItem) && !albums.contains(newName))
    	        {
    	        	int index = albums.indexOf(selectedItem);
    	        	User currentUser = UserDataController.getCurrentSessionUser();
    	        	currentUser.renameAlbum(selectedItem, newName);
    	            albums.set(index, newName);
    	            displayalbum();
    	        }
    	        else
    	        {
    	        	Alert alert = new Alert(Alert.AlertType.ERROR);
    	            alert.setTitle("Duplicate Name");
    	            alert.setHeaderText("The name \"" + newName + "\" already exists.");
    	            alert.setContentText("Please enter a different name.");
    	            alert.showAndWait();
    	            rename(event);
    	        }
    	    }
    	}
    }
    
    @FXML
    public void displayalbum()
	{	
    	Label label0 = new Label("Selected Album Details: ");
		Label label1 = new Label("Album Name: ");
	    Label label2 = new Label("No. of Photos: ");
	    Label label3 = new Label("Date Range: ");
	    
	    if(!albums.isEmpty()) 
	    {
			int selectedID = allAlbumsList.getSelectionModel().getSelectedIndex();
			if(selectedID == -1)
				allAlbumsList.getSelectionModel().select(0);
			else
				allAlbumsList.getSelectionModel().select(selectedID);
			
			String name = null;
			int photosNum = 0;
			String dateRange = null;
			
			User currentUser = UserDataController.getCurrentSessionUser();
	    	
	    	ArrayList<Album> currentAlbums = currentUser.getAlbumList();
	    	for(Album a: currentAlbums)
	    	{
	    		if(a.getAlbumName().equals(allAlbumsList.getSelectionModel().getSelectedItem()))
	    		{
	    			name = a.getAlbumName();
	    			photosNum = a.PhotosNum(a);
	    			//
	    		}
	    	}
	    	
			label0.setText("Selected Album Details: ");
			label0.setFont(new Font(20));
			label1.setText("Album Name: " + name);
			label1.setFont(new Font(14));
		    label2.setText("No. of Photos: " + photosNum);
		    label2.setFont(new Font(14));
		    label3.setText("Date Range: " + dateRange);
		    label3.setFont(new Font(14));
	    }
	    displayAlbumDetails.getChildren().clear();
	    displayAlbumDetails.setSpacing(10);
	    displayAlbumDetails.getChildren().addAll(label0, label1, label2, label3);
	    if(albums.isEmpty()) 
	    	displayAlbumDetails.getChildren().clear();
	}

    @FXML
    public void tagPairsSearch(ActionEvent event)
    {
    	
    }
    
    @FXML
    public void dateRangeSearch(ActionEvent event)
    {
    	
    }
}