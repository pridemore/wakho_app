package com.pamsillah.wakho.Adapters;

import android.content.Context;
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
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by .Net Developer on 9/9/2017.
 */

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.PendingViewHolder> {
    List<Agent> list = Collections.emptyList();
    Context context;
    Agent item;
    String post_id;

    public AgentAdapter(List<Agent> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public AgentAdapter.PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.his_agent_card, parent, false);
        return new AgentAdapter.PendingViewHolder(v);

    }

    public class PendingViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;


        ImageView imageView;

        public PendingViewHolder(View v) {
            super(v);

            cv = (CardView) itemView.findViewById(R.id.cardview);
            name = (TextView) itemView.findViewById(R.id.post_title);
            imageView = (ImageView) itemView.findViewById(R.id.postImage);
        }


    }

    @Override
    public void onBindViewHolder(AgentAdapter.PendingViewHolder holder, final int position) {


        item = list.get(position);

        holder.name.setText(item.getCompanyName());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder).dontAnimate();
        Glide.with(context).applyDefaultRequestOptions(requestOptions)
                .load("" + item.getCompanyLogo().replace("~", ""))
                .into(holder.imageView);

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
    public void insert(int position, Agent data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Data data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }
}


