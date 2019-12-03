package com.pamsillah.wakho.Models;

/**
 * Created by psillah on 6/16/2017.
 */

import java.util.List;

public class Chat {
    public String id;
    public String CreatorID;

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String last;
    public String TimeCreated;
    public String DateCreated;
    public List<Message> message;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String location;

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }

    public String Recipient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorID() {
        return CreatorID;
    }

    public void setCreatorID(String creatorID) {
        CreatorID = creatorID;
    }

    public String getTimeCreated() {
        return TimeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        TimeCreated = timeCreated;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public List<Message> getMessagess() {
        return message;
    }

    public void setMessagess(List<Message> messagess) {
        this.message = message;
    }

    public String getRecipName() {
        return recipName;
    }

    public void setRecipName(String recipName) {
        this.recipName = recipName;
    }

    public String recipName;
}







