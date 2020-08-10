package com.pamsillah.wakho.helpers;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.Parser.AgentParser;
import com.pamsillah.wakho.VolleyCallback;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONArray;

import java.io.IOException;
import java.util.Map;

public class AgentHelper {

    public void searchAgent(final String Id, VolleyCallback volleyCallback) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                ConnectionConfig.BASE_URL + "/api/Agents/"+Id, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Agent agent = new ObjectMapper().readValue(response.toString(), Agent.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }})
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }
        };
        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }

}
