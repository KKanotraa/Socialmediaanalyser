package application;

import java.util.ArrayList;

/**
 * Definition of post object
 *
 * @author AA
 * @version 1.0.0
 */
public class Post {
    private int ID;
    private String content;
    private String author;
    private int likes;
    private int shares;
    private String dateTime;

    public Post(int ID, String content, String author, int likes, int shares, String dateTime) {
        this.ID = ID;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.shares = shares;
        this.dateTime = dateTime;
    }

    public int getID() {
        return this.ID;
    }

    public String getContent() {
        return this.content;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getLikes() {
        return this.likes;
    }

    public int getShares() {
        return this.shares;
    }

    public String getDateTime() {
        return this.dateTime;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d\nContent: %s\nAuthor: %s\nLikes: %d\nShares: %d\nDate-Time: %s",
                ID, content, author, likes, shares, dateTime);
    }


    /**
     * Display all details of the posts instance
     */
    public void displayDetails() {
        System.out.printf("%-7s | %-16s | %-7s | %-7s | %-7s | %-11s\n", "ID", "date/time", "likes", "shares", "author",
                        "content");
        System.out.printf("%-7s | %-16s | %-7s | %-7s | %-7s | %-11s\n", "-".repeat(7), "-".repeat(16), "-".repeat(7),
                        "-".repeat(7), "-".repeat(7), "-".repeat(11));
        System.out.printf("%-7s | %-16s | %-7s | %-7s | %-7s | %-11s\n", this.getID(), this.getDateTime(),
                        this.getLikes(), this.getShares(), this.getAuthor(), this.getContent());
    }

    /**
     * Display ID, content, likes or shares of each post in the provided list
     *
     * @param posts list of posts objects
     * @param filterBy filter columns by `likes` or `shares`
     * @throws InvalidArgumentException when `filterBy` is not `likes` or `shares`
     */
    public static void displayTable(ArrayList<Post> posts, String filterBy) throws InvalidArgumentException {
        if (!(filterBy == "likes" || filterBy == "shares")) {
            throw new InvalidArgumentException("Posts can only be filtered by 'likes' or 'shares'", null);
        }
        System.out.printf("%-7s | %-7s | %-50s\n", "ID", filterBy, "content");
        System.out.printf("%-7s | %-7s | %-50s\n", "-".repeat(7), "-".repeat(7), "-".repeat(50));
        for (Post post : posts) {
            System.out.printf("%-7s | %-7s | %-50s\n", post.getID(),
                            filterBy == "likes" ? post.getLikes() : post.getShares(), post.getContent());
        }
    }
}
