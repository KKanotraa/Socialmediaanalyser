package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;


import application.UserProfile;

import javafx.scene.chart.PieChart;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Map;


public class SocialMediaAnalyzerGUI extends Application {
    private Button loadButton;
    private Button addButton;
    private Button deleteButton;
    private Button viewButton;
    private Button vipButton;
    private Button topLikesButton;
    private Button logoutButton;
    private Button editProfileButton;
    
    private HBox buttonBox;
    private TextArea textArea;

    private UserAccounts userAccounts;
    private UserProfile currentUser;
    private Stage primaryStage;
    private Records records;
    private TextField firstNameField;
    private TextField familyNameField;
    private TextField newUsernameField;
    private PasswordField newPasswordField;
    private VBox registrationVBox;
    private Label welcomeLabel;


    //private Map<Integer, Post> postMap = new HashMap<>();


    private Scene loginScene;
    
   
    
    public void setCurrentUser(UserProfile user) {
        this.currentUser = user;
        if (user != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Data Analytics Hub");

        // Initialize user accounts
        userAccounts = new UserAccounts();
        records = new Records(); // Initialize Records
        
     // Load posts automatically when the application is launched
       // loadPosts();



        // Create login scene
        BorderPane loginPane = createLoginPane();
        loginScene = new Scene(loginPane, 400, 300);
 

        // Show login scene
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private BorderPane createLoginPane() {
        BorderPane pane = new BorderPane();

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> showRegistrationScene());
        
        vbox.getChildren().addAll(usernameField, passwordField, loginButton, registerButton);

        // Initialize welcomeLabel here
        welcomeLabel = new Label();

        pane.setCenter(vbox);
        return pane;
    }
    
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void handleLogin(String username, String password) {
        if (userAccounts.isValidUser(username, password)) {
            UserProfile userProfile = userAccounts.getUserProfiles().get(username); // Get the UserProfile
            setCurrentUser(userProfile); // Set the current user
            primaryStage.setScene(createMainScene());
        } else {
            showAlert("Invalid username or password. Please try again.", Alert.AlertType.ERROR);
        }
    }
    
    
    private BorderPane createRegistrationPane() {
        BorderPane pane = new BorderPane();
        
        
        Button backButton = new Button("Back to Login");
        backButton.setOnAction(e -> {
            primaryStage.setScene(loginScene);
        });

        registrationVBox = new VBox(10); // Create a class-level VBox
        registrationVBox.setAlignment(Pos.CENTER);

        firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        familyNameField = new TextField();
        familyNameField.setPromptText("Family Name");

        newUsernameField = new TextField();
        newUsernameField.setPromptText("Username");

        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Password");
        
        
            
        

        
        // Use registrationVBox here instead of vbox

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> handleRegistration(
                firstNameField.getText(),
                familyNameField.getText(),
                newUsernameField.getText(),
                newPasswordField.getText()
        ));

        // Add the VBox to the class-level variable
        registrationVBox.getChildren().addAll(backButton,firstNameField, familyNameField, newUsernameField, newPasswordField, registerButton);

        pane.setCenter(registrationVBox); // Use the class-level VBox here
        return pane;
        
    }
    
    
    
    
    private void handleRegistration(String firstName, String familyName, String username, String password) {
        if (!firstName.isEmpty() && !familyName.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
            while (!userAccounts.isUsernameAvailable(username)) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Username Already Taken");
                dialog.setHeaderText(null);
                dialog.setContentText("Username is already taken. Please choose a different username:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    username = result.get();
                } else {
                    return; // User canceled input
                }
            }

            UserProfile userProfile = new UserProfile(username, password, firstName, familyName);
            userAccounts.addUserProfile(userProfile);
            setCurrentUser(userProfile);
            showAlert("User registered successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("All fields are required.", Alert.AlertType.ERROR);
        }
    }
    
    private void showRegistrationScene() {
        BorderPane registerPane = createRegistrationPane();
        Scene registerScene = new Scene(registerPane, 400, 300);
        primaryStage.setScene(registerScene);
    }


