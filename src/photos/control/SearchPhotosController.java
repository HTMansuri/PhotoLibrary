package photos.control;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

/**
 * The class is controller for Search Photos Scene
 * 
 * @author Pavitra Patel, Huzaif Mansuri
 */
public class SearchPhotosController
{
    @FXML
    private Button createAlbumB;

    @FXML
    private Button searchByDateRange;

    @FXML
    private Button searchByTags;

    @FXML
    private ListView<String> searchPhotosList;
    
    private final ObservableList<String> photos= FXCollections.observableArrayList();
    
    private ObservableList<String> categories;
	

    /**
     * Creates an album based on search results. The album created will have photos referenced to same Photos that 
     * were displayed
     * 
     * @param event		The ActionEvent that triggered upon selecting "Create" button
     */
    @FXML
    public void createAlbum(ActionEvent event)
    {
    	if(photos.isEmpty())
    	{
    		Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Search Results!!!");
            alert.setHeaderText("No search results to create an album!!!");
            alert.showAndWait();
    	}
    	else
    	{
    		ArrayList<Photo> tempP = new ArrayList<>();
    		User currUser = UserDataController.getCurrentSessionUser();
    		ArrayList<Album> userAlbums = currUser.getAlbumList();
    		for(Album a: userAlbums)
    		{
    			ArrayList<Photo> userPhotos = a.getPhotoList();
    			for(Photo p: userPhotos)
    			{
    				if(photos.contains(p.getImagePath()))
    				{
    					tempP.add(p);
    				}
    			}
    		}
    		TextInputDialog dialog = new TextInputDialog();
    	    dialog.setTitle("Album Name: ");
    	    dialog.setHeaderText("Enter a album name that best matches these search results: ");
    	    Optional<String> result = dialog.showAndWait();
    	    if(result.isPresent())
    	    {
    	    	String nm = result.get();
    	    	userAlbums.add(new Album(nm, tempP));
    	    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("New Album!!!");
                alert.setHeaderText(nm + " album created successfully!!!");
                alert.showAndWait();
    	    }
    	}
    }

    /**
     * Displays a popup window for searching photos by tag pairs. The user can select two tag categories and values,
     * and choose whether to search for photos that have both tags or either tag. The method validates user input 
     * and displays an error message if necessary. Upon successful validation, the method loads the searchPhotos.fxml 
     * file and passes the tag pair to the controller.
     * 
     * @param event		The ActionEvent that is triggered upon clicking "Search By Tag Pairs" button.
     */
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
                alert.setTitle("Invalid Search!!!");
                alert.setHeaderText("Please Select a Valid Conjunction/Disjunction Operator!");
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
	            popup.close();
	            photos.clear();
	            if(categoryChoiceBox2.getSelectionModel().getSelectedIndex() == 0) {
                	tagStr2 = null;
                	operator = null;
                }
                search(null, null, tagStr, operator, tagStr2);
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

