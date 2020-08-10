package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 24/2/2017.
 */

//public class Subscriber {
//
//    private int SubscriberId;
//    private String Name;
//    private String Surname;
//
//    public void setPhone(String phone) {
//        Phone = phone;
//    }
//
//    public void setAddress(String address) {
//        Address = address;
//    }
//
//    public void setIDNumber(String IDNumber) {
//        this.IDNumber = IDNumber;
//    }
//
//    public void setStatus(String status) {
//        Status = status;
//    }
//
//    public void setProfilePic(String profilePic) {
//        ProfilePic = profilePic;
//    }
//
//    public void setVerificationCode(String verificationCode) {
//        VerificationCode = verificationCode;
//    }
//
//    public void setRepeatPassword(String repeatPassword) {
//        RepeatPassword = repeatPassword;
//    }
//
//    public void setPassword(String password) {
//        Password = password;
//    }
//
//    public void setEmail(String email) {
//        Email = email;
//    }
//
//    public void setSurname(String surname) {
//        Surname = surname;
//    }
//
//    public void setName(String name) {
//        Name = name;
//    }
//
//    public void setSubscriberId(int subscriberId) {
//        SubscriberId = subscriberId;
//    }
//
//    public String getPhone() {
//        return Phone;
//    }
//
//    public int getSubscriberId() {
//        return SubscriberId;
//    }
//
//    public String getName() {
//        return Name;
//    }
//
//    public String getSurname() {
//        return Surname;
//    }
//
//    public String getEmail() {
//        return Email;
//    }
//
//    public String getPassword() {
//        return Password;
//    }
//
//    public String getRepeatPassword() {
//        return RepeatPassword;
//    }
//
//    public String getVerificationCode() {
//        return VerificationCode;
//    }
//
//    public String getProfilePic() { return ProfilePic;}
//
//    public String getStatus() {
//        return Status;
//    }
//
//    public String getIDNumber() {
//        return IDNumber;
//    }
//
//    public String getAddress() {
//        return Address;
//    }
//
//    public ImageUp getUpload() {
//        return upload;
//    }
//
//    public void setUpload(ImageUp upload) {
//        this.upload = upload;
//    }
//
//    public ImageUp upload;
//
//    private String Phone;
//    private String Email;
//    private String Password;
//    private String RepeatPassword;
//    private String VerificationCode;
//    private String ProfilePic;
//    private String Status;
//    private String IDNumber;
//    private String Address;
//}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Agents",
        "ChatRooms",
        "HiddenChats",
        "Notifications",
        "Posts",
        "ShopTransactions",
        "TagPays",
        "SubscriberId",
        "Name",
        "Surname",
        "Phone",
        "Email",
        "Password",
        "RepeatPassword",
        "VerificationCode",
        "ProfilePic",
        "Status",
        "IDNumber",
        "Address",
        "DateRegistered",
        "upload"
})
public class Subscriber {

    @JsonProperty("Agents")
    private List<Object> agents = new ArrayList<Object>();
    @JsonProperty("ChatRooms")
    private List<Object> chatRooms = new ArrayList<Object>();
    @JsonProperty("HiddenChats")
    private List<Object> hiddenChats = new ArrayList<Object>();
    @JsonProperty("Notifications")
    private List<Object> notifications = new ArrayList<Object>();
    @JsonProperty("Posts")
    private List<Object> posts = new ArrayList<Object>();
    @JsonProperty("ShopTransactions")
    private List<Object> shopTransactions = new ArrayList<Object>();
    @JsonProperty("TagPays")
    private List<Object> tagPays = new ArrayList<Object>();
    @JsonProperty("SubscriberId")
    private Integer subscriberId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Surname")
    private String surname;
    @JsonProperty("Phone")
    private String phone;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("RepeatPassword")
    private String repeatPassword;
    @JsonProperty("VerificationCode")
    private String verificationCode;
    @JsonProperty("ProfilePic")
    private String profilePic;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("IDNumber")
    private String iDNumber;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("DateRegistered")
    private String dateRegistered;
    @JsonProperty("upload")
    private ImageUp upload;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Agents")
    public List<Object> getAgents() {
        return agents;
    }

    @JsonProperty("Agents")
    public void setAgents(List<Object> agents) {
        this.agents = null;
    }

    @JsonProperty("ChatRooms")
    public List<Object> getChatRooms() {
        return chatRooms;
    }

    @JsonProperty("ChatRooms")
    public void setChatRooms(List<Object> chatRooms) {
        this.chatRooms = null;
    }

    @JsonProperty("HiddenChats")
    public List<Object> getHiddenChats() {
        return hiddenChats;
    }

    @JsonProperty("HiddenChats")
    public void setHiddenChats(List<Object> hiddenChats) {
        this.hiddenChats = null;
    }

    @JsonProperty("Notifications")
    public List<Object> getNotifications() {
        return notifications;
    }

    @JsonProperty("Notifications")
    public void setNotifications(List<Object> notifications) {
        this.notifications = null;
    }

    @JsonProperty("Posts")
    public List<Object> getPosts() {
        return posts;
    }

    @JsonProperty("Posts")
    public void setPosts(List<Object> posts) {
        this.posts = null;
    }

    @JsonProperty("ShopTransactions")
    public List<Object> getShopTransactions() {
        return shopTransactions;
    }

    @JsonProperty("ShopTransactions")
    public void setShopTransactions(List<Object> shopTransactions) {
        this.shopTransactions = shopTransactions;
    }

    @JsonProperty("TagPays")
    public List<Object> getTagPays() {
        return tagPays;
    }

    @JsonProperty("TagPays")
    public void setTagPays(List<Object> tagPays) {
        this.tagPays = tagPays;
    }

    @JsonProperty("SubscriberId")
    public Integer getSubscriberId() {
        return subscriberId;
    }

    @JsonProperty("SubscriberId")
    public void setSubscriberId(Integer subscriberId) {
        this.subscriberId = subscriberId;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Surname")
    public String getSurname() {
        return surname;
    }

    @JsonProperty("Surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonProperty("Phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("Phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("Email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("Email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("Password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("RepeatPassword")
    public String getRepeatPassword() {
        return repeatPassword;
    }

    @JsonProperty("RepeatPassword")
    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    @JsonProperty("VerificationCode")
    public String getVerificationCode() {
        return verificationCode;
    }

    @JsonProperty("VerificationCode")
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @JsonProperty("ProfilePic")
    public String getProfilePic() {
        return profilePic;
    }

    @JsonProperty("ProfilePic")
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("IDNumber")
    public String getIDNumber() {
        return iDNumber;
    }

    @JsonProperty("IDNumber")
    public void setIDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    @JsonProperty("Address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("Address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("DateRegistered")
    public String getDateRegistered() {
        return dateRegistered;
    }

    @JsonProperty("DateRegistered")
    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @JsonProperty("upload")
    public ImageUp getUpload() {
        return upload;
    }

    @JsonProperty("upload")
    public void setUpload(ImageUp upload) {
        this.upload = upload;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "subscriberId=" + subscriberId +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}
