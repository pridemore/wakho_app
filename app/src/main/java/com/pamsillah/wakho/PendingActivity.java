package com.pamsillah.wakho;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pamsillah.wakho.Adapters.PendingAdapter;
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

public class PendingActivity extends Activity {
    Toolbar toolbar;
    RecyclerView recyclerView4;
    private TextView emptyState4;
    List<Post> data = new ArrayList<>();
    ProgressDialog progressDialog = null;
    PendingAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_activity);
        adapter=new PendingAdapter(data, getApplicationContext());
        emptyState4 = (TextView)findViewById(R.id.txtEmptyState);
        recyclerView4 = (RecyclerView) findViewById(R.id.pendingRecycler);
        CardView cv = (CardView) findViewById(R.id.cardview);
        recyclerView4.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.setTitle("Updating ");
        //progressDialog.show();
        recyclerView4.setItemAnimator(new DefaultItemAnimator());

        fill_with_data();

        toolbar = (Toolbar) findViewById(R.id.pendingToolBar);
        toolbar.setTitle("Pending Deliveries");
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    List<Post> datta = new ArrayList<>();

    public void fill_with_data() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                ConnectionConfig.BASE_URL + "/api/pendingPost", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                datta = PostsParser.parseFeed(response);
                showEmptyState(datta.isEmpty());
                Agent currentAgent = MyApplication.getinstance().getSession().getAgent();
                Subscriber sub = MyApplication.getinstance().getSession().getSubscriber();

                // callback.onSuccess(publications);
                if (datta.size() > 0) {
                    for (Post p : datta
                            ) {


                        if ((p.getSubscriberId().equals(String.valueOf(sub.getSubscriberId())) && (p.getStatus().contains("Paid.Waiting")))) {
                             Agent agent=MyApplication.getinstance().getAgent();
                            for (Agent a : MyApplication.getinstance().getLstAgent()) {
                                if (String.valueOf(a.getAgentId()).equals(p.getAgentID())) {

                                    p.setStatus(p.getStatus() + "#" + a.getCompanyName() + "#" + a.getCompanyLogo() + "#" + sub.getName() + " " + sub.getSurname());
                                    data.add(p);
                                    break;
                                }
                            }


                        }
                        else if ((currentAgent != null) && (String.valueOf(currentAgent.getAgentId()).equals(p.getAgentID())) && (p.getStatus().contains("Paid.Waiting") )) {
                            for (Subscriber a : MyApplication.getinstance().getSubscribers()
                                    ) {
                                if (String.valueOf(a.getSubscriberId()).equals(p.getSubscriberId())) {
                                    p.setStatus(p.getStatus() + "#" + currentAgent.getCompanyName() + "#" + currentAgent.getCompanyLogo() + "#" + sub.getName() + " " + sub.getSurname());

                                    data.add(p);
                                    break;
                                }
                            }

                        }
                        else {
                            String number =sub.getPhone().replace("+263", "0").replace(" ", "");
                            if (p.getDescription().split(":")[1].replace("263","0").contains(number)) {

                            if(MyApplication.getinstance().getLstAgent()!=null)
                                for (Agent a : MyApplication.getinstance().getLstAgent()) {
                                    if (String.valueOf(a.getAgentId()).equals(p.getAgentID())) {
                                        p.setStatus(p.getStatus() + "#" + a.getCompanyName() + "#" + a.getCompanyLogo());
                                        break;
                                    }

                            }

                                if(MyApplication.getinstance().getSubscribers()!=null)
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
                    progressDialog.cancel();
                     adapter = new PendingAdapter(data, getApplicationContext());
                    MyApplication.getinstance().listPost.addAll(data);
                    recyclerView4.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

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
    private void showEmptyState(boolean b) {
        if (b) {
            emptyState4.setVisibility(View.VISIBLE);
            recyclerView4.setVisibility(View.GONE);
        } else {
            emptyState4.setVisibility(View.GONE);
            recyclerView4.setVisibility(View.VISIBLE);
        }
    }

}
