package socialmedia;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Represents an account
 */
public class Account implements Serializable {
    //Account handle and description
    private String handle;
    private String description;
    private int ID;
    // posts and comments owned by the account
    private HashSet<Integer> posts;
    private HashSet<Integer> comments;
    private HashSet<Integer> endorsementPosts;

    /**
     * Account constructor
     * @param handle the handle of the account
     * @param description the description of the account
     * @param ID the id of the account
     */
    public Account(String handle, String description, int ID) {
        this.handle = handle;
        this.description = description;
        this.ID = ID;
        posts = new HashSet<>();
        comments = new HashSet<>();
        endorsementPosts = new HashSet<>();
    }

    //getters and setters
    public HashSet<Integer> getPosts() {
        return posts;
    }

    public void setPosts(HashSet<Integer> posts) {
        this.posts = posts;
    }

    public HashSet<Integer> getComments() {
        return comments;
    }

    public void setComments(HashSet<Integer> comments) {
        this.comments = comments;
    }

    public HashSet<Integer> getEndorsementPosts() {
        return endorsementPosts;
    }

    public void setEndorsementPosts(HashSet<Integer> endorsementPosts) {
        this.endorsementPosts = endorsementPosts;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
