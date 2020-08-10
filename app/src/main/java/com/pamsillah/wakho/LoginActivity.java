package com.pamsillah.wakho;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.FirebaseApp;
import org.json.JSONObject;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.FirebaseNotificationServices;
import com.pamsillah.wakho.Models.ReadContacts;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Models.Wallet;
import com.pamsillah.wakho.Parser.AgentParser;
import com.pamsillah.wakho.Parser.SubscribersParser;
import com.pamsillah.wakho.Parser.WalletParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.facebook.CallbackManager;
//import com.facebook.login.widget.LoginButton;

/**
 * Created by user on 01/04/2017.
 */


public class LoginActivity extends Activity {
    Button login;
    TextView register, fogot;
    ProgressDialog progressDialog;
    EditText pass_tv;
    EditText email_tv;
    List<Subscriber> publications = new ArrayList<>();
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        stopService(new Intent(this, FirebaseNotificationServices.class));
        super.onCreate(savedInstanceState);
        if (MyApplication.getinstance().getSession().getSubscriber() != null) {
            Intent mainAct = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainAct);

            this.finish();
        }
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...!");
        fogot = findViewById(R.id.fogot);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.linkSignup);
        email_tv = findViewById(R.id.login_email);
        pass_tv = findViewById(R.id.input_password);
        pass_tv.setTransformationMethod(new PasswordTransformationMethod());

        fogot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button send;

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView;
                convertView = inflater.inflate(R.layout.emailrec, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Password Recovery");
                alertDialog.setIcon(R.drawable.logo);

                send = (Button) convertView.findViewById(R.id.send);
                email = (EditText) convertView.findViewById(R.id.email);
                alertDialog.show();
//

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject obj = new JSONObject();
                        try {

                            obj.accumulate("email", email.getText().toString());
                            sendEmail(obj, email.getText().toString(), alertDialog);
                            email.setText("");
                        } catch (Exception x) {
                        }
                    }
                });
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog.show();
                login(new VolleyCallback() {
                    @Override
                    public void onSuccess(List<Subscriber> subscribers) {
                    }
                    @Override
                    public void onSuccess(Subscriber subscriber) {
                        Subscriber authenticatedUser = null;
                        progressDialog.cancel();
                        if (subscriber!=null) {
                            authenticatedUser = SubscribersParser.Login(subscriber);
                            MyApplication.getinstance().getSession().setSubscriber(authenticatedUser);
                        }
                        SharedPreferences sp = LoginActivity.this.getSharedPreferences("user_details", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("reg_email", MyApplication.getinstance().getSession().getSubscriber().getEmail());
                        editor.putString("reg_name", MyApplication.getinstance().getSession().getSubscriber().getName());
                        editor.putString("reg_surname", MyApplication.getinstance().getSession().getSubscriber().getSurname());
                        editor.putString("reg_password", MyApplication.getinstance().getSession().getSubscriber().getPassword());
                        editor.commit();
                        Intent main = new Intent(LoginActivity.this, ReadContacts.class);

                        MyApplication.getinstance().getSession().setSubscriber(authenticatedUser);


                        if (authenticatedUser.getStatus().equals("Active")) {
                            Toast.makeText(LoginActivity.this, "Welcome " + MyApplication.getinstance().getSession().getSubscriber().getName() + " " + MyApplication.getinstance().getSession().getSubscriber().getSurname(), Toast.LENGTH_SHORT).show();
                            searchAgent(String.valueOf(authenticatedUser.getSubscriberId()));
                            startActivity(main);

                        } else {

                            startActivity(new Intent(LoginActivity.this, VerificationScreen.class));
                        }




                    }
                    @Override
                    public void onObjectSuccess(String object) {
                    }
                }, email_tv.getText().toString(),pass_tv.getText().toString());

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reintent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(reintent);
            }
        });


    }

    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }


    public void login(final VolleyCallback callback ,String email,String password) {
        final JSONObject object = new JSONObject();
 try {
     object.accumulate("email",email);
     object.accumulate("password",password);
 }catch (Exception jsonObject)
 {

 }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DTransUrls.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Subscriber subscriber= SubscribersParser.parseSubscriber(response);
                callback.onSuccess(subscriber);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR LOGIN", "onErrorResponse: "+error);
                Toast.makeText(LoginActivity.this,"Log in failed for user",Toast.LENGTH_SHORT).show();
                progressDialog.cancel();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }

            @Override
            public byte[] getBody() {
                return object.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        MyApplication.getinstance().addToRequestQueue(stringRequest);
    }

    public void sendEmail(final JSONObject sub, String email, final AlertDialog.Builder dialog) {
        progressDialog.show();
        progressDialog.setMessage("Sending email...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConnectionConfig.BASE_URL + "/api/fogotpassword?email=" + email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("OK")) {
                    progressDialog.cancel();
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                    Toast.makeText(LoginActivity.this, "Email delivered.", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.cancel();
                Toast.makeText(LoginActivity.this, "Wrong email", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return sub.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        MyApplication.getinstance().addToRequestQueue(stringRequest);
    }

    public static List<Agent> agentList;

    public void searchAgent(final String Id) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                ConnectionConfig.BASE_URL + "/api/agents", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        agentList = AgentParser.parseFeed(response);

                        if (agentList.size() > 0) {
                            for (Agent agent : agentList
                                    ) {
                                if (Id.equals(agent.getSubscriberId())) {
                                    MyApplication.getinstance().getSession().setAgent(agent);
                                    getWallet(agent.getAgentId());

                                    Toast.makeText(MyApplication.getinstance(), MyApplication.getinstance().getSession().getAgent().getCompanyName() + " dont forget to post your journeys to make more money", Toast.LENGTH_SHORT).show();
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
            public Map<String, String> getHeaders() throws AuthFailureError {

                return AuthHeader.getHeaders();
            }
        };


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }

    Wallet wallet = null;

    public void getWallet(int id) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, DTransUrls.wallet + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                wallet = WalletParser.parseWallet(response);

                MyApplication.getinstance().getSession().setWallet(wallet);
                Toast.makeText(LoginActivity.this, "Wallet Retrieved", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }
        };

        MyApplication.getinstance().addToRequestQueue(stringRequest);

    }

}
