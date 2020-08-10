package com.pamsillah.wakho;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.ReadContacts;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Models.Wallet;
import com.pamsillah.wakho.Parser.AgentParser;
import com.pamsillah.wakho.Parser.SubscribersParser;
import com.pamsillah.wakho.Parser.WalletParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.Utils.Session;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class SplashScreen extends AppCompatActivity {

    Session session;


    @Override
    public void onCreate(Bundle bundle) {
        subS();


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(bundle);

        setContentView(R.layout.splash_screen);


        session = new Session(SplashScreen.this);
        FirebaseApp.initializeApp(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session.getSubscriber() != null) {
                    Subscriber sub = session.getSubscriber();
                    if(sub.getSubscriberId()!=null) {
                        subS(sub.getSubscriberId());
                        searchAgent(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()));
                    }

                    if (sub.getStatus().contains("Pending")) {
                        Intent myac = new Intent(SplashScreen.this, VerificationScreen.class);
                        startActivity(myac);
                    } else {
                        Intent myac = new Intent(SplashScreen.this, ReadContacts.class);
                        startActivity(myac);
                    }

                } else {
                    Intent login = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(login);

                }
            }
        }, 4000);


    }

    public List<Agent> agentList;

    public void searchAgent(final String Id) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, DTransUrls.agents, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                agentList = AgentParser.parseFeed(response);

                if (agentList.size() > 0) {
                    for (Agent agent : agentList
                            ) {
                        if (Id.equals(agent.getSubscriberId())) {
                            MyApplication.getinstance().getSession().setAgent(agent);
                            getWallet(agent.getAgentId());
                            break;
                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());
                //progressDialog.cancel();

            }
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                return AuthHeader.getHeaders();
            }
        };


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }

    public List<Subscriber> subsList;

    public void subS() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, DTransUrls.Subscribers,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                subsList = SubscribersParser.parseFeed(response);
                MyApplication.getinstance().getSession().setSubs(subsList);
                MyApplication.getinstance().setSubscribers(subsList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());
                //progressDialog.cancel();

            }
        }) {


            @Override
            public Map<String, String> getHeaders() {
                return AuthHeader.getHeaders();
            }

        };


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }

    //GET WALLET
    Wallet wallet = null;

    public void getWallet(int id) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, DTransUrls.wallet + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                wallet = WalletParser.parseWallet(response);
                Toast.makeText(SplashScreen.this, "Wallet Retrieved", Toast.LENGTH_SHORT).show();
                MyApplication.getinstance().getSession().setWallet(wallet);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashScreen.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return AuthHeader.getHeaders();
            }
        };

        MyApplication.getinstance().addToRequestQueue(stringRequest);

    }

    public void subS(int id) {

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, DTransUrls.Subscribers + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Subscriber subscriber = SubscribersParser.parseSubscriber(response);

                MyApplication.getinstance().getSession().setSubscriber(subscriber);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());
                Toast.makeText(SplashScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                return AuthHeader.getHeaders();
            }
        };


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }
}
