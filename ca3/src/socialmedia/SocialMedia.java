package socialmedia;

import java.io.*;
import java.util.*;

public class SocialMedia implements SocialMediaPlatform{
    //stores the account in a hashmap to quickly find a
    //account by it's handle
    private HashMap<String,Account> accountsByHandle;

    //stores the account in a hashmap to quickly find a
    //account by it's ID
    private HashMap<Integer,Account> accountsByID;

    //stores posts against its ID;
    private HashMap<Integer,Post> posts;
    private HashMap<Integer,Comment> comments;
    private HashMap<Integer,EndorsementPost> endorsementPosts;



    //used to assign a unique ID to a account
    private int accountIDTracker;

    //used to assign unique ID to a post
    private int postIdTracker;

    //empty post
    private Post emptyPost;

    private int childComments;

    //constructor to initialize the variables
    public SocialMedia() {
        accountsByHandle=new HashMap<>();
        accountsByID =new HashMap<>();
        postIdTracker=accountIDTracker=0;
        endorsementPosts = new HashMap<>();
        posts = new HashMap<>();
        comments = new HashMap<>();
        // generic empty post template
        emptyPost=new Post(-1,null,"The original content was removed" +
                " from the system and is no longer available.");

    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        //checking if the handle exists already
        if(accountsByHandle.containsKey(handle))throw new IllegalHandleException();

        //check if the handle is valid
        if(handle.isEmpty() || handle.length()>30 || handle.matches("\\s"))throw new InvalidHandleException();

        Account newAccount=new Account(handle,description,accountIDTracker);
        //save the account
        accountsByHandle.put(handle,newAccount);
        accountsByID.put(accountIDTracker,newAccount);

        //update the tracker to assign in the next create call
        accountIDTracker++;

        return accountIDTracker-1;

    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        //checking if the handle does not exists
        if(!accountsByHandle.containsKey(handle)) throw new HandleNotRecognisedException();

        Account removedAC=accountsByHandle.get(handle);
        //delete all its posts first
        ArrayList<Integer> toRemove = new ArrayList<>(removedAC.getPosts());
        toRemove.addAll(removedAC.getComments());
        toRemove.addAll(removedAC.getEndorsementPosts());
        for(int id:toRemove) {
            try {
                deletePost(id);
            } catch (PostIDNotRecognisedException e) {
                e.printStackTrace();
            }
        }


        //remove the account
        accountsByHandle.remove(removedAC.getHandle());
        accountsByID.remove(removedAC.getID());


    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        //checking if the handle does not exists
        if(!accountsByHandle.containsKey(handle))throw  new HandleNotRecognisedException();

        //update
        accountsByHandle.get(handle).setDescription(description);

    }

    @Override
    public int getNumberOfAccounts() {
        return accountsByHandle.size();
    }

    @Override
    public int getTotalOriginalPosts() {
        return posts.size();
    }

    @Override
    public int getTotalEndorsmentPosts() {
        return endorsementPosts.size();
    }

    @Override
    public int getTotalCommentPosts() { return comments.size(); }

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        //checking if the handle exists already
        if(accountsByHandle.containsKey(handle))throw  new IllegalHandleException();

        //check if the handle is valid
        if(handle.isEmpty() || handle.length()>30 || handle.matches("\\s"))throw new InvalidHandleException();

        Account newAccount=new Account(handle,"",accountIDTracker);
        //save the account
        accountsByHandle.put(handle,newAccount);
        accountsByID.put(accountIDTracker,newAccount);

        //update the tracker to assign in the next create call
        accountIDTracker++;

        return accountIDTracker-1;

    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {
        //checking if the handle does not exists
        if(!accountsByID.containsKey(id))throw  new AccountIDNotRecognisedException();

        //remove the account
        Account removedAC=accountsByID.remove(id);
        accountsByHandle.remove(removedAC.getHandle());

    }

    @Override
    public void changeAccountHandle(String oldHandle, String newHandle) throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        // checking if the handle does not exist
        if(!accountsByHandle.containsKey(oldHandle))throw  new HandleNotRecognisedException();

        // check if new handle already exists
        if(accountsByHandle.containsKey(newHandle))throw new IllegalHandleException();

