package com.pamsillah.wakho.Models;

import com.firebase.client.ServerValue;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by .Net Developer on 9/25/2017.
 */

public class Notification {

    String user_id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    String message;
    String description;
    String type;
    long timestamp, status;

    public String getConverid() {
        return converid;
    }

    public void setConverid(String converid) {
        this.converid = converid;
    }

    String converid;

    public Notification() {
    }

    public Notification(String user_id, String message, String description,
                        String type, long timestamp, long status, String converid) {
        this.user_id = user_id;
        this.message = message;
        this.description = description;
        this.type = type;
        this.timestamp = timestamp;
        this.status = status;
        this.converid = converid;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("converid", converid);
        result.put("message", message);
        result.put("description", description);
        result.put("timestamp", ServerValue.TIMESTAMP);
        result.put("type", type);
        result.put("status", status);
        return result;
    }


}