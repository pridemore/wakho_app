package com.pamsillah.wakho;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import com.google.common.collect.Lists;
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
    RecyclerView recyclerView;
    List<TransactionFee> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_money);
        toolbar = (Toolbar) findViewById(R.id.hismoToolbar);
        toolbar.setTitle("Money Transferes made");
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.his_money_recy);
        CardView cv = (CardView) findViewById(R.id.his_money_card);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new TransactionAdapter(data));
        fill_with_data();
    }

    public void fill_with_data() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, DTransUrls.transactions, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                List<TransactionFee> datta = new ArrayList<>();
                datta = TransactionsParser.parseFeed(response);
                Agent aget = MyApplication.getinstance().getSession().getAgent();
                Subscriber sub = MyApplication.getinstance().getSession().getSubscriber();


                if (datta.size() > 0) {

                    for (TransactionFee fee : datta
                            ) {
                        if (fee.getReff_Number() != null) {
                            String arr[] = fee.getReff_Number().replace("[", "").replace("]", "").replace("_", ":").split(":");
                            if (aget != null && arr[1].equals(String.valueOf(aget.getAgentId()))) {

                                data.add(fee);
                            } else if (arr[2].equals(String.valueOf(sub.getSubscriberId()))) {
                                data.add(fee);

                            }
                        }

                    }
                    recyclerView.setAdapter(new TransactionAdapter(Lists.reverse(data)));
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
