package application;



public class MainLauncher {

    public static void main(String[] args) {
        // Initialize user accounts
        UserAccounts userAccounts = new UserAccounts();

        // Launch main application
        SocialMediaAnalyzerGUI mainApp = new SocialMediaAnalyzerGUI(userAccounts);
        mainApp.launch();
    }
}
