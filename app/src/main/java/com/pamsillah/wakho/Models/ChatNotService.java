package com.pamsillah.wakho.Models;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.pamsillah.wakho.ChatsFragment;
import com.pamsillah.wakho.MainActivity;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.Parser.SubscribersParser;
import com.pamsillah.wakho.R;
import com.pamsillah.wakho.Utils.DTransUrls;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by .Net Developer on 9/25/2017.
 */

public class ChatNotService extends Service {

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public FirebaseDatabase rootData = FirebaseDatabase.getInstance();

    Context context;
    static String TAG = "FirebaseService";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        loadSubs();

        rootData.getReference().child("notifications_Chat").child(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()))
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Notification notification = new Notification();
                        notification = dataSnapshot.getValue(Notification.class);

                        if (notification.getStatus() == 0) {
                            showNotification(getApplicationContext(), notification, dataSnapshot.getKey());
                        }

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
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Cancelled", databaseError.toString());
                    }
                });


        if (MyApplication.getinstance().getSession().getSubscriber().getPhone() != null) {
            mDatabase.getReference().child("notifications_Chat").child(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getPhone()))
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            Notification notification = new Notification();
                            notification = dataSnapshot.getValue(Notification.class);

                            if (notification.getStatus() == 0) {
                                showNotification(getApplicationContext(), notification, dataSnapshot.getKey());
                            }

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Log.e("Changed", dataSnapshot.toString());

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            Log.e("Removed", dataSnapshot.toString());
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            Log.e("Moved", dataSnapshot.toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Cancelled", databaseError.toString());
                        }
                    });

        }


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showNotification(Context context, Notification notification, String notification_key) {

        if (notification.getType().equals("chat")) {

            flagNotificationAsSent(notification_key);

        } else if (notification.getType().equals("out_of_chat")) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_logo)
                    .setContentTitle(notification.getDescription())
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentText(Html.fromHtml(notification.getMessage()))
                    .setAutoCancel(true);


            Intent backIntent = new Intent(context, ChatsFragment.class);
            backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Intent intent = new Intent(context, ChatsFragment.class);
            intent = new Intent(context, ChatsFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            final PendingIntent pendingIntent = PendingIntent.getActivities(context, 900,
                    new Intent[]{backIntent}, PendingIntent.FLAG_ONE_SHOT);


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);

            mBuilder.setContentIntent(pendingIntent);


            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());

            /* Update firebase set notifcation with this key to 1 so it doesnt get pulled by our notification listener*/
            flagNotificationAsSent(notification_key);

        }

    }

    private void flagNotificationAsSent(String notification_key) {
        mDatabase.getReference().child("notifications_Chat").child(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()))
                .child(notification_key)
                .child("status")
                .setValue(1);
    }


    List<Subscriber> subs = new ArrayList<>();

    public void loadSubs() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, DTransUrls.Subscribers, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                subs = SubscribersParser.parseFeed(response);
                MyApplication.getinstance().setSubscribers(subs);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());


            }
        });


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);


    }

}
