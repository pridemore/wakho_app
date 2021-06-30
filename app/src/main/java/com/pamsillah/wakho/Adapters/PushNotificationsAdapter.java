package com.pamsillah.wakho.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pamsillah.wakho.Help;
import com.pamsillah.wakho.Models.Message;
import com.pamsillah.wakho.Models.Notification;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Java Developer on 22/11/2017.
 */

public class PushNotificationsAdapter extends RecyclerView.Adapter<PushNotificationsAdapter.ViewHolder> {


    private Context context;
    String user;

    List<Notification> notifications;
    Message item;
    public final int TYPE_SENDER = 0;
    public final int TYPE_RECEIVER = 1;

    SingleChatAdapter.OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;


    public PushNotificationsAdapter(List<Notification> notifications, Context context) {
        super();
        //Getting all the superheroes
        this.notifications = notifications;
        this.context = context;
    }
    @Override
    public PushNotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificard, parent, false);
        PushNotificationsAdapter.ViewHolder viewHolder = new PushNotificationsAdapter.ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PushNotificationsAdapter.ViewHolder holder, final int position) {

        View v = holder.itemView;
        Set<Notification> set = new HashSet<>(notifications);
        notifications.clear();
        notifications.addAll(set);
        final Notification notification = notifications.get(position);
        holder.title.setText(notification.getDescription());

        DateFormat format = new SimpleDateFormat("mm");
//        try {
//            Date date = format.parse(String.valueOf(notification.getTimestamp()));
//            holder.time.setText(date.toString().split("GMT")[0].split("CAT")[0].trim());
//
//        } catch (ParseException e) {
//            holder.time.setText(String.valueOf( notification.getTimestamp()));
//        }
        holder.time.setText("");

        holder.textMessage.setText(notification.getMessage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getinstance().setNotification(notification);
                Intent intent = new Intent(context, Help.class);
                intent.putExtra("notifKey", notifications.get(position).getSnapshotKey());
                context.startActivity(intent);
                notifyDataChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView textMessage;
        TextView time;

        public ViewHolder(View v) {
            super(v);

            time = (TextView) v.findViewById(R.id.time);
            textMessage = (TextView) v.findViewById(R.id.txtMessage);

            title = (TextView) v.findViewById(R.id.title);


        }


    }

    @Override
    public int getItemViewType(int position) {


        return 0;
    }

    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    public void setLoadMoreListener(SingleChatAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

}

