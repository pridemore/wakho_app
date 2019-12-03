package com.pamsillah.wakho;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import com.pamsillah.wakho.Models.PostsByAgent;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by psillah on 3/24/2017.
 */
public class PostByAgent extends AppCompatActivity {
    EditText propFee, addrTo, pPoint, etPostTitle, desc;
    Spinner weight, pRecipient;
    SeekBar fragile;
    String seekValue;
    ImageView uploadPic, profilemain;
    private int PIC_IMAGE_REQUEST;
    Bitmap bitmap;
    static String pick_url;
    JSONObject obj;
    SharedPreferences settings;
    private static int RESULT_LOAD_IMG = 1;

    Calendar myCalendar = Calendar.getInstance();


    Button edit, delete, send;

    PostsByAgent agent = new PostsByAgent();

    Agent aa = MyApplication.getinstance().getSession().getAgent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postbyagent);
        agent = MyApplication.getinstance().getPostsByAgent();
        delete = (Button) findViewById(R.id.cancelBtn);
        progressDialog = new ProgressDialog(this);
        profilemain = (ImageView) findViewById(R.id.profilemain);

        edit = (Button) findViewById(R.id.quote);


        // post = MyApplication.getinstance().getPost();
        agent = MyApplication.getinstance().getPostsByAgent();
        TextView agent_name, pDepatDate, pTo, pFrom, pPicUp, weigt, ETA, frag;
        agent = MyApplication.getinstance().getPostsByAgent();
        agent_name = (TextView) findViewById(R.id.pName);
        agent_name.setText(agent.getAgent().getCompanyName());
        pDepatDate = (TextView) findViewById(R.id.pDate);
        pDepatDate.setText(agent.getDepatureDate());
        pTo = (TextView) findViewById(R.id.pTo);
        pTo.setText(agent.getLocationTo());
        pFrom = (TextView) findViewById(R.id.pFrom);
        pFrom.setText(agent.getLocationFrom());
        pPicUp = (TextView) findViewById(R.id.pPicUp);
        pPicUp.setText(agent.getPickUp());
        weigt = (TextView) findViewById(R.id.pSize);
        weigt.setText(agent.getWeight().replace("kgs", ""));
        ETA = (TextView) findViewById(R.id.txtdelby);
        ETA.setText(agent.getETA());


        Glide.with(this).load(com.pamsillah.wakho.app_settings.ConnectionConfig.BASE_URL + "/" + agent.getAgent()
                .getCompanyLogo().replace("~", "")).into(profilemain);

        frag = (TextView) findViewById(R.id.pFragile);
        frag.setText(agent.getFragility() + "/10");


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(PostByAgent.this);
                dialog.setContentView(R.layout.layout_accept_dialog);
                dialog.setTitle("DELETE JOURNEY");

                TextView dialogtxt = (TextView) dialog.findViewById(R.id.textView);
                dialogtxt.setText("You are about to delete your post , press OK to proceed or back to cancel.");


                //adding button click event
                Button dismissButton = (Button) dialog.findViewById(R.id.button);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Delete Post

                        try {

                            JSONObject post = new JSONObject();
                            post.accumulate("Id", posts.getId());


                            deletePost(post);

                        } catch (Exception e) {
                        }
                        dialog.dismiss();

                    }
                });


                dialog.show();


            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PostByAgent.this, EditParcel.class));

            }
        });


    }


    ProgressDialog progressDialog;


    final PostsByAgent posts = MyApplication.getinstance().getPostsByAgent();

    //Delete Method
    private void deletePost(final JSONObject obj) {
        progressDialog.setMessage("Deleting please wait...");
        progressDialog.setTitle("Delete Post");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, DTransUrls.PostJourney + "/" + posts.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                Toast.makeText(PostByAgent.this, "Post Deleted", Toast.LENGTH_SHORT).show();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
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
