package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class SopTransactions {

    public int TransactionID;
    public String Date_Created;
    public String Time_Created;

    public String getReff_Number() {
        return Reff_Number;
    }

    public void setReff_Number(String reff_Number) {
        Reff_Number = reff_Number;
    }

    public int getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(int transactionID) {
        TransactionID = transactionID;
    }

    public String getDate_Created() {
        return Date_Created;
    }

    public void setDate_Created(String date_Created) {
        Date_Created = date_Created;
    }

    public String getTime_Created() {
        return Time_Created;
    }

    public void setTime_Created(String time_Created) {
        Time_Created = time_Created;
    }

    public String getPay_Method() {
        return Pay_Method;
    }

    public void setPay_Method(String pay_Method) {
        Pay_Method = pay_Method;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public int getSubscriberID() {
        return SubscriberID;
    }

    public void setSubscriberID(int subscriberID) {
        SubscriberID = subscriberID;
    }

    public String getSub_Phone() {
        return Sub_Phone;
    }

    public void setSub_Phone(String sub_Phone) {
        Sub_Phone = sub_Phone;
    }

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

    public double getTransactionFee() {
        return TransactionFee;
    }

    public void setTransactionFee(double transactionFee) {
        TransactionFee = transactionFee;
    }

    public String Reff_Number;
    public String Pay_Method;
    public int ItemID;
    public int SubscriberID;
    public String Sub_Phone;
    public String Status;
    public String PollUrl;
    public double TransactionFee;
}
