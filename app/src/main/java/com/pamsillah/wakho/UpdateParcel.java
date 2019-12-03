package com.pamsillah.wakho;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Utils.Notifications.ParcelNotificationsManager;

/**
 * Created by .Net Developer on 10/12/2017.
 */

public class UpdateParcel extends AppCompatActivity {
    Toolbar toolbar;
    Button confirm;
    TextView ttile, towner, stat, address, pickup;
    Post p = MyApplication.getinstance().getPost();

    Subscriber subscriber = MyApplication.getinstance().getSession().getSubscriber();
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_parcel);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle("Post Delivery Update");
        toolbar.setSubtitle("delivery updates.");
        confirm = findViewById(R.id.confirm);
        towner = findViewById(R.id.towner);
        stat = findViewById(R.id.stat);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.setMessage("Updating post...");
        progressDialog.setTitle("Parcel Update");
        address = findViewById(R.id.add);
        pickup = findViewById(R.id.pickup);
        ttile = findViewById(R.id.pTitle);
        final Agent thisAgent = MyApplication.getinstance().getSession().getAgent();
        MyApplication.getinstance().setPost(null);

        //Initializing texts fields
        ttile.setText("POST TITLE     :  " + p.getTitle());

        Subscriber ss = null;
        boolean isempty = true;

        for (Subscriber a : MyApplication.getinstance().getSubscribers()
                ) {
            if (String.valueOf(a.getSubscriberId()).equals(p.getSubscriberId())) {
                ss = a;
                isempty = false;

                break;
            }
        }

        if (!isempty) {

            towner.setText("PARCEL OWNER     :  " + ss.getName());
        }

        pickup.setText("PICK UP     :  " + p.getPickUpPoint());
        stat.setText(p.getStatus().split("#")[0]);
        address.setText(p.getAddressTo());

        if (p.getSubscriberId().equals
                (String.valueOf(MyApplication.getinstance()
                        .getSession().getSubscriber().getSubscriberId()))) {

            if (p.getStatus().split("#")[0].contains(subscriber.getName() )) {
                confirm.setText("Awaiting Delivery");
                confirm.setEnabled(false);
                if(p.getStatus().contains("Closed"))
                {
                    confirm.setText("Closed");
                    confirm.setEnabled(false);
                }
            } else {
                confirm.setText("HandOver");
            }

        }
        else if (thisAgent != null && p.getAgentID().equals(String.valueOf(thisAgent.getAgentId())))

        {
            if (p.getStatus().split("#")[0].contains(subscriber.getName()) |
                    p.getStatus().split("#")[0].contains("Confirmed Parcel TakeOver")) {
                confirm.setText("Deliver");

            } else if (p.getStatus().split("#")[0].contains("Handed over")) {
                confirm.setText("TakeOver");
            } else {
                confirm.setText("TakeOver");
                confirm.setEnabled(false);
            }
            if (p.getStatus().split("#")[0].contains("delivered the parcel to Recipient")) {
                confirm.setText("Waiting for Recipient's confirmation");
            }
            if (p.getStatus().split("#")[0].contains("Parcel Officially Closed")) {
                confirm.setText("Thank you , Recipient Confirmed your delivery.");
            }

        } else if (p.getStatus().contains("Closed")) {
            confirm.setText("Closed");
            confirm.setEnabled(false);

        }
        else if (p.getStatus().split("#")[0].contains("delivered the parcel to Recipient on ")) {


            confirm.setText("Confirm Delivery");
        }
        else {
            confirm.setText("Closed");
            confirm.setEnabled(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final Dialog dialog = new Dialog(UpdateParcel.this);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirm.getText().toString().contains("TakeOver"))//Take Over
                {
                    ParcelNotificationsManager.takeover(UpdateParcel.this, dialog, progressDialog, thisAgent,p);
                } else if (confirm.getText().toString().contains("HandOver") ||
                        towner.getText().toString().equals(MyApplication.getinstance()
                                .getSession().getSubscriber().getName()))//PARCEL OWNER
                {
                    ParcelNotificationsManager.handOver(UpdateParcel.this, progressDialog,thisAgent,p,subscriber);
                } else if (confirm.getText().toString().equals("Deliver")) {
                    ParcelNotificationsManager.deliver(UpdateParcel.this, progressDialog, thisAgent,p);

                } else if (confirm.getText().toString().equals("Confirm Delivery")) {
                    ParcelNotificationsManager.conFirmDelivery(UpdateParcel.this, progressDialog,thisAgent,p,subscriber);

                } else {
                    Toast.makeText(UpdateParcel.this, "" + confirm.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
