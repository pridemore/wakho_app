package com.pamsillah.wakho;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by psillah on 3/27/2017.
 */
public class ChatsRecyclerView extends RecyclerView.Adapter<Chats_View_Holder> implements View.OnClickListener {
    List<ChatsObject> chatList;
    Context context;
    ImageView deletechatImage;

    public ChatsRecyclerView(Context context, List<ChatsObject> chats) {

        this.chatList = chats;
        this.context = context;

    }


    @Override
    public Chats_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_view, null);
        Chats_View_Holder cvh = new Chats_View_Holder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(Chats_View_Holder holder, final int position) {
        holder.user_name.setText(chatList.get(position).getUsername());
        holder.content.setText(chatList.get(position).getMessage());
        holder.time.setText(chatList.get(position).time);
        holder.userpic.setImageResource(chatList.get(position).userpic);
        holder.userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeChatAt(position);
            }
            
        });
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }


    @Override
    public void onClick(View view) {

        if(view.equals(deletechatImage))
        {

        }

    }

    public void removeChatAt(int position) {
        chatList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, chatList.size());
    }

}
