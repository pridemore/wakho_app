package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class TransactionFee {

    public int transID;
    public String Submitted;

    public String getSubmitted() {
        return Submitted;
    }

    public void setSubmitted(String submitted) {
        Submitted = submitted;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getTransID() {
        return transID;
    }

    public void setTransID(int transID) {
        this.transID = transID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReff_Number() {
        return reff_Number;
    }

    public void setReff_Number(String reff_Number) {
        this.reff_Number = reff_Number;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    private int transactionID;
    private int postID;
    private double amount;
    private String reff_Number;
    private String Date;
    private String Time;
    private String Status;
    private String PollUrl;
    private String paymentReff;
    private String Hash;

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    private String AgentID;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPollUrl() {
        return PollUrl;
    }

    public void setPollUrl(String pollUrl) {
        PollUrl = pollUrl;
    }

    public String getPaymentReff() {
        return paymentReff;
    }

    public void setPaymentReff(String paymentReff) {
        this.paymentReff = paymentReff;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String hash) {
        Hash = hash;
    }
}
