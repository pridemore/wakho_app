package com.pamsillah.wakho.Parsers;

import com.pamsillah.wakho.Models.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psillah on 3/27/2017.
 */
public class PostsParser {
    public static List<Post> parseFeed(JSONArray array) {
        List<Post> Posts = new ArrayList<Post>();
        try {

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Post sub = new Post();
                sub.setPostId(Integer.parseInt(obj.getString("PostId")));
                sub.setAddressTo(obj.getString("AddressTo"));
                sub.setDatePosted(obj.getString("DatePosted"));
                sub.setDeliveryDate(obj.getString("DeliveryDate"));
                sub.setDescription(obj.getString("Description").replace(" ", "").trim());
                sub.setFragility(obj.getString("Fragility"));
                sub.setLocationFromId(obj.getString("LocationFromId"));
                sub.setLocationToId(obj.getString("LocationToId"));
                sub.setParcelPic(obj.getString("ParcelPic"));
                sub.setStatus(obj.getString("Status"));
                sub.setPickUpPoint(obj.getString("PickUpPoint"));
                sub.setAgentID(obj.getString("AgentID"));
                sub.setProposedFee(obj.getString("ProposedFee"));
                sub.setSubscriberId(obj.getString("SubscriberId"));
                sub.setTimePosted(obj.getString("TimePosted"));
                sub.setTitle(obj.getString("Title"));
                sub.setWeight(obj.getString("Weight"));
                Posts.add(sub);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return Posts;
    }

}
