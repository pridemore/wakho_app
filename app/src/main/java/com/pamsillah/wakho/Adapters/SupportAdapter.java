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
import com.pamsillah.wakho.Models.SupportModel;
import com.pamsillah.wakho.R;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import java.util.List;

/**
 * Created by psillah on 7/25/2017.
 */

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.SupportViewHolder> {
    List<SupportModel> negList;
    Context context;
    CardView cardView;


    public SupportAdapter(List<SupportModel> negList, Context context) {
        this.negList = negList;
        this.context = context;
    }

    @Override
    public SupportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.support, parent, false);
        return new SupportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SupportAdapter.SupportViewHolder holder, int position) {
        final SupportModel negModel = negList.get(position);

        holder.senderName.setText(negModel.getSenderName().split(" ")[0]);
        if (negModel.getImage() != null) {
            Glide.with(context).load(ConnectionConfig.BASE_URL + "/" + negModel.getImage().replace("~", "")).into(holder.image);
        }
        holder.message.setText(negModel.getMessage());
        holder.date.setText(negModel.getDate().split("CAT")[0].split("GMT")[0].trim());


    }

    @Override
    public int getItemCount() {
        return negList.size();
    }

    public class SupportViewHolder extends RecyclerView.ViewHolder {
        TextView senderName, message, date;


        ImageView image;

        public SupportViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.supportcard);
            senderName = (TextView) itemView.findViewById(R.id.negName);
            image = (ImageView) itemView.findViewById(R.id.pic);

            date = (TextView) itemView.findViewById(R.id.date);
            message = (TextView) itemView.findViewById(R.id.postTitle);
        }
    }


}
