package com.pamsillah.wakho;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pamsillah.wakho.Adapters.AgentAdapter;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Parser.AgentParser;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by .Net Developer on 9/9/2017.
 */

public class HistoryAgents extends Activity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<Agent> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_agents);
        toolbar = (Toolbar) findViewById(R.id.hisagToolBa);
        toolbar.setTitle("Trusted Agents");
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.history_re);
        CardView cv = (CardView) findViewById(R.id.cv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        fill_with_data();

    }

    public void fill_with_data() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ConnectionConfig.BASE_URL + "/api/agents", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Agent> datta = new ArrayList<>();
                datta = AgentParser.parseFeed(response);
                Agent aget = MyApplication.getinstance().getSession().getAgent();
                Subscriber sub = MyApplication.getinstance().getSession().getSubscriber();


                if (datta.size() > 0) {
                    data = datta;
                    MyApplication.getinstance().setLstAgent(data);
                    recyclerView.setAdapter(new AgentAdapter(data, getApplicationContext()));

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());


            }
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }
        };


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }
}
