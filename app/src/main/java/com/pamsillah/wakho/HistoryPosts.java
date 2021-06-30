package com.pamsillah.wakho;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Parsers.PostsParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by .Net Developer on 9/9/2017.
 */

public class HistoryPosts extends Activity {
    RecyclerView recyclerView3;
     TextView emptyState3;
    List<Post> data = new ArrayList<>();
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
        recyclerView3 = (RecyclerView) findViewById(R.id.help_post_re);
        emptyState3 =  (TextView) findViewById(R.id.txtEmptyState);
        CardView cv = (CardView) findViewById(R.id.cardview);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressDialog = new ProgressDialog(this);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());



        fill_with_data();
    }

    public void fill_with_data() {

        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Loading Data");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.show();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, DTransUrls.PostPost, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Post> datta = new ArrayList<>();
                datta = PostsParser.parseFeed(response);
                showEmptyState(datta.isEmpty());
                Agent aget = MyApplication.getinstance().getSession().getAgent();
                Subscriber sub = MyApplication.getinstance().getSession().getSubscriber();
                data.clear();
                // callback.onSuccess(publications);
                if (datta.size() > 0) {
                    for (Post p : datta
                            ) {
                        if (p.getSubscriberId().equals(String.valueOf(sub.getSubscriberId()))) {

                            data.add(p);
                            Log.d("ERROR ", p.getTitle());
                        }
                    }
                    data.addAll(MyApplication.getinstance().getPending());
                    recyclerView3.setAdapter(new PostsRecyclerViewAdapter(data, getApplicationContext()));
                } else {
                    data.addAll(MyApplication.getinstance().getPending());
                    recyclerView3.setAdapter(new PostsRecyclerViewAdapter(data, getApplicationContext()));
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
            emptyState3.setVisibility(View.VISIBLE);
            recyclerView3.setVisibility(View.GONE);
        }else{
            emptyState3.setVisibility(View.GONE);
            recyclerView3.setVisibility(View.VISIBLE);
        }
    }

}
