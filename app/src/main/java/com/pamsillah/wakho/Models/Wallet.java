package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 10/25/2017.
 */

public class Wallet {
    private String ID;
    private String Overal;
    private String Total;
    private String Drawable;
    private String Pending;
    private String Available;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOveral() {
        return Overal;
    }

    public void setOveral(String overal) {
        Overal = overal;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getDrawable() {
        return Drawable;
    }

    public void setDrawable(String drawable) {
        Drawable = drawable;
    }

    public String getPending() {
        return Pending;
    }

    public void setPending(String pending) {
        Pending = pending;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    private String AgentID;


}
