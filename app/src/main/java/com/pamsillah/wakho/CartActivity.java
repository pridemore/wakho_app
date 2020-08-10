package com.pamsillah.wakho;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Utils.Notifications.NotificationsSender;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

/**
 * Created by psillah on 5/15/2017.
 */
public class CartActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView seeImg, recipient, poststo, postsfrom, postspickup, postsfee, delby, txtsize, titleLA, fragile;
    Button quote, accept, button3, button6;
    Post posts = new Post();
    ImageView imageView;
    String post_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("Agent_prefs", Context.MODE_PRIVATE);
        MyApplication.getinstance().getSession().setNeg(null);
        setContentView(R.layout.activity_cart);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary));

        final Agent isAgent = MyApplication.getinstance().getSession().getAgent();
        final Subscriber isSub = MyApplication.getinstance().getSession().getSubscriber();

        delby = (TextView) findViewById(R.id.txtdelby);
        txtsize = (TextView) findViewById(R.id.txtsize);
        titleLA = (TextView) findViewById(R.id.titleLA);


        posts = MyApplication.getinstance().getPost();
        recipient = (TextView) findViewById(R.id.recname);
        postspickup = (TextView) findViewById(R.id.ppoint);
        postsfee = (TextView) findViewById(R.id.fee);
        poststo = (TextView) findViewById(R.id.txtto);
        postsfrom = (TextView) findViewById(R.id.txtfrm);
        imageView = (ImageView) findViewById(R.id.image);
        quote = (Button) findViewById(R.id.quote);
        accept = (Button) findViewById(R.id.accept);
        recipient.setText(posts.getFragility() + "/10");
        postspickup.setText(posts.getPickUpPoint());
        postsfee.setText("$ " + posts.getProposedFee());
        delby.setText(posts.getDeliveryDate());
        poststo.setText(posts.getLocationToId().replace(",", " "));
        postsfrom.setText(posts.getLocationFromId().replace(",", " "));
        txtsize.setText(posts.getWeight().replace("kgs", ""));
        titleLA.setText(posts.getTitle());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder).dontAnimate();
        Glide.with(CartActivity.this).applyDefaultRequestOptions(requestOptions)
                .load(ConnectionConfig.BASE_URL + "/" + posts.getParcelPic().replace("~", ""))
                .into(imageView);

        if (isAgent != null) {
            if (String.valueOf(isSub.getSubscriberId()).equals(posts.getSubscriberId())) {
                quote.setEnabled(false);
                accept.setEnabled(false);

                quote.setBackgroundColor(getResources().getColor(R.color.BackgroundDoveGrey));
                accept.setBackgroundColor(getResources().getColor(R.color.BackgroundDoveGrey));


            } else if (isAgent.getCompanyRegNumber().contains("Pending")) {
                quote.setEnabled(false);
                accept.setEnabled(false);

                quote.setBackgroundColor(getResources().getColor(R.color.BackgroundDoveGrey));
                accept.setBackgroundColor(getResources().getColor(R.color.BackgroundDoveGrey));

                Toast.makeText(this, "Agent not verified", Toast.LENGTH_SHORT).show();
            } else {
                quote.setEnabled(true);
                accept.setEnabled(true);
            }
        } else {

            quote.setEnabled(false);
            accept.setEnabled(false);

            quote.setBackgroundColor(getResources().getColor(R.color.BackgroundDoveGrey));
            accept.setBackgroundColor(getResources().getColor(R.color.BackgroundDoveGrey));


        }

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getinstance().setPost(posts);
                Intent i = new Intent(CartActivity.this, NegotiateActivity.class);

                startActivity(i);


            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(CartActivity.this);
                dialog.setContentView(R.layout.layout_accept_dialog);
                dialog.setTitle("Sent");

                TextView dialogtxt = (TextView) dialog.findViewById(R.id.textView);
                dialogtxt.setTextColor(Color.BLACK);
                dialogtxt.setText("The user has been notified about your interest in their post. We will let you know once they give a response");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_green_check));

                //adding button click event
                Button dismissButton = (Button) dialog.findViewById(R.id.button);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CartActivity.this, "", Toast.LENGTH_SHORT).show();
                        NotificationsSender.sendNotification(posts.getSubscriberId(),
                                posts.getPostId() + "_" + MyApplication.getinstance().getSession().getAgent().getAgentId() + "_" + posts.getSubscriberId() + "_" + MyApplication.getinstance().getSession().getSubscriber().getPhone(),
                                "Congrats Agent " + MyApplication.getinstance().getSession().getAgent().getCompanyName() + " accepted Your offer of $" + posts.getProposedFee() + " . Please proceed to payment or cancel the deal.",/*Message to be displayed on the notification*/
                                " Negotiations", /*Message title*/
                                "accept" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

                        );
                        Intent i = new Intent(CartActivity.this, MainActivity.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });


    }

}