package com.pamsillah.wakho.Utils.Notifications;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.pamsillah.wakho.MainActivity;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.R;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONObject;

import java.util.Date;

public class ParcelNotificationsManager {



    public static void handOver(AppCompatActivity context, final ProgressDialog progressDialog,final Agent agent,final Post p,final Subscriber subscriber) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.updates, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Parcel Hand Over");
        alertDialog.setIcon(R.drawable.logo);


        final EditText desc = convertView.findViewById(R.id.desc);

        TextView dialogtxt = convertView.findViewById(R.id.textview);
        dialogtxt.setText("Thank You ," +
                " Your confirmition will be sent to your Agent. Please input your parcel description bellow");
        Button dismissButton = convertView.findViewById(R.id.send);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationsSender.sendNotification(p.getAgentID(), null, "Please confirm  takeover.",
                        " Negotiations",
                        "pending"

                );

                p.setDescription(p.getDescription() + ":" + desc.getText().toString());

                update("Subscriber " + subscriber.getName() + " " + subscriber.getSurname()
                        + " Handed over  Parcel to Agent on [" + new Date().toString() + "] .", p, progressDialog);


            }
        });


        alertDialog.show();
    }


    public static void takeover(AppCompatActivity context, final Dialog dialog, final ProgressDialog progressDialog,final Agent agent,final Post p) {


        dialog.setContentView(R.layout.layout_accept_dialog);
        dialog.setTitle("Takeover Confirmition");

        TextView dialogtxt = dialog.findViewById(R.id.textView);
        dialogtxt.setText("Thank You , your confirmition will be sent to the parcel owner and to the recipient." +
                " Bellow is your parcel description.\n\n [ " + p.getDescription().split(":")[2] + " .]");

        ImageView image = dialog.findViewById(R.id.image);
        image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_green_check));

        //adding button click event
        Button dismissButton = (Button) dialog.findViewById(R.id.button);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationsSender.sendNotification(String.valueOf(p.getSubscriberId()), null,
                        "Your Agent  for your parcel (" + p.getTitle() + ") has confirmed takeover .",
                        /*Message to be displayed on the notification*/
                        " Negotiations", /*Message title*/
                        "pending"
                        /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

                );

                NotificationsSender.sendNotification(p.getDescription().split(":")[1]
                                .replace("+", ""), null,
                        "Your Agent for your parcel (" + p.getTitle() + ") has confirmed  takeover" +
                                " .On delivery dont forget to confirm delivery by clicking" +
                                " the confirm button on the pending parcel.Thank you",
                        /*Message to be displayed on the notification*/
                        " Negotiations", /*Message title*/
                        "pending" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

                );

                update(" Agent " + agent.CompanyName + " has Confirmed Parcel TakeOver on [" + new Date().toString() + "] .", p, progressDialog);

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public static void deliver(AppCompatActivity context, final ProgressDialog progressDialog,final Agent agent,final Post p) {
        //
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.updates, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Parcel Delivery");
        alertDialog.setIcon(R.drawable.logo);


        final EditText desc = (EditText) convertView.findViewById(R.id.desc);
        desc.setText(p.getDescription().split(":")[2]);
        desc.setEnabled(false);
        TextView dialogtxt = (TextView) convertView.findViewById(R.id.textview);
        dialogtxt.setText("Thank You  " + agent.CompanyName + " " +
                "Your confirmition will  be sent to Recipient. Below is your parcel description");
        NotificationsSender.sendNotification(p.getDescription().split(":")[1]
                        .replace("+", ""), null,
                "Your agent " + agent.getCompanyName() + " has confirmed that  they have delivered your parcel " +
                        ".Please confirm on your pending parcel",/*Message to be displayed on the notification*/
                " Negotiations", /*Message title*/
                "pending" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

        );




        //adding button click event
        Button dismissButton = (Button) convertView.findViewById(R.id.send);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber= p.getDescription().split(":")[1]
                        .replace("+", "");
                if(phoneNumber.substring(0,5).contains("263"))
                {
                    NotificationsSender.sendNotification(phoneNumber, null,
                            "Your agent " + agent.getCompanyName() + " has confirmed that  they have delivered your parcel " +
                                    ".Please confirm on your pending parcel",/*Message to be displayed on the notification*/
                            " Negotiations", /*Message title*/
                            "pending" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

                    );
                }
                if(!(phoneNumber.substring(0,4).contains("263")))
                {
                    StringBuffer  stringBuffer= new StringBuffer(phoneNumber);
                    stringBuffer.replace(0,1,"263");
                    NotificationsSender.sendNotification(stringBuffer.toString(), null,
                            "Your agent " + agent.getCompanyName() + " has confirmed that  they have delivered your parcel " +
                                    ".Please confirm on your pending parcel",/*Message to be displayed on the notification*/
                            " Negotiations", /*Message title*/
                            "pending" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

                    );
                }

                update(" Agent " + agent.getCompanyName() + " delivered the parcel to Recipient on [" + new Date().toString() + "] .", p, progressDialog);
                deleteChat(p,p.getSubscriber(),agent);



            }
        });

        dismissButton.setText("Deliver");
        alertDialog.show();

    }


    public static void conFirmDelivery(AppCompatActivity context, final ProgressDialog progressDialog,final Agent agent,final Post p,final Subscriber subscriber) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.updates, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Parcel Delivery");
        alertDialog.setIcon(R.drawable.logo);


        final EditText desc = convertView.findViewById(R.id.desc);
        desc.setText(p.getDescription().split(":")[2]);
        desc.setEnabled(false);
        TextView dialogtxt = convertView.findViewById(R.id.textview);
        dialogtxt.setText("Your confirmation has been sent to Agent and Parcel Owner.");
        NotificationsSender.sendNotification(p.getAgentID(), null,
                "Recipient " + p.getDescription() + " has confirmed that  they have Received your parcel" +
                        ".Post Closed.  Request payment via your Wallet Services ",/*Message to be displayed on the notification*/
                " Negotiations", /*Message title*/
                "pending" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

        );
        NotificationsSender.sendNotification(p.getSubscriberId(), null, "Recipient " + p.getDescription() + " has confirmed that  they have Received your parcel.Post Closed.",/*Message to be displayed on the notification*/
                " Negotiations", /*Message title*/
                "pending" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

        );


        //adding button click event
        Button dismissButton = (Button) convertView.findViewById(R.id.send);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                update("Recipient received Parcel from Agent on [" + new Date().toString() + "] " +
                        ". Parcel Officially Closed no disputes will be opened for this parcel now ", p, progressDialog);


            }
        });
        dismissButton.setText("Confirm Delivery");


        alertDialog.show();
    }

    private static void update(String desc, Post p, final ProgressDialog progressDialog) {
        progressDialog.show();
        Post posts = p;

        final JSONObject obj = new JSONObject();
        try {
            obj.accumulate("PostId", posts.getPostId());
            obj.accumulate("DeliveryDate", posts.getDeliveryDate());
            obj.accumulate("Description", posts.getDescription().trim().replace(" ",""));
            obj.accumulate("Fragility", posts.getFragility());
            obj.accumulate("LocationFromId", posts.getLocationFromId());
            obj.accumulate("LocationToId", posts.getLocationToId());
            obj.accumulate("Title", posts.getTitle());
            obj.accumulate("PickUpPoint", posts.getPickUpPoint());
            obj.accumulate("ParcelPic", posts.getParcelPic());
            obj.accumulate("Weight", posts.getWeight());
            obj.accumulate("SubscriberId", posts.getSubscriberId());
            obj.accumulate("ProposedFee", posts.getProposedFee());
            obj.accumulate("DatePosted", posts.getDatePosted());
            obj.accumulate("TimePosted", posts.getDatePosted());
            obj.accumulate("AddressTo", posts.getAddressTo());
            obj.accumulate("TimePosted", posts.getTimePosted());
            obj.accumulate("AgentID", posts.getAgentID());
            obj.accumulate("Status", posts.getStatus().split("#")[0] + ". " + desc);
            StringRequest request = new StringRequest(Request.Method.PUT, DTransUrls.PostPost + posts.getPostId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.cancel();
                    Toast.makeText(MyApplication.getinstance(), "Done !", Toast.LENGTH_SHORT).show();
                    Context context = MyApplication.getinstance();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", error.toString());
                    progressDialog.cancel();


                }
            }) {
                @Override
                public byte[] getBody() {
                    return obj.toString().getBytes();
                }

                @Override
                public java.util.Map<String, String> getHeaders() {
                    return AuthHeader.getHeaders();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            MyApplication.getinstance().addToRequestQueue(request);
        } catch (Exception ex) {

        }

    }


    public static  void deleteChat(Post p,Subscriber subscriber, Agent agent){
        String negId = ""+p.getPostId()+"_"+agent.getAgentId()+"_"+subscriber.getSubscriberId();
        String chatId = subscriber.getPhone().replace("+","")
                +"_"+agent.getCompanyTel().replace("+","");
        String alternative = agent.getCompanyTel().
                replace("+","")+"_"+subscriber.getPhone()
                .replace("+","")
                ;

        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        firebase.getReference().child("Conversations").child(chatId).removeValue();
        firebase.getReference().child("Conversations").child(alternative).removeValue();
        firebase.getReference().child(("Negotiations")).child(negId).removeValue();
    }
}
