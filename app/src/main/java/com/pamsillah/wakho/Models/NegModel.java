package com.pamsillah.wakho.Models;

/**
 * Created by psillah on 6/20/2017.
 */
public class NegModel {
    private String sender;
    private String amount;
    private String messagetime;
    private String agentNum;

    public String getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(String agentNum) {
        this.agentNum = agentNum;
    }

    public String getSubNum() {
        return subNum;
    }

    public void setSubNum(String subNum) {
        this.subNum = subNum;
    }

    private String subNum;

    public String getMesageDate() {
        return mesageDate;
    }

    public void setMesageDate(String mesageDate) {
        this.mesageDate = mesageDate;
    }

    private String mesageDate;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(String messagetime) {
        this.messagetime = messagetime;
    }


}
