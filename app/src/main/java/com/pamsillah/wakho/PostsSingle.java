package com.pamsillah.wakho;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.pamsillah.wakho.Models.Post;

/**
 * Created by Sir Allen on 4/22/2017.
 */

public class PostsSingle extends AppCompatActivity {
    private TextView weight, fragility, postsingleitem, poststo, postsfrom, postspickup, postsfee, descr;
    private ImageView parcelpicture;
    Post post = new Post();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_posts);


        post = MyApplication.getinstance().getPost();
        weight = (TextView) findViewById(R.id.weight_value);
        fragility = (TextView) findViewById(R.id.frag_value);
        postsingleitem = (TextView) findViewById(R.id.post_single_title); //title
        poststo = (TextView) findViewById(R.id.postsngleto);
        postsfrom = (TextView) findViewById(R.id.postsnglefrom);
        postspickup = (TextView) findViewById(R.id.postspickup);
        postsfee = (TextView) findViewById(R.id.postsfee);
        parcelpicture = (ImageView) findViewById(R.id.full_post_img);
        descr = (TextView) findViewById(R.id.descr);


        descr.setText(post.getDescription());
        postsingleitem.setText(post.getTitle());
        weight.setText(post.getWeight());
        fragility.setText(post.getFragility() + "/10");
        poststo.setText("To: Masvingo ");
        postsfrom.setText("From: Harare");
        postspickup.setText("Pick Up: " + post.getPickUpPoint());
        postsfee.setText("Proposed Fee: " + "$" + post.getProposedFee());
        //parcelpicture.setImageBitmap(post.getParcelPic());


    }
}
