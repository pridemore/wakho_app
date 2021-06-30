package com.pamsillah.wakho;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.pamsillah.wakho.Models.Chat;
import com.pamsillah.wakho.Models.Message;
import com.pamsillah.wakho.Models.Notification;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by .Net Developer on 9/27/2017.
 */

public class PayInit extends AppCompatActivity {
    TextView message, negTitle;
    Button proceed, cancel;
    ProgressDialog progressDialog;
    Notification notification = MyApplication.getinstance().getNotification();

    List<Post> lstpost = MyApplication.getinstance().getListPost();

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payinit);
        FirebaseApp.initializeApp(this);
        proceed = (Button) findViewById(R.id.proceed);
        cancel = (Button) findViewById(R.id.cancel);
        message = (TextView) findViewById(R.id.message);
        negTitle = (TextView) findViewById(R.id.n);
        progressDialog = new ProgressDialog(PayInit.this);
        message.setText(notification.getMessage());
        Post post1=MyApplication.getinstance().getPost();

        for (Post post : MyApplication.getinstance().getListPost()
                ) {
            if (String.valueOf(post.getPostId()).equals(notification.getConverid().split("_")[0].trim())) {
                MyApplication.getinstance().setPost(post);
                negTitle.setText(post.getTitle().toUpperCase() );
                break;
            }

        }

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = message.getText().toString().split(" ")[10];

                if (amount.contains("$")) {

                    amount = amount.replace("$", "");
                } else {
                    amount = message.getText().toString().split(" ")[7];

                    amount = amount.replace("$", "");


                }


                progressDialog.setCancelable(false);
                progressDialog.setMessage("Preparing Payment Process...!");
                progressDialog.show();

                //pay
                final Post posts = MyApplication.getinstance().getPost();

                JSONObject obj = new JSONObject();
                try {
                    obj.accumulate("PostId", posts.getPostId());
                    obj.accumulate("DeliveryDate", posts.getDeliveryDate());
                    obj.accumulate("Description", posts.getDescription().replace(" ","").trim());;
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
                    obj.accumulate("AddressTo", posts.getAddressTo());
                    obj.accumulate("TimePosted", posts.getTimePosted());
                    obj.accumulate("AgentID", notification.getConverid().split("_")[1]);
                    obj.accumulate("Status", "Pay Initiated");

                    // MyApplication.getinstance().setChat(chat);

                    //Initiate pay
                    final JSONObject object = obj;

                    final Post pts = posts;
                    pts.setProposedFee(amount);

                    StringRequest request = new StringRequest(Request.Method.PUT, DTransUrls.PostPost + posts.getPostId(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            resp = response;

                            try {

                                JSONObject pay = new JSONObject();
                                pay.accumulate("Id", "1");
                                pay.accumulate("PostID", pts.getPostId());
                                pay.accumulate("payeeID", notification.getConverid().split("_")[1]);
                                progressDialog.setMessage("Initiating  Payment. Please wait...!");
                                final JSONObject object1 = pay;
                                //Create Cht
                                String ID = MyApplication.getinstance().getSession().getSubscriber().getPhone() + "_" + notification.getConverid().split("_")[3];
                                createChat(ID);

                                //Sending data to paynow

                                StringRequest request = new StringRequest(Request.Method.POST,
                                        com.pamsillah.wakho.app_settings.ConnectionConfig.BASE_URL + "/api/Paynow", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        MyApplication.getinstance().setPost(pts);

                                        progressDialog.setMessage("Please wait for paynow response");

                                        response = response.substring(1, response.length() - 2);

                                        MyApplication.getinstance().setEmail(response);
                                        Context context = MyApplication.getinstance();

                                        //Start web activity
                                        Intent intent = new Intent(context, WebviewActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        progressDialog.cancel();
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
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        return AuthHeader.getHeaders();
                                    }

                                    @Override
                                    public byte[] getBody() throws AuthFailureError {
                                        return object1.toString().getBytes();
                                    }

                                    @Override
                                    public String getBodyContentType() {
                                        return "application/json";
                                    }
                                };
                                MyApplication.getinstance().addToRequestQueue(request);
                            } catch (Exception e) {
                                Log.d("Error ", "onResponse: " + e.getMessage());
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
                            return object.toString().getBytes();

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
                    MyApplication.getinstance().addToRequestQueue(request);


                } catch (Exception e) {

                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PayInit.this, MainActivity.class));
            }
        });
    }

//    public Post getPost()
//    {
//        Post post=null;
//        for (Post p:lstpost
//             ) {
//            if(notification.getConverid().split("_")[0].equals(p.getPostId()))
//            {
//                post=p;
//
//            }
//
//
//        }
//
//        return post;
//    }


    String resp = "";


    public void createChat(String ID) {
        Chat chat = new Chat();
        chat.setDateCreated(new Date().toString());
        chat.setCreatorID(MyApplication.getinstance().getSession().getSubscriber().getPhone());
        chat.setLocation(Locale.getDefault().getCountry());
        chat.setRecipient(notification.getConverid().split("_")[3]);
        Message m = new Message();
        m.setDateSend(new Date().toString());
        m.setId(MyApplication.getinstance().getSession().getSubscriber().getPhone());
        m.setMessageFormat("Text");
        m.setRecipient(notification.getConverid().split("_")[3]);
        m.setSender(MyApplication.getinstance().getSession().getSubscriber().getName());
        m.setStatus("post_chat");
        m.setTimeSend("");
        m.setMessage("Hello there,lets Chat. ");
        List<Message> lst = new ArrayList<>();
        lst.add(m);
        chat.message = lst;

        final Firebase mRef = new Firebase("https://wakhoapp.firebaseio.com//Conversations/" + ID + "/");


        mRef.setValue(chat);


    }
}
