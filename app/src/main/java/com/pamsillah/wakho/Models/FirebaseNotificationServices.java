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

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.pamsillah.wakho.MainActivity;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.PushNotifications;
import com.pamsillah.wakho.R;

import java.util.Objects;

/**
 * Created by .Net Developer on 9/25/2017.
 */

public class FirebaseNotificationServices extends Service {

    public FirebaseDatabase notifications;

    Context context;
    static String TAG = "FirebaseService";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        FirebaseApp.initializeApp(this);
        notifications = FirebaseDatabase.getInstance();

        notifications.getReference().child("notifications").child(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()))
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Notification notification = new Notification();
                        notification = dataSnapshot.getValue(Notification.class);

                        if (notification.getStatus() == 0) {
                            showNotification(getApplicationContext(), notification, dataSnapshot.getKey());
                            MyApplication.getinstance().setNotification(notification);
                            MyApplication.getinstance().lstNots.add(notification);
                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Notification notification = new Notification();
                        notification = dataSnapshot.getValue(Notification.class);

                        if (notification.getStatus() == 0) {
                            showNotification(getApplicationContext(), notification, dataSnapshot.getKey());
                            MyApplication.getinstance().setNotification(notification);
                            MyApplication.getinstance().lstNots.add(notification);
                        }

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
        if (MyApplication.getinstance().getSession().getAgent() != null) {
            notifications.getReference().child("notifications").child(String.valueOf(MyApplication.getinstance().getSession().getAgent().getAgentId()))
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            Notification notification = new Notification();
                            notification = dataSnapshot.getValue(Notification.class);

                            if (notification.getStatus() == 0) {
                                showNotification(getApplicationContext(), notification, dataSnapshot.getKey());
                                MyApplication.getinstance().setNotification(notification);
                                MyApplication.getinstance().lstNots.add(notification);
                            }
                            flagNotificationAsSent(dataSnapshot.getKey());

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Notification notification = new Notification();
                            notification = dataSnapshot.getValue(Notification.class);

                            if (notification.getStatus() == 0) {
                                showNotification(getApplicationContext(), notification, dataSnapshot.getKey());
                                MyApplication.getinstance().setNotification(notification);
                                MyApplication.getinstance().lstNots.add(notification);
                            }


                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            Notification notification;
                            notification = dataSnapshot.getValue(Notification.class);
                            MyApplication.getinstance().lstNots.remove(notification);

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

            notifications.getReference().child("notifications").child(String.valueOf(MyApplication.getinstance().getSession().getAgent().getCompanyTel().replace("+","263")))
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            Notification notification = new Notification();
                            notification = dataSnapshot.getValue(Notification.class);

                            if (notification.getStatus() == 0) {
                                showNotification(getApplicationContext(), notification, dataSnapshot.getKey());
                                MyApplication.getinstance().setNotification(notification);
                                MyApplication.getinstance().lstNots.add(notification);
                            }
                            flagNotificationAsSent(dataSnapshot.getKey());

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Notification notification = new Notification();
                            notification = dataSnapshot.getValue(Notification.class);

                            if (notification.getStatus() == 0) {
                                showNotification(getApplicationContext(), notification, dataSnapshot.getKey());
                                MyApplication.getinstance().setNotification(notification);
                                MyApplication.getinstance().lstNots.add(notification);
                            }


                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            Notification notification;
                            notification = dataSnapshot.getValue(Notification.class);
                            MyApplication.getinstance().lstNots.remove(notification);

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

        notifications.getReference().child("notifications").child(String.valueOf(
                MyApplication.getinstance().getSession().getSubscriber().getPhone().replace("+", "")))
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Notification notification = new Notification();
                        notification = dataSnapshot.getValue(Notification.class);

                        if (notification.getStatus() == 0) {
                            showNotification(getApplicationContext(), notification, dataSnapshot.getKey());
                            MyApplication.getinstance().setNotification(notification);
                            MyApplication.getinstance().lstNots.add(notification);
                        }
                        flagNotificationAsSent(dataSnapshot.getKey());

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Notification notification;
                        notification = dataSnapshot.getValue(Notification.class);

                        if (notification.getStatus() == 0) {
                            showNotification(getApplicationContext(), notification, dataSnapshot.getKey());
                            MyApplication.getinstance().setNotification(notification);
                            MyApplication.getinstance().lstNots.add(notification);
                        }


                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Notification notification;
                        notification = dataSnapshot.getValue(Notification.class);
                        MyApplication.getinstance().lstNots.remove(notification);

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
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle(notification.getDescription())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentText(Html.fromHtml(notification.getMessage()))
                .setAutoCancel(true);

        Intent backIntent = new Intent(context, PushNotifications.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent intent;
        intent = new Intent(context, PushNotifications.class);
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

    private void flagNotificationAsSent(String notification_key) {
        notifications.getReference().child("notifications").child(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()))
                .child(notification_key)
                .child("status")
                .setValue(1);

        notifications.getReference().child("notifications").child(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getPhone().replace("+","")))
                .child(notification_key)
                .child("status")
                .setValue(1);

        if(MyApplication.getinstance().getSession().getAgent()!=null){

            notifications.getReference().child("notifications").child(String.valueOf(MyApplication.getinstance().getSession().getAgent().getAgentId()))
                    .child(notification_key)
                    .child("status")
                    .setValue(1);
            notifications.getReference().child("notifications").child(String.valueOf(MyApplication.getinstance().getSession().getAgent().getCompanyTel().replace("+","")))
                    .child(notification_key)
                    .child("status")
                    .setValue(1);
        }


    }



}
