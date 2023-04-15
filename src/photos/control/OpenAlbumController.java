package photos.control;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * This Class is the controller class for the OpenAlbum scene. When a user double clicks on an album this controller is
 * triggered.
 * 
 * @author Pavitra Patel, Huzaif Mansuri
 */
public class OpenAlbumController
{
    @FXML
    private ListView<String> allPhotosList;
    @FXML
    private ImageView photoDisplay;
    @FXML
    private ChoiceBox<String> dropDownMoveCopy;
    @FXML
    private AnchorPane managePhotoAP;
    @FXML
    private ChoiceBox<String> dropDownTagCategory;
    @FXML
    private TextField tagValue;
    @FXML
    private ListView<String> tagsList;
	private ObservableList<String> photos;
	private ObservableList<String> tags;
	private ObservableList<String> categories;
	@FXML
	private Button slideshowB;
	@FXML
	private Button removeTagB;
	@FXML
    private Text capT;
    @FXML
    private Text dateTimeT;
	
    /**
     * This is the first method that will be called. This method performs task of observing a List View of tags and Photos.
     * All the UI components are set to invisible if there are no photos. If there are photos both the List Views are 
     * populated with their data. The allPhotosList List View is updated to show a image and it's caption. An event 
     * listener is enabled to display details as per selection of photo on the List view.
     */
    public void start()
	{
    	tags = FXCollections.observableArrayList();
    	tagsList.setItems(tags);
    	
    	
    	tagsList.getSelectionModel().selectedItemProperty().addListener(
    	        (observable, oldValue, newValue) -> 
    	        {
    	            if (newValue != null) 
    	            {
    	                String selectedString = newValue.toString();
    	                String categoryName = selectedString.substring(0, selectedString.indexOf(":")).trim();
    	                String tagValueStr = selectedString.substring(selectedString.indexOf(":")+1).trim();

    	                // Set the selected category in the dropDownTagCategory choice box
    	                dropDownTagCategory.getSelectionModel().select(categoryName);

    	                // Set the tagValue in the tagValue text field
    	                tagValue.setText(tagValueStr);
    	            }
    	        });
    	
    	allPhotosList.setCellFactory(new Callback<ListView<String>, ListCell<String>>()
    	{
            @Override
            public ListCell<String> call(ListView<String> listView)
            {
                return new ListCell<String>()
                {
                    private ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if(empty || item == null)
                        {
                            setText(null);
                            setGraphic(null);
                        }
                        else
                        {
                            // Load the image from the file path
                            Image image = new Image("file:" + item);
                            Label caption = new Label();
                            Label date = new Label();
                            imageView.setFitWidth(150);
                            imageView.setFitHeight(120);
                            imageView.setImage(image);
  
                            
                            // Set the image and text in the cell
                            // setText(item);
                            Album a = User.getCurrentSessionAlbum();
                            ArrayList<Photo> p = a.getPhotoList();
                            for(Photo po: p)
                            {
                            	if(item.equals(po.getImagePath()))
                            	{
                            		caption.setText(po.getCaption());
                            		caption.setFont(new Font(20));
                            		//date.setText("" + po.getLastModDate());
                            		capT.setText("Caption: " + po.getCaption());
                            		dateTimeT.setText("Date/Time: " + po.getLastModDate());
                            	}
                            }
                            HBox hb = new HBox();
                            VBox vb = new VBox();
                            vb.getChildren().addAll(caption, date);
                            vb.setPadding(new Insets(40, 0, 0, 40));
                            hb.getChildren().addAll(imageView, vb);
                            setGraphic(hb);
                            
                            this.setOnMouseClicked(event ->
                            {
                            	//display cap & d/t
                            	if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1)
                            	{
                                    for(Photo po: p)
                                    {
                                    	if(item.equals(po.getImagePath()))
                                    	{
                                    		capT.setText("Caption: " + po.getCaption());
                                    		dateTimeT.setText("Date/Time: " + po.getLastModDate());
                                    	}
                                    }
                            		
                            		photoDisplay.setImage(image);
                            		photoDisplay.setPreserveRatio(true);
                            		tags.clear();
                            		int selectedIndex = allPhotosList.getSelectionModel().getSelectedIndex();
                            		Photo currPhoto = p.get(selectedIndex);
                        	    	tags.addAll(currPhoto.getTags());
                        	    	if(tags.isEmpty())
                        	    	{
                        				removeTagB.setDisable(true);
                        	    	}
                        	    	else
                        	    	{
                        	    		removeTagB.setDisable(false);
                        	    	}

                    	    		tagValue.clear();
                    	    		dropDownTagCategory.getSelectionModel().select(0);
                    	    		
                        	    	tagsList.getSelectionModel().selectedItemProperty().addListener(
                        	    	        (observable, oldValue, newValue) -> {
                        	    	            if (newValue != null) {
                        	    	                String selectedString = newValue.toString();
                        	    	                String categoryName = selectedString.substring(0, selectedString.indexOf(":")).trim();
                        	    	                String tagValueStr = selectedString.substring(selectedString.indexOf(":")+1).trim();

                        	    	                // Set the selected category in the dropDownTagCategory choice box
                        	    	                dropDownTagCategory.getSelectionModel().select(categoryName);

                        	    	                // Set the tagValue in the tagValue text field
                        	    	                tagValue.setText(tagValueStr);
                        	    	            }
                        	    	        });
                            	}
                            });
                        }
                    }
                };
            }
    	});

    	photos = FXCollections.observableArrayList();
    	allPhotosList.setItems(photos);
    	
    	Album currentAlbum = User.getCurrentSessionAlbum();
    	
    	ArrayList<Photo> currentPhotos = currentAlbum.getPhotoList();
    	for(Photo p: currentPhotos)
    	{
    		photos.add(p.getImagePath());
    	}
    	if(!photos.isEmpty())
    	{
    		allPhotosList.getSelectionModel().select(0);
    		String selectedItem = allPhotosList.getSelectionModel().getSelectedItem();
    		Image i = new Image("file:" + selectedItem);
    		photoDisplay.setImage(i);
    		photoDisplay.setPreserveRatio(true);
    		
    		Album a = User.getCurrentSessionAlbum();
    		int selectedIndex = allPhotosList.getSelectionModel().getSelectedIndex();
    		ArrayList<Photo> p = a.getPhotoList();
	    	Photo currPhoto = p.get(selectedIndex);
	    	
	    	capT.setText("Caption: " + currPhoto.getCaption());
    		dateTimeT.setText("Date/Time: " + currPhoto.getLastModDate());
	    	tags.addAll(currPhoto.getTags());
    	}
    	
    	dropDownMoveCopy.getItems().add(0, "Please Select");
    	AllAlbumsController.albums.remove(currentAlbum.getAlbumName());
    	dropDownMoveCopy.getItems().addAll(AllAlbumsController.albums);
    	dropDownMoveCopy.getSelectionModel().selectFirst();
    	    	
    	categories = FXCollections.observableArrayList();
    	dropDownTagCategory.setItems(categories);
    	categories.add(0, "--Select--");
    	User currentUser = UserDataController.getCurrentSessionUser();
    	categories.addAll(currentUser.getCategoryList());
    	
    	dropDownTagCategory.getSelectionModel().selectFirst();
    	
    	if(photos.isEmpty())
    	{
    		managePhotoAP.setVisible(false);
    		slideshowB.setDisable(true);
    	}
    	if(tags.isEmpty())
    	{
			removeTagB.setDisable(true);
    	}
    	else
    	{
    		removeTagB.setDisable(false);
    	}
    }
    
    /**
     * A user can add upon selecting this button to enter a new tag if and only if it's category and value is defined. 
     * If not they will get error popup.
     * 
     * @param event		The ActionEvent that is triggered on selecting "Add Tag" button
     */
    @FXML
    public void addTag(ActionEvent event)
    {
    	if(dropDownTagCategory.getSelectionModel().getSelectedIndex() == 0 || tagValue.getText().trim() == "")
    	{
    		Alert confirm = new Alert(Alert.AlertType.ERROR);
    		confirm.setTitle("ERROR!!!");
    		confirm.setContentText("Tag Category or Value not entered!!!");
    		confirm.setHeaderText(null);
    		confirm.setResizable(false);
    		confirm.getButtonTypes().setAll(ButtonType.OK);
    		confirm.showAndWait();
    	}
    	else
    	{
			
	    	String selectedCategoryName = dropDownTagCategory.getSelectionModel().getSelectedItem();
	    	String value = tagValue.getText().trim();
	    	
	    	if(!tags.contains(selectedCategoryName+" : "+value)) {
	    		User currentUser = UserDataController.getCurrentSessionUser();
            	ArrayList<TagCategory> t = currentUser.getCategories();
            	TagCategory selectedCategory = null;
            	
            	for(TagCategory c : t)
            	{
            		if(c.getCategoryName().equals(selectedCategoryName)) {
            			selectedCategory = c;
            			break;
            		}
            	}
            	
            	Boolean isSingularCategory = selectedCategory.isSinglular();
            	
            	if(isSingularCategory) {
            		String check = selectedCategoryName;
            		Predicate<String> duplicate = (String s) -> (s.substring(0, s.indexOf(":")).trim().equals(check.trim()));
            		if(tags.stream().anyMatch(duplicate))
            		{
            			Alert confirm = new Alert(Alert.AlertType.ERROR);
                		confirm.setTitle("ERROR!!!");
                		confirm.setContentText("Multiples Tags for Tag Category \"" + selectedCategoryName + "\" is not allowed!!!");
                		confirm.setHeaderText(null);
                		confirm.setResizable(false);
                		confirm.getButtonTypes().setAll(ButtonType.OK);
                		confirm.showAndWait();
                		return;
            		}
            			
            	}
            	
            	TagCategory.Tag newTag = selectedCategory.new Tag(value);
            	
            	Album a = User.getCurrentSessionAlbum();
    	    	int selectedID = allPhotosList.getSelectionModel().getSelectedIndex();
    	    	ArrayList<Photo> p = a.getPhotoList();
    	    	Photo currPhoto = p.get(selectedID);
    	    	
    	    	currPhoto.addTag(newTag);
                tags.add(newTag.toString());
                currPhoto.setLastModDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
                tagsList.getSelectionModel().select(newTag.toString());
				removeTagB.setDisable(false);
				allPhotosList.getSelectionModel().clearSelection();
				allPhotosList.getSelectionModel().select(selectedID);
            	//int index = categories.indexOf(str);
                //tagsList.getSelectionModel().select(index);
            	//displayalbum();
	    	}
	    	else
            {
            	Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Duplicate Tag");
                alert.setHeaderText(null);
                alert.setContentText("The Tag \"" + selectedCategoryName+" : "+value + "\" already exists for the selected photo.");
                alert.showAndWait();
            }
    	}
    	
    }

    /**
     * A popup is shown upon selecting this button to enter a new tag category value. A user can specify if 
     * they want single or multiple values for that category.
     * 
     * @param event		The ActionEvent that is triggered on selecting "Add Tag Category" button
     */
    @FXML
    public void addTagCategory(ActionEvent event)
    {
    	TextInputDialog dialog = new TextInputDialog();
    	dialog.setTitle("Create New Tag Category");
    	dialog.setHeaderText(null);
    	// Add label and text field for tag category title
    	Label titleLabel = new Label("Enter the tag category title: ");
    	TextField titleTextField = new TextField();
    	HBox titleBox = new HBox(titleLabel, titleTextField);
    	titleBox.setSpacing(10);

    	// Add label and toggle switch for number of tags allowed
    	Label label = new Label("Number of Values Allowed: ");
    	ToggleGroup toggleGroup = new ToggleGroup();
    	RadioButton multipleTagsRadioButton = new RadioButton("Multiple Values");
    	multipleTagsRadioButton.setSelected(true); // Default to multiple tags
    	RadioButton singleTagRadioButton = new RadioButton("Single Values");
    	singleTagRadioButton.setSelected(false);
    	multipleTagsRadioButton.setToggleGroup(toggleGroup);
    	singleTagRadioButton.setToggleGroup(toggleGroup);
    	HBox hbox = new HBox(label, multipleTagsRadioButton, singleTagRadioButton);
    	hbox.setSpacing(10);

    	// Add both boxes to a VBox
    	VBox vbox = new VBox(titleBox, hbox);
    	vbox.setSpacing(20);
    	dialog.getDialogPane().setContent(vbox);

    	dialog.showAndWait().ifPresent(str -> 
    	{
    	    String newCategoryName = titleTextField.getText().trim();
    	    
    	    if(!categories.contains(newCategoryName))
    	    {
            	// Get number of tags allowed based on toggle switch selection
                boolean isSingular = singleTagRadioButton.isSelected();
                
            	TagCategory newCategory = new TagCategory(newCategoryName);
            	newCategory.setSinglular(isSingular);
            	User currentUser = UserDataController.getCurrentSessionUser();
                currentUser.addTagCategory(newCategory);
                categories.add(newCategoryName);
            	//int index = categories.indexOf(str);
                //tagsList.getSelectionModel().select(index);
            	//displayalbum();
            }
    	    else if(newCategoryName.isBlank())
    	    {
        		Alert alert = new Alert(AlertType.ERROR);
        		alert.setTitle("Invalid Tag Category!!!");
        		alert.setHeaderText("Please enter a valid Tag Category Name!!!");
        		alert.showAndWait();
        		addTagCategory(event);
    	    }
            else
            {
            	Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Duplicate Tag Category");
                alert.setHeaderText(null);
                alert.setContentText("A tag category with the name \"" + newCategoryName + "\" already exists.");
                alert.showAndWait();
                addTagCategory(event);
            }
        });
    }
    
    /**
     * A user can remove tag upon selecting this button.
     * 
     * @param event		The ActionEvent that is triggered on selecting "Remove Tag" button
     */
    @FXML
    public void removeTag(ActionEvent event)
    {
    	//String tagCategory = dropDownTagCategory.getSelectionModel().getSelectedItem();
    	//String tag = tagCategory + " : " + tagValue.getText();
    	String tag = tagsList.getSelectionModel().getSelectedItem();
    	if(tags.contains(tag))
		{
			Alert confirm = new Alert(Alert.AlertType.WARNING);
			confirm.setTitle("WARNING!!!");
			confirm.setContentText("Are you sure you want to delete selected Tag?");
			confirm.setHeaderText(null);
			confirm.setResizable(false);
			confirm.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
			Optional<ButtonType> result = confirm.showAndWait();
			if(result.get() == ButtonType.OK)
			{
				Album a = User.getCurrentSessionAlbum();
				int selectedID = allPhotosList.getSelectionModel().getSelectedIndex();
		    	ArrayList<Photo> p = a.getPhotoList();
		    	Photo currPhoto = p.get(selectedID);
		    	
		    	currPhoto.removeTag(tag);
		    	tags.remove(tag);
                currPhoto.setLastModDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
                allPhotosList.getSelectionModel().clearSelection();
				allPhotosList.getSelectionModel().select(selectedID);
            	if(tags.isEmpty())
				{
					dropDownTagCategory.getSelectionModel().select(0);
					tagValue.clear();
					removeTagB.setDisable(true);
				}
			}
		}
    	else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Invalid Tag Values!!!");
    		alert.setHeaderText("No Tag with provided tag-category and tag-value exists!!!");
    		alert.showAndWait();
    	}
    	
    }
    
    /**
     * A user can upload a photo. A user can setup a caption for that photo. If the photo already exists in 
     * other album for that user then the new photo object will be referenced to that photo.
     * 
     * @param event 	The ActionEvent that is triggered upon selecting "Add Photo" button
     */
    @FXML
    public void addPhoto(ActionEvent event)
    {
    	Album a = User.getCurrentSessionAlbum();
    	FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.bmp", "*.jpeg", "*.png", "*.gif", "*.BMP", "*.JPEG", "*.PNG", "*.GIF"));

        // Show the dialog and wait for the user to select a file
        File file = fileChooser.showOpenDialog(null);

        if(file != null)
        {
            // Set the image path to the selected file's path
            String imagePath = file.getAbsolutePath();
            if(photos.contains(imagePath))
            {
            	Alert alert = new Alert(AlertType.ERROR);
        		alert.setTitle("Duplicate Photo!!!");
        		alert.setHeaderText("Duplicate photo not allowed!!!");
        		alert.showAndWait();
        		addPhoto(event);
            }
            else
            {
            	Photo addP = null;
            	User currUser = UserDataController.getCurrentSessionUser();
            	ArrayList<Album> a1 = currUser.getAlbumList();
            	boolean dup = false;
            	for(Album tempA: a1)
            	{
            		ArrayList<Photo> p1 = tempA.getPhotoList();
            		for(Photo tempP: p1)
            		{
            			if(tempP.getImagePath().equals(imagePath))
            			{
            				addP = tempP;
            				dup = true;
            			}
            		}
            	}
            	
            	if(!dup)
            	{
	            	String captionNm = null;
	            	TextInputDialog captionDialog = new TextInputDialog();
	                captionDialog.setTitle("Add Photo Details: ");
	                captionDialog.setHeaderText(null);
	                captionDialog.setContentText("Please enter caption name. If you are not sure you can add a caption later.");
	                captionDialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK);
	                Optional<String> captionResult = captionDialog.showAndWait();
	                
	                if(captionResult.isPresent())
	                {
	                    captionNm = captionResult.get();
	                    capT.setText("Caption: " + captionNm);
	                }
	                else
	                {
	                	capT.setText("Caption: ");
	                }
	                String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"));
	                dateTimeT.setText("Date/Time: " + dt);
	            	
	            	photos.add(imagePath);
	                a.addPhotos(new Photo(captionNm, imagePath, dt));
            	}
            	else
            	{
            		photos.add(addP.getImagePath());
            		a.addPhotos(addP);
            	}
                
                int index = photos.indexOf(imagePath);
                allPhotosList.getSelectionModel().select(index);
                photoDisplay.setImage(new Image("file:" + imagePath));
                photoDisplay.setPreserveRatio(true);
           		managePhotoAP.setVisible(true);
           		slideshowB.setDisable(false);
            }
        }
    }
    
    /**
     * Upon selecting a different album, a new Photo object will be added to selected album which will reference 
     * the selected photo.
     * 
     * @param event		The ActionEvent that is triggered upon selecting "Copy" button
     */
    @FXML
    public void copy(ActionEvent event)
    {
    	if(photos.isEmpty())
    	{
    		Alert confirm = new Alert(Alert.AlertType.ERROR);
    		confirm.setTitle("ERROR!!!");
    		confirm.setContentText("No photos in current album to copy!!!");
    		confirm.setHeaderText(null);
    		confirm.setResizable(false);
    		confirm.getButtonTypes().setAll(ButtonType.OK);
    		confirm.showAndWait();
    	}
    	else if(dropDownMoveCopy.getSelectionModel().getSelectedIndex() == 0)
    	{
    		Alert confirm = new Alert(Alert.AlertType.ERROR);
    		confirm.setTitle("ERROR!!!");
    		confirm.setContentText("No album selected!!!");
    		confirm.setHeaderText(null);
    		confirm.setResizable(false);
    		confirm.getButtonTypes().setAll(ButtonType.OK);
    		confirm.showAndWait();
    	}
    	else
    	{
			Album a = User.getCurrentSessionAlbum();
	    	int selectedID = allPhotosList.getSelectionModel().getSelectedIndex();
	    	ArrayList<Photo> p = a.getPhotoList();
	    	Photo toCopyPhoto = p.get(selectedID);
	    	Photo ref = toCopyPhoto;
	    	
	    	String selectedItem = dropDownMoveCopy.getSelectionModel().getSelectedItem();
	    	Album toCopyAlbum = a.getAlbum(selectedItem);
	    	ArrayList<Photo> ddmcP = toCopyAlbum.getPhotoList();
	    	
	    	boolean duplicate = false;
	    	for(Photo dP: ddmcP)
	    	{
	    		if(dP.getImagePath().equals(toCopyPhoto.getImagePath()))
	    		{
	    			duplicate = true;
	    			break;
	    		}
	    	}
	    	
	    	if(!duplicate)
	    	{
	    		Alert confirm = new Alert(Alert.AlertType.WARNING);
	    		confirm.setTitle("WARNING!!!");
	    		confirm.setContentText("Are you sure you want to copy selected photo to selected album?");
	    		confirm.setHeaderText(null);
	    		confirm.setResizable(false);
	    		confirm.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
	    		Optional<ButtonType> result = confirm.showAndWait();
	    		if(result.get() == ButtonType.OK)
	    			toCopyAlbum.addPhotos(ref);
	    	}
	    	else
	    	{
	    		Alert confirm1 = new Alert(Alert.AlertType.ERROR);
	    		confirm1.setTitle("Already Exists!!!");
	    		confirm1.setContentText("The photo you are trying to copy already exists and can't be duplicated!!!");
	    		confirm1.setHeaderText(null);
	    		confirm1.setResizable(false);
	    		confirm1.getButtonTypes().setAll(ButtonType.OK);
	    		confirm1.showAndWait();
	    	}
    	}
    }
    
    /** This method is called when the user clicks on the Move button in the application. It checks whether an album is 
     * selected from the drop down list. If these condition is not met, an error message is displayed to the user. 
     * Otherwise, the selected photo is moved to the selected album. If the selected photo already exists in the 
     * destination album, a warning message is displayed to the user. If the user confirms the move, the photo is 
     * added to the destination album and removed from the current album. The selected photo is then displayed in the 
     * UI along with its caption and date/time information. If no photos remain in the album after the move, the UI is 
     * updated accordingly. 
     * 
     * @param event The ActionEvent object that is generated when the Move button is clicked. */
    @FXML
    public void move(ActionEvent event)
    {
    	if(photos.isEmpty())
    	{
    		Alert confirm = new Alert(Alert.AlertType.ERROR);
    		confirm.setTitle("ERROR!!!");
    		confirm.setContentText("No photos in current album to move!!!");
    		confirm.setHeaderText(null);
    		confirm.setResizable(false);
    		confirm.getButtonTypes().setAll(ButtonType.OK);
    		confirm.showAndWait();
    	}
    	else if(dropDownMoveCopy.getSelectionModel().getSelectedIndex() == 0)
    	{
    		Alert confirm = new Alert(Alert.AlertType.ERROR);
    		confirm.setTitle("ERROR!!!");
    		confirm.setContentText("No album selected!!!");
    		confirm.setHeaderText(null);
    		confirm.setResizable(false);
    		confirm.getButtonTypes().setAll(ButtonType.OK);
    		confirm.showAndWait();
    	}
    	else
    	{
   			Album a = User.getCurrentSessionAlbum();
	    	int selectedID = allPhotosList.getSelectionModel().getSelectedIndex();
	    	ArrayList<Photo> p = a.getPhotoList();
	    	Photo toMovePhoto = p.get(selectedID);
	    	
	    	String selectedItem = dropDownMoveCopy.getSelectionModel().getSelectedItem();
	    	Album toMoveAlbum = a.getAlbum(selectedItem);
	    	ArrayList<Photo> ddmcP = toMoveAlbum.getPhotoList();
	    	
	    	boolean duplicate = false;
	    	for(Photo dP: ddmcP)
	    	{
	    		if(dP.getImagePath().equals(toMovePhoto.getImagePath()))
	    		{
	    			duplicate = true;
	    			break;
	    		}
	    	}
	    	
	    	if(!duplicate)
	    	{
	    		Alert confirm = new Alert(Alert.AlertType.WARNING);
	    		confirm.setTitle("WARNING!!!");
	    		confirm.setContentText("Are you sure you want to move selected photo to selected album?");
	    		confirm.setHeaderText(null);
	    		confirm.setResizable(false);
	    		confirm.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
	    		Optional<ButtonType> result = confirm.showAndWait();
	    		if(result.get() == ButtonType.OK)
	    		{
	    			toMoveAlbum.addPhotos(toMovePhoto);
	    	    	p.remove(selectedID);
	    	    	photos.remove(selectedID);
	    	    	
	    	    	if(selectedID >= photos.size())
	    				selectedID -= 1;
	    			
	    			if(!photos.isEmpty())
	    			{
	    				allPhotosList.getSelectionModel().clearSelection();
	    		        allPhotosList.getSelectionModel().select(selectedID);
	    		        String cdt = allPhotosList.getSelectionModel().getSelectedItem();
	    		        Photo curr = p.get(selectedID);
	    		        capT.setText("Caption: " + curr.getCaption());
	    		        dateTimeT.setText("Date/Time: " + curr.getLastModDate());
	    		        
	                    photoDisplay.setImage(new Image("file:" + cdt));
	                    photoDisplay.setPreserveRatio(true);
	    			}
	    			else
	    			{
	    				tags.clear();
	    				photoDisplay.setImage(null);
	    				managePhotoAP.setVisible(false);
	    				slideshowB.setDisable(true);
	    			}
	    		}
	    	}
	    	else
	    	{
	    		Alert confirm1 = new Alert(Alert.AlertType.ERROR);
	    		confirm1.setTitle("Already Exists!!!");
	    		confirm1.setContentText("The photo you are trying to move already exists and can't be duplicated!!!");
	    		confirm1.setHeaderText(null);
	    		confirm1.setResizable(false);
	    		confirm1.getButtonTypes().setAll(ButtonType.OK);
	    		confirm1.showAndWait();
	    	}
    	}
    }
    
    /** This method is called when the user clicks on the "Re-Caption" button in the application. It prompts the user to 
     * enter a new caption for the selected photo. The new caption is then set for the selected photo, and the updated 
     * photo is displayed in the UI along with its new caption. 
     * 
     * @param event The ActionEvent object that is generated when the "Re-Caption" button is clicked. */
    @FXML
    public void reCaption(ActionEvent event)
    {
    	String captionNm = null;
    	int selectedID = allPhotosList.getSelectionModel().getSelectedIndex();
    	TextInputDialog captionDialog = new TextInputDialog();
        captionDialog.setTitle("Caption/Re-Caption: ");
        captionDialog.setHeaderText(null);
        captionDialog.setContentText("Please enter caption name: ");
        captionDialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<String> captionResult = captionDialog.showAndWait();
        
        if(captionResult.isPresent())
        {
            captionNm = captionResult.get();
        }
    	
        Album a = User.getCurrentSessionAlbum();
        ArrayList<Photo> p = a.getPhotoList();
        Photo currentPhoto = p.get(selectedID);
        currentPhoto.setCaption(captionNm);
        allPhotosList.getSelectionModel().clearSelection();
        allPhotosList.getSelectionModel().select(selectedID);
    }

    /** This method is called when the user clicks on the "Back" button in the application. It loads the AllAlbums.fxml 
     * file and displays the AllAlbums view to the user.
     * 
     * @param event 	The ActionEvent object that is generated when the "Back" button is clicked. */
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
			Stage mainStage = (Stage) allPhotosList.getScene().getWindow();
			mainStage.setScene(allAlbumsScene);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    	
    /**
     * A user is directed to Login Scene.
     * @param event		The ActionEvent that is triggered upon selecting "LogOut" button
     */
    @FXML
    public void logout(ActionEvent event)
    {
    	try
    	{
    		Parent login = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
            Scene loginScene = new Scene(login);
            Stage mainStage = (Stage) allPhotosList.getScene().getWindow();
            mainStage.setScene(loginScene);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    /** Removes the currently selected photo from the album and updates the UI accordingly. If there is no photo selected,
     * does nothing. Prompts the user for confirmation before deleting the photo. 
     * 
     * @param event 	The button click event that triggered this method.
     * */
    @FXML
    public void removePhoto(ActionEvent event)
    {
    	int selectedID = allPhotosList.getSelectionModel().getSelectedIndex();
		if(selectedID != -1)
		{
			Alert confirm = new Alert(Alert.AlertType.WARNING);
			confirm.setTitle("WARNING!!!");
			confirm.setContentText("Are you sure you want to delete selected Photo?");
			confirm.setHeaderText(null);
			confirm.setResizable(false);
			confirm.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
			Optional<ButtonType> result = confirm.showAndWait();
			if(result.get() == ButtonType.OK)
			{
				Album currentAlbum = User.getCurrentSessionAlbum();
				ArrayList<Photo> currentPhotos = currentAlbum.getPhotoList();
				currentPhotos.remove(selectedID);
				
				String selectedPhoto = allPhotosList.getSelectionModel().getSelectedItem();
				photos.remove(selectedPhoto);
				
				if(selectedID >= photos.size())
					selectedID -= 1;
				
				if(!photos.isEmpty())
				{
					allPhotosList.getSelectionModel().clearSelection();
			        allPhotosList.getSelectionModel().select(selectedID);
	                photoDisplay.setImage(new Image("file:" + allPhotosList.getSelectionModel().getSelectedItem()));
	                photoDisplay.setPreserveRatio(true);
	                
	                tags.clear();
	                Album a = User.getCurrentSessionAlbum();
	                ArrayList<Photo> p = a.getPhotoList();
            		int selectedIndex = allPhotosList.getSelectionModel().getSelectedIndex();
            		Photo currPhoto = p.get(selectedIndex);
            		capT.setText("Caption: " + currPhoto.getCaption());
            		dateTimeT.setText("Date/Time: " + currPhoto.getLastModDate());
        	    	tags.addAll(currPhoto.getTags());
				}
				else
				{
					tags.clear();
					photoDisplay.setImage(null);
					managePhotoAP.setVisible(false);
					slideshowB.setDisable(true);
				}
			}
		}
    }
    
    /** This method handles the action event for starting a slideshow of photos in the current album. It first checks 
     * if there are any photos in the album, and if not, displays an error message. If there are photos, it loads the 
     * Slideshow.fxml file, initializes the SlideshowController, and starts the slideshow. The current stage is then set 
     * to display the slideshow. 
     * 
     * @param event 	The action event triggered by clicking the "Slideshow" button.
     */
    @FXML
    public void slideShow(ActionEvent event)
    {
    	try
    	{
    		Album a = User.getCurrentSessionAlbum();
        	ArrayList<Photo> photos = a.getPhotoList();
        	if(photos.isEmpty())
        	{
        		Alert confirm1 = new Alert(Alert.AlertType.ERROR);
	    		confirm1.setTitle("Not Available!!!");
	    		confirm1.setContentText("There are no photos in this album to start slideshow!!!");
	    		confirm1.setHeaderText(null);
	    		confirm1.setResizable(false);
	    		confirm1.getButtonTypes().setAll(ButtonType.OK);
	    		confirm1.showAndWait();
        	}
        	else
        	{
        		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Slideshow.fxml"));
    			Parent sS = loader.load();
    			SlideShowController controller = loader.getController();
    			Scene ssScene = new Scene(sS);
    			int ssPointer = allPhotosList.getSelectionModel().getSelectedIndex();
    			controller.start(ssPointer);
    			Stage mainStage = (Stage) allPhotosList.getScene().getWindow();
    			mainStage.setScene(ssScene);
        	}	
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}