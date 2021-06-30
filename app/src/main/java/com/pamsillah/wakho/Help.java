package com.pamsillah.wakho;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pamsillah.wakho.Models.Notification;

/**
 * Created by .Net Developer on 9/9/2017.
 */

public class Help extends Activity {
    Toolbar toolbar;
    Notification notif;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        notif = MyApplication.getinstance().getNotification();
        toolbar = (Toolbar) findViewById(R.id.helpToolBar);
        toolbar.setTitle("Notification");
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(Help.this, MainActivity.class));
            }
        });
        final Dialog dialog = new Dialog(Help.this);
        dialog.setContentView(R.layout.layout_accept_dialog);
        dialog.setTitle(notif.getDescription());

        TextView dialogtxt = (TextView) dialog.findViewById(R.id.textView);
        dialogtxt.setText(notif.getMessage());

        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_green_check));

        //adding button click event
        Button dismissButton = (Button) dialog.findViewById(R.id.button);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (notif.getType().contains("accept")) {
                    Intent i = new Intent(Help.this, PayInit.class);
                    MyApplication.getinstance().setNotification(notif);
                    startActivity(i);
                } else if (notif.getType().contains("chat_view")) {
                    Intent i = new Intent(Help.this, NegotiationsActivity.class);
                    MyApplication.getinstance().setNotification(notif);
                    startActivity(i);

                } else if (notif.getType().contains("hire")) {
                    Intent i = new Intent(Help.this, HireRequests.class);
                    MyApplication.getinstance().setNotification(notif);
                    startActivity(i);

                } else if (notif.getType().contains("pending")) {
                    Intent i = new Intent(Help.this, PendingActivity.class);
                    MyApplication.getinstance().setNotification(notif);
                    startActivity(i);

                } else {
                    Intent i = new Intent(Help.this, MainActivity.class);
                    MyApplication.getinstance().setNotification(notif);
                    startActivity(i);

                }
                dialog.dismiss();
                MyApplication.getinstance().lstNots.remove(notif);
            }
        });


        dialog.show();


    }
}
