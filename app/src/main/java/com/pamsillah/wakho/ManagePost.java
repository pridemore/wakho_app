package com.pamsillah.wakho;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONObject;

/**
 * Created by Java Developer on 14/12/2017.
 */

public class ManagePost extends AppCompatActivity {
    Toolbar toolbar;
    TextView seeImg, recipient, poststo, postsfrom, postspickup, postsfee, delby, txtsize, titleLA, fragile;
    Button edit, delete;
    Post posts = new Post();
    ImageView imageView;
    String post_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        MyApplication.getinstance().getSession().setNeg(null);
        setContentView(R.layout.manageposts);
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
        edit = (Button) findViewById(R.id.quote);
        delete = (Button) findViewById(R.id.accept);
        recipient.setText(posts.getFragility() + "/10");
        postspickup.setText(posts.getPickUpPoint());
        postsfee.setText("$ " + posts.getProposedFee());
        delby.setText(posts.getDeliveryDate());
        poststo.setText(posts.getLocationToId().replace(",", " "));
        postsfrom.setText(posts.getLocationFromId().replace(",", " "));
        txtsize.setText(posts.getWeight().replace("kgs", ""));
        titleLA.setText(posts.getTitle());

        Glide.with(ManagePost.this).load(ConnectionConfig.BASE_URL + "/" + posts.getParcelPic().replace("~", "")).into(imageView);
        edit.setEnabled(true);
        delete.setEnabled(true);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getinstance().setPost(posts);
                Intent i = new Intent(ManagePost.this, EditPost.class);

                startActivity(i);


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    final Dialog dialog = new Dialog(ManagePost.this);
                    dialog.setContentView(R.layout.layout_accept_dialog);
                    dialog.setTitle("DELETE POST");

                    TextView dialogtxt = (TextView) dialog.findViewById(R.id.textView);
                    dialogtxt.setTextColor(Color.BLACK);
                    dialogtxt.setText("You are about to delete your post , press OK to proceed or back to cancel.");

                    //adding button click event
                    Button dismissButton = (Button) dialog.findViewById(R.id.button);
                    dismissButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //Delete Post

                            try {

                                JSONObject post = new JSONObject();
                                post.accumulate("PostId", posts.getPostId());
                                post.accumulate("SubscriberId", posts.getSubscriberId());

                                post.accumulate("DeliveryDate", posts.getDeliveryDate());

                                post.accumulate("AddressTo", posts.getAddressTo().toString());

                                deletePost(post);

                            } catch (Exception e) {
                            }
                            dialog.dismiss();

                        }
                    });


                    dialog.show();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    ProgressDialog progressDialog;

    private void deletePost(final JSONObject obj) {
        progressDialog.setMessage("Deleting please wait...");
        progressDialog.setTitle("Delete Post");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, DTransUrls.PostPost + "" + posts.getPostId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                Toast.makeText(ManagePost.this, "Post Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HistoryPosts.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return obj.toString().getBytes();
            }

            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        MyApplication.getinstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


}