package com.pamsillah.wakho.Utils.Notifications;

import com.firebase.client.ServerValue;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pamsillah.wakho.Models.Notification;

import java.util.HashMap;
import java.util.Map;

public final class NotificationsSender {
    public static void sendNotification(String user_id, String converid, String message, String description, String type) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/notifications").child(user_id);

        String pushKey = databaseReference.push().getKey();

        Notification notification = new Notification()

                ;
        notification.setDescription(description);
        notification.setMessage(message);
        notification.setUser_id(user_id);
        notification.setType(type);
        notification.setConverid(converid);

        Map<String, Object> forumValues = notification.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(pushKey, forumValues);
        databaseReference.setPriority(ServerValue.TIMESTAMP);
        databaseReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                }
            }
        });
    }


}
