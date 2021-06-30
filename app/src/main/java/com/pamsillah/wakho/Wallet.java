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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by .Net Developer on 10/24/2017.
 */

public class Wallet extends AppCompatActivity {
    Toolbar toolbar;
    EditText wbankName, wacc;
    TextView wtotal, wpending, wdrawable;
    Button update, pay;
    Agent agent;
    Subscriber subscriber;
    com.pamsillah.wakho.Models.Wallet wallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);
        update = (Button) findViewById(R.id.update);
        pay = (Button) findViewById(R.id.pay);
        wtotal = (TextView) findViewById(R.id.wtotal);
        wpending = (TextView) findViewById(R.id.wpending);
        wdrawable = (TextView) findViewById(R.id.wdrawable);
        wbankName = (EditText) findViewById(R.id.wbankName);
        wacc = (EditText) findViewById(R.id.wacc);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        subscriber = MyApplication.getinstance().getSession().getSubscriber();
        agent = MyApplication.getinstance().getSession().getAgent();
        wallet = MyApplication.getinstance().getSession().getWallet();
        progressDialog = new ProgressDialog(this);
if(wallet ==null)
    wallet=new com.pamsillah.wakho.Models.Wallet();
//Set TEXT
        wbankName.setText(agent.getBankName());
        wacc.setText(agent.getAccNumber());
        wdrawable.setText(wallet.getDrawable());
        wtotal.setText(wallet.getTotal());
        wpending.setText(wallet.getOveral());
        toolbar.setSubtitle(agent.getCompanyRegNumber());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update.getText().toString().contains("Update Bank Detls")) {
                    update.setTextColor(Color.BLACK);
                    update.setText("  Save Changes  ");

                    wacc.setEnabled(true);
                    wbankName.setEnabled(true);
                    Toast.makeText(Wallet.this, "Edit mode enabled", Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog dialog = new Dialog(Wallet.this);
                    dialog.setContentView(R.layout.layout_accept_dialog);
                    dialog.setTitle("Confirmation");
                    TextView dialogtxt = (TextView) dialog.findViewById(R.id.textView);
                    dialogtxt.setTextColor(Color.BLACK);
                    dialogtxt.setText("Changes Will be saved.");
                    ImageView image = (ImageView) dialog.findViewById(R.id.image);
                    image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_green_check));

                    final JSONObject obj = new JSONObject();

                    try {

                        obj.accumulate("AgentId", agent.getAgentId());
                        obj.accumulate("SubscriberId", agent.getSubscriberId());
                        obj.accumulate("CompanyName", agent.getCompanyName());
                        obj.accumulate("CompanyAdress", agent.getCompanyAdress());
                        obj.accumulate("Bank_Adress", agent.getCompanyRegNumber());
                        obj.accumulate("CompanyTel", agent.getCompanyTel());
                        obj.accumulate("CompanyLogo", agent.getCompanyLogo());
                        obj.accumulate("IDpic", agent.getIDpic());
                        obj.accumulate("ProofRes", agent.getProofRes());
                        obj.accumulate("BankName", wbankName.getText().toString());
                        obj.accumulate("Account_Number", wacc.getText().toString());
                        agent.setBankName(wbankName.getText().toString());
                        agent.setAccNumber(wacc.getText().toString());

                    } catch (JSONException e) {
                        Log.e("JSonError", "at JSON OBJECT CREATE");
                    }

                    //adding button click event
                    Button dismissButton = (Button) dialog.findViewById(R.id.button);
                    dismissButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            postAgentDetails(obj);
                            dialog.dismiss();
                        }
                    });


                    dialog.show();

                }
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentRequestService.requestPayment(agent.AgentId);

                final Dialog dialog = new Dialog(Wallet.this);
                dialog.setContentView(R.layout.layout_accept_dialog);
                dialog.setTitle("Confirmation");
                TextView dialogtxt = (TextView) dialog.findViewById(R.id.textView);
                dialogtxt.setTextColor(Color.BLACK);
                dialogtxt.setText("Your Request was sent to the Administrator .");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_green_check));
                Button dismissButton = (Button) dialog.findViewById(R.id.button);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Firebase mRef = new Firebase("https://wakhoapp.firebaseio.com/PayRequests/" + MyApplication.getinstance().session.getSubscriber().getSubscriberId());
                        mRef.child("Message").setValue(subscriber.getName() + " Have requested payment for the job done" + "Here are this Agent Details Agent ID :" + agent.getAgentId() + " Drawable Amount : " + wallet.getDrawable() + ". Thank you");
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });
    }


    ProgressDialog progressDialog;

    private void postAgentDetails(final JSONObject obj) {
        progressDialog.setMessage("Updating");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.setTitle("Bank Details Update");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                com.pamsillah.wakho.app_settings.ConnectionConfig.BASE_URL + "/api/agents/" + agent.getAgentId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("succeeded")) {
                    MyApplication.getinstance().setAgent(agent);
                    progressDialog.cancel();
                    Toast.makeText(Wallet.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Wallet.this, Wallet.class));
                } else {
                    progressDialog.cancel();
                    Toast.makeText(Wallet.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().contains("timeout")) {
                    progressDialog.cancel();
                    Toast.makeText(Wallet.this, "Waiting for delivery", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Wallet.this, Wallet.class));
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


}
