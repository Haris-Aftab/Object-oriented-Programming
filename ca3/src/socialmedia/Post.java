package socialmedia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Represents a post
 */
public class Post implements Serializable {
    //ID of the post
    protected int ID;
    //account that this post belongs to
    protected Account account;
    //post's message
    protected String message;

    //stores ID of the comments of this post
    protected HashSet<Integer> comments;
    //stores ID of the posts that endorse this post
    protected HashSet<Integer> endorsementPosts;

    /**
     * Post constructor
     * @param ID the id of the post
     * @param account the account posting
     * @param message the message of the post
     */
    public Post(int ID, Account account, String message) {
        this.ID = ID;
        this.account = account;
        this.message = message;
        comments=new HashSet<>();
        endorsementPosts=new HashSet<>();
    }

    //getters and setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
