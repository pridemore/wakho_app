package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class Post {

    public ImageUp getUpload() {
        return upload;
    }

    public void setUpload(ImageUp upload) {
        this.upload = upload;
    }

    public ImageUp upload;

    public int getPostId() {
        return PostId;
    }

    public void setPostId(int postId) {
        PostId = postId;
    }

    public String getDatePosted() {
        return DatePosted;
    }

    public void setDatePosted(String datePosted) {
        DatePosted = datePosted;
    }

    public String getTimePosted() {
        return TimePosted;
    }

    public void setTimePosted(String timePosted) {
        TimePosted = timePosted;
    }

    public String getSubscriberId() {
        return SubscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        SubscriberId = subscriberId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getFragility() {
        return Fragility;
    }

    public void setFragility(String fragility) {
        Fragility = fragility;
    }

    public String getLocationToId() {
        return LocationToId;
    }

    public void setLocationToId(String locationToId) {
        LocationToId = locationToId;
    }

    public String getLocationFromId() {
        return LocationFromId;
    }

    public void setLocationFromId(String locationFromId) {
        LocationFromId = locationFromId;
    }

    public String getPickUpPoint() {
        return PickUpPoint;
    }

    public void setPickUpPoint(String pickUpPoint) {
        PickUpPoint = pickUpPoint;
    }

    public String getProposedFee() {
        return ProposedFee;
    }

    public void setProposedFee(String proposedFee) {
        ProposedFee = proposedFee;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getParcelPic() {
        return ParcelPic;
    }

    public void setParcelPic(String parcelPic) {
        ParcelPic = parcelPic;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        this.AgentID = agentID;
    }

    private String AgentID;
    private int PostId;
    private String DatePosted;
    private String TimePosted;
    private String SubscriberId;
    private String Title;
    private String Description;
    private String Weight;
    private String Fragility;
    private String LocationToId;
    private String LocationFromId;
    private String PickUpPoint;
    private String ProposedFee;
    private String DeliveryDate;
    private String Status;
    private String ParcelPic;
    private String AddressTo;

    public String getAddressTo() {
        return AddressTo;
    }

    public void setAddressTo(String addressTo) {
        AddressTo = addressTo;
    }


}
