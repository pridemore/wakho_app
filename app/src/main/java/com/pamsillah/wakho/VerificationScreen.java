package com.pamsillah.wakho;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.android.volley.toolbox.StringRequest;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONObject;

/**
 * Created by psillah on 4/1/2017.
 */
public class VerificationScreen extends AppCompatActivity {
    ProgressDialog progressDialog;
    EditText verify;
    Button btnverify;
    TextView linkSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying account Please wait...");
        verify = (EditText) findViewById(R.id.verify);
        linkSignup = (TextView) findViewById(R.id.linkSignup);
        final Subscriber su = MyApplication.getinstance().getSession().getSubscriber();

        btnverify = (Button) findViewById(R.id.btnverify);
        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Sending Email");
                JSONObject object = new JSONObject();
                try {
                    object.accumulate("name", su.getName());
                    object.accumulate("surname", su.getSurname());
                    object.accumulate("password", su.getPassword());
                    object.accumulate("RepeatPassword", su.getRepeatPassword());
                    object.accumulate("phone", su.getPhone());
                    object.accumulate("email", su.getEmail());
                    object.accumulate("verificationCode", su.getVerificationCode());
                    object.accumulate("Status", "Pending");
                    object.accumulate("SubscriberId", su.getSubscriberId());
                } catch (Exception e) {
                }
                sendEmail(object);


            }
        });
        verify.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {

                    if (su.getVerificationCode().trim().equals(verify.getText().toString())) {
                        progressDialog.show();
                                progressDialog.cancel();
                                startActivity(new Intent(VerificationScreen.this, EditProfile.class));
                    }
                }

            }
        });


        btnverify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (su.getVerificationCode().trim().equals(verify.getText().toString())) {
                    progressDialog.dismiss();
                            startActivity(new Intent(VerificationScreen.this, EditProfile.class));

                }

            }
        });


    }

    public void sendEmail(final JSONObject sub) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                com.pamsillah.wakho.app_settings.ConnectionConfig.BASE_URL + "/api/email", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("20")) {
                    progressDialog.cancel();

                    Toast.makeText(VerificationScreen.this, "Email delivered  Check your Email", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(VerificationScreen.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.cancel();
                Toast.makeText(VerificationScreen.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return sub.toString().getBytes();
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
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        MyApplication.getinstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(VerificationScreen.this, SplashScreen.class));

    }
}
