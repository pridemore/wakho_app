package com.pamsillah.wakho;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.collect.Lists;
import com.pamsillah.wakho.Models.PostsByAgent;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by psillah on 3/24/2017.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<View_Holder> {

    List<PostsByAgent> list = Collections.emptyList();
    Context context;
    PostsByAgent item;
    PostsByAgent agent;

    public RecyclerViewAdapter(List<PostsByAgent> list, Context context) {
        this.list = list;
        this.context = context;
        Lists.reverse(this.list);
    }


    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agents_cards_layout, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(View_Holder holder, final int position) {
        item = list.get(position);


        //int pic = Integer.parseInt(list.get(position).getPostId());
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(" " + item.agent.getCompanyName().split(" ")[0]);


        holder.departTime.setText("Departs on : " + item.getDepatureDate());
        String arr[] = item.getLocationTo().split(" ");
        if (arr != null && arr.length > 1) {
            holder.destination.setText(" To   : " + arr[0]);
            holder.depart.setText(" From : " + item.getLocationFrom());
        } else {
            holder.destination.setText(" To   : " + item.getLocationFrom());

            holder.depart.setText(" From : " + item.getLocationFrom());
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        Glide.with(context).applyDefaultRequestOptions(requestOptions)
                .load(com.pamsillah.wakho.app_settings.ConnectionConfig.BASE_URL + "/" +
                        item.getAgent().CompanyLogo.replace("~", ""))
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getinstance().setPostsByAgent(list.get(position));
                if (MyApplication.getinstance().getPostsByAgent().agent.getSubscriberId()
                        .equals(String.valueOf(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId()))) {
                    context.startActivity(new Intent(context, PostByAgent.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    context.startActivity(new Intent(context, ItemSingle.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, PostsByAgent data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Data data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = this.list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.list.remove(0);
            }
            this.notifyDataSetChanged();
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

