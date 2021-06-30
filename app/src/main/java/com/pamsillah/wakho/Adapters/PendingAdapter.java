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
import com.bumptech.glide.request.RequestOptions;
import com.pamsillah.wakho.Data;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.R;
import com.pamsillah.wakho.UpdateParcel;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import java.util.Collections;
import java.util.List;

/**
 * Created by .Net Developer on 9/9/2017.
 */

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingViewHolder> {
    List<Post> list = Collections.emptyList();
    Context context;
    Post item;
    String post_id;

    public PendingAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public PendingAdapter.PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_card, parent, false);
        return new PendingAdapter.PendingViewHolder(v);

    }

    public class PendingViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView weight, departDate;
        TextView postTo;
        TextView postFrom;
        TextView arrDate;
        TextView price, recip, agent;


        ImageView imageView, agentpic;

        public PendingViewHolder(View v) {
            super(v);

            departDate = (TextView) itemView.findViewById(R.id.departDate);
            agent = (TextView) itemView.findViewById(R.id.agent);
            agentpic = (ImageView) itemView.findViewById(R.id.agent_img);
            cv = (CardView) itemView.findViewById(R.id.cardview);
            title = (TextView) itemView.findViewById(R.id.post_title);
            postTo = (TextView) itemView.findViewById(R.id.postTo);
            postFrom = (TextView) itemView.findViewById(R.id.postFrom);
            arrDate = (TextView) itemView.findViewById(R.id.postArrDate);
            price = (TextView) itemView.findViewById(R.id.proposedFee);
            weight = (TextView) itemView.findViewById(R.id.postWeight);
            recip = (TextView) itemView.findViewById(R.id.recipient);
            imageView = (ImageView) itemView.findViewById(R.id.postImage);
        }


    }

    @Override
    public void onBindViewHolder(PendingAdapter.PendingViewHolder holder, final int position) {
        item = list.get(position);

        holder.title.setText("   " + list.get(position).getTitle());
        holder.weight.setText(list.get(position).getWeight());
        holder.arrDate.setText("$ " + item.getProposedFee());
        holder.price.setText(list.get(position).getDeliveryDate());
        if (item.getLocationFromId().length() > 2) {
            holder.postFrom.setText(item.getLocationFromId().split(",")[0]);

        } else {
            holder.postFrom.setText(item.getLocationFromId());
        }
        if (item.getLocationToId().length() > 2) {
            holder.postTo.setText(item.getLocationToId().split(",")[0]);
        } else {
            holder.postTo.setText(item.getLocationToId());
        }

        if (item.getDescription().split(":").length > 2) {
            holder.recip.setText("Recipient   :  " + item.getDescription().split(":")[0] + ":" + item.getDescription().split(":")[1]);
        } else {

            holder.recip.setText("Recipient   :  " + item.getDescription());
        }

        holder.agent.setText(item.getStatus().split("#")[1].split(" ")[0]);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.parc).dontAnimate();

        RequestOptions requestOptions2 = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder).dontAnimate();

        Glide.with(context).applyDefaultRequestOptions(requestOptions).load(ConnectionConfig.BASE_URL + "/" + item.getParcelPic().replace("~", "")).into(holder.imageView);
        Glide.with(context).applyDefaultRequestOptions(requestOptions2).load(ConnectionConfig.BASE_URL + "/" + item.getStatus().split("#")[2].replace("~", "")).into(holder.agentpic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getinstance().setPost(null);
                MyApplication.getinstance().setPost(list.get(position));


                Intent intet = new Intent(context, UpdateParcel.class);

                context.startActivity(intet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        //animate(holder);

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