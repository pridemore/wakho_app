package com.pamsillah.wakho.Parser;

import com.pamsillah.wakho.Models.PostsByAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by .Net Developer on 9/7/2017.
 */

public class PostsByAgentParser {

    public static List<PostsByAgent> parseFeed(JSONArray array) {
        List<PostsByAgent> agents = new ArrayList<PostsByAgent>();
        try {

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                PostsByAgent sub = new PostsByAgent();
                sub.setAgentId(obj.getString("Id"));
                sub.setAgentId(obj.getString("AgentId"));
                sub.setDatePosted(obj.getString("DatePosted"));
                sub.setDepatureDate(obj.getString("DepatureDate"));
                sub.setPickUp(obj.getString("PickUp"));
                sub.setId(obj.getString("Id"));
                sub.setLocationFrom(obj.getString("LocationFrom"));
                sub.setLocationTo(obj.getString("LocationTo"));
                sub.setTransPort(obj.getString("TransPort"));
                sub.setWeight(obj.getString("Weight"));
                sub.setFragility(obj.getString("Fragility"));
                sub.setETA(obj.getString("ETA"));

                sub.setAgent(obj.getJSONObject("Agent").toString());
                agents.add(sub);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return agents;
    }
}
