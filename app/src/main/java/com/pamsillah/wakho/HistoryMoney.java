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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pamsillah.wakho.Adapters.TransactionAdapter;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Models.TransactionFee;
import com.pamsillah.wakho.Parser.TransactionsParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by .Net Developer on 9/9/2017.
 */

public class HistoryMoney extends Activity {
    Toolbar toolbar;
    RecyclerView recyclerView5;
    private TextView emptyState5;
    List<TransactionFee> data = new ArrayList<>();
    TransactionAdapter adapter = new TransactionAdapter(data);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_money);
        emptyState5 = (TextView) findViewById(R.id.txtEmptyState);
        toolbar = (Toolbar) findViewById(R.id.hismoToolbar);
        toolbar.setTitle("Money Transferes made");
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView5 = (RecyclerView) findViewById(R.id.his_money_recy);
        CardView cv = (CardView) findViewById(R.id.his_money_card);
        recyclerView5.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        recyclerView5.setItemAnimator(new DefaultItemAnimator());

        recyclerView5.setAdapter(adapter);
        Subscriber subscriber = MyApplication.getinstance().getSession().getSubscriber();
        Agent agent = MyApplication.getinstance().getSession().getAgent();
        if (subscriber != null) {
            getTransactions(DTransUrls.usertransactions + subscriber.getSubscriberId());
        }
        if (agent != null) {
            getTransactions(DTransUrls.agentTransactions + agent.getAgentId());
        }
        recyclerView5.setAdapter(adapter);
    }

    public void getTransactions(String url) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                List<TransactionFee> datta = new ArrayList<>();
                datta = TransactionsParser.parseFeed(response);
                showEmptyState(datta.isEmpty());

                data.addAll(datta);

                recyclerView5.setAdapter(adapter);
                adapter.notifyDataSetChanged();

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
            emptyState5.setVisibility(View.VISIBLE);
            recyclerView5.setVisibility(View.GONE);
        } else {
            emptyState5.setVisibility(View.GONE);
            recyclerView5.setVisibility(View.VISIBLE);
        }
    }
}
