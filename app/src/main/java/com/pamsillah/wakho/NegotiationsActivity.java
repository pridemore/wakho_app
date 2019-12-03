package com.pamsillah.wakho;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.pamsillah.wakho.Adapters.NegListAdapter;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.NegModel;
import com.pamsillah.wakho.Models.NegotiationsRoot;
import com.pamsillah.wakho.Models.Notifications;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by .Net Developer on 8/31/2017.
 */

public class NegotiationsActivity extends AppCompatActivity {
    Toolbar toolbar;
    NegListAdapter adapter;
    ArrayList<NegModel> listModel = new ArrayList<>();
    RecyclerView negRecycler;
    Firebase mRef;
    List<NegotiationsRoot> negList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.negotiationsactivity_layout);
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://wakhoapp.firebaseio.com//Negotiations/");

        negRecycler = (RecyclerView) findViewById(R.id.negRecycler);
        toolbar = (Toolbar) findViewById(R.id.barng);
        Notifications notification = MyApplication.getinstance().getNotifications();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String mess, date;
                final String negotiation = dataSnapshot.getKey();
                date = "";

                final NegotiationsRoot neg = new NegotiationsRoot();

                List<Subscriber> subs = new ArrayList<Subscriber>();
                subs = MyApplication.getinstance().getSubscribers();
                Agent agent = MyApplication.getinstance().getSession().getAgent();
                Subscriber subscriber = MyApplication.getinstance().getSession().getSubscriber();
                List<Post> lstPost = MyApplication.getinstance().getListPost();
                List<Agent> lstAgents = MyApplication.getinstance().getLstAgent();
                String arr[] = negotiation.split("_");
                if (agent != null && (agent.getSubscriberId() != (arr[2]))) {
                    if (arr[1].equals(String.valueOf(agent.getAgentId()))) {
                        for (Post p : lstPost) {
                            if (String.valueOf(p.getPostId()).equals(arr[0]) && p.getSubscriberId() != arr[2]) {
                                Post post = p;
                                neg.setNegotiatinName(post.getTitle());
                                neg.setPic(post.getParcelPic());


                                for (Subscriber sub : subs
                                        ) {
                                    if (String.valueOf(sub.getSubscriberId()).equals(arr[2])) {

                                        neg.setLast(sub.getName() + " " + sub.getSurname());
                                        neg.setDate(post.getDeliveryDate());
                                        neg.setID(dataSnapshot.getKey());
                                        negList.add(neg);
                                        break;
                                    }


                                }


                            }

                        }

                    } else if (arr[2].contains(String.valueOf(subscriber.getSubscriberId()))) {
                        for (Agent p : lstAgents) {
                            if ((String.valueOf(p.getAgentId())).equals(arr[1])) {
                                Agent post = p;
                                neg.setNegotiatinName(post.getCompanyName());
                                neg.setPic(post.getCompanyLogo());
                                negList.add(neg);
                                for (Post pp : lstPost
                                        ) {
                                    if (String.valueOf(pp.getPostId()).equals(arr[0])) {

                                        neg.setLast(pp.getTitle());
                                        neg.setDate(pp.getDeliveryDate());
                                        break;
                                    }
                                }
                                break;
                            }

                        }
                        neg.setID(dataSnapshot.getKey());
                    }
                } else if (String.valueOf(subscriber.getSubscriberId()).equals(arr[2])) {

                    for (Agent post : lstAgents
                            ) {
                        if (String.valueOf(post.getAgentId()).equals(arr[1])) {

                            neg.setNegotiatinName(post.getCompanyName());
                            neg.setPic(post.getCompanyLogo());
                            neg.setID(dataSnapshot.getKey());
                            for (Post pp : lstPost
                                    ) {

                                if (pp.getPostId() == Integer.parseInt(arr[0])) {

                                    neg.setLast(pp.getTitle());
                                    neg.setDate(pp.getDeliveryDate());
                                    break;
                                }


                            }
                            negList.add(neg);
                            break;
                        }
                    }

                }

                negRecycler = (RecyclerView) findViewById(R.id.negRecycler);
                CardView cv = (CardView) findViewById(R.id.root_card);
                negRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                negRecycler.setItemAnimator(new DefaultItemAnimator());
                adapter = new NegListAdapter(negList, getApplicationContext());
                negRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
