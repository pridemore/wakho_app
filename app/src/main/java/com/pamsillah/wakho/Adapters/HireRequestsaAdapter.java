package com.pamsillah.wakho.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pamsillah.wakho.Data;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.R;
import com.pamsillah.wakho.SingleRequest;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by .Net Developer on 10/9/2017.
 */

public class HireRequestsaAdapter extends RecyclerView.Adapter<HireRequestsaAdapter.Posts_View_Holder> {

    List<Post> list = new ArrayList<>();
    Context context;
    Post item;
    String post_id;

    public HireRequestsaAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public HireRequestsaAdapter.Posts_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);
        return new HireRequestsaAdapter.Posts_View_Holder(v);

    }

    public class Posts_View_Holder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView weight;
        TextView postTo;
        TextView postFrom;
        TextView arrDate;
        TextView price;

        TextView dateposted;

        ImageView imageView;

        public Posts_View_Holder(View v) {
            super(v);

            cv = (CardView) itemView.findViewById(R.id.cardview);
            title = (TextView) itemView.findViewById(R.id.post_title);
            postTo = (TextView) itemView.findViewById(R.id.postTo);
            postFrom = (TextView) itemView.findViewById(R.id.postFrom);
            arrDate = (TextView) itemView.findViewById(R.id.postArrDate);
            price = (TextView) itemView.findViewById(R.id.proposedFee);
            weight = (TextView) itemView.findViewById(R.id.postWeight);
            imageView = (ImageView) itemView.findViewById(R.id.postImage);
            dateposted = (TextView) itemView.findViewById(R.id.newpost);
        }


    }

    @Override
    public void onBindViewHolder(HireRequestsaAdapter.Posts_View_Holder holder, final int position) {


        item = list.get(position);


//int pic = Integer.parseInt(list.get(position).getPostId());
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(list.get(position).getTitle());
        holder.weight.setText(list.get(position).getWeight());
        holder.arrDate.setText(list.get(position).getDeliveryDate());
        holder.price.setText(list.get(position).getProposedFee());
        holder.postFrom.setText(list.get(position).getLocationFromId().split(",")[0]);
        holder.postTo.setText(list.get(position).getLocationToId().split(",")[0]);
        String day = String.valueOf(new Date().toString()).split(" ")[2];


        String arr[] = item.getDatePosted().replace(":", "-").replace("T", "-").split("-");
        if (arr[2].equals(day)) {
            holder.dateposted.setText("New");
        } else {
            String date = arr[2] + "/" + arr[1] + "/" + arr[0];
            holder.dateposted.setText("Posted On :" + date);
        }
        Glide.with(context).load(ConnectionConfig.BASE_URL + "/" + item.getParcelPic().replace("~", "")).load(R.drawable.parc).into(holder.imageView);
        post_id = String.valueOf(list.get(position).getPostId());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getinstance().setPost(list.get(position));


                //context.startActivity(new Intent(context, CartActivity.class));
                Intent intet = new Intent(context, SingleRequest.class);

                context.startActivity(intet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        if (list != null)
            return list.size();
        else
            return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Post data) {
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


    public void setFilter(List<Post> postList) {
        list.addAll(postList);
        notifyDataSetChanged();
    }
}
