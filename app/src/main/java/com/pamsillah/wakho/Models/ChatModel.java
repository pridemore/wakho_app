package com.pamsillah.wakho.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psillah on 6/6/2017.
 */
public class ChatModel {
    String sender;
    String receiver;
    List<String> messagetexts = new ArrayList<>();

    public ChatModel() {
    }

    public ChatModel(String sender, String receiver, ArrayList<String> messagetexts) {
        this.sender = sender;
        this.receiver = receiver;
        this.messagetexts = messagetexts;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public List<String> getMessagetexts() {
        return messagetexts;
    }

    public void setMessagetexts(List<String> messagetexts) {
        this.messagetexts = messagetexts;
    }
}
