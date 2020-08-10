package com.pamsillah.wakho.Models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pamsillah.wakho.MainActivity;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.PushNotifications;
import com.pamsillah.wakho.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Created by .Net Developer on 9/25/2017.
 */

public class FirebaseNotificationServices extends Service {

    private static  String CHANNEL_ID = null;
    public FirebaseDatabase notifications =FirebaseDatabase.getInstance();

    Context context;

    static String TAG = "FirebaseService";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        FirebaseApp.initializeApp(this);
        CHANNEL_ID=getString(R.string.channel_id);
        createNotificationChannel();
     final   String userParentKey=MyApplication.getinstance().getSession().getSubscriber().getSubscriberId().toString();
        notifications.getReference().child("notifications").child(userParentKey)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Notification notification = new Notification();
                        notification = dataSnapshot.getValue(Notification.class);

                        if (notification.getStatus() == 0 && MyApplication.getinstance().getSession().getSubscriber()!=null) {
                            showNotification(getApplicationContext(), notification, dataSnapshot.getKey(),userParentKey);
                            MyApplication.getinstance().setNotification(notification);
                            MyApplication.getinstance().lstNots.add(notification);
                            List<Notification> notifications = new ArrayList<>(
                                                                    new HashSet<>( MyApplication
                                                                            .getinstance()
                                                                            .lstNots));
                            MyApplication.getinstance().setLstNots(notifications);
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

        final String userParentKeyPhone= MyApplication
                .getinstance()
                .getSession()
                .getSubscriber()
                .getPhone()
                .replace("+", "");
        notifications.getReference().child("notifications").child(String.valueOf(userParentKeyPhone))
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Notification notification = new Notification();
                        notification = dataSnapshot.getValue(Notification.class);

                        if (notification.getStatus() == 0 && MyApplication.getinstance().getSession().getSubscriber()!=null) {
                            showNotification(getApplicationContext(), notification, dataSnapshot.getKey(),userParentKeyPhone);
                            MyApplication.getinstance().setNotification(notification);
                            MyApplication.getinstance().lstNots.add(notification);
                            List<Notification> notifications = new ArrayList<>(
                                    new HashSet<>( MyApplication
                                            .getinstance()
                                            .lstNots));
                            MyApplication.getinstance().setLstNots(notifications);
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
                        Log.e("Moved", dataSnapshot.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Cancelled", databaseError.toString());
                    }
                });
        if (MyApplication.getinstance().getSession().getAgent() != null) {
           final String agentParentKey =String
                    .valueOf(MyApplication.getinstance()
                                    .getSession()
                                    .getAgent()
                                    .getAgentId());
            notifications.getReference().child("notifications").child(agentParentKey)

                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            Notification notification = new Notification();
                            notification = dataSnapshot.getValue(Notification.class);

                            if (notification.getStatus() == 0 && MyApplication.getinstance().getSession().getSubscriber()!=null) {
                                showNotification(getApplicationContext(), notification, dataSnapshot.getKey(),agentParentKey);
                                MyApplication.getinstance().setNotification(notification);
                                MyApplication.getinstance().lstNots.add(notification);
                                List<Notification> notifications = new ArrayList<>(
                                        new HashSet<>( MyApplication
                                                .getinstance()
                                                .lstNots));
                                MyApplication.getinstance().setLstNots(notifications);
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            Notification notification;
                            notification = dataSnapshot.getValue(Notification.class);
                            MyApplication.getinstance().lstNots.remove(notification);
                            List<Notification> notifications = new ArrayList<>(
                                    new HashSet<>( MyApplication
                                            .getinstance()
                                            .lstNots));
                            MyApplication.getinstance().setLstNots(notifications);

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

           final String agentParentKeyTell=String.valueOf(
                    MyApplication.getinstance().getSession().getAgent().getCompanyTel().replace("+",""));
            notifications.getReference().child("notifications").child(agentParentKeyTell)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            Notification notification = new Notification();
                            notification = dataSnapshot.getValue(Notification.class);

                            if (notification.getStatus() == 0 && MyApplication.getinstance().getSession().getSubscriber()!=null) {
                                showNotification(getApplicationContext(), notification, dataSnapshot.getKey(),agentParentKeyTell);
                                MyApplication.getinstance().setNotification(notification);
                                MyApplication.getinstance().lstNots.add(notification);
                                List<Notification> notifications = new ArrayList<>(
                                        new HashSet<>( MyApplication
                                                .getinstance()
                                                .lstNots));
                                MyApplication.getinstance().setLstNots(notifications);
                            }

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            Notification notification;
                            notification = dataSnapshot.getValue(Notification.class);
                            MyApplication.getinstance().lstNots.remove(notification);
                            List<Notification> notifications = new ArrayList<>(
                                    new HashSet<>( MyApplication
                                            .getinstance()
                                            .lstNots));
                            MyApplication.getinstance().setLstNots(notifications);

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
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showNotification(Context context, Notification notification, String notification_key,String parent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
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
        flagNotificationAsSent(notification_key,parent);
    }


    private void flagNotificationAsSent(String notification_key,String parent) {
        notifications.getReference()
                .child("notifications")
                .child(parent)
                .child(notification_key)
                .child("status")
                .setValue(1);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
