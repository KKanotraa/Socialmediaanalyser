package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginApp extends Application {
    private UserAccounts userAccounts;

    public static void main(String[] args) {
        launch(args);
    }
    
    

    @Override
    public void start(Stage primaryStage) {
        userAccounts = new UserAccounts(); // Initialize user accounts

        GridPane grid = new GridPane();

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (userAccounts.isValidUser(username, password)) {
                showAlert("Login Successful!", Alert.AlertType.INFORMATION);
                // Redirect to the main application page
                primaryStage.hide();
                launchMainApp(username); // Launch main application with the logged-in user
            } else {
                showAlert("Invalid username or password. Please try again.", Alert.AlertType.ERROR);
            }

            // Clear fields after login attempt
            usernameField.clear();
            passwordField.clear();
        });

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);

        Scene scene = new Scene(grid, 300, 200);

        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Login Status");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void launchMainApp(String username) {
        UserProfile userProfile = userAccounts.getUserProfiles().get(username);
        
        

        if (userProfile != null) {
            String password = userProfile.getPassword();
            String firstName = userProfile.getFirstName();
            String lastName = userProfile.getLastName();

            UserProfile user = new UserProfile(username, password, firstName, lastName);
            
            SocialMediaAnalyzerGUI mainApp = new SocialMediaAnalyzerGUI();
            
            mainApp.setCurrentUser(user);
            Stage mainStage = new Stage();
            mainApp.start(mainStage);
        }
    }






}
