package com.pamsillah.wakho;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.Utils.Notifications.NotificationsSender;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONObject;

/**
 * Created by .Net Developer on 10/9/2017.
 */

public class SingleRequest extends AppCompatActivity {
    Toolbar toolbar;
    TextView recipient, poststo, postsfrom, postspickup, postsfee, delby, txtsize, titleLA, fragile;
    Button reject, accept;
    Post posts = new Post();
    ImageView imageView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.single_request);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        delby = findViewById(R.id.txtdelby);
        txtsize = findViewById(R.id.txtsize);
        titleLA = findViewById(R.id.titleLA);
        posts = MyApplication.getinstance().getPost();
        recipient = findViewById(R.id.recname);
        postspickup = findViewById(R.id.ppoint);
        postsfee = findViewById(R.id.fee);
        poststo = findViewById(R.id.txtto);
        postsfrom = findViewById(R.id.txtfrm);
        imageView = findViewById(R.id.image);
        reject = findViewById(R.id.quote);
        accept = findViewById(R.id.accept);
        recipient.setText(posts.getFragility() + "/10");
        postspickup.setText(posts.getPickUpPoint());
        postsfee.setText("$ " + posts.getProposedFee());
        delby.setText(posts.getDeliveryDate());
        poststo.setText(posts.getLocationToId().replace(",", " "));
        postsfrom.setText(posts.getLocationFromId().replace(",", " "));
        txtsize.setText(posts.getWeight().replace("kgs", ""));
        titleLA.setText(posts.getTitle());

        Glide.with(SingleRequest.this).load(ConnectionConfig.BASE_URL + "/" + posts.getParcelPic().replace("~", "")).into(imageView);


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    obj.accumulate("ProposedFee", posts.getProposedFee());
                    obj.accumulate("DatePosted", posts.getDatePosted());
                    obj.accumulate("TimePosted", posts.getDatePosted());
                    obj.accumulate("AddressTo", posts.getAddressTo());
                    obj.accumulate("agentID", null);
                    obj.accumulate("Status", "pending");


                    //Initiate pay
                    final JSONObject object = obj;

                    StringRequest request = new StringRequest(Request.Method.PUT, DTransUrls.PostPost + posts.getPostId(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            startActivity(new Intent(SingleRequest.this, MainActivity.class));


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error", error.toString());

                        }
                    }) {
                        @Override
                        public byte[] getBody() {
                            return object.toString().getBytes();

                        }

                        @Override
                        public java.util.Map<String, String> getHeaders() {
                            return AuthHeader.getHeaders();
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };
                    MyApplication.getinstance().addToRequestQueue(request);


                } catch (Exception ignored) {

                }


            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(SingleRequest.this);
                dialog.setContentView(R.layout.layout_accept_dialog);
                dialog.setTitle("Sent");

                TextView dialogtxt = dialog.findViewById(R.id.textView);
                dialogtxt.setText("The user has been notified about your interest in their post. We will let you know once they give a response");

                ImageView image = dialog.findViewById(R.id.image);
                image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_green_check));

                //adding button click event
                Button dismissButton = dialog.findViewById(R.id.button);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NotificationsSender.sendNotification(posts.getSubscriberId(), posts.getPostId() + "_"
                                        + MyApplication.getinstance().getSession().getAgent().getAgentId() + "_"
                                        + posts.getSubscriberId() + "_" + MyApplication.getinstance().getSession()
                                        .getSubscriber().getPhone(), "Congrats Agent "
                                        + MyApplication.getinstance().getSession().getAgent().
                                        getCompanyName() + " Have accepted Your offer of $" + posts.getProposedFee() + " " +
                                        ". Please proceed to payment or cancel the deal.",/*Message to be displayed on the notification*/
                                " Negotiations", /*Message title*/
                                "accept" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

                        );
                        Intent i = new Intent(SingleRequest.this, MainActivity.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });


    }

}