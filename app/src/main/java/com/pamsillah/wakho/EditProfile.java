package com.pamsillah.wakho;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.pamsillah.wakho.Models.ReadContacts;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Parser.SubscribersParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by psillah on 3/31/2017.
 */
public class EditProfile extends AppCompatActivity {
    private int PIC_IMAGE_REQUEST;
    int CAMERA_REQUEST = 1;
    Bitmap bitmap = null;
    Bitmap bitmapCach = null;
    Button cancel, done;
    EditText pasword, repaatpass, email, adress, name, city, surbub, country;
    Spinner spinner;
    TextView phone;
    ImageView editpic;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    ProgressDialog progressDialog;
    Subscriber s = MyApplication.getinstance().getSession().getSubscriber();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Saving changes..!");
        progressDialog.setIcon(getResources().getDrawable(R.drawable.logo));
        setContentView(R.layout.activity_edit_profile);
        cancel = (Button) findViewById(R.id.cancel_editing);
        done = (Button) findViewById(R.id.done_editing);
        editpic = (ImageView) findViewById(R.id.editpic);

        name = (EditText) findViewById(R.id.name);
        country = (EditText) findViewById(R.id.country);

        city = (EditText) findViewById(R.id.city);
        surbub = (EditText) findViewById(R.id.surbub);
        phone = (TextView) findViewById(R.id.phone);
        adress = (EditText) findViewById(R.id.adress);
        email = (EditText) findViewById(R.id.email);
        pasword = (EditText) findViewById(R.id.password);
        repaatpass = (EditText) findViewById(R.id.repeatPassword);

        //spinner = (Spinner) findViewById(R.id.spinner);

        name.setText(s.getName() + " " + s.getSurname());


        email.setText(s.getEmail());
        phone.setText(s.getPhone());
        pasword.setText(s.getPassword());
        if (s.getAddress() != null && s.getAddress().length() > 5) {
            adress.setText(s.getAddress().split("_")[0]);
            surbub.setText(s.getAddress().split("_")[2]);
            city.setText(s.getAddress().split("_")[1]);
            country.setText(s.getAddress().split("_")[3]);

        } else {

        }

        if (s.getProfilePic() != null) {
            Glide.with(this).load(ConnectionConfig.BASE_URL + "/" + s.getProfilePic().replace("~", "")).into(editpic);
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Subscriber subscriber = s;

                subscriber.setName(name.getText().toString());
                subscriber.setSurname("");
                subscriber.setPassword(pasword.getText().toString());
                subscriber.setRepeatPassword(repaatpass.getText().toString());
                subscriber.setAddress(adress.getText().toString() + "_" + city.getText() + "_" + surbub.getText() + "_" + country.getText());
                subscriber.setEmail(email.getText().toString());
                String img = null;

                if (s.getProfilePic() != null || bitmap != null) {


                    Date datee = new Date();
                    String nam = subscriber.getName().replace(" ", "") + "_"
                            + datee.getDay() + datee.getSeconds() + datee.getYear() + datee.getMinutes() + datee.getHours() + ".png";
                    JSONObject object = new JSONObject();
                    try {
                        if (s.getProfilePic() != null && bitmap == null) {
                            object.accumulate("ProfilePic", s.getProfilePic());

                        } else if (bitmap != null) {
                            img = getStringImage(bitmap);
                        }

                        JSONObject upload = new JSONObject();
                        upload.accumulate("id", "1");
                        upload.accumulate("Name", nam);
                        upload.accumulate("image", img);


                        object.accumulate("SubscriberId", subscriber.getSubscriberId());
                        object.accumulate("VerificationCode", subscriber.getVerificationCode());
                        object.accumulate("IDNumber", subscriber.getIDNumber());
                        object.accumulate("DateRegistered", new Date().toString());
                        object.accumulate("name", subscriber.getName());
                        object.accumulate("Surname", " ");
                        object.accumulate("Address", subscriber.getAddress());
                        object.accumulate("password", subscriber.getPassword());
                        object.accumulate("RepeatPassword", subscriber.getRepeatPassword());
                        object.accumulate("phone", subscriber.getPhone());
                        object.accumulate("email", subscriber.getEmail());
                        object.accumulate("Status", "Active");
                        object.accumulate("upload", upload);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (subscriber.getName() != null && subscriber.getPassword() != null && repaatpass.getText().toString() != null) {

                        if (subscriber.getPassword().equals(subscriber.getRepeatPassword())) {
                            if (subscriber.getPassword().length() >= 5) {

                                if (subscriber.getName().length() > 2) {


                                    RegisterUser(object, subscriber);
                                } else {

                                    progressDialog.cancel();
                                    Toast.makeText(getApplicationContext(), "Name or Surname Too Short", Toast.LENGTH_SHORT).show();
                                }


                            } else {

                                progressDialog.cancel();
                                Toast.makeText(EditProfile.this, "Passwords  must have more than 4 characters", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progressDialog.cancel();
                            Toast.makeText(EditProfile.this, "Passwords did not match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.cancel();
                        Toast.makeText(EditProfile.this, "All fields are required", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    progressDialog.cancel();
                    Toast.makeText(EditProfile.this, "Profile picture cannot be null.", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                editpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                editpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {


        }
    }

    //CONVERTING BITMAP TO STRING
    public String getStringImage(Bitmap bmp) {
        if (bmp != null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
        return null;
    }

    //UPDATING AGENTS

    Agent ag = MyApplication.getinstance().getSession().getAgent();

    public void UpdateAgent(final JSONObject agent) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, ConnectionConfig.BASE_URL + "/api/Agents/" + ag.getAgentId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(EditProfile.this, "User Info Updated !", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return agent.toString().getBytes();
            }

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

    //UPDATING USER


    public void RegisterUser(final JSONObject subscriber, final Subscriber subscribers) {
         progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, ConnectionConfig.BASE_URL + "/api/Subscribers/" + subscribers.getSubscriberId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null && response.contains("Updated_")) {
                    progressDialog.cancel();
                    subscribers.setProfilePic(response.split("_")[1]);
                    subSearch(subscribers.getSubscriberId());
                    MyApplication.getinstance().getSession().setVerification(null);
                    startActivity(new Intent(EditProfile.this, ReadContacts.class));
                } else {
                    progressDialog.cancel();
                    Toast.makeText(EditProfile.this, response, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfile.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
                Log.e("Error: ", error.toString());

            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return subscriber.toString().getBytes();
            }

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

    //SEARCH  FOR SUBSCRIBER
    public void subSearch(int id) {


        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, DTransUrls.Subscribers + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Subscriber subscriber = SubscribersParser.parseSubscriber(response);

                MyApplication.getinstance().getSession().setSubscriber(subscriber);
                if (ag != null) {
                    try {
                        JSONObject object1 = new JSONObject();


                        object1.accumulate("SubscriberId", ag.getSubscriberId());
                        object1.accumulate("CompanyName", ag.getCompanyName());
                        object1.accumulate("CompanyAdress", ag.getCompanyAdress());
                        object1.accumulate("Bank_Adress", ag.getCompanyRegNumber());
                        object1.accumulate("CompanyTel", ag.getCompanyTel());
                        object1.accumulate("CompanyLogo", subscriber.getProfilePic());
                        object1.accumulate("BankName", ag.getBankName());
                        object1.accumulate("Account_Number", ag.getAccNumber());
                        object1.accumulate("AgentId", ag.getAgentId());
                        object1.accumulate("IDPic", ag.getIDpic());
                        object1.accumulate("ProofRes", ag.getProofRes());
                        UpdateAgent(object1);

                    } catch (Exception e) {

                    }
                }


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