    private Scene createMainScene() {
        // Buttons
        loadButton = createStyledButton("Load Posts", Color.MAGENTA);
        addButton = createStyledButton("Add Post", Color.YELLOW);
        deleteButton = createStyledButton("Delete Post", Color.ORANGE);
        viewButton = createStyledButton("View Post", Color.PINK);
        vipButton = createStyledButton("VIP Subscription", Color.GREEN);
        topLikesButton = createStyledButton("Top Liked Posts", Color.BLUE);
        logoutButton = createStyledButton("Logout", Color.RED);
        editProfileButton = createStyledButton("Edit Profile", Color.PURPLE);

        buttonBox = new HBox(10, loadButton, addButton, deleteButton, viewButton,topLikesButton, vipButton,editProfileButton,logoutButton );

        // Text area
        textArea = createStyledTextArea();
        
        welcomeLabel = new Label("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName());
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Button action   
        
        topLikesButton.setOnAction(e -> {
               if (currentUser != null) {
                   int limit = getLimitFromUser();
                   if (limit != -1) {
                       try {
                           ArrayList<Post> topLikedPosts = records.getTopPosts("likes", limit);

                           if (!topLikedPosts.isEmpty()) {
                               displayTopLikedPosts(topLikedPosts);
                           } else {
                               textArea.appendText("No posts found.\n");
                           }
                       } catch (InvalidArgumentException ex) {
                           showAlert(ex.getMessage(), Alert.AlertType.ERROR);
                       }
                   }
               } else {
                   showAlert("Please log in first!", Alert.AlertType.WARNING);
               }
            });
      
        logoutButton.setOnAction(e -> handleLogout());
        
        editProfileButton.setOnAction(e -> showEditProfileDialog());
        //(editProfileButton, 1, 3);

        
        
        
        loadButton.setOnAction(e -> {
            if (currentUser != null) {
                loadPosts();
            } else {
                showAlert("Please log in first!", Alert.AlertType.WARNING);
            }
        });

        addButton.setOnAction(e -> {
            if (currentUser != null) {
                addPost();
            } else {
                showAlert("Please log in first!", Alert.AlertType.WARNING);
            }
        });

        deleteButton.setOnAction(e -> {
            if (currentUser != null) {
                deletePost();
            } else {
                showAlert("Please log in first!", Alert.AlertType.WARNING);
            }
        });

        viewButton.setOnAction(e -> {
            if (currentUser != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("View Post");
                dialog.setHeaderText("Enter Post ID");
                dialog.setContentText("Post ID:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(postID -> {
                    try {
                        int id = Integer.parseInt(postID);
                        String postDetails = viewPostFromDataStructure(id);

                        if (postDetails != null) {
                            textArea.clear(); // Clear existing content
                            textArea.appendText(postDetails);
                        } else {
                            showAlert("Post with ID " + id + " not found!", Alert.AlertType.WARNING);
                        }
                    } catch (NumberFormatException ex) {
                        showAlert("Invalid Post ID. Please enter a valid number.", Alert.AlertType.ERROR);
                    } catch (InvalidIDException ex) {
                        showAlert("Error viewing post: " + ex.getMessage(), Alert.AlertType.ERROR);
                    }
                });
            } else {
                showAlert("Please log in first!", Alert.AlertType.WARNING);
            }
        });
           
        vipButton.setOnAction(e -> {
            if (currentUser != null) {
                activateVIPSubscription();
            } else {
                showAlert("Please log in first!", Alert.AlertType.WARNING);
            }
            
          
        });
    
        // Layout

        VBox vbox = new VBox(10, buttonBox, welcomeLabel, textArea);
        vbox.setPadding(new Insets(10));

        return new Scene(vbox, 800, 600);

    }

    private Button createStyledButton(String label, Color color) {
        Button button = new Button(label);
        button.setStyle("-fx-background-color: #" + color.toString().substring(2));
        button.setTextFill(Color.BLACK);
        return button;
    }

    private TextArea createStyledTextArea() {
        TextArea textArea = new TextArea();
        textArea.setFont(new javafx.scene.text.Font("Arial", 14));
        textArea.setWrapText(true);
        return textArea;
    }
    
