package com.pamsillah.wakho.Parsers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
                String json = obj.toString();
                Post post = new ObjectMapper().readValue(json, Post.class);
                Posts.add(post);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Posts;
    }

}
