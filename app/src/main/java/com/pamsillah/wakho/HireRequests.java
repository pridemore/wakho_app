package com.pamsillah.wakho;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pamsillah.wakho.Adapters.HireRequestsaAdapter;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Parsers.PostsParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by .Net Developer on 10/5/2017.
 */

public class HireRequests extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    public List<Post> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hire_request);
        recyclerView = (RecyclerView) findViewById(R.id.rec);
        toolbar = (Toolbar) findViewById(R.id.tubha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<>();
        fill_with_data();
    }

    Agent aa = MyApplication.getinstance().getSession().getAgent();

    public void fill_with_data() {
        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading data...!");
        progressDialog.show();


        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, DTransUrls.HireReqs, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Post> dat = new ArrayList<>();
                        dat = PostsParser.parseFeed(response);
                        progressDialog.cancel();

                        if (dat.size() > 0) {
                            if (aa != null) {
                                for (Post post : dat) {


                                    if (post.getAgentID().equals(String.valueOf(aa.getAgentId()))) {
                                        data.add(post);


                                    }
                                }

                            }
                            recyclerView.setAdapter(new HireRequestsaAdapter(data, getApplicationContext()));

                        }


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
}
