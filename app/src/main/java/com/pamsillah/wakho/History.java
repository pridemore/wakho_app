package com.pamsillah.wakho;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Parsers.PostsParser;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by .Net Developer on 9/9/2017.
 */

public class History extends Activity {
    Toolbar toolbar;
    CardView cardPost, cardMoney, cardAgents, cardWallet, cardServices, cardAgentPosts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        filWithPending();
        toolbar = (Toolbar) findViewById(R.id.historyToolBar);
        toolbar.setTitle("History");
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //Initialize Cards
        //Post
        cardPost = (CardView) findViewById(R.id.cardPost);
        cardPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HistoryPosts.class));
            }
        });
//Money
        cardMoney = (CardView) findViewById(R.id.cardMoney);
        cardMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HistoryMoney.class));
            }
        });
//Agents

        cardAgentPosts = (CardView) findViewById(R.id.cardAgentPost);
        cardAgentPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (MyApplication.getinstance().getSession().getAgent() != null) {
                    startActivity(new Intent(getApplicationContext(), HistoryAgentPosts.class));
                } else
                    Toast.makeText(getApplicationContext(), "This feature is available for agents only", Toast.LENGTH_SHORT).show();
            }
        });
        //Wallet

        cardWallet = (CardView) findViewById(R.id.cardWallet);
        cardWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.getinstance().getSession().getAgent() != null) {
                    startActivity(new Intent(getApplicationContext(), Wallet.class));
                } else
                    Toast.makeText(getApplicationContext(), "This feature is available for agents only", Toast.LENGTH_SHORT).show();
            }
        });

        //Wallet

        cardServices = (CardView) findViewById(R.id.cardServices);
        cardServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    List<Post> datta = new ArrayList<>();
    List<Post> data = new ArrayList<>();

    public void filWithPending() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ConnectionConfig.BASE_URL + "/api/pendingPost", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                datta = PostsParser.parseFeed(response);
                Agent aget = MyApplication.getinstance().getSession().getAgent();
                Subscriber sub = MyApplication.getinstance().getSession().getSubscriber();

                // callback.onSuccess(publications);
                if (datta.size() > 0) {
                    for (Post p : datta
                            ) {


                        if ((p.getSubscriberId().equals(String.valueOf(sub.getSubscriberId())) && (p.getStatus().contains("Waiting for Delivery") | p.getStatus().contains("Pay Initiated") | p.getStatus().contains("Created")))) {
                            for (Agent a : MyApplication.getinstance().getLstAgent()) {
                                if (String.valueOf(a.getAgentId()).equals(p.getAgentID())) {

                                    p.setStatus(p.getStatus() + "#" + a.getCompanyName() + "#" + a.getCompanyLogo() + "#" + sub.getName() + " " + sub.getSurname());
                                    data.add(p);
                                    break;
                                }
                            }


                        } else if ((aget != null) && (String.valueOf(aget.getAgentId()).equals(p.getAgentID())) && (p.getStatus().contains("Waiting for Delivery") | p.getStatus().contains("Pay Initiated") | p.getStatus().contains("Pay Initiated"))) {
                            for (Subscriber a : MyApplication.getinstance().getSubscribers()
                                    ) {
                                if (String.valueOf(a.getSubscriberId()).equals(p.getSubscriberId())) {
                                    p.setStatus(p.getStatus() + "#" + aget.getCompanyName() + "#" + aget.getCompanyLogo() + "#" + sub.getName() + " " + sub.getSurname());

                                    data.add(p);
                                    break;
                                }
                            }

                        } else {

                            if (p.getDescription().contains(sub.getPhone().replace("+263", "0").replace(" ", ""))) {

                                for (Agent a : MyApplication.getinstance().getLstAgent()) {
                                    if (String.valueOf(a.getAgentId()).equals(p.getAgentID())) {

                                        p.setStatus(p.getStatus() + "#" + a.getCompanyName() + "#" + a.getCompanyLogo());

                                        break;
                                    }
                                }

                                for (Subscriber a : MyApplication.getinstance().getSubscribers()
                                        ) {
                                    if (String.valueOf(a.getSubscriberId()).equals(p.getSubscriberId())) {
                                        p.setStatus(p.getStatus() + "#" + sub.getName() + " " + sub.getSurname());


                                        break;
                                    }
                                }

                                data.add(p);
                            }


                        }

                    }

                    MyApplication.getinstance().pending.addAll(data);


                } else {


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
