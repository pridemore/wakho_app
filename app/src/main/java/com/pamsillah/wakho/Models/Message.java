package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 8/29/2017.
 */

public class Message {
    public String id;
    public String Sender;
    public String Recipient;
    public String Message;
    public String MessageFormat;
    public String DateSend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessageFormat() {
        return MessageFormat;
    }

    public void setMessageFormat(String messageFormat) {
        MessageFormat = messageFormat;
    }

    public String getDateSend() {
        return DateSend;
    }

    public void setDateSend(String dateSend) {
        DateSend = dateSend;
    }

    public String getTimeSend() {
        return TimeSend;
    }

    public void setTimeSend(String timeSend) {
        TimeSend = timeSend;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String TimeSend;
    public String status;
}