    /** Opens a popup window for searching photos within a specified date range. The user selects a start date and 
     * end date using JavaFX DatePicker widgets, and then clicks a "Search" button to perform the search. If the date 
     * range is valid (i.e., the start date is not after the end date and both dates are provided), the 
     * searchPhotos.fxml file is loaded and the dates are passed to the SearchPhotosController to perform the search. 
     * If the date range is invalid, an error alert is displayed to the user. 
     * 
     * @param event 	The ActionEvent that triggered the method call */
    @FXML
    void dateRangeSearch(ActionEvent event) {
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
                } else 
                {
                    // Load the searchPhotos.fxml file and pass the dates to the controller
                	popup.close();
                	photos.clear();
                	search(fromDate, toDate, null, null, null);
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

    /**
     * A user is sent to AllAlbums Scene
     * 
     * @param event		The ActionEvent that triggered upon selecting "LogOut" button
     */
	 @FXML
	    public void back(ActionEvent event)
	    {
	    	try
	    	{
		    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AllAlbums.fxml"));
				Parent allAlbums = loader.load();
				AllAlbumsController controller = loader.getController();
				Scene allAlbumsScene = new Scene(allAlbums);
				controller.start();
				Stage mainStage = (Stage) searchByDateRange.getScene().getWindow();
				mainStage.setScene(allAlbumsScene);
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
	 
	 /**
	  * A user is sent to Login Page
	  * @param event 	The ActionEvent that triggered upon selecting "LogOut" button
	  */
	 @FXML
	    public void logout(ActionEvent event)
	    {
	    	try
	    	{
	    		Parent login = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
	            Scene loginScene = new Scene(login);
	            Stage mainStage = (Stage) searchByDateRange.getScene().getWindow();
	            mainStage.setScene(loginScene);
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    }

	 /**
	  * The method that searches as per user input
	  * @param fromDate		The start date for search
	  * @param toDate		The end date for search
	  * @param tagStr		The first tag in String format
	  * @param operator		The AND/OR operater if passed
	  * @param tagStr2		The second tag in String Format
	  */
	public void search(LocalDate fromDate, LocalDate toDate, String tagStr,  String operator, String tagStr2) {
		//start();
    	
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		User currentUser = UserDataController.getCurrentSessionUser();
		ArrayList<Album> currAlbums = currentUser.getAlbumList();
		
		Predicate<Photo> searchPredicate;
	    if(operator != null && tagStr2 != null) {
	        if(operator.equalsIgnoreCase("AND")) {
	            searchPredicate = photo -> photo.containsTag(tagStr) && photo.containsTag(tagStr2);
	        } else if(operator.equalsIgnoreCase("OR")) {
	            searchPredicate = photo -> photo.containsTag(tagStr) || photo.containsTag(tagStr2);
	        } else {
	            searchPredicate = photo -> photo.containsTag(tagStr);
	        }
	    } else {
	        searchPredicate = photo -> photo.containsTag(tagStr);
	    }
	    
	    for(Album album : currAlbums)
	    {
	        String albumDateRange = album.dateRange();
	        LocalDate albumFromDate = LocalDate.parse(albumDateRange.substring(0, albumDateRange.indexOf("-")).trim(), dateFormatter);
	        LocalDate albumToDate = LocalDate.parse(albumDateRange.substring(albumDateRange.indexOf("-")+1).trim(), dateFormatter);
	        if((fromDate==null && toDate == null) || ((albumFromDate.isAfter(fromDate) || albumFromDate.isEqual(fromDate)) && (albumToDate.isBefore(toDate) || albumToDate.isEqual(toDate))))
	        {
	            ArrayList<Photo> albumPhotos = album.getPhotoList();
	            for(Photo photo : albumPhotos)
	            {
	                LocalDate photoLastModDate = LocalDate.parse(photo.getLastModDate().substring(0,10), dateFormatter);
	                if((fromDate == null && toDate == null) || ((photoLastModDate.isAfter(fromDate) || photoLastModDate.isEqual(fromDate)) && (photoLastModDate.isBefore(toDate) || photoLastModDate.isEqual(toDate))))
	                {
	                    if((fromDate != null && toDate != null) || searchPredicate.test(photo))
	                    {
	                        if(!photos.contains(photo.getImagePath()))
	                        	photos.add(photo.getImagePath());
	                        
	                    }
	                }
	            }
	        }       
	    }
	    displayPhotos();
	}
	
	/**
	 * The method that displays Photos as per user search
	 */
	// Call this method to display the photos in the searchPhotosList ListView
	public void displayPhotos() {
	    // Load the images from the image paths
		if(photos.isEmpty())
		{
			Label noResultsLabel = new Label("No Search Results Exist.");
            noResultsLabel.setStyle("-fx-text-fill: gray;");
            searchPhotosList.setPlaceholder(noResultsLabel);
		}
	    List<Image> images = new ArrayList<>();
	    for (String imagePath : photos) {
	        images.add(new Image("file:"+imagePath));
	    }
	    

	    // Set the cell factory to display the ImageViews in the ListView
	    searchPhotosList.setCellFactory(param -> new ListCell<String>() {
	        private final HBox hbox = new HBox();
	        private final List<ImageView> imageViews = new ArrayList<>();

	        @Override
	        protected void updateItem(String imagePath, boolean empty) {
	            super.updateItem(imagePath, empty);

	            if (empty || imagePath == null) {
	                setText(null);
	                setGraphic(null);
	            } else {
	                // Add the image to the HBox
	                hbox.getChildren().clear();
	                imageViews.clear();
	                int index = getIndex() * 3;
	                for (int i = 0; i < 3 && index < images.size(); i++, index++) {
	                    ImageView imageView = new ImageView(images.get(index));
	                    imageView.setFitWidth(220);
	                    imageView.setFitHeight(187);
	                    imageViews.add(imageView);
	                    hbox.getChildren().add(imageView);
	                    
	                 // Add spacing between the images
	                    if (i < 2 && index < images.size() - 1) {
	                        Region spacer = new Region();
	                        spacer.setPrefWidth(41.5);
	                        hbox.getChildren().add(spacer);
	                    }
	                }
	                setGraphic(hbox);
	            }
	        }
	    });
	}



	/**
	 * This method acts as initializing the List and is called first when the controller is triggered
	 */
	// Initialize the controller
    public void initialize() {
        // Set the adminList to display the userList
        searchPhotosList.setItems(photos);
        categories = FXCollections.observableArrayList();
    	categories.add(0, "--Select--");
    	User currentUser = UserDataController.getCurrentSessionUser();
    	categories.addAll(currentUser.getCategoryList());
    }
}