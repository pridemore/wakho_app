package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class Subscriber {

    private int SubscriberId;
    private String Name;
    private String Surname;

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public void setVerificationCode(String verificationCode) {
        VerificationCode = verificationCode;
    }

    public void setRepeatPassword(String repeatPassword) {
        RepeatPassword = repeatPassword;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSubscriberId(int subscriberId) {
        SubscriberId = subscriberId;
    }

    public String getPhone() {
        return Phone;
    }

    public int getSubscriberId() {
        return SubscriberId;
    }

    public String getName() {
        return Name;
    }

    public String getSurname() {
        return Surname;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getRepeatPassword() {
        return RepeatPassword;
    }

    public String getVerificationCode() {
        return VerificationCode;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public String getStatus() {
        return Status;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public String getAddress() {
        return Address;
    }

    public ImageUp getUpload() {
        return upload;
    }

    public void setUpload(ImageUp upload) {
        this.upload = upload;
    }

    public ImageUp upload;

    private String Phone;
    private String Email;
    private String Password;
    private String RepeatPassword;
    private String VerificationCode;
    private String ProfilePic;
    private String Status;
    private String IDNumber;
    private String Address;
}
