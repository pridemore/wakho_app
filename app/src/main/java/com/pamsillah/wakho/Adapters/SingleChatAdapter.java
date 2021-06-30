package com.pamsillah.wakho.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pamsillah.wakho.Models.Message;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.R;

import java.util.List;

/**
 * Created by .Net Developer on 8/29/2017.
 */

public class SingleChatAdapter extends RecyclerView.Adapter<SingleChatAdapter.ViewHolder> {


    private Context context;
    String user;

    List<Message> msgs;
    Message item;
    public final int TYPE_SENDER = 0;
    public final int TYPE_RECEIVER = 1;

    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;


    public SingleChatAdapter(List<Message> msgs, Context context) {
        super();
        //Getting all the superheroes
        this.msgs = msgs;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messagecard, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        View v = holder.itemView;


        item = msgs.get(position);


        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == TYPE_SENDER) {
            holder.receiver.setVisibility(View.GONE);
            holder.textMessage.setText(item.getMessage());
            holder.time.setText(item.getDateSend());
        } else {

            holder.sender.setVisibility(View.GONE);
            holder.rtextMessage.setText(item.getMessage());
            holder.rtime.setText(item.getTimeSend());
        }


    }


    @Override
    public int getItemCount() {
        return msgs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView textMessage;
        TextView rtime;
        TextView rtextMessage;
        LinearLayout receiver;
        LinearLayout sender;

        public ViewHolder(View v) {
            super(v);


            time = (TextView) v.findViewById(R.id.txtTime);
            textMessage = (TextView) v.findViewById(R.id.txtMessage);
            rtime = (TextView) v.findViewById(R.id.rtxtTime);
            rtextMessage = (TextView) v.findViewById(R.id.rtxtMessage);
            receiver = (LinearLayout) v.findViewById(R.id.reciever);
            sender = (LinearLayout) v.findViewById(R.id.sender);


        }


    }

    @Override
    public int getItemViewType(int position) {
        user = MyApplication.getinstance().getSession().getSubscriber().getPhone().trim();

        int type = 0;
        if (msgs.get(position).getSender().equals(user)) {
            type = TYPE_SENDER;
        } else {
            type = TYPE_RECEIVER;
        }

        return type;
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


    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

}

