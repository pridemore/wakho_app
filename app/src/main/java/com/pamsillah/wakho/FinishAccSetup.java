package com.pamsillah.wakho;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Parser.AgentParser;
import com.pamsillah.wakho.Parser.SubscribersParser;
import com.pamsillah.wakho.Parser.WalletParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by psillah on 4/13/2017.
 */
public class FinishAccSetup extends AppCompatActivity {
    private EditText mName;
    private EditText mSurname;
    private EditText mAddress;
    private EditText mCity;
    private EditText mIDnumber;
    private EditText mModeOf;
    private EditText mBankName;
    private EditText mBanckAcc;
    private ImageView mSelectPic;
    private ImageView mIDpic, mproof;
    private Button mFinish;
    Toolbar toolbar;
    private int PIC_IMAGE_REQUEST = 1;
    int CAMERA_REQUEST = 1;
    private int PIC_IMAGE_REQUEST1 = 2;
    private int PIC_IMAGE_REQUEST2 = 3;
    Bitmap bitmap, bitmap1, bitmap2;

    JSONObject obj, wallet;
    SharedPreferences settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_setup);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Register as Agent");
        progressDialog = new ProgressDialog(this);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary));
        progressDialog.setMessage("Registering Agent...");
        progressDialog.setTitle("Agent Registration");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.setCancelable(false);

        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        settings = getSharedPreferences("Agent_prefs", Context.MODE_PRIVATE);


        mSurname = (EditText) findViewById(R.id.setupsurname);
        mAddress = (EditText) findViewById(R.id.setupadd);

        mIDnumber = (EditText) findViewById(R.id.setupidnum);
        mFinish = (Button) findViewById(R.id.finish);

        mBanckAcc = (EditText) findViewById(R.id.accnum);
        mBankName = (EditText) findViewById(R.id.bankname);
        mIDpic = (ImageView) findViewById(R.id.mSelectIDPic);
        mproof = (ImageView) findViewById(R.id.mproof);
        mproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser(PIC_IMAGE_REQUEST1);
            }
        });

        mAddress.setText(MyApplication.getinstance().getSession().getSubscriber().getAddress().replace("_", " "));
        mAddress.setEnabled(false);
        mIDpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showFileChooser(PIC_IMAGE_REQUEST2);

            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subscriber sub = MyApplication.getinstance().getSession().getSubscriber();
                String name = sub.getName();
                String surname = sub.getSurname();

                String IDnum = mIDnumber.getText().toString().trim();

                String bankName = mBankName.getText().toString().trim();
                String accNum = mBanckAcc.getText().toString().trim();

                Date datee = new Date();

                //ID
                String img1 = getStringImage(bitmap1);
                Date datee1 = new Date();
                String nam1 = name.replace(" ", "_") + "_ C" + datee.getDay() + datee.getSeconds() + datee.getYear() + datee.getMinutes() + datee.getHours() + ".png";
                //Proof of residence
                String img2 = getStringImage(bitmap2);
                Date datee2 = new Date();
                String nam2 = name.replace(" ", "_") + "_ A" + datee.getDay() + datee.getSeconds() + datee.getYear() + datee.getMinutes() + datee.getHours() + ".png";


                obj = new JSONObject();
                wallet = new JSONObject();

                JSONObject upload1 = new JSONObject();
                JSONObject upload2 = new JSONObject();

                try {

                    //ID
                    upload1.accumulate("id", "1");
                    upload1.accumulate("Name", nam1);
                    upload1.accumulate("image", img1);
                    //Proof of Residence
                    upload2.accumulate("id", "2");
                    upload2.accumulate("Name", nam2);
                    upload2.accumulate("image", img2);
                    obj.accumulate("SubscriberId", sub.getSubscriberId());
                    obj.accumulate("CompanyName", name + " " + surname);
                    obj.accumulate("CompanyAdress", sub.getAddress());
                    obj.accumulate("Bank_Adress", "Pending");
                    obj.accumulate("CompanyTel", sub.getPhone());
                    obj.accumulate("CompanyLogo", sub.getProfilePic());

                    obj.accumulate("BankName", String.valueOf(bankName));
                    obj.accumulate("Account_Number", accNum);

                    obj.accumulate("upload1", upload1);
                    obj.accumulate("upload2", upload2);

                } catch (JSONException e) {
                    Log.e("JSonError", "at JSON OBJECT CREATE");
                }
                Agent aa = null;
                if (MyApplication.getinstance().getSession().getAgent() != null) {
                    for (Agent ag : MyApplication.getinstance().getLstAgent()
                            ) {
                        if (ag.getSubscriberId().equals(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()))) {
                            aa = ag;
                            MyApplication.getinstance().getSession().setAgent(aa);
                            break;
                        }
                    }
                    if (aa != null) {
                        Toast.makeText(FinishAccSetup.this, "You cannot create two Agent accounts accounts", Toast.LENGTH_SHORT).show();
                    } else {
                        postAgentDetails(obj);
                    }

                } else {
                    postAgentDetails(obj);
                }


            }

        });


    }

    ProgressDialog progressDialog;

    private void postAgentDetails(final JSONObject obj) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConnectionConfig.BASE_URL + "/api/agents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response yacho", response);
                searchAgent(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()));
//subSearch(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId());
                progressDialog.cancel();
                startActivity(new Intent(FinishAccSetup.this, MainActivity.class));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().contains("timeout")) {
                    progressDialog.cancel();
                    searchAgent(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()));
                    startActivity(new Intent(FinishAccSetup.this, MainActivity.class));
                } else {
                    progressDialog.cancel();
                    searchAgent(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()));
                    startActivity(new Intent(FinishAccSetup.this, FinishAccSetup.class));

                }
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getinstance().addToRequestQueue(stringRequest);

    }


    private void showFileChooser(int REQ) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        // Start the Intent
        startActivityForResult(galleryIntent, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PIC_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                mproof.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PIC_IMAGE_REQUEST2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                mIDpic.setImageBitmap(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                mIDpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public List<Agent> agentList;

    public void searchAgent(final String Id) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ConnectionConfig.BASE_URL + "/api/agents", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                agentList = AgentParser.parseFeed(response);

                if (agentList.size() > 0) {
                    for (Agent agent : agentList
                            ) {
                        if (Id.equals(agent.getSubscriberId())) {
                            MyApplication.getinstance().getSession().setAgent(agent);

                            break;
                        }
                    }
                }
                try {


                    obj.accumulate("Overal", "0");
                    obj.accumulate("AgentID", String.valueOf(MyApplication.getinstance().getSession().getAgent().getAgentId()));
                    obj.accumulate("Available", "0");
                    obj.accumulate("Drawable", "0");
                    obj.accumulate("Pending", "0");
                    obj.accumulate("Total", "0");
                    createWallet(obj);

                } catch (Exception ex) {
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
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }

        };


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }

    private void createWallet(final JSONObject obj) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DTransUrls.wallets, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(FinishAccSetup.this, "Your Wallet has been created.", Toast.LENGTH_SHORT).show();
                MyApplication.getinstance().getSession().setWallet(WalletParser.parseWallet(response));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().contains("timeout")) {

                } else {
                    progressDialog.cancel();

                }
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getinstance().addToRequestQueue(stringRequest);

    }

    public void subSearch(int id) {


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

