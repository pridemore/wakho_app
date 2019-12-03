package com.pamsillah.wakho.Parser;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SubscribersParser {


    public static Subscriber parseSubscriber(String resp) {
        Subscriber sub = new Subscriber();
        try {


            JSONObject obj = new JSONObject(resp);

            sub.setSubscriberId(Integer.parseInt(obj.getString("SubscriberId")));
            sub.setAddress(obj.getString("Address"));
            sub.setEmail(obj.getString("Email"));
            sub.setIDNumber(obj.getString("IDNumber"));
            sub.setName(obj.getString("Name"));
            sub.setPassword(obj.getString("Password"));
            sub.setPhone(obj.getString("Phone"));
            sub.setProfilePic(obj.getString("ProfilePic"));
            sub.setRepeatPassword(obj.getString("RepeatPassword"));
            sub.setStatus(obj.getString("Status"));
            sub.setSurname(obj.getString("Surname"));
            sub.setVerificationCode(obj.getString("VerificationCode"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sub;
    }

    public static List<Subscriber> parseFeed(JSONArray array) {
        List<Subscriber> subscribers = new ArrayList<Subscriber>();
        try {

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Subscriber sub = new Subscriber();
                sub.setSubscriberId(Integer.parseInt(obj.getString("SubscriberId")));
                sub.setAddress(obj.getString("Address"));
                sub.setEmail(obj.getString("Email"));
                sub.setIDNumber(obj.getString("IDNumber"));
                sub.setName(obj.getString("Name"));
                sub.setPassword(obj.getString("Password"));
                sub.setPhone(obj.getString("Phone"));
                sub.setProfilePic(obj.getString("ProfilePic"));
                sub.setRepeatPassword(obj.getString("RepeatPassword"));
                sub.setStatus(obj.getString("Status"));
                sub.setSurname(obj.getString("Surname"));
                sub.setVerificationCode(obj.getString("VerificationCode"));

                subscribers.add(sub);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return subscribers;
    }

    public static Subscriber Login(Subscriber subscriber) {
        searchAgent(String.valueOf(subscriber.getSubscriberId()));
        return subscriber;
    }

    public static List<Agent> agentList;

    public static void searchAgent(final String Id) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                ConnectionConfig.BASE_URL + "/api/agents", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                agentList = AgentParser.parseFeed(response);
                if (agentList.size() > 0) {
                    for (Agent agent : agentList
                            ) {
                        if (Id.equals(agent.getSubscriberId())) {
                            MyApplication.getinstance().getSession().setAgent(agent);

                            Toast.makeText(MyApplication.getinstance(), MyApplication.getinstance().getSession().getAgent().getCompanyName() + " dont forget to post your journeys to make more money", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());
            }
        });
        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }

}
