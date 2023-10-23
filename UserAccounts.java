package application;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserAccounts {
    private HashMap<String, UserProfile> userProfiles;
    private final String userDataFile = "user_profiles.dat";

    public UserAccounts() {
        this.userProfiles = new HashMap<>();
        loadUserData();
    }
    


    private void loadUserData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userDataFile))) {
            userProfiles = (HashMap<String, UserProfile>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("User data file not found. Creating a new one.");
            userProfiles = new HashMap<>();
            saveUserData(); // Save a new user data file
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userDataFile))) {
            oos.writeObject(userProfiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidUser(String username, String password) {
        UserProfile userProfile = userProfiles.get(username);
        return userProfile != null && userProfile.getPassword().equals(password);
    }

    public void addUserProfile(UserProfile userProfile) {
        userProfiles.put(userProfile.getUsername(), userProfile);
        saveUserData();
    }

    public HashMap<String, UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public boolean isUsernameAvailable(String username) {
        for (UserProfile userProfile : userProfiles.values()) {
            if (userProfile.getUsername().equals(username)) {
                return false; // Username already exists
            }
        }
        return true; // Username is available
    }

}
