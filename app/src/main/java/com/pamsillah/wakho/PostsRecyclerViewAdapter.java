package com.pamsillah.wakho;

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
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import java.util.Collections;
import java.util.Date;
import java.util.List;


public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.Posts_View_Holder> {

    List<Post> list = Collections.emptyList();
    Context context;
    Post item;
    String post_id;

    public PostsRecyclerViewAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public Posts_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);
        return new Posts_View_Holder(v);

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
    public void onBindViewHolder(Posts_View_Holder holder, final int position) {


        item = list.get(position);

        if (item.getAddressTo() != null && item.getLocationToId().length() > 3 && item.getFragility() != null) {


            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            holder.title.setText(list.get(position).getTitle());
            holder.weight.setText(list.get(position).getWeight());
            holder.arrDate.setText(list.get(position).getDeliveryDate());
            holder.price.setText(list.get(position).getProposedFee());
            if (list.get(position).getLocationFromId().length() > 2) {
                holder.postFrom.setText(list.get(position).getLocationFromId().split(",")[0]);
            } else {
                holder.postFrom.setText(list.get(position).getLocationFromId().split(" ")[0]);
            }

            if (list.get(position).getLocationToId().split(" ").length > 2) {
                holder.postTo.setText(list.get(position).getLocationToId().split(" ")[0]);
            } else {
                holder.postTo.setText(list.get(position).getLocationToId().split(",")[0]);
            }

            String day = String.valueOf(new Date().toString()).split(" ")[2];

            String arr[] = item.getDatePosted().replace(":", "-").replace("T", "-").split("-");
            if (arr != null && arr[2].equals(day)) {
                holder.dateposted.setText("New");
            } else {
                String date = arr[2] + "/" + arr[1] + "/" + arr[0];
                holder.dateposted.setText("Posted On :" + date);
            }
            if(list.get(position).getStatus().contains("Pay Initiated"))
                holder.dateposted.setText("Awaiting Payment");


            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder).dontAnimate();
            Glide.with(context).applyDefaultRequestOptions(requestOptions)
                    .load(ConnectionConfig.BASE_URL + "/" + item.getParcelPic().replace("~", ""))
                    .into(holder.imageView);
            post_id = String.valueOf(list.get(position).getPostId());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.getinstance().setPost(list.get(position));


                    //context.startActivity(new Intent(context, CartActivity.class));
                    if (MyApplication.getinstance().getSession().getSubscriber().getSubscriberId() == Integer.parseInt(MyApplication.getinstance().getPost().getSubscriberId())) {
                        Intent intet = new Intent(context, ManagePost.class);

                        context.startActivity(intet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else {
                        Intent intet = new Intent(context, CartActivity.class);

                        context.startActivity(intet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }

                }
            });
        }


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
