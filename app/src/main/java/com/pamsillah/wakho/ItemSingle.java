package com.pamsillah.wakho;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.bumptech.glide.request.RequestOptions;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.PostsByAgent;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.Utils.Notifications.NotificationsSender;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by psillah on 3/24/2017.
 */
public class ItemSingle extends AppCompatActivity {
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


    Button hireBtn, cancel, send;

    PostsByAgent agent = new PostsByAgent();

    Agent aa = MyApplication.getinstance().getSession().getAgent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);
        agent = MyApplication.getinstance().getPostsByAgent();
        cancel = (Button) findViewById(R.id.cancelBtn);
        progressDialog = new ProgressDialog(this);
        profilemain = (ImageView) findViewById(R.id.profilemain);

        hireBtn = (Button) findViewById(R.id.quote);


        // post = MyApplication.getinstance().getPost();

        TextView agent_name, pDepatDate, pTo, pFrom, pPicUp, weigt, ETA, frag;

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


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        Glide.with(this).applyDefaultRequestOptions(requestOptions)
                .load(ConnectionConfig.BASE_URL + "/" + agent.getAgent().getCompanyLogo().replace("~", "")).into(profilemain);

        frag = (TextView) findViewById(R.id.pFragile);
        frag.setText(agent.getFragility() + "/10");
        if ((agent != null) && (aa != null) && (agent.getAgentId().equals(String.valueOf(aa.getAgentId())))) {
            hireBtn.setBackgroundColor(getResources().getColor(R.color.BackgroundDoveGrey));
            cancel.setBackgroundColor(getResources().getColor(R.color.BackgroundDoveGrey));

        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        hireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((agent != null) && (aa != null) && (agent.getAgentId().equals(String.valueOf(aa.getAgentId())))) {
                    Toast.makeText(MyApplication.getinstance(), "You cannot hire yourself", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ItemSingle.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.popup, null);
                    alertDialog.setView(convertView);
                    alertDialog.setTitle("Post Parcel");
                    alertDialog.setIcon(R.drawable.logo);


                    send = (Button) convertView.findViewById(R.id.send);

//
//
                    weight = (Spinner) convertView.findViewById(R.id.size_spinner);


                    fragile = (SeekBar) convertView.findViewById(R.id.fragility);
                    propFee = (EditText) convertView.findViewById(R.id.etPropFee);
                    etPostTitle = (EditText) convertView.findViewById(R.id.etPostTitle);
                    uploadPic = (ImageView) convertView.findViewById(R.id.uploadPic);
                    addrTo = (EditText) convertView.findViewById(R.id.etAddrTo);

                    pPoint = (EditText) convertView.findViewById(R.id.etPpoint);
                    pRecipient = (Spinner) convertView.findViewById(R.id.etRecipient);

                    desc = (EditText) convertView.findViewById(R.id.desc);
                    List<String> arr = new ArrayList<String>();

                    arr = MyApplication.getinstance().getContacts();

                    //creating an array adapter using string array and default spinner layout. this id for the searchable spinner
                    ArrayAdapter<String> ad = new ArrayAdapter<String>(ItemSingle.this, android.R.layout.simple_spinner_dropdown_item, arr);
                    ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    pRecipient.setAdapter(ad);


                    fragile.setMax(10);
                    fragile.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            seekValue = String.valueOf(i);
                            Toast.makeText(ItemSingle.this, seekValue, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                    uploadPic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showFileChooser();
                        }
                    });
                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, month);
                            myCalendar.set(Calendar.DAY_OF_MONTH, day);

                            String myFormat = "dd/MM/yy";
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                            //deliverBy.setText(sdf.format(myCalendar.getTime()));
                        }
                    };

//                deliverBy.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        DatePickerDialog dialog = new DatePickerDialog(ItemSingle.this, date, myCalendar.get(Calendar.YEAR),
//                                myCalendar.get(Calendar.MONTH),
//                                myCalendar.get(Calendar.DAY_OF_MONTH));
//                        dialog.show();
//                    }
//                });

                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendData();
                        }
                    });


                    alertDialog.show();

                }

            }
        });


    }


    private void showFileChooser() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PIC_IMAGE_REQUEST);
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
                uploadPic.setImageBitmap(bitmap);
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

    ProgressDialog progressDialog;


    private void postPostDetails(final JSONObject obj) {
        progressDialog.setMessage("Posting...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DTransUrls.PostPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ItemSingle.this, "Hire Request Sent.", Toast.LENGTH_SHORT).show();
                NotificationsSender.sendNotification(
                        agent.getAgentId(), "", /*who the notification is meant for*/
                        "New Hire Request",/*Message to be displayed on the notification*/
                        "Hire Request", /*Message title*/
                        "hire" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

                );
                progressDialog.cancel();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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


    public void sendData() {
        progressDialog.setCancelable(false);
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.setTitle("Posting Parcel");
        progressDialog.setMessage("Preparing your post please wait...");
        progressDialog.show();

        Date datee = new Date();
        String nam = "Post_" + datee.getDay() + datee.getSeconds() + datee.getYear() + datee.getMinutes() + datee.getHours() + ".png";

        JSONObject post = new JSONObject();
        try {

            if (bitmap != null) {
                String img = getStringImage(bitmap);
                if (!TextUtils.isEmpty(img) && !TextUtils.isEmpty(pPoint.getText().toString())
                        && !TextUtils.isEmpty(addrTo.getText().toString()) && !TextUtils.isEmpty(propFee.getText().toString())
                        && !TextUtils.isEmpty(desc.getText().toString())) {


                    JSONObject upload = new JSONObject();
                    upload.accumulate("id", "1");
                    upload.accumulate("Name", nam);
                    upload.accumulate("image", img);
                    post.accumulate("DatePosted", new Date().toString());
                    post.accumulate("PostId", 0);
                    post.accumulate("SubscriberId", MyApplication.getinstance().getSession().getSubscriber().getSubscriberId());
                    post.accumulate("Title", desc.getText());
                    post.accumulate("Description", pRecipient.getSelectedItem().toString().replace(" ","").trim());
                    post.accumulate("Weight", weight.getSelectedItem());
                    post.accumulate("Fragility", seekValue);
                    post.accumulate("LocationToId", agent.getLocationTo().replace(" ", ","));
                    post.accumulate("LocationFromId", agent.getLocationFrom());
                    post.accumulate("PickUpPoint", pPoint.getText());
                    post.accumulate("ProposedFee", propFee.getText());
                    post.accumulate("DeliveryDate", agent.getETA());
                    post.accumulate("Status", "Hire Request");
                    post.accumulate("AgentID", agent.getAgentId());
                    post.accumulate("AddressTo", addrTo.getText().toString());
                    post.accumulate("upload", upload);
                    postPostDetails(post);
                } else {
                    Toast.makeText(ItemSingle.this, "All fields are required ,there are some fields which are empty.", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            } else {
                progressDialog.cancel();
                Toast.makeText(ItemSingle.this, "Pic must not be null", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            progressDialog.cancel();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Nullable
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);


            //crop to circle
            Bitmap output;
            //check if its a rectangular image
            if (bitmap.getWidth() > bitmap.getHeight()) {
                output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            } else {
                output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(output);

            float r = 0;

            if (bitmap.getWidth() > bitmap.getHeight()) {
                r = bitmap.getHeight() / 2;
            } else {
                r = bitmap.getWidth() / 2;
            }

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());


            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);

            canvas.drawCircle(r, r, r, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;


        } catch (IOException e) {
            // Log exception
            return null;
        }

    }

}
