package com.pamsillah.wakho;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.pamsillah.wakho.Adapters.PushNotificationsAdapter;
import com.pamsillah.wakho.Models.Notification;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Java Developer on 22/11/2017.
 */

public class PushNotifications extends AppCompatActivity {
    Toolbar toolbar;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    TextView emptyState;
    RecyclerView.Adapter adapter;
    List<Notification> notifsList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushnotifications);
        FirebaseApp.initializeApp(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PushNotifications.this, MainActivity.class));
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.rec);
        emptyState=(TextView)findViewById(R.id.txtEmptyState);
        notifsList = MyApplication.getinstance().getLstNots();
        adapter = new PushNotificationsAdapter(notifsList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PushNotifications.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //loadNotifications();
        showEmptyState(notifsList.isEmpty());
    }
    private void showEmptyState(boolean b) {
        if (b) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
