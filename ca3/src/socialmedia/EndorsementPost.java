package socialmedia;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an endorsement
 */
public class EndorsementPost implements Serializable {
    private Post parentPost;
    private int ID;
    private Account account;
    private String message;

    /**
     * Endorsement post constructor
     * @param parentPost the post to be endorsed
     * @param ID the id of the endorsement post
     * @param account the account endorsing the post
     */
    public EndorsementPost(Post parentPost, int ID, Account account) {
        this.parentPost= parentPost;
        this.ID = ID;
        this.account = account;
    }

    // getters and setters
    public Post getParentPost() {
        return parentPost;
    }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    // for using hashset we need to provide a method so that
    // two object can be compared
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EndorsementPost)) return false;
        EndorsementPost post = (EndorsementPost) o;
        return ID == post.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    public void createMessage() {
        message = "EP@" +
                parentPost.getAccount().getHandle() +
                ": " +
                parentPost.getMessage();
    }
}
