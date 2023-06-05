package socialmedia;

import java.util.Objects;

/**
 * Represents a comment
 */
public class Comment extends Post {

    private Post parentPost;

    /**
     * Comment constructor
     * @param ID the id of the comment
     * @param account the account commenting
     * @param message the message being commented
     * @param parentPost the post that is being commented on
     */
    public Comment(int ID, Account account, String message,Post parentPost) {
        super(ID, account, message);
        this.parentPost=parentPost;
    }

    public Post getParentPost() {
        return parentPost;
    }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;
    }

    // for using hashset we need to provide a method so that
    // two object can be compared
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return getID() == comment.getID();
    }
    // also used for comparison in hashset
    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }
}
