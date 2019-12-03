package com.pamsillah.wakho.Models;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.WebviewActivity;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONObject;

/**
 * Created by .Net Developer on 9/15/2017.
 */

public class PayPee {
    public Context context;


    public void createChat() {

        final Firebase mRef = new Firebase("https://chat-prototypes.firebaseio.com/Chats/");


        mRef.setValue(MyApplication.getinstance().getChat());


    }

    public void make(String amount)

    {

        Post posts = MyApplication.getinstance().getPost();

        JSONObject obj = new JSONObject();
        try {
            obj.accumulate("PostId", posts.getPostId());
            obj.accumulate("DeliveryDate", posts.getDeliveryDate());
            obj.accumulate("Description", posts.getDescription().trim().replace(" ",""));
            obj.accumulate("Fragility", posts.getFragility());
            obj.accumulate("LocationFromId", posts.getLocationFromId());
            obj.accumulate("LocationToId", posts.getLocationToId());
            obj.accumulate("Title", posts.getTitle());
            obj.accumulate("PickUpPoint", posts.getPickUpPoint());
            obj.accumulate("ParcelPic", posts.getParcelPic());
            obj.accumulate("Weight", posts.getWeight());
            obj.accumulate("SubscriberId", posts.getSubscriberId());
            obj.accumulate("ProposedFee", amount);
            obj.accumulate("DatePosted", posts.getDatePosted());
            obj.accumulate("TimePosted", posts.getDatePosted());

            obj.accumulate("Status", "Pay Initiated");


            addPayment(obj, String.valueOf(posts.getPostId()));


        } catch (Exception e) {

        }
        String url = "";
    }


    String resp = "";
    ProgressDialog progressDialog = new ProgressDialog(MyApplication.getinstance());


    private void addPayment(final JSONObject obj, final String id) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Preparing Payment Process...!");
        progressDialog.show();

        final Post pts = MyApplication.getinstance().getPost();

        StringRequest request = new StringRequest(Request.Method.PUT, DTransUrls.PostPost + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                resp = response;

                try {

                    JSONObject pay = new JSONObject();
                    pay.accumulate("Id", "1");
                    pay.accumulate("PostID", pts.getPostId());
                    pay.accumulate("payeeID", 37);
                    progressDialog.setMessage("Redirecting to Paynow...!");
                    initiatePayment(pay);
                    createChat();
                } catch (Exception e) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                progressDialog.cancel();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return obj.toString().getBytes();

            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        MyApplication.getinstance().addToRequestQueue(request);


    }


    void initiatePayment(final JSONObject Pay) {
        StringRequest request = new StringRequest(Request.Method.POST, ConnectionConfig.BASE_URL + "/api/Paynow", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                response = response.substring(1, response.length() - 2);

                MyApplication.getinstance().setEmail(response);
                Context context = MyApplication.getinstance();
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                progressDialog.cancel();

            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return Pay.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        MyApplication.getinstance().addToRequestQueue(request);
    }
}
