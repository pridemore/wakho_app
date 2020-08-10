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
import com.google.common.collect.Lists;
import com.pamsillah.wakho.Models.PostsByAgent;
import com.pamsillah.wakho.Parser.PostsByAgentParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by .Net Developer on 9/9/2017.
 */

public class HistoryAgentPosts extends Activity {
    RecyclerView recyclerView9;
    TextView emptyState9;
    List<PostsByAgent> data = new ArrayList<>();
    Toolbar toolbar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_posts);
        toolbar = (Toolbar) findViewById(R.id.hispoToolBa);
        toolbar.setTitle("Posts You Posted");
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView9 = (RecyclerView) findViewById(R.id.help_post_re);
        CardView cv = (CardView) findViewById(R.id.cardview);
        recyclerView9.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        emptyState9=(TextView) findViewById(R.id.txtEmptyState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Loading Data");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.show();
        recyclerView9.setItemAnimator(new DefaultItemAnimator());

        fill_with_data();
    }

    public void fill_with_data() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, DTransUrls.PostJourney, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<PostsByAgent> dat1 = new ArrayList<>();
                dat1 = PostsByAgentParser.parseFeed(response);

                for (PostsByAgent item : dat1
                        ) {


                    if (item.agent.getSubscriberId().equals(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()))) {
                        Date da1 = new Date(), da2 = new Date();
                        String d2 = item.getDepatureDate().split("@")[0].trim();
                        String d1 = new SimpleDateFormat("dd/MM/yy").format(new Date());
                        try {
                            da1 = new SimpleDateFormat("dd/MM/yy").parse(d1);
                            da2 = new SimpleDateFormat("dd/MM/yy").parse(d2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        try {
                            if (da2.after(da1) || d2.equals(d1)) {
                                data.add(item);
                            }


                        } catch (Exception e) {
                        }
                    }
                }
                showEmptyState(!data.isEmpty());
                if (data.size() > 0) {

                    recyclerView9.setAdapter(new RecyclerViewAdapter(Lists.reverse(data), getApplicationContext()));
                    MyApplication.getinstance().getPostsByAgent();
                    recyclerView9.getAdapter().notifyDataSetChanged();
                }
                progressDialog.cancel();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());
                progressDialog.cancel();

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
        if(b){
            emptyState9.setVisibility(View.GONE);
            recyclerView9.setVisibility(View.VISIBLE);
        }else{
            emptyState9.setVisibility(View.VISIBLE);
            recyclerView9.setVisibility(View.GONE);
        }
    }
}
