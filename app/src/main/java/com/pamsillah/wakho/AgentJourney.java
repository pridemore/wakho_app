package com.pamsillah.wakho;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Sir Allen on 8/13/2017.
 */

public class AgentJourney extends AppCompatActivity {
    Button mPost;
    Toolbar toolbar;
    EditText mTo, mToSurb;
    EditText mFrom;
    EditText mPickUp;
    EditText mDate, etaDate, etaTime;
    EditText mTime;
    ProgressDialog progressDialog;
    String strName, seekValue;
    String strTo, strToSurb;
    String strFrom;
    String strPickup;
    String strDate, strEtaDate, strEtaTime, strFragile, strSize;
    String url;
    Agent agent;
    SeekBar fragility;
    Spinner weight;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_journey);
        toolbar = (Toolbar) findViewById(R.id.toolbarj);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle("    Post a Journey");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Posting Please wait...!");


        mTo = (EditText) findViewById(R.id.et_agent_to);
        mToSurb = (EditText) findViewById(R.id.et_agent_toSurb);
        mFrom = (EditText) findViewById(R.id.et_agent_from);
        mPickUp = (EditText) findViewById(R.id.et_agent_pickup);
        mDate = (EditText) findViewById(R.id.et_agent_date);
        mTime = (EditText) findViewById(R.id.et_agent_time);
        weight = (Spinner) findViewById(R.id.et_weigt);
        mPost = (Button) findViewById(R.id.post_agent);
        etaTime = (EditText) findViewById(R.id.eta_time);
        etaDate = (EditText) findViewById(R.id.eta_date);
        fragility = (SeekBar) findViewById(R.id.fragility);
        url = DTransUrls.PostJourney;

        fragility.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekValue = String.valueOf(i);
                Toast.makeText(AgentJourney.this, seekValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        final String date = sdf.format(new Date());


        final DatePickerDialog.OnDateSetListener datte = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                mDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AgentJourney.this, datte, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });


        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AgentJourney.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedMinute < 10) {
                            mTime.setText(selectedHour + ":" + 0 + selectedMinute);
                        } else {
                            mTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);
                mTimePicker.show();

            }
        });

        etaTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AgentJourney.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedMinute < 10) {
                            etaTime.setText(selectedHour + ":" + 0 + selectedMinute);
                        } else {
                            etaTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);
                mTimePicker.show();
            }
        });

        final DatePickerDialog.OnDateSetListener dateEta = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etaDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        etaDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AgentJourney.this, dateEta, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });


        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                strName = "name";
                strTo = mTo.getText().toString().trim();
                strToSurb = mToSurb.getText().toString();
                strEtaDate = etaDate.getText().toString();
                strEtaTime = etaTime.getText().toString();
                strFragile = seekValue;
                strSize = weight.getSelectedItem().toString();
                strFrom = mFrom.getText().toString().trim();
                strPickup = mPickUp.getText().toString().trim();
                strDate = mDate.getText().toString().trim();
                agent = MyApplication.getinstance().getSession().getAgent();
                JSONObject object = new JSONObject();
                try {
                    object.accumulate("AgentId", agent.getAgentId());
                    object.accumulate("DatePosted", date);
                    object.accumulate("PickUp", strPickup);
                    object.accumulate("LocationFrom", strFrom);
                    object.accumulate("LocationTo", strTo + " " + strToSurb);
                    object.accumulate("DepatureDate", strDate + " @ " + mTime.getText().toString());
                    object.accumulate("ETA", strEtaDate + " @ " + strEtaTime);
                    object.accumulate("Fragility", strFragile);
                    object.accumulate("Weight", strSize);
                    object.accumulate("TransPort", agent.getCompanyName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                postAgentTrip(object);
            }
        });
    }

    private void postAgentTrip(final JSONObject object) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AgentJourney.this, "Journey Posted...!", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
                startActivity(new Intent(AgentJourney.this, MainActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                Log.e("Error: ", error.toString());
                progressDialog.cancel();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return object.toString().getBytes();
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
}