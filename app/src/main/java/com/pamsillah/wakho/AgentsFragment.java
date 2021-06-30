package com.pamsillah.wakho;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
 * Created by psillah on 3/24/2017.
 */
public class AgentsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView2;
    private TextView emptyState2;
    List<PostsByAgent> data = new ArrayList<>();
    FloatingActionButton fab;
    SwipeRefreshLayout srl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.agents_activity, container, false);
        setHasOptionsMenu(true);
        recyclerView2 = v.findViewById(R.id.my_recycler_view);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        fab = v.findViewById(R.id.fabagents);
        srl = v.findViewById(R.id.agentSwipeContainer);
        emptyState2 = v.findViewById(R.id.txtEmptyState);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.getinstance().getSession().getAgent() != null) {
                    if (MyApplication.getinstance().getSession().getAgent()
                            .getCompanyRegNumber() != null && MyApplication.getinstance()
                            .getSession().getAgent().getCompanyRegNumber().contains("Active")) {

                        startActivity(new Intent(getContext(), AgentJourney.class));
                    } else {
                        Toast.makeText(getContext(), "Agent not verified",
                                Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getContext(), "Register as an agent to post a Journey",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(recyclerView2!=null &&recyclerView2.getAdapter()!=null){
                    ((RecyclerViewAdapter) recyclerView2.getAdapter()).clear();
                }
                fill_with_data();
                srl.setRefreshing(false);
            }
        });

        srl.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.BackgroundDoveGrey);

        fill_with_data();


        return v;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchMeuItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) searchMeuItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
    }


    public void fill_with_data() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                DTransUrls.PostJourney, null, new Response.Listener<JSONArray>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onResponse(JSONArray response) {
                List<PostsByAgent> dat1 = new ArrayList<>();
                dat1.clear();
                dat1 = PostsByAgentParser.parseFeed(response);
                 data= new ArrayList<>();
                for (PostsByAgent item : dat1) {

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
                    } catch (Exception ignored) {
                    }
                }
                if (dat1.size() > 0) {
                    recyclerView2.setAdapter(new RecyclerViewAdapter(dat1,getContext()));
                    MyApplication.getinstance().getPostsByAgent();
                    recyclerView2.getAdapter().notifyDataSetChanged();
                }
                showEmptyState(!data.isEmpty());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());
                //progressDialog.cancel();

            }
        }) {
            public java.util.Map<String, String> getHeaders() {
                return AuthHeader.getHeaders();
            }
        };


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }

    private void showEmptyState(boolean b) {
        if(b){
            emptyState2.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.VISIBLE);
        }else{
            emptyState2.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s = s.toLowerCase();
        ArrayList<PostsByAgent> newlist = new ArrayList<>();
        for (PostsByAgent name : data) {
            String to = name.getLocationTo().toLowerCase();
            String from = name.getLocationFrom().toLowerCase();
            String agentName = name.agent.getCompanyName().toLowerCase();

            if (agentName.contains(s) || to.contains(s) || from.contains(s)) {
                newlist.add(name);

            }
        }

        RecyclerView.Adapter newAdapter;
        newAdapter = new RecyclerViewAdapter(newlist, getActivity());
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setAdapter(newAdapter);
        newAdapter.notifyDataSetChanged();

        return true;

    }
}

