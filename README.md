# PhotoLib
Important Notice: Spring 2023 course project for CS213 at Rutgers University. Please follow Rutgers University Academic Integrity Policy.

**Description:** Java Application to manage library of Photos.

**Features:**

- **Login Page**
    - When the application starts, a user logs in with username.
    - The admin uses username "admin" to Login to admin Panel.
  
- **Admin Panel**
    - The admin can view all active users and manage them.
    - The admin panel provides admin user the functionality to Add/Create/Delete User(s).

- **User View**
    - Once the user logs in successfully, all albums and photo information for this user from a previous session (if any) are loaded. 
    
        Initially, all the albums belonging to the user is displayed. For each album, its name, the number of photos in it, and the range of dates (earliest and latest last-mod date) is displayed.
        
    - The user can then:
        - **Create albums**
        - **Delete albums**
        - **Rename albums**
        - **Open an album:** Opening an album displays all photos, with their thumbnail images, caption, and last-mod date. Once an album is open the user can then:
            - **Add a photo**
            - **Remove a photo**
            - **Caption/recaption a photo**
            - **Display Photo:** Select a photo to display it with its details. The photo displayed also show its caption, its last-mod date, and all its tags.
            - **Add a tag to a photo**
            - **Delete a tag from a photo**
            - **Copy a photo:** The photo is copied from one album to another with the functionality to have copies of the same photo in multiple albums.
        
                **Note:** If a photo is in multiple albums, it is the same photo, just referenced into multiple albums. This means any changes made to the photo (caption, tags) anywhere in the application will be reflected in all the albums in which the photo appears.

            - **Move a photo:** The photo is moved from one album to another, resulting in the photo being deleted from the source album.
            - **View Slideshow:** Go through photos in an album in sequence forward or backward, one at a time.
        
        - **Search for photos** provides the following features:
            - Search for photos by **date range**
            - Search for photos by **tag type-value pairs.** The following types of tag-based searches is implemented:
                - **Single tag-value pair,** User selects one of the tag category and provides the tag value to search for. e.g: User selects tag-category "location" and provides tag-value "NJ" to search for photos containing tag "location: NJ".
                - **Conjunctive/Disjunctive combination of two tag-value pairs,** User can create conjunctive or disjunctive combination search by selecting two tags and a conjunctive word (AND) or disjunctive word (OR) as per the search requirements. e.g. person: huzaif AND person: pavitra
            - User is provided with a functionality to create a new album containing the search results photos.

- **Logout**
    - When the user (whether admin or non-admin) logs out at the end of the session, all updates made by the user are saved to disk.
    - After a user logs out, the application is redirected to Login page, allowing another user to log in.

- **Quit Application**
    - The user can shut down/quit the application safely at any time by killing the main window without logging out. All updates that are made in the application in the user's session are saved on disk.
    - Unlike logout, the application stops running. The next user that wants to use the application will have to restart it.

- **Persistance**
    - The photo library data for each user persists across different sessions of the program. This means any update the user makes to the songs in a run session is carried over into the next session when that particular user runs the app again.
    - Used the java.io.Serializable interface, and the java.io.ObjectOutputStream/java.io.ObjectInputStream classes to store and retrieve data.

**DATA DEFINITIONS and its Features**

- **Last-mod Date of photo,** represents the date when the photo was last updated/added.
- **Tags,** Photos are tagged with pretty much any attribute that the user think is useful to search on, or group by. Examples are location where photo was taken, and names of people in a photo.
    - A tag is uniquely identified by the combination of tag-category name and tag value. A photo cannot have duplicate tags i.e. no two tags for the same photo can have the same name and value combination.
    - We have set up two tag-category types beforehand for the user to pick from {Location, Person}.
    - Depending on the tag-category type, a user can either have a single value for it, or multiple values (e.g. for any photo, location can only have one value, but if there's a person tag, that can have multiple values).
    - A user can define their own tag-category type and add it to the list so the user can then also create and search tags using this new tag-category type. The user is asked if the new tag-category type that they create can contain multiple values or single values only.
- **Location of Photos:**
    - There are two sets of photos, stock photos that come pre-loaded with the application, and user photos that are loaded/imported by a user when they run the application.
    - **Stock photos** are photos that are available in the application's workspace.
      - A special username called "stock" is a default prebuilt user that holds the stock photos in an album named "stock".
    - **User photos** are photos that are loaded from the user's computer, hence they are housed anywhere on the user's machine. The actual photos are NOT in the application's workspace. Instead, our application only stores the location of the photo on the user's machine.

**Technical Skills Implemented**

- JavaFX Application Development.
- Back-end Functionality Coding in Java.
- Testing Application for a variety of Scenarios.
- Understanding and Implementing Project Requirements.