    private int getLimitFromUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Limit");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the limit:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                int limit = Integer.parseInt(result.get());
                if (limit > 0) {
                    return limit;
                } else {
                    showAlert("Limit must be a positive integer.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid input. Please enter a valid integer.", Alert.AlertType.ERROR);
            }
        }

        return -1; // Default return value (you can choose a different default if needed)
    }
    
    private void viewTopLikedPosts() {
        int limit = getLimitFromUser(); // Implement getLimitFromUser method to get user input for N

        ArrayList<Post> topLikedPosts = records.getTopLikedPosts(limit);

        if (!topLikedPosts.isEmpty()) {
        	textArea.clear();
            textArea.appendText("Top Liked Posts:\n\n");
            for (Post post : topLikedPosts) {
                String postInfo = String.format("ID: %d\nContent: %s\nAuthor: %s\nLikes: %d\nShares: %d\nDateTime: %s\n\n",
                        post.getID(), post.getContent(), post.getAuthor(), post.getLikes(),
                        post.getShares(), post.getDateTime());
                textArea.appendText(postInfo);
            }
        } else {
            textArea.appendText("No posts found.\n");
        }
    }

    
    
    private void retrieveTopLikedPosts() {
        int limit = getLimitFromUser(); // Implement getLimitFromUser method to get user input for N

        try {
            ArrayList<Post> topLikedPosts = records.getTopPosts("likes", limit);

            if (!topLikedPosts.isEmpty()) {
                displayTopLikedPosts(topLikedPosts);
            } else {
                textArea.appendText("No posts found.\n");
            }
        } catch (InvalidArgumentException ex) {
            showAlert(ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    
    private void displayTopLikedPosts(ArrayList<Post> topLikedPosts) {
        textArea.appendText("Top Liked Posts:\n\n");
        for (Post post : topLikedPosts) {
            String postInfo = String.format("ID: %d\nContent: %s\nAuthor: %s\nLikes: %d\nShares: %d\nDateTime: %s\n\n",
                    post.getID(), post.getContent(), post.getAuthor(), post.getLikes(),
                    post.getShares(), post.getDateTime());
            textArea.appendText(postInfo);
        }
    }
    
    

    private void loadPosts() {
        try {
            records.loadPostsFromCSV("posts.csv");
            displayLoadedPosts();
        } catch (FileNotFoundException e) {
            showAlert("Error loading posts: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void displayLoadedPosts() {
        textArea.clear(); // Clear the text area before displaying loaded posts
        HashMap<Integer, Post> posts = records.getPosts();
        for (Post post : posts.values()) {
            String postInfo = String.format("ID: %d\nContent: %s\nAuthor: %s\nLikes: %d\nShares: %d\nDateTime: %s\n\n",
                    post.getID(), post.getContent(), post.getAuthor(), post.getLikes(),
                    post.getShares(), post.getDateTime());
            textArea.appendText(postInfo);
        }
    }
    
    

    private void addPost() {
        try {
            int id = getValidPostID();
            String content = getValidContent();
            String author = getValidAuthor();
            int likes = getValidLikes();
            int shares = getValidShares();
            String dateTime = getValidDateTime();

            if (content != null && author != null && likes != -1 && shares != -1 && dateTime != null) {
                records.addPostToDataStructure(id, content, author, likes, shares, dateTime);
                textArea.appendText("Post added successfully!\n\n");

                // Save the posts to CSV after adding
                records.savePostsToCSV("posts.csv");
            }
        } catch (Exception e) {
            showAlert("An error occurred while adding the post: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }




    private int getValidPostID() {
        while (true) {
            try {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Enter Post ID");
                dialog.setHeaderText(null);
                dialog.setContentText("Please provide the post ID:");

                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    int id = Integer.parseInt(result.get());
                    if (id >= 0) {
                    	if (!records.getPosts().containsKey(id)) {

                            return id;
                        } else {
                            showAlert("Post ID already exists. Please choose a different ID.", Alert.AlertType.ERROR);
                        }
                    } else {
                        showAlert("Post ID must be a non-negative integer.", Alert.AlertType.ERROR);
                    }
                } else {
                    return -1; // User canceled input
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid input. Please enter a valid integer.", Alert.AlertType.ERROR);
            }
        }
    }







    private String getValidContent() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Post Content");
        dialog.setHeaderText(null);
        dialog.setContentText("Please provide the post content:");

        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }

    private String getValidAuthor() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Post Author");
        dialog.setHeaderText(null);
        dialog.setContentText("Please provide the post author:");

        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }

    private int getValidLikes() {
        while (true) {
            try {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Enter Number of Likes");
                dialog.setHeaderText(null);
                dialog.setContentText("Please provide the number of likes of the post:");

                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    int likes = Integer.parseInt(result.get());
                    if (likes >= 0) {
                        return likes;
                    } else {
                        showAlert("Likes must be a non-negative integer.", Alert.AlertType.ERROR);
                    }
                } else {
                    return -1; // User canceled input
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid input. Please enter a valid integer.", Alert.AlertType.ERROR);
            }
        }
    }

    private int getValidShares() {
        while (true) {
            try {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Enter Number of Shares");
                dialog.setHeaderText(null);
                dialog.setContentText("Please provide the number of shares of the post:");

                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    int shares = Integer.parseInt(result.get());
                    if (shares >= 0) {
                        return shares;
                    } else {
                        showAlert("Shares must be a non-negative integer.", Alert.AlertType.ERROR);
                    }
                } else {
                    return -1; // User canceled input
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid input. Please enter a valid integer.", Alert.AlertType.ERROR);
            }
        }
    }

    private String getValidDateTime() {
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Date and Time");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the date and time in the format DD/MM/YYYY HH:MM:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String dateTime = result.get();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                dateFormat.setLenient(false); // This makes the date validation strict

                try {
                    // Try parsing the date and time
                    Date parsedDate = dateFormat.parse(dateTime);

                    // Ensure the parsed date matches the original string (no partial matching)
                    if (dateTime.equals(dateFormat.format(parsedDate))) {
                        return dateTime;
                    } else {
                        showAlert("Invalid date and time format. Please enter in DD/MM/YYYY HH:MM format.", Alert.AlertType.ERROR);
                    }
                } catch (ParseException e) {
                    showAlert("Invalid date and time format. Please enter in DD/MM/YYYY HH:MM format.", Alert.AlertType.ERROR);
                }
            } else {
                // User canceled the input, you can handle it as needed
                return null;
            }
        }
    }





    
    private void displayPosts() {
        textArea.clear(); // Clear the text area before displaying posts
        HashMap<Integer, Post> posts = records.getPosts();
        for (Post post : posts.values()) {
            String postInfo = String.format("ID: %d\nContent: %s\nAuthor: %s\nLikes: %d\nShares: %d\nDateTime: %s\n\n",
                    post.getID(), post.getContent(), post.getAuthor(), post.getLikes(),
                    post.getShares(), post.getDateTime());
            textArea.appendText(postInfo);
        }
    }

    private void deletePost() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Post");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter ID of the post to delete:");

        dialog.showAndWait().ifPresent(postId -> {
            try {
                int id = Integer.parseInt(postId);
                boolean deleted = records.deletePostByID(id);

                if (deleted) {
                    showAlert("Post deleted successfully.", Alert.AlertType.INFORMATION);

                    // Clear the CSV file before writing the updated posts
                    try {
                        new PrintWriter("posts.csv").close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    // Save the changes to the CSV file
                    records.savePostsToCSV("posts.csv");
                } else {
                    showAlert("Post with ID " + id + " not found.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid input. Please enter a valid integer ID.", Alert.AlertType.ERROR);
            }
        });
    }
    
    private void handleLogout() {
        currentUser = null; // Clear the current user
        records.savePostsToCSV("posts.csv");
        primaryStage.setScene(loginScene); // Switch back to the login scene
    }
    
    private String viewPostFromDataStructure(int id) throws InvalidIDException {
        Post post = records.getPost(id);
        if (post != null) {
            return post.toString();
        }
        return null;
    }

    private void activateVIPSubscription() {
        if (currentUser != null) {
            // Simulate VIP subscription activation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("VIP Subscription");
            alert.setHeaderText(null);
            alert.setContentText("VIP Subscription for 1 year successfully activated!");
            alert.showAndWait();
        } else {
            showAlert("Please log in first!", Alert.AlertType.WARNING);
        }
    }
    
    private void showEditProfileDialog() {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Profile");

        // Create a grid for the dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Add fields for editing profile
        TextField firstNameField = new TextField(currentUser.getFirstName());
        TextField lastNameField = new TextField(currentUser.getLastName());
        TextField usernameField = new TextField(currentUser.getUsername());
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("New Password");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Get the new values
        	String newFirstName = firstNameField.getText();
            String newLastName = lastNameField.getText();
            String newUsername = usernameField.getText();
            String newPassword = passwordField.getText();

            if (!newUsername.equals(currentUser.getUsername())) {
                // If the username is different from the current one
                while (!newUsername.isEmpty() && !userAccounts.isUsernameAvailable(newUsername)) {
                    TextInputDialog dialog = new TextInputDialog(newUsername);
                    dialog.setTitle("Username Already Taken");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Username is already taken. Please choose a different username:");
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        newUsername = result.get();
                    } else {
                        return; // User canceled input
                    }
                }
            }

            // Update the user profile
            currentUser.setFirstName(newFirstName);
            currentUser.setLastName(newLastName);
            currentUser.setUsername(newUsername);
            if (!newPassword.isEmpty()) {
                currentUser.setPassword(newPassword);
            }

            // Close the dialog
            dialogStage.close();

            // Update the UserProfile in the userAccounts HashMap
            userAccounts.getUserProfiles().put(currentUser.getUsername(), currentUser);

            // Save the updated user profiles to the .dat file
            userAccounts.saveUserData();
        });

        // Add fields and buttons to the grid
        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(new Label("New Password:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(saveButton, 1, 4);

        Scene scene = new Scene(grid);
        dialogStage.setScene(scene);

        // Show the dialog
        dialogStage.show();
    }




    
    


    public static void main(String[] args) {
       // Launch the GUI
        launch(args);
    }
}
