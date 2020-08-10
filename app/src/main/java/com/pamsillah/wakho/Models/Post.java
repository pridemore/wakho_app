package com.pamsillah.wakho.Models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pamsillah.wakho.Models.ImageUp;
import com.pamsillah.wakho.Models.Subscriber;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "PostId",
        "DatePosted",
        "TimePosted",
        "SubscriberId",
        "Title",
        "Description",
        "Weight",
        "Fragility",
        "LocationToId",
        "LocationFromId",
        "PickUpPoint",
        "ProposedFee",
        "DeliveryDate",
        "Status",
        "ParcelPic",
        "AddressTo",
        "AgentID",
        "upload",
        "Subscriber"
})
public class Post {

    @JsonProperty("PostId")
    private Integer postId;
    @JsonProperty("DatePosted")
    private String datePosted;
    @JsonProperty("TimePosted")
    private String timePosted;
    @JsonProperty("SubscriberId")
    private String subscriberId;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Weight")
    private String weight;
    @JsonProperty("Fragility")
    private String fragility;
    @JsonProperty("LocationToId")
    private String locationToId;
    @JsonProperty("LocationFromId")
    private String locationFromId;
    @JsonProperty("PickUpPoint")
    private String pickUpPoint;
    @JsonProperty("ProposedFee")
    private String proposedFee;
    @JsonProperty("DeliveryDate")
    private String deliveryDate;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("ParcelPic")
    private String parcelPic;
    @JsonProperty("AddressTo")
    private String addressTo;
    @JsonProperty("AgentID")
    private String agentID;
    @JsonProperty("upload")
    private ImageUp upload;
    @JsonProperty("Subscriber")
    private Subscriber subscriber;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Integer getPostId() {
        return postId;
    }


    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public Object getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }


    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFragility() {
        return fragility;
    }

    public void setFragility(String fragility) {
        this.fragility = fragility;
    }

    public String getLocationToId() {
        return locationToId;
    }

    public void setLocationToId(String locationToId) {
        this.locationToId = locationToId;
    }

    public String getLocationFromId() {
        return locationFromId;
    }

    public void setLocationFromId(String locationFromId) {
        this.locationFromId = locationFromId;
    }

    public String getPickUpPoint() {
        return pickUpPoint;
    }

    public void setPickUpPoint(String pickUpPoint) {
        this.pickUpPoint = pickUpPoint;
    }

    public String getProposedFee() {
        return proposedFee;
    }

    public void setProposedFee(String proposedFee) {
        this.proposedFee = proposedFee;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParcelPic() {
        return parcelPic;
    }


    public void setParcelPic(String parcelPic) {
        this.parcelPic = parcelPic;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public ImageUp getUpload() {
        return upload;
    }


    public void setUpload(ImageUp upload) {
        this.upload = upload;
    }


    public Subscriber getSubscriber() {
        return subscriber;
    }


    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}
