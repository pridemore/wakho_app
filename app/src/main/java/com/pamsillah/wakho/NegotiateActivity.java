package com.pamsillah.wakho;

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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pamsillah.wakho.Adapters.NegAmountsAdapter;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.NegModel;
import com.pamsillah.wakho.Models.NegotiationsRoot;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pamsillah.wakho.Utils.Notifications.NotificationsSender.sendNotification;

/**
 * Created by psillah on 6/20/2017.
 */
public class NegotiateActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView mSendNeg;
    EditText mTypeNeg;
    NegModel negModel = new NegModel();
    String child;
    String messagetime;
    String amount;
    Post posts = new Post();
    List<NegModel> negsList = new ArrayList<>();
    RecyclerView neg_rv;
    ImageView acept;
    public Context context;
    NegotiationsRoot key = new NegotiationsRoot();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negotiations);

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
        acept = findViewById(R.id.addToCart);

        key = MyApplication.getinstance().getSession().getNeg();
        MyApplication.getinstance().setEmail(null);
        neg_rv = findViewById(R.id.list_msg_neg);
        neg_rv.setLayoutManager(new LinearLayoutManager(this));
        final Subscriber subscriber = MyApplication.getinstance().getSession().getSubscriber();
        final Agent thisAg = MyApplication.getinstance().getSession().getAgent();

        String sub = "";

        if (key != null) {
            if (!key.getID().split("_")[2].equals(String.valueOf(subscriber.getSubscriberId()))) {
                sub = key.getID().split("_")[1];
            }


            toolbar.setTitle(key.getNegotiatinName());


            child = key.getID();
            // MyApplication.getinstance().getSession().setNegRoot(null);
        } else {
            NegotiationsRoot keys = new NegotiationsRoot();
            posts = MyApplication.getinstance().getPost();
            child = posts.getPostId() + "_" + thisAg.getAgentId() + "_" + posts.getSubscriberId();
            keys.setID(child);
            negsList.clear();


            toolbar.setTitle(posts.getTitle());
            toolbar.setSubtitle(posts.getTitle());
            sub = posts.getSubscriberId();
            MyApplication.getinstance().getSession().setNeg(keys);

        }
        Firebase m = new Firebase("https://wakhoapp.firebaseio.com/Status/" + sub);
        m.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                toolbar.setSubtitle(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                toolbar.setSubtitle(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                toolbar.setSubtitle(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                toolbar.setSubtitle(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        final Firebase mRef = new Firebase("https://wakhoapp.firebaseio.com/Negotiations").child(child);
        mSendNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recip = "";
                messagetime = DateFormat.getDateTimeInstance().format(new Date());
                amount = mTypeNeg.getText().toString().trim();

                if (thisAg != null && thisAg.getAgentId() == Integer.parseInt(child.split("_")[1])) {
                    negModel.setSender(thisAg.CompanyName);
                    negModel.setAgentNum(thisAg.CompanyTel);
                    recip = child.split("_")[2];
                } else if (subscriber.getSubscriberId() == Integer.parseInt(child.split("_")[2])) {
                    negModel.setSender(subscriber.getName());
                    negModel.setSubNum(subscriber.getPhone());

                    recip = child.split("_")[1];
                }


                negModel.setMessagetime(messagetime);
                negModel.setAmount(amount);


                mRef.push().setValue(negModel);
                sendNotification(recip, child, negModel.getSender()+ " Wants $"+mTypeNeg.getText(),"Fee negotiation", "negotiation");

                mTypeNeg.setText("");
            }
        });


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                negsList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    NegModel neg = ds.getValue(NegModel.class);
                    negsList.add(neg);
                }

                NegAmountsAdapter adapter = new NegAmountsAdapter(negsList, getApplicationContext());
                neg_rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


}
