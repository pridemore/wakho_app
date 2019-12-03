package com.pamsillah.wakho.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pamsillah.wakho.HireRequests;
import com.pamsillah.wakho.History;
import com.pamsillah.wakho.Models.Notifications;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.NegotiationsActivity;
import com.pamsillah.wakho.PendingActivity;
import com.pamsillah.wakho.PushNotifications;
import com.pamsillah.wakho.R;

import java.util.List;

/**
 * Created by psillah on 8/24/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {
    private List<Notifications> notificationsList;
    private Context context;

    public NotificationsAdapter(List<Notifications> notificationsList, Context context) {
        this.notificationsList = notificationsList;
        this.context = context;
    }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_card, parent, false);
        return new NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, final int position) {
        final Notifications notifs = notificationsList.get(position);
        holder.nDesc.setText(notifs.getnDesc());
        holder.nTitle.setText(notifs.getnTitle());
        holder.nImg.setImageResource(notifs.getnImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notifs.getnTitle().contains("Negotiations")) {
                    context.startActivity(new Intent(context, NegotiationsActivity.class));
                } else if (notifs.getnTitle().contains("Pending")) {
                    context.startActivity(new Intent(context, PendingActivity.class));
                } else if (notifs.getnTitle().contains("History")) {
                    context.startActivity(new Intent(context, History.class));

                } else if (notifs.getnTitle().contains("Notifications")) {
                    context.startActivity(new Intent(context, PushNotifications.class));

                } else if (notifs.getnTitle().contains("Hire Request")) {
                    if (MyApplication.getinstance().getSession().getAgent() != null) {
                        context.startActivity(new Intent(context, HireRequests.class));
                    } else {
                        Toast.makeText(context, "This feature is available for Agents only", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder {
        TextView nTitle, nDesc;
        ImageView nImg;

        public NotificationsViewHolder(View itemView) {
            super(itemView);

            nImg = (ImageView) itemView.findViewById(R.id.notifImg);
            nTitle = (TextView) itemView.findViewById(R.id.ttile);
            nDesc = (TextView) itemView.findViewById(R.id.notifDesc);

        }
    }
}
