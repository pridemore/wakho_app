package com.pamsillah.wakho.Adapters;

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
import com.pamsillah.wakho.ChatSingle;
import com.pamsillah.wakho.Models.Chat;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.R;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

import java.util.List;

/**
 * Created by .Net Developer on 8/29/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Subscriber subscriber = MyApplication.getinstance().getSession().getSubscriber();
    ChatAdapter.OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private List<Chat> mylist;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View textView;
        public TextView time, userName, message;
        CardView cardView;
        ImageView picture;

        public ViewHolder(View v) {

            super(v);
            textView = v;
            time = (TextView) v.findViewById(R.id.time);
            userName = (TextView) v.findViewById(R.id.user_name);
            message = (TextView) v.findViewById(R.id.content);
            cardView = (CardView) v.findViewById(R.id.chatCard);
            picture = (ImageView) v.findViewById(R.id.picture);
        }
    }

    public ChatAdapter(List<Chat> ds) {

        mylist = ds;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_view, parent, false);

        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {


        final Chat bfo = mylist.get(position);

        if (bfo.getRecipient().trim().equals(subscriber.getPhone().trim())) {


            List<Subscriber> subs = MyApplication.getinstance().getSubscribers();
            for (Subscriber ss : subs) {
                if (ss.getPhone().equals(bfo.getCreatorID())) {
                    bfo.setRecipName(ss.getName() + " " + ss.getSurname());
                    holder.userName.setText(ss.getName() + " " + ss.getSurname());
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.placeholder);
                    Glide.with(MyApplication.getinstance()).applyDefaultRequestOptions(requestOptions)
                            .load(ConnectionConfig.BASE_URL + "/" + ss.getProfilePic().replace("~", ""))
                            .into(holder.picture);
                    break;
                }
            }
        } else if (bfo.getCreatorID().trim().equals(subscriber.getPhone().trim())) {
            List<Subscriber> subs = MyApplication.getinstance().getSubscribers();
            for (Subscriber ss : subs) {
                if (ss.getPhone().equals(bfo.getRecipient())) {
                    bfo.setRecipName(ss.getName() + " " + ss.getSurname());
                    holder.userName.setText(ss.getName() + " " + ss.getSurname());

                    Glide.with(MyApplication.getinstance()).load(ConnectionConfig.BASE_URL + "/" + ss.getProfilePic().replace("~", "")).into(holder.picture);
                    break;
                }

            }
        }
        holder.message.setText(bfo.getLast());
        holder.time.setText(bfo.getTimeCreated());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), ChatSingle.class);


                MyApplication.getinstance().setChat(bfo);
                view.getContext().startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mylist.size();
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


    public void setLoadMoreListener(ChatAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
