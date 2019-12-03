package com.pamsillah.wakho;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.FirebaseApp;
import com.pamsillah.wakho.Adapters.SupportAdapter;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Models.SupportModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Java Developer on 14/12/2017.
 */

public class Support extends AppCompatActivity {
    Toolbar toolbar;
    ImageView mSendNeg;
    EditText mTypeNeg;
    SupportModel negModel = new SupportModel();
    String child;
    String amount;
    Post posts = new Post();
    List<SupportModel> negsList = new ArrayList<>();
    RecyclerView neg_rv;
    public Context context;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        FirebaseApp.initializeApp(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary));
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mSendNeg = findViewById(R.id.btn_send_neg);
        mTypeNeg = findViewById(R.id.msg_type_neg);
        toolbar.setSubtitle("Contact Support");
        toolbar.setTitle("Wakho Support");


        neg_rv = findViewById(R.id.list_msg_neg);
        neg_rv.setLayoutManager(new LinearLayoutManager(this));
        final Subscriber subscriber = MyApplication.getinstance().getSession().getSubscriber();

        child = subscriber.getPhone();


        final Firebase mRef = new Firebase("https://wakhoapp.firebaseio.com/Support").child(child);

        mSendNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy @ h:mm");

                String date = sdf.format(new Date());

                negModel.setMessage(mTypeNeg.getText().toString());
                negModel.setImage(subscriber.getProfilePic().replace("~", ""));
                negModel.setDate(date);
                negModel.setSenderName(subscriber.getName());
                negModel.setID(subscriber.getPhone());

                mRef.push().setValue(negModel);

                mTypeNeg.setText("");
            }
        });


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                negsList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SupportModel neg = ds.getValue(SupportModel.class);
                    negsList.add(neg);
                }

                SupportAdapter adapter = new SupportAdapter(negsList, getApplicationContext());
                neg_rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
