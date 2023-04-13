//disappear buttons when no photo in list - optional or add alerts
package photos.control;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import photos.app.UserDataController;

public class OpenAlbumController
{
    @FXML
    private ListView<String> allPhotosList;

    @FXML
    private ImageView photoDisplay;
    
    @FXML
    private ChoiceBox<String> dropDownMoveCopy;
    
    @FXML
    private ChoiceBox<String> dropDownTagCategory;
    
    @FXML
    private TextField tagValue;
    
    @FXML
    private ListView<String> tagsList;

	private ObservableList<String> photos;
	private ObservableList<String> tags;
	private ObservableList<String> categories;
	
    public void start()
	{
    	tags = FXCollections.observableArrayList();
    	tagsList.setItems(tags);
    	
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
                            		date.setText("" + po.getLastModDate());
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
                            	if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1)
                            	{
                            		photoDisplay.setImage(image);
                            		photoDisplay.setPreserveRatio(false);
                            		tags.clear();
                            		int selectedIndex = allPhotosList.getSelectionModel().getSelectedIndex();
                            		Photo currPhoto = p.get(selectedIndex);
                        	    	tags.addAll(currPhoto.getTags());
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
    		photoDisplay.setPreserveRatio(false);
    		
    		Album a = User.getCurrentSessionAlbum();
    		int selectedIndex = allPhotosList.getSelectionModel().getSelectedIndex();
    		ArrayList<Photo> p = a.getPhotoList();
	    	Photo currPhoto = p.get(selectedIndex);
	    	
	    	tags.addAll(currPhoto.getTags());
    	}
    	
    	dropDownMoveCopy.getItems().add(0, "Please Select");
    	AllAlbumsController.albums.remove(currentAlbum.getAlbumName());
    	dropDownMoveCopy.getItems().addAll(AllAlbumsController.albums);
    	dropDownMoveCopy.getSelectionModel().selectFirst();
    	
    	//dropDownTagCategory.getItems().add(0, "--Select--");
    	
    	categories = FXCollections.observableArrayList();
    	dropDownTagCategory.setItems(categories);
    	categories.add(0, "--Select--");
    	User currentUser = UserDataController.getCurrentSessionUser();
    	categories.addAll(currentUser.getCategoryList());
    	
    	dropDownTagCategory.getSelectionModel().selectFirst();
    	
    	
    	
    	}
    
    @FXML
    public void addTag(ActionEvent event)
    {
    	if(dropDownTagCategory.getSelectionModel().getSelectedIndex() == 0)
    	{
    		Alert confirm = new Alert(Alert.AlertType.ERROR);
    		confirm.setTitle("ERROR!!!");
    		confirm.setContentText("No Tag Category selected!!!");
    		confirm.setHeaderText(null);
    		confirm.setResizable(false);
    		confirm.getButtonTypes().setAll(ButtonType.OK);
    		confirm.showAndWait();
    	}
    	else
    	{
			
	    	String selectedCategoryName = dropDownTagCategory.getSelectionModel().getSelectedItem();
	    	String value = tagValue.getText();
	    	
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
            	
            	TagCategory.Tag newTag = selectedCategory.new Tag(value);
            	
            	Album a = User.getCurrentSessionAlbum();
    	    	int selectedID = allPhotosList.getSelectionModel().getSelectedIndex();
    	    	ArrayList<Photo> p = a.getPhotoList();
    	    	Photo currPhoto = p.get(selectedID);
    	    	
    	    	currPhoto.addTag(newTag);
                tags.add(selectedCategoryName+" : "+value);
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

    @FXML
    public void addTagCategory(ActionEvent event)
    {
    	TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Tag Category");
        dialog.setHeaderText("Enter the tag category title: ");
        
        dialog.showAndWait().ifPresent(str ->
        {
            if(!categories.contains(str))
            {
            	TagCategory newCategory = new TagCategory(str);
            	User currentUser = UserDataController.getCurrentSessionUser();
                currentUser.addTagCategory(newCategory);
                categories.add(str);
            	//int index = categories.indexOf(str);
                //tagsList.getSelectionModel().select(index);
            	//displayalbum();
            }
            else
            {
            	Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Duplicate Tag Category");
                alert.setHeaderText(null);
                alert.setContentText("A tag category with the name \"" + str + "\" already exists.");
                alert.showAndWait();
                addTagCategory(event);
            }
        });
    }
    
    @FXML
    public void removeTag(ActionEvent event)
    {
    	
    }
    
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
                }
            	
            	photos.add(imagePath);
                a.addPhotos(new Photo(captionNm, imagePath, LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"))));
                int index = photos.indexOf(imagePath);
                allPhotosList.getSelectionModel().select(index);
                photoDisplay.setImage(new Image("file:" + imagePath));
                photoDisplay.setPreserveRatio(false);
            }
        }
    }
    
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
	    			toCopyAlbum.addPhotos(toCopyPhoto);
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
	                    photoDisplay.setImage(new Image("file:" + allPhotosList.getSelectionModel().getSelectedItem()));
	                    photoDisplay.setPreserveRatio(false);
	    			}
	    			else
	    			{
	    				photoDisplay.setImage(null);
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

    @FXML
    public void back(ActionEvent event)
    {
    	try
    	{
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../design/AllAlbums.fxml"));
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
    	
    @FXML
    public void logout(ActionEvent event)
    {
    	try
    	{
    		Parent login = FXMLLoader.load(getClass().getResource("../design/Login.fxml"));
            Scene loginScene = new Scene(login);
            Stage mainStage = (Stage) allPhotosList.getScene().getWindow();
            mainStage.setScene(loginScene);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

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
	                photoDisplay.setPreserveRatio(false);
	                
	                tags.clear();
	                Album a = User.getCurrentSessionAlbum();
	                ArrayList<Photo> p = a.getPhotoList();
            		int selectedIndex = allPhotosList.getSelectionModel().getSelectedIndex();
            		Photo currPhoto = p.get(selectedIndex);
        	    	tags.addAll(currPhoto.getTags());
				}
				else
				{
					tags.clear();
					photoDisplay.setImage(null);
				}
			}
		}
    }
    
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
        		FXMLLoader loader = new FXMLLoader(getClass().getResource("../design/Slideshow.fxml"));
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