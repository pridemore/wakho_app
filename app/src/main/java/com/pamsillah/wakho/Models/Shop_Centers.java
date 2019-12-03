package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class Shop_Centers {

    public int CentreId;
    public String Name;

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getCentreId() {
        return CentreId;
    }

    public void setCentreId(int centreId) {
        CentreId = centreId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContact_Phone() {
        return Contact_Phone;
    }

    public void setContact_Phone(String contact_Phone) {
        Contact_Phone = contact_Phone;
    }

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int shopID) {
        ShopID = shopID;
    }

    public String getContact_Email() {
        return Contact_Email;
    }

    public void setContact_Email(String contact_Email) {
        Contact_Email = contact_Email;
    }

    public String getAccount_Number() {
        return Account_Number;
    }

    public void setAccount_Number(String account_Number) {
        Account_Number = account_Number;
    }

    public String Location;
    public String Address;
    public String Contact_Phone;
    public String Account_Number;
    public String Contact_Email;
    public int ShopID;
}