        // check if the handle is valid
        if(newHandle.isEmpty() || newHandle.length()>30 || newHandle.matches("\\s"))throw new InvalidHandleException();

        // update the maps and account
        Account account=accountsByHandle.remove(oldHandle);
        account.setHandle(newHandle);

        accountsByHandle.put(newHandle,account);

    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        if(!accountsByHandle.containsKey(handle))throw  new HandleNotRecognisedException();

        Account account=accountsByHandle.get(handle);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("ID: ").append(account.getID()).append("\n")
                .append("Handle: ").append(handle).append("\n")
                .append("Description: ").append(account.getDescription()).append("\n")
                .append("Post count: ").append(account.getPosts().size()+account.getComments().size()+account.getEndorsementPosts().size())
                .append("\n")
                .append("Endorse count: ");

        int endorsementCount=0;
        for(int id:account.getPosts()){
            endorsementCount+=posts.get(id).getEndorsementPosts().size();
        }
        for(int id:account.getComments()){
            endorsementCount+=comments.get(id).getEndorsementPosts().size();
        }

        stringBuilder.append(endorsementCount).append("\n");

        return stringBuilder.toString();
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        //checking if the handle does not exist
        if(!accountsByHandle.containsKey(handle))throw  new HandleNotRecognisedException();

        if(message.isEmpty() || message.length()>100)throw  new InvalidPostException();

        Post post=new Post(postIdTracker,accountsByHandle.get(handle),message);
        //store this post
        posts.put(postIdTracker,post);

        //add a reference to this post to its owner account
        post.getAccount().getPosts().add(postIdTracker);

