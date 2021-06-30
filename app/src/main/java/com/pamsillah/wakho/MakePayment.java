package com.pamsillah.wakho;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pamsillah.wakho.Models.Post;

/**
 * Created by psillah on 4/28/2017.
 */
public class MakePayment extends AppCompatActivity {
    Post post = new Post();
    TextView details;
    Button pay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        pay = (Button) findViewById(R.id.pay);
        details = (TextView) findViewById(R.id.details);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://apiyangu.byethost3.com/bw/logintest.html"));
                startActivity(browserIntent);
            }
        });

    }


}
