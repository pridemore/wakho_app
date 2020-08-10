package com.pamsillah.wakho;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Parser.SubscribersParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.Utils.IntlPhoneInput;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by user on 01/12/2017.
 */

public class RegisterActivity extends Activity {
    EditText name, lastname, input_password, input_password2, email;
    IntlPhoneInput reg_phone;
    ProgressDialog progressDialog;
    String myname, mysurname, mypass, myrepeat, myemail, myphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering..!");
        name = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.surname);
        input_password = (EditText) findViewById(R.id.password);
        input_password2 = (EditText) findViewById(R.id.repeatPassword);
        reg_phone = (IntlPhoneInput) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        input_password.setTransformationMethod(new PasswordTransformationMethod());
        input_password2.setTransformationMethod(new PasswordTransformationMethod());

        Button btn_reg = (Button) findViewById(R.id.btnRegister);


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Subscriber subscriber = new Subscriber();
                myname = name.getText().toString();

                mysurname = lastname.getText().toString();

                mypass = input_password.getText().toString();

                myrepeat = input_password2.getText().toString();

                if (reg_phone.getNumber() != null) {
                    myphone = reg_phone.getNumber().toString();
                }
                if (email.getText() != null) {
                    myemail = email.getText().toString();
                }

                subscriber.setName(myname);
                subscriber.setSurname(mysurname);
                subscriber.setPassword(mypass);
                subscriber.setRepeatPassword(myrepeat);
                subscriber.setPhone(myphone);
                subscriber.setEmail(myemail);
                subscriber.setStatus("Pending");
                JSONObject object = new JSONObject();
                try {
                    object.accumulate("name", subscriber.getName());
                    object.accumulate("surname", subscriber.getSurname());
                    object.accumulate("password", subscriber.getPassword());
                    object.accumulate("RepeatPassword", subscriber.getRepeatPassword());
                    object.accumulate("phone", subscriber.getPhone());
                    object.accumulate("email", subscriber.getEmail());
                    object.accumulate("Status", subscriber.getStatus());
                    object.accumulate("SubscriberId", null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String check = "all good";
                if (myname != null && mypass != null && myphone != null && myrepeat != null && mysurname != null) {

                    if (mypass.equals(myrepeat)) {
                        if (mypass.length() >= 5) {

                            if (myname.length() > 2 && mysurname.length() > 2) {
                                        RegisterUser(object, subscriber);
                                    } else {
                                progressDialog.cancel();
                                        Toast.makeText(RegisterActivity.this, "Name or Surname Too Short", Toast.LENGTH_SHORT).show();
                                    }
                            } else {
                                progressDialog.cancel();
                                Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            progressDialog.cancel();
                            Toast.makeText(RegisterActivity.this, "Passwords  must have more than 4 characters", Toast.LENGTH_SHORT).show();
                        }

                    }
                 else {
                    progressDialog.cancel();
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }


    public void RegisterUser(final JSONObject subscriber, final Subscriber subscribers) {

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DTransUrls.PostSubscribers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("Suceeded")) {
                   // subS();
                    progressDialog.cancel();
                    String verificationcode = response.split(",")[2].trim();
                    verificationcode= verificationcode.substring(0,verificationcode.length()-1);
                    Integer subscriberId = Integer.parseInt(response.split(",")[1]);
                    subscribers.setVerificationCode(verificationcode);
                    subscribers.setSubscriberId(subscriberId);
                    MyApplication.getinstance().getSession().setSubscriber(subscribers);
                    Intent intent = new Intent(RegisterActivity.this, VerificationScreen.class);
                    startActivity(intent);
if(response.contains("email already exists"))
{
    Toast.makeText(RegisterActivity.this,"Your account has been created please login and finish setting up your profile", Toast.LENGTH_SHORT).show();
     intent = new Intent(RegisterActivity.this, LoginActivity.class);
    startActivity(intent);
}

                } else {
                    progressDialog.cancel();
                    Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage()!=null &&error.getMessage().contains("timeout")) {
                    progressDialog.setMessage("Network timed out , waiting for a network connection please wait ...");
                } else {
                    Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("Error: ", error.toString());
                    progressDialog.cancel();
                }
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return subscriber.toString().getBytes();
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getinstance().addToRequestQueue(stringRequest);
    }

    public List<Subscriber> subsList;

    public void subS() {


        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, DTransUrls.Subscribers,
                        null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.cancel();
                subsList = SubscribersParser.parseFeed(response);
                MyApplication.getinstance().getSession().setSubs(subsList);
                MyApplication.getinstance().setSubscribers(subsList);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());
                if (error.toString().contains("timeout")) {

                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }
        };


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }
}
