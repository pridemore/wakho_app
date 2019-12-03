package com.pamsillah.wakho;

/**
 * Created by psillah on 3/27/2017.
 */
public class ChatsObject {
    String username;
    String message;
    String time;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getUserpic() {
        return userpic;
    }

    public void setUserpic(Integer userpic) {
        this.userpic = userpic;
    }

    Integer userpic;

    public ChatsObject(String username, String message, String time, Integer userpic) {
        this.username = username;
        this.message = message;
        this.time = time;
        this.userpic = userpic;
    }

    public ChatsObject() {

    }
}
