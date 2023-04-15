package photos.control;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

/**
 * This class is entry point when a user logsin. All the albums of a user will be displayed here.
 * 
 * @author Pavitra Patel, Huzaif Mansuri
 */
public class AllAlbumsController
{
	//data fields
    @FXML
    private ListView<String> allAlbumsList;
    public static ObservableList<String> albums;
    @FXML
    private VBox displayAlbumDetails;
    @FXML
    private Button renameB;
    @FXML
    private Button deleteB;
    @FXML
    private Button searchByDateRange;
    @FXML
    private Button searchByTags;
    private ObservableList<String> categories;
    
    /**
     * This is the first method that will be executed after AllAlbums scene opens. The List View allAlbumsList is
     * set to observable list albums. All the albums for that user are populated in the list. In the every row of List
     * View, we update --
     */
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
    	
    	if(albums.isEmpty())
    	{
    		deleteB.setDisable(true);
    		renameB.setDisable(true);
    	}
    	
    	allAlbumsList.setCellFactory(lv ->
    	{
    	    ListCell<String> cell = new ListCell<>()
    	    {
    	    	protected void updateItem(String item, boolean empty)
                {
    	    		String path = null;
                    super.updateItem(item, empty);
                    if(empty || item == null)
                    {
                        setText(null);
                        setGraphic(null);
                    }
                    else
                    {
                        User u = UserDataController.getCurrentSessionUser();
                        ArrayList<Album> a = u.getAlbumList();
                        for(Album am: a)
                        {
                        	if(am.getAlbumName().equals(item))
                        	{
                        		ArrayList<Photo> p = am.getPhotoList();
                        		if(p.size() > 0)
                        		{
                        			Photo lastP = p.get(p.size()-1);
                            		path = lastP.getImagePath();
                        		}
                        	}
                        }
                        if(path == null)
                        {
                        	path = "data/tempphoto.png";
                        }
                        ImageView imageView = new ImageView();
                        Image image = new Image("file:" + path);
                        Label name = new Label(item);
                        name.setFont(new Font(20));
                        imageView.setFitWidth(150);
                        imageView.setFitHeight(120);
                        imageView.setImage(image);
                        
                        HBox hb = new HBox();
                        VBox vb = new VBox();
                        vb.getChildren().addAll(name);
                        vb.setPadding(new Insets(40, 0, 0, 40));
                        hb.getChildren().addAll(imageView, vb);
                        setGraphic(hb);
                    }
                }
    	    };
    	    
    	    cell.setOnMouseClicked(event ->
    	    {
    	        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
    	        {
    	            try
    	            {
    	            	String aNm = allAlbumsList.getSelectionModel().getSelectedItem();
    	            	User u = UserDataController.getCurrentSessionUser();
    	            	u.setCurrentSessionAlbum(aNm);
    	            	
    	                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/OpenAlbum.fxml"));
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
    	
    	categories = FXCollections.observableArrayList();
    	categories.add(0, "--Select--");
    	categories.addAll(currentUser.getCategoryList());	
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
        		deleteB.setDisable(false);
        		renameB.setDisable(false);
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
				else
				{
		    		deleteB.setDisable(true);
		    		renameB.setDisable(true);
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
    		Parent login = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
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
	    			photosNum = a.PhotosNum();
	    			dateRange = a.dateRange();
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
    	Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Tag Pair Search: ");
        popup.setResizable(false);
        // Create the UI elements for the popup
        
        VBox v1 = new VBox();
        Label categoryLabel = new Label("Select Tag Category 1:");
        ChoiceBox<String> categoryChoiceBox = new ChoiceBox<>(categories);
        categoryChoiceBox.getSelectionModel().selectFirst();
        Label tagLabel = new Label("Tag Value:");
        TextField tagTextField = new TextField();
        v1.getChildren().addAll(categoryLabel, categoryChoiceBox, tagLabel, tagTextField);
        v1.setAlignment(Pos.CENTER);
        v1.setSpacing(10);
        v1.setPadding(new Insets(10));
        
        VBox vm = new VBox();
        ChoiceBox<String> cbs = new ChoiceBox<>();
        cbs.getItems().add(0, "--Select--");
        cbs.getItems().add(1, "AND");
        cbs.getItems().add(2, "OR");
        cbs.getSelectionModel().select(0);
        vm.getChildren().add(cbs);
        vm.setAlignment(Pos.CENTER);
        vm.setSpacing(80);
        vm.setPadding(new Insets(10));
        
        VBox v2 = new VBox();
        Label categoryLabel2 = new Label("Select Tag Category 1:");
        ChoiceBox<String> categoryChoiceBox2 = new ChoiceBox<>(categories);
        categoryChoiceBox2.getSelectionModel().selectFirst();
        Label tagLabel2 = new Label("Tag Value:");
        TextField tagTextField2 = new TextField();
        v2.getChildren().addAll(categoryLabel2, categoryChoiceBox2, tagLabel2, tagTextField2);
        v2.setAlignment(Pos.CENTER);
        v2.setSpacing(10);
        v2.setPadding(new Insets(10));
        
        HBox h = new HBox();
        h.getChildren().addAll(v1, vm, v2);
        
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String category = categoryChoiceBox.getValue();
            String tagValue = tagTextField.getText();
            String tagStr = category + " : " + tagValue;
            String category2 = categoryChoiceBox2.getValue();
            String tagValue2 = tagTextField2.getText();
            String tagStr2 = category2 + " : " + tagValue2;
            String operator = cbs.getValue();
            // Load the searchPhotos.fxml file and pass the tag pair to the controller
            if(categoryChoiceBox.getSelectionModel().getSelectedIndex() == 0)
        	{
            	Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Tag Pair!!!");
                alert.setHeaderText("No Tag Category Selected!!!");
                alert.showAndWait();
        	}
            else if(tagValue.isBlank())
        	{
            	Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Tag Pair!!!");
                alert.setHeaderText("Please Enter a Valid Tag Value!");
                alert.showAndWait();
        	}
            else if(categoryChoiceBox2.getSelectionModel().getSelectedIndex() != 0 && tagValue2.isBlank())
            {
            	Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Tag Pair!!!");
                alert.setHeaderText("Please Enter a Valid Tag Value!");
                alert.showAndWait();
            }
            else if(cbs.getSelectionModel().getSelectedIndex()==0 && categoryChoiceBox2.getSelectionModel().getSelectedIndex() != 0 && !tagValue2.isBlank())
            {
            	Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Tag Pair!!!");
                alert.setHeaderText("Please Enter a Valid Tag Value!");
                alert.showAndWait();
            }
            else if(categoryChoiceBox2.getSelectionModel().getSelectedIndex() == 0 && !tagValue2.isBlank())
            {
            	Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Tag Pair!!!");
                alert.setHeaderText("Please Enter a Valid Tag Value!");
                alert.showAndWait();
            }
            else 
            {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/searchPhotos.fxml"));
	            try {
	                Parent searchPhotos = loader.load();
	                SearchPhotosController controller = loader.getController();
	                Scene searchPhotosScene = new Scene(searchPhotos);
	                if(categoryChoiceBox2.getSelectionModel().getSelectedIndex() == 0) {
	                	tagStr2 = null;
	                	operator = null;
	                }

	                System.out.println(operator);

	                System.out.println(tagStr2);
	                controller.search(null, null, tagStr, operator, tagStr2);
	                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                mainStage.setScene(searchPhotosScene);
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	            popup.close();
            }
        });
        
        // Add UI elements to the popup window
        // VBox vbox = new VBox(categoryLabel, categoryChoiceBox, tagLabel, tagTextField, searchButton);
        vm.getChildren().add(searchButton);
        h.setAlignment(Pos.CENTER);
        h.setSpacing(10);
        h.setPadding(new Insets(10));
        popup.setScene(new Scene(h));
        popup.showAndWait();
    }
    
    @FXML
    public void dateRangeSearch(ActionEvent event) 
    {
        // Create a new popup window
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Date Range Search");
        popup.setResizable(false);
        // Create the UI elements for the popup
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        Label fromDateLabel = new Label("From Date:");
        DatePicker fromDateField = new DatePicker();
        fromDateField.setEditable(false);
        fromDateField.getEditor().setOnMouseClicked(e -> fromDateField.show());
        fromDateField.setConverter(new LocalDateStringConverter(dateFormatter, null));
        Label toDateLabel = new Label("To Date:");
        DatePicker toDateField = new DatePicker();
        toDateField.setEditable(false);
        toDateField.getEditor().setOnMouseClicked(e -> toDateField.show());
        toDateField.setConverter(new LocalDateStringConverter(dateFormatter, null));
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            LocalDate fromDate = fromDateField.getValue();
            LocalDate toDate = toDateField.getValue();
                if (fromDate == null || toDate == null) {
                    // Alert the user if no date is provided
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Date Range!!!");
                    alert.setHeaderText("Please select a valid Date-Range");
                    alert.showAndWait();
                } else if (fromDate.isAfter(toDate)) {
                    // Alert the user if fromDate is after toDate
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Date Range!!!");
                    alert.setHeaderText("From Date cannot be after To Date");
                    alert.showAndWait();
                } else {
                    // Load the searchPhotos.fxml file and pass the dates to the controller
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/searchPhotos.fxml"));
                    try {
                        Parent searchPhotos = loader.load();
                        SearchPhotosController controller = loader.getController();
                        Scene searchPhotosScene = new Scene(searchPhotos);
                        controller.search(fromDate, toDate, null, null, null);
                        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        mainStage.setScene(searchPhotosScene);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    popup.close();
                }
        });

        // Add the UI elements to the popup window
        VBox vbox = new VBox(fromDateLabel, fromDateField, toDateLabel, toDateField, searchButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        popup.setScene(new Scene(vbox));
        popup.showAndWait();
    }
}