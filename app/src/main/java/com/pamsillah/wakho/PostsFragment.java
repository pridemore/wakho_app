package com.pamsillah.wakho;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Parser.AgentParser;
import com.pamsillah.wakho.Parsers.PostsParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psillah on 3/24/2017.
 */


public class PostsFragment extends Fragment implements SearchView.OnQueryTextListener {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeContainer;
    List<Post> data = new ArrayList<>();
    private SearchView mSearchView;
    private MenuItem searchMeuItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.post_layout, container, false);
        setHasOptionsMenu(true);
        fab = (FloatingActionButton) v.findViewById(R.id.postfab);
        recyclerView = (RecyclerView) v.findViewById(R.id.posts_recycler);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data.clear();

        fill_with_data();
        LoadAgents();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostParcel.class));
            }
        });


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((PostsRecyclerViewAdapter) recyclerView.getAdapter()).clear();
                fill_with_data();
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.BackgroundDoveGrey);


        return v;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        searchMeuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMeuItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
    }


    public void fill_with_data() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading data...!");
        progressDialog.show();

        final List<Post> datta = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, DTransUrls.PostPost, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                data = PostsParser.parseFeed(response);
progressDialog.dismiss();
                progressDialog.cancel();
                if (data.size() > 0) {

                    recyclerView.setAdapter(new PostsRecyclerViewAdapter(data, getActivity()));
                    MyApplication.getinstance().setListPost(data);
                    recyclerView.getAdapter().notifyDataSetChanged();
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


    public void LoadAgents() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                com.pamsillah.wakho.app_settings.ConnectionConfig.BASE_URL + "/api/agents", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Agent> datta = new ArrayList<>();
                datta = AgentParser.parseFeed(response);
                Agent aget = MyApplication.getinstance().getSession().getAgent();
                Subscriber sub = MyApplication.getinstance().getSession().getSubscriber();


                if (datta.size() > 0) {

                    MyApplication.getinstance().setLstAgent(datta);


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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s = s.toLowerCase();
        ArrayList<Post> newlist = new ArrayList<>();
        for (Post name : data) {
            String to = name.getLocationToId().toLowerCase();
            String title = name.getTitle().toLowerCase();
            String from = name.getLocationFromId().toLowerCase();

            if (to.contains(s) || title.contains(s) || from.contains(s)) {
                newlist.add(name);

            }
        }

        RecyclerView.Adapter newAdapter;
        newAdapter = new PostsRecyclerViewAdapter(newlist, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(newAdapter);
        newAdapter.notifyDataSetChanged();

        return true;

    }
}