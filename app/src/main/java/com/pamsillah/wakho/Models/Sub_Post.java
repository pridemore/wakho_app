package com.pamsillah.wakho.Models;

/**
 * Created by psillah on 5/10/2017.
 */
public class Sub_Post {
    int id;
    String PostID;
    String SubscriberId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getSubscriberId() {
        return SubscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        SubscriberId = subscriberId;
    }
}
