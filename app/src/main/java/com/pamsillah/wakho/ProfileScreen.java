package com.pamsillah.wakho;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pamsillah.wakho.Models.Subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by psillah on 3/31/2017.
 */
public class ProfileScreen extends AppCompatActivity {

    ImageView edit;
    TextView username, email, phone, Adress, status;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        Subscriber s = MyApplication.getinstance().getSession().getSubscriber();
        edit = (ImageView) findViewById(R.id.edit_profile);
        username = (TextView) findViewById(R.id.user_profile_name);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        Adress = (TextView) findViewById(R.id.adress);
        status = (TextView) findViewById(R.id.status);
        imageView = (ImageView) findViewById(R.id.image);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, EditProfile.class);

                startActivity(intent);
            }
        });

        username.setText(s.getName() + " " + s.getSurname());
        phone.setText(s.getPhone());
        email.setText("Email Address : " + s.getEmail());
        if (s.getAddress() != null) {
            Adress.setText("Physical Address  : " + s.getAddress().replace("_", " "));
        }
        else
            {
            Adress.setText("Physical Address  : " + s.getAddress());
        }
        status.setText("Status : " + s.getStatus());
        if (s.getProfilePic() != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder);
            Glide.with(this).applyDefaultRequestOptions(requestOptions)
                    .load(com.pamsillah.wakho.app_settings.ConnectionConfig.BASE_URL + "/" +
                            s.getProfilePic().replace("~", ""))
                    .into(imageView);
        }
    }

    @Nullable
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);


            //crop to circle
            Bitmap output;
            //check if its a rectangular image
            if (bitmap.getWidth() > bitmap.getHeight()) {
                output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            } else {
                output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(output);

            float r = 0;

            if (bitmap.getWidth() > bitmap.getHeight()) {
                r = bitmap.getHeight() / 2;
            } else {
                r = bitmap.getWidth() / 2;
            }

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());


            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);

            canvas.drawCircle(r, r, r, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;


        } catch (IOException e) {
            // Log exception
            return null;
        }

    }

}
