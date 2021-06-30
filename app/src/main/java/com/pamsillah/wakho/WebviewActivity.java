package com.pamsillah.wakho;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by psillah on 7/19/2017.
 */

public class WebviewActivity extends AppCompatActivity {
    WebView webView;
    Toolbar toolbar;
    String id = MyApplication.getinstance().getNotification().getConverid();
    String[] arr = id.split("_");

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        id = null;
        id = arr[0] + "_" + arr[1] + "_" + arr[2];
        Firebase mref = new Firebase("https://wakhoapp.firebaseio.com/Negotiations/" + id);
        mref.removeValue();
        // Firebase mref= new Firebase("https://wakhoapp.firebaseio.com/Negotiations/");

        // mref.removeValue();
        toolbar = findViewById(R.id.toolbarweb);
        toolbar.setTitle(MyApplication.getinstance().getPost().getTitle()+ " Payment");
        toolbar.setSubtitle(MyApplication.getinstance().getSession().getSubscriber().getName());
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.textColorPrimary));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
             //   startActivity(new Intent(WebviewActivity.this, MainActivity.class));
                Toast.makeText(WebviewActivity.this, "Redirecting to main .", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WebviewActivity.this, MainActivity.class));
                Toast.makeText(WebviewActivity.this, "Redirecting to main. ", Toast.LENGTH_SHORT).show();
            }
        });

//        setSupportActionBar(toolbar);
        webView = findViewById(R.id.paynowWebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        String url = MyApplication.getinstance().getEmail();

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().split("_")[0].contains(arr[0])) {

                    Firebase mref = new Firebase("https://wakhoapp.firebaseio.com/Negotiations/" + dataSnapshot.getKey());
                    mref.removeValue();
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WebviewActivity.this, MainActivity.class));
        Toast.makeText(WebviewActivity.this, "Check post for result", Toast.LENGTH_SHORT).show();
    }

}