        postIdTracker++;
        return post.getID();


    }

    @Override
    public int endorsePost(String handle, int id) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        // checking if the handle does not exist
        if(!accountsByHandle.containsKey(handle))throw  new HandleNotRecognisedException();

        // checking if the post is available as post or comments or endorsementPosts or not
        if(!(posts.containsKey(id) || comments.containsKey(id) || endorsementPosts.containsKey(id))){
            throw new PostIDNotRecognisedException();
        }
        // endorsable posts are not endorsable.
        if(endorsementPosts.containsKey(id))throw  new NotActionablePostException();

        EndorsementPost post=new EndorsementPost(null,postIdTracker,accountsByHandle.get(handle));

        // store this post
        endorsementPosts.put(postIdTracker,post);

        // add this post to its owner
        post.getAccount().getEndorsementPosts().add(postIdTracker);

        if(posts.containsKey(id)){

            // the main post should refer to this post
            posts.get(id).getEndorsementPosts().add(postIdTracker);

            // add a reference to the main post
            post.setParentPost(posts.get(id));
        }
        else if(comments.containsKey(id)){

            //the main post should refer to this post
            comments.get(id).getEndorsementPosts().add(postIdTracker);

            //add a reference to the main post
            post.setParentPost(comments.get(id));
        }

        //create the message of the endorsementPost
        post.createMessage();

        postIdTracker++;

        return post.getID();

    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        // checking if the handle does not exist
        if(!accountsByHandle.containsKey(handle))throw  new HandleNotRecognisedException();

        // checking if the post is available as post or comments or endorsementPosts or not
        if(!(posts.containsKey(id) || comments.containsKey(id) || endorsementPosts.containsKey(id))){
            throw new PostIDNotRecognisedException();
        }
        if(endorsementPosts.containsKey(id))throw new NotActionablePostException();

        if(message.isEmpty() || message.length()>100)throw new InvalidPostException();


        Comment comment=new Comment(postIdTracker,accountsByHandle.get(handle),message,null);

        //store this post
        comments.put(postIdTracker,comment);

        //add this post to its owner
        comment.getAccount().getComments().add(postIdTracker);

        if(posts.containsKey(id)){
            // the main post should refer to this post
            posts.get(id).getComments().add(postIdTracker);

            // add a reference to the main post
            comment.setParentPost(posts.get(id));
        }
        else if(comments.containsKey(id)){
            // the main post should refer to this post
            comments.get(id).getComments().add(postIdTracker);

            // add a reference to the main post
            comment.setParentPost(comments.get(id));
        }

        postIdTracker++;
        return comment.getID();

    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        if(!(posts.containsKey(id) || comments.containsKey(id) || endorsementPosts.containsKey(id))){
            throw new PostIDNotRecognisedException();
        }

        if(posts.containsKey(id)){
            Post post=posts.remove(id);

            // remove all its endorsement
            for(int endorsements:post.getEndorsementPosts()){
                EndorsementPost endorsementPost=endorsementPosts.remove(endorsements);
                // remove the endorsement post from its owner account
                endorsementPost.getAccount().getEndorsementPosts().remove(endorsementPost.getID());
            }
            // all its comments should refer to the generic empty post
            for(int commentsID:post.getComments()){
                comments.get(commentsID).setParentPost(emptyPost);
            }
            // finally remove the post from its owner account
            post.getAccount().getPosts().remove(post.getID());
        }
        else if(comments.containsKey(id)){
            Comment post=comments.remove(id);

            //remove all its endorsement
            for(int endorsements:post.getEndorsementPosts()){
                EndorsementPost endorsementPost=endorsementPosts.remove(endorsements);
                //remove the endorsement post from its owner account
                endorsementPost.getAccount().getEndorsementPosts().remove(endorsementPost.getID());
            }
            //all its comments should refer to the generic empty post
            for(int commentsID:post.getComments()){
                comments.get(commentsID).setParentPost(emptyPost);
            }

            //remove the post from its owner account
            post.getAccount().getComments().remove(post.getID());

            //finally remove the reference from the comments parentPost
            post.getParentPost().getComments().remove(post.getID());

        }
        else if(endorsementPosts.containsKey(id)){
            EndorsementPost post=endorsementPosts.remove(id);

            //remove the post from its owner account
            post.getAccount().getEndorsementPosts().remove(post.getID());

            //finally remove the reference from its parentPost
            post.getParentPost().getEndorsementPosts().remove(post.getID());
        }


    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        if(!(posts.containsKey(id) || comments.containsKey(id) || endorsementPosts.containsKey(id))){
            throw new PostIDNotRecognisedException();
        }

        if(!endorsementPosts.containsKey(id)){
            Post post_fake;
            if(posts.containsKey(id))post_fake=posts.get(id);
            else post_fake=comments.get(id);

            ArrayList<Integer> commentsID_fake=new ArrayList<>(post_fake.getComments());
            commentsID_fake.sort(Integer::compareTo);

            StringBuilder fake=new StringBuilder();
            if(commentsID_fake.size()>0)fake.append("|\n");
            for(Integer cmID_Fake:commentsID_fake){
                buildPostTree(comments.get(cmID_Fake),fake,1);
            }
        }

        String msg = "ID: "+
                id +
                "\n";

        if (posts.containsKey(id)) {
            Post post = posts.get(id);

            msg += "Account: " +
                    post.getAccount().getHandle() +
                    "\n" +
                    "No. endorsements: " +
                    post.getEndorsementPosts().size() +
                    " | No. comments: " +
                    (post.getComments().size() + childComments) +
                    "\n" +
                    post.getMessage() +
                    "\n";
            childComments=0;
        }
        else if (comments.containsKey(id)) {
            Comment post = comments.get(id);
            msg += "Account: " +
                    post.getAccount().getHandle() +
                    "\n" +
                    "No. endorsements: " +
                    post.getEndorsementPosts().size() +
                    " | No. comments: " +
                    post.getComments().size() +
                    "\n" +
                    post.getMessage() +
                    "\n";
        }
        else if(endorsementPosts.containsKey(id)) {
            EndorsementPost post = endorsementPosts.get(id);
            msg += "Account: " +
                    post.getAccount().getHandle() +
                    "\n" +
                    "No. endorsements: 0 | No. comments: 0\n" +
                    post.getMessage() +
                    "\n";
        }
        return  msg;
    }

    @Override
    public StringBuilder showPostChildrenDetails(int id) throws PostIDNotRecognisedException, NotActionablePostException {

        // checking if the post is available as post or comments or endorsementPosts or not
        if(!(posts.containsKey(id) || comments.containsKey(id) || endorsementPosts.containsKey(id))){
            throw new PostIDNotRecognisedException();
        }
        if(endorsementPosts.containsKey(id)){
            throw  new NotActionablePostException();
        }

        StringBuilder stringBuilder=new StringBuilder();
        Post post;
        if(posts.containsKey(id))post=posts.get(id);
        else post=comments.get(id);

        stringBuilder.append(showIndividualPost(id));

        ArrayList<Integer> commentsID=new ArrayList<>(post.getComments());
        commentsID.sort(Integer::compareTo);

        if(commentsID.size()>0)stringBuilder.append("|\n");
        for(Integer cmID:commentsID){
            buildPostTree(comments.get(cmID),stringBuilder,1);
        }
        childComments=0;
        return stringBuilder;

    }

    private void buildPostTree(Comment post,StringBuilder stringBuilder,int tabSize){

        StringBuilder initialLine= new StringBuilder();
        StringBuilder tabs= new StringBuilder();

        for (int i = 0; i <tabSize-1 ; i++) {
            initialLine.append('\t');
        }
        initialLine.append("| > ");

        for (int i = 0; i <tabSize ; i++) {
            tabs.append('\t');
        }


        String msg=initialLine+"ID: "+post.getID()+"\n"+
                tabs+"Account: " + post.getAccount().getHandle() + "\n" +
                tabs+"No. endorsements: " + post.getEndorsementPosts().size() + " | No. comments: " + post.getComments().size() + "\n" +
                tabs+post.getMessage() + "\n";

        stringBuilder.append(msg);


        ArrayList<Integer> commentsID=new ArrayList<>(post.getComments());
        commentsID.sort(Integer::compareTo);
        if(commentsID.size()>0) stringBuilder.append(tabs).append("|\n");
        for(Integer cmID:commentsID){
            childComments++;
            buildPostTree(comments.get(cmID),stringBuilder,tabSize+1);
        }

    }

    @Override
    public int getMostEndorsedPost() {
        HashMap<Integer,Integer> frequency = new HashMap<>();

        // traversing through all endorsed posts and counting their parent id's frequency
        for(Map.Entry<Integer,EndorsementPost> entry:endorsementPosts.entrySet()){
            Integer parentPostID=entry.getValue().getParentPost().getID();
            int cnt = frequency.getOrDefault(parentPostID,0);
            frequency.put(parentPostID,cnt+1);
        }
        // traversing through the frequency map
        Integer mostEndorsedCount=-1;
        Integer ID=0;
        for (Map.Entry<Integer,Integer> entry : frequency.entrySet()){
            //
            if(entry.getValue().compareTo(mostEndorsedCount)>0){
                mostEndorsedCount = entry.getValue();
                ID = entry.getKey();
            }
        }

        return ID;
    }

    @Override
    public int getMostEndorsedAccount() {
        HashMap<Integer,Integer>map = new HashMap<>();

        for(Map.Entry<Integer,EndorsementPost> entry : endorsementPosts.entrySet()) {
            Integer parentPostAccountID = entry.getValue().getParentPost().getAccount().getID();
            int cnt = map.getOrDefault(parentPostAccountID,0);
            map.put(parentPostAccountID,cnt+1);
        }

        Integer mostEndorsedCount=-1;
        Integer ID=0;
        for (Map.Entry<Integer,Integer> entry:map.entrySet()){

            if(entry.getValue().compareTo(mostEndorsedCount)>0){
                mostEndorsedCount = entry.getValue();
                ID = entry.getKey();
            }
        }

        return ID;

    }

    @Override
    public void erasePlatform() {

        accountsByHandle.clear();
        accountsByID.clear();
        postIdTracker=accountIDTracker=0;
        endorsementPosts.clear();
        posts.clear();
        comments.clear();
        //add a generic empty post
        emptyPost=new Post(-1,null,"The original content was removed" +
                " from the system and is no longer available.");

    }

    @Override
    public void savePlatform(String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(fos);

        out.writeObject(this);

        out.close();
    }

    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fis);
        SocialMedia md = (SocialMedia)in.readObject();

        accountsByHandle = md.accountsByHandle;
        accountsByID = md.accountsByID;
        postIdTracker = md.postIdTracker;
        accountIDTracker = md.accountIDTracker;
        endorsementPosts = md.endorsementPosts;
        posts = md.posts;
        comments = md.comments;
        //add a generic empty post
        emptyPost = md.emptyPost;
    }
}
