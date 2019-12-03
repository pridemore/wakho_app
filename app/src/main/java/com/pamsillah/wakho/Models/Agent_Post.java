package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class Agent_Post {
    public int Eng_PostId;
    public int AgentId;

    public int getPostId() {
        return PostId;
    }

    public void setPostId(int postId) {
        PostId = postId;
    }

    public String getRec_Date() {
        return Rec_Date;
    }

    public void setRec_Date(String rec_Date) {
        Rec_Date = rec_Date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getAgentId() {
        return AgentId;
    }

    public void setAgentId(int agentId) {
        AgentId = agentId;
    }

    public int getEng_PostId() {
        return Eng_PostId;
    }

    public void setEng_PostId(int eng_PostId) {
        Eng_PostId = eng_PostId;
    }

    public int PostId;
    public String Date;
    public String Rec_Date;
}
