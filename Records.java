package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Holds the data for the duration of running the program. Also performs CRUD
 * operations on the data.
 *
 * @author AA
 * @version 1.0.0
 */
public class Records {
    private HashMap<Integer, Post> posts;
    

    Records() {
        this.posts = new HashMap<Integer, Post>();
    }

    public HashMap<Integer, Post> getPosts() {
        return this.posts;
    }
    
    
    public void loadPostsFromCSV(String filename) throws FileNotFoundException {
        readPosts(filename);
    }

    public boolean savePostsToCSV(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write("ID,content,author,likes,shares,date-time\n");

            for (Post post : posts.values()) {
                writer.write(String.format("%d,%s,%s,%d,%d,%s\n",
                        post.getID(), post.getContent(), post.getAuthor(), post.getLikes(),
                        post.getShares(), post.getDateTime()));
            }
//
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void addPostToDataStructure(int ID, String content, String author, int likes, int shares, String dateTime) {
        this.posts.put(ID, new Post(ID, content, author, likes, shares, dateTime));
    }


    
    
    /**
     * Read file that has posts data and initialse posts
     *
     * @param filename name of the file to read the data from
     * @throws FileNotFoundException when trying to read a file that does not exist
     */
    public void readPosts(String filename) throws FileNotFoundException {
        // create file instance
        System.out.printf("Reading '%s' file ...\n", filename);
        java.io.File file = new java.io.File(filename);

        // create scanner for the file
        int expectedFieldsNum = 6;
        int readRowsCount = 0;
        Scanner scan = new Scanner(file);
        // skip first row (headers)
        if (scan.hasNextLine()) {
            scan.nextLine();
        }
        // process each row
        while (scan.hasNextLine()) {
            try {
                readRowsCount++;
                String[] fields = parseCSV(scan.nextLine(), expectedFieldsNum);
                // instantiate post object and added to the collection
                int ID = CustomScanner.parseInt(fields[0], 0);
                try {
                    // Skip if there is an already existing post
                    getPost(ID);
                    System.out.printf("Post with ID = %d already exist!\n", ID);
                } catch (InvalidIDException e) {
                    // parse fields
                    String content = fields[1];
                    String author = CustomScanner.parseStr(fields[2]);
                    int likes = CustomScanner.parseInt(fields[3], 0);
                    int shares = CustomScanner.parseInt(fields[4], 0);
                    String dateTime = CustomScanner.parseDateTime(fields[5]);
                    // create post obj
                    posts.put(ID, new Post(ID, content, author, likes, shares, dateTime));
                }
            } catch (InvalidFieldsNumException e) {
                System.out.println("Expected " + e.expectedFieldNum + " fields but got " + e.numOfFields + "!");
            } catch (ParseValueException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.printf("%d valid posts has been loaded\n", posts.size());
        System.out.printf("%d invalid posts has been ignored\n", readRowsCount - posts.size());
        
        
    }

    /**
     * Create new post obj then add it to posts collection
     *
     * @param ID
     * @param content
     * @param author
     * @param likes
     * @param shares
     * @param dateTime
     * @throws InvalidIDException when trying to use ID already given to another
     * post
     * @throws ParseValueException when any of the post details is invalid
     */
    public void addPost(int ID, String content, String author, int likes, int shares, String dateTime)
            throws InvalidIDException, ParseValueException {
        // Make sure content doesn't have commas
        try {
            parseCSV(content, 1);
        } catch (InvalidFieldsNumException e) {
            throw new ParseValueException("Invalid content! Content cannot have commas!");
        }

        // Check if the ID already exists
        if (this.posts.containsKey(ID)) {
            System.out.println("A post with ID = " + ID + " already exists. Please enter a different and unique post ID.");
            return; // Exit the method without adding the post
        }

        // Add post to the list
        this.posts.put(ID, new Post(ID, content, author, likes, shares, dateTime));
    }


    /**
     * Delete post from posts collection by specifying its ID
     *
     * @param ID ID of the post to be deleted
     * @throws InvalidIDException when the post with the specified ID is not found
     * in the collection
     */
    public boolean deletePostByID(int ID) {
        try {
            Post post = posts.remove(ID);
            if (post != null) {
                System.out.println("Post removed: " + post);
                return true; // Post was successfully removed
            } else {
                System.out.println("Post not found for ID: " + ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Post was not found or an error occurred
    }

    /**
     * Get a post by ID
     *
     * @param ID ID of the post to be returned
     * @return post
     * @throws InvalidIDException when the post with the specified ID is not found
     */
    public Post getPost(int ID) throws InvalidIDException {
        // retrieve post from collection
        Post post = this.posts.get(ID);
        // return post
        if (post == null) {
            System.out.println("Post with ID = " + ID + " not found!"); // Add this line for debug
            throw new InvalidIDException(String.format("Post with ID = %d is not found!", ID), "Post", ID);
        } else {
            return post;
        }
    }

    
    
    public String viewPostByID(int ID) {
        try {
            Post post = getPost(ID);
            return post.toString();
        } catch (InvalidIDException e) {
            System.out.println("Error: " + e.getMessage()); // Add this line for debug
            return null;
        }
    }

    /**
     * Get top N shared or liked posts
     *
     * @param sortBy the field that the posts should be sorted by. Accepts either
     * `likes` or `shares`
     * @param limit limit the number of post to return
     * @return collection of posts
     * @throws InvalidArgumentException when the argument of `sortBy` is neither
     * `likes` nor `shares`
     */
    public ArrayList<Post> getTopPosts(String sortBy, int limit) throws InvalidArgumentException {
        ArrayList<Post> postsArray = new ArrayList<>(this.posts.values());

        if (sortBy.equals("likes")) {
            postsArray.sort((o1, o2) -> o2.getLikes() - o1.getLikes());
        } else {
            ArrayList<String> args = new ArrayList<>();
            args.add("sortBy");
            throw new InvalidArgumentException("Posts can only be filtered by 'likes''", args);
        }

        if (postsArray.size() > limit) {
            postsArray = new ArrayList<>(postsArray.subList(0, limit));
        }

        return postsArray;
    }
    
    
   // public ArrayList<Post> getTopLikedPosts(int limit) {
        

    public ArrayList<Post> getTopLikedPosts(int limit) {
		// TODO Auto-generated method stub
		try {
            return getTopPosts("likes", limit);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list if there's an error
        }
    }
    /**
     * Parse CSV string
     *
     * @param str the CSV string to be parsed
     * @param expectedFieldsNum the number of expected fields
     * @return array of strings
     * @throws InvalidFieldsNumException when parsed string doesn't match the
     * expected number of fields
     */
    public static String[] parseCSV(String str, int expectedFieldsNum) throws InvalidFieldsNumException {
        String[] fields = str.split(",");

        if (fields.length != expectedFieldsNum) {
            throw new InvalidFieldsNumException(expectedFieldsNum, fields.length);
        }

        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].strip();
        }
        
        // Debug Output
        System.out.println(Arrays.toString(fields));  // Add this line

        return fields;
    }  
}

