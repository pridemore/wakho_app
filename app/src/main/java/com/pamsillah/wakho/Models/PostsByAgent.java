package com.pamsillah.wakho.Models;

import com.google.gson.Gson;

/**
 * Created by .Net Developer on 9/7/2017.
 */

public class PostsByAgent {
    public String Id;
    public String AgentId;
    public String DatePosted;
    public String PickUp;
    public String LocationFrom;
    public String LocationTo;
    public String DepatureDate;
    public String TransPort;

    public String getETA() {
        return ETA;
    }

    public void setETA(String ETA) {
        this.ETA = ETA;
    }

    public String getFragility() {
        return fragility;
    }

    public void setFragility(String fragility) {
        this.fragility = fragility;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String ETA;
    public String fragility;
    public String weight;

    public Agent agent;

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(String agen) {

        Gson gson = new Gson();
        agent = gson.fromJson(agen, Agent.class);

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAgentId() {
        return AgentId;
    }

    public void setAgentId(String agentId) {
        AgentId = agentId;
    }

    public String getDatePosted() {
        return DatePosted;
    }

    public void setDatePosted(String datePosted) {
        DatePosted = datePosted;
    }

    public String getPickUp() {
        return PickUp;
    }

    public void setPickUp(String pickUp) {
        PickUp = pickUp;
    }

    public String getLocationFrom() {
        return LocationFrom;
    }

    public void setLocationFrom(String locationFrom) {
        LocationFrom = locationFrom;
    }

    public String getLocationTo() {
        return LocationTo;
    }

    public void setLocationTo(String locationTo) {
        LocationTo = locationTo;
    }

    public String getDepatureDate() {
        return DepatureDate;
    }

    public void setDepatureDate(String depatureDate) {
        DepatureDate = depatureDate;
    }

    public String getTransPort() {
        return TransPort;
    }

    public void setTransPort(String transPort) {
        TransPort = transPort;
    }

}
