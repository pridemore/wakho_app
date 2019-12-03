package com.pamsillah.wakho.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pamsillah.wakho.Models.NegotiationsRoot;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.NegotiateActivity;
import com.pamsillah.wakho.R;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import java.util.List;

/**
 * Created by psillah on 7/25/2017.
 */

public class NegListAdapter extends RecyclerView.Adapter<NegListAdapter.NegListViewHolder> {
    List<NegotiationsRoot> negList;
    Context context;
    CardView cardView;


    public NegListAdapter(List<NegotiationsRoot> negList, Context context) {
        this.negList = negList;
        this.context = context;
    }

    @Override
    public NegListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.neg_root_card, parent, false);
        return new NegListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NegListViewHolder holder, int position) {
        final NegotiationsRoot negModel = negList.get(position);

        holder.negName.setText(negModel.getNegotiatinName());
        if (negModel.getPic() != null) {
            Glide.with(context).load(ConnectionConfig.BASE_URL + "/" + negModel.getPic().replace("~", "")).into(holder.image);
        }
        holder.postTitle.setText(negModel.getLast());
        holder.date.setText(negModel.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intet = new Intent(context, NegotiateActivity.class);
                MyApplication.getinstance().getSession().setNeg(negModel);
                intet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getinstance().getSession().setNeg(negModel);

                context.startActivity(intet);
            }
        });


    }

    @Override
    public int getItemCount() {
        return negList.size();
    }

    public class NegListViewHolder extends RecyclerView.ViewHolder {
        TextView negName, postTitle, date;
        ImageView image;

        public NegListViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.root_card);
            negName = (TextView) itemView.findViewById(R.id.negName);
            image = (ImageView) itemView.findViewById(R.id.pic);

            date = (TextView) itemView.findViewById(R.id.date);
            postTitle = (TextView) itemView.findViewById(R.id.postTitle);
        }
    }


}
