package com.pamsillah.wakho;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

/**
 * Created by Java Developer on 14/12/2017.
 */

public class EditPost extends AppCompatActivity implements VerticalStepperForm {

    private VerticalStepperFormLayout verticalStepperForm;
    EditText postTo, postFrom, propFee, addrTo, deliverBy, pPoint, deliverByTime, etPostTitle, postToSub, postFromSub;
    Spinner weight;
    Spinner recipient;
    SeekBar fragile;
    String seekValue;
    ImageView uploadPic;
    private final int PIC_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    List<String> arr;
    static String pick_url;
    JSONObject obj;
    SharedPreferences settings;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Calendar myCalendar = Calendar.getInstance();
    ProgressDialog progressDialog;
    Toolbar toolbar;
    public AppCompatActivity app;
    Post parcel = MyApplication.getinstance().getPost();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_parcel);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle("Edit Post");
        progressDialog = new ProgressDialog(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        uploadPic = (ImageView) findViewById(R.id.uploadPic);
        String[] postParcelSteps = {"Parcel Details", "Shipment Details"};
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
        app = this;


        // Finding the view
        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);
        // Setting up and initializing the form
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, postParcelSteps, this, this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true) // It is true by default, so in this case this line is not necessary
                .init();
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case 0:
                view = parcelDetailsStep();
                break;
            case 1:
                view = shipmentDetailsStep();
                break;

        }
        return view;
    }

    @Override
    public void onStepOpening(int stepNumber) {
        verticalStepperForm.setActiveStepAsCompleted();
    }

    @Override
    public void sendData() {
        Date datee = new Date();
        String nam = "Post_" + datee.getDay() + datee.getSeconds() + datee.getYear() + datee.getMinutes() + datee.getHours() + ".png";
        String img = null;
        if (bitmap != null) {

            img = getStringImage(bitmap);
        }

        JSONObject post = new JSONObject();
        try {

            JSONObject upload = new JSONObject();
            upload.accumulate("id", "1");
            upload.accumulate("Name", nam);
            upload.accumulate("image", img);
            if ((etPostTitle.getText() != null) && (recipient.getSelectedItem() != null)
                    && (postTo.getText() != null) && (postFrom.getText() != null) && (pPoint.getText() != null)
                    && (propFee.getText() != null) && (deliverBy.getText() != null) && (addrTo.getText() != null)) {
                post.accumulate("PostId", parcel.getPostId());
                post.accumulate("SubscriberId", MyApplication.getinstance().getSession().getSubscriber().getSubscriberId());
                post.accumulate("Title", etPostTitle.getText().toString());
                post.accumulate("Description", recipient.getSelectedItem().toString().replace(" ","").trim());
                post.accumulate("Weight", weight.getSelectedItem());
                post.accumulate("Fragility", seekValue);
                post.accumulate("LocationToId", postTo.getText().toString() + "," + postToSub.getText().toString());
                post.accumulate("LocationFromId", postFrom.getText().toString() + "," + postFromSub.getText().toString());
                post.accumulate("PickUpPoint", pPoint.getText().toString());
                post.accumulate("ProposedFee", propFee.getText().toString());
                post.accumulate("DeliveryDate", deliverBy.getText().toString() + " @ " + deliverByTime.getText().toString());
                post.accumulate("Status", parcel.getStatus());
                post.accumulate("DatePosted", parcel.getDatePosted());
                post.accumulate("AddressTo", addrTo.getText().toString());
                post.accumulate("TimePosted", parcel.getTimePosted());
                post.accumulate("ParcelPic", parcel.getParcelPic());
                post.accumulate("upload", upload);


                postPostDetails(post);

            } else {
                Toast.makeText(EditPost.this, "All fields are required ,there are some fields which are empty.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }

    private View parcelDetailsStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout stepper_one_layout = (LinearLayout) inflater.inflate(R.layout.stepper1, null, false);

        postTo = (EditText) stepper_one_layout.findViewById(R.id.etPostToCity);
        postFrom = (EditText) stepper_one_layout.findViewById(R.id.etPostFromCity);
        postToSub = (EditText) stepper_one_layout.findViewById(R.id.etPostToSub);
        postFromSub = (EditText) stepper_one_layout.findViewById(R.id.etPostFromSub);
        weight = (Spinner) stepper_one_layout.findViewById(R.id.size_spinner);
        fragile = (SeekBar) stepper_one_layout.findViewById(R.id.fragility);
        propFee = (EditText) stepper_one_layout.findViewById(R.id.etPropFee);
        fragile.setMax(10);


        fragile.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekValue = String.valueOf(i);
                Toast.makeText(EditPost.this, seekValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //setting text to the text boxes

        if (parcel.getLocationFromId() != null && parcel.getLocationFromId().contains(",")) {
            postFromSub.setText(parcel.getLocationFromId().split(",")[1]);
            postFrom.setText(parcel.getLocationFromId().split(",")[0]);
        } else {
            postFromSub.setText(parcel.getLocationFromId());
            postFrom.setText(parcel.getLocationFromId());

        }

        if (parcel.getLocationToId() != null && parcel.getLocationToId().contains(",")) {
            postToSub.setText(parcel.getLocationToId().split(",")[1]);

            postTo.setText(parcel.getLocationToId().split(",")[0]);


        } else {

            postToSub.setText(parcel.getLocationToId());
            postFromSub.setText(parcel.getLocationFromId());
        }

        // weight.setSelection(R.string.a);
        propFee.setText(parcel.getProposedFee());

        if (parcel.getFragility() != "null" && parcel.getFragility().length() > 0) {
            fragile.setProgress(Integer.parseInt(parcel.getFragility().trim()));
        } else {
            fragile.setProgress(0);
        }
        return stepper_one_layout;

    }

    private View shipmentDetailsStep() {

        // In this case we generate the view by inflating a XML file

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout stepper_two_layout = (LinearLayout) inflater.inflate(R.layout.stepper2, null, false);
        etPostTitle = (EditText) stepper_two_layout.findViewById(R.id.etPostTitle);
        uploadPic = (ImageView) stepper_two_layout.findViewById(R.id.uploadPic);
        addrTo = (EditText) stepper_two_layout.findViewById(R.id.etAddrTo);
        deliverBy = (EditText) stepper_two_layout.findViewById(R.id.etDeliverBy);
        pPoint = (EditText) stepper_two_layout.findViewById(R.id.etPpoint);

        recipient = (Spinner) stepper_two_layout.findViewById(R.id.etRecipient);


        deliverByTime = (EditText) stepper_two_layout.findViewById(R.id.etDeliverByTime);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                deliverBy.setText(sdf.format(myCalendar.getTime()));
            }
        };

        deliverBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditPost.this, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        deliverByTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditPost.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedMinute < 10) {
                            deliverByTime.setText(selectedHour + ":" + 0 + selectedMinute);
                        } else {
                            deliverByTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);
                mTimePicker.show();

            }
        });
        arr = new ArrayList<>();
        arr = MyApplication.getinstance().getContacts();

        //creating an array adapter using string array and default spinner layout. this id for the searchable spinner
        ArrayAdapter<String> ad = new ArrayAdapter<String>(EditPost.this, android.R.layout.simple_spinner_dropdown_item, arr);
        recipient.setAdapter(ad);
        //recipient.setSelection(2);
        if (parcel.getDeliveryDate() != null && parcel.getDeliveryDate().length() > 4) {
            deliverBy.setText(parcel.getDeliveryDate().split("@")[0].trim());
            deliverByTime.setText(parcel.getDeliveryDate().split("@")[1].trim());
        }

        pPoint.setText(parcel.getPickUpPoint());
        addrTo.setText(parcel.getAddressTo());
        etPostTitle.setText(parcel.getTitle());


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        Glide.with(this).applyDefaultRequestOptions(requestOptions)
                .load(ConnectionConfig.BASE_URL + "/" + parcel.getParcelPic().replace("~", "")).into(uploadPic);
        return stepper_two_layout;
    }


    private void showFileChooser() {
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
                uploadPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                uploadPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {


        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void postPostDetails(final JSONObject obj) {
        progressDialog.setMessage("Posting please wait...");
        progressDialog.setTitle("Edit Parcel");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, DTransUrls.PostPost + "/" + parcel.getPostId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                Toast.makeText(EditPost.this, "Succeeded..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return obj.toString().getBytes();
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




