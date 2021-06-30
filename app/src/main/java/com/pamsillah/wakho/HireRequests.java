package com.pamsillah.wakho;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.pamsillah.wakho.Adapters.HireRequestsaAdapter;
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
 * Created by .Net Developer on 10/5/2017.
 */

public class HireRequests extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    private TextView emptyState9;
    public List<Post> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hire_request);
        recyclerView = (RecyclerView) findViewById(R.id.rec);
        emptyState9 = (TextView)findViewById(R.id.txtEmptyState);
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
    Subscriber sub=MyApplication.getinstance().getSession().getSubscriber();

    public void fill_with_data() {
        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading data...!");
        progressDialog.show();


        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, DTransUrls.HireReqs, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Post> dat;
                        dat = PostsParser.parseFeed(response);
                        progressDialog.cancel();

                        if (dat.size() > 0) {
                            if (aa != null) {
                                for (Post post : dat) {
                                    if (post.getAgentID().equals(String.valueOf(aa.getAgentId()))) {
                                        data.add(post);
                                    }
                                }
                                recyclerView.setAdapter(new HireRequestsaAdapter(data, getApplicationContext()));
                                MyApplication.getinstance().setListPost(data);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }

                            if(sub!=null){
                                for(Post post:dat){
                                    if(post.getSubscriberId().equals(String.valueOf(sub.getSubscriberId()))){
                                        data.add(post);
                                    }
                                }
                                recyclerView.setAdapter(new PostsRecyclerViewAdapter(data, getApplicationContext()));
                                MyApplication.getinstance().setListPost(data);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                        showEmptyState(data.isEmpty());

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
            emptyState9.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            emptyState9.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
