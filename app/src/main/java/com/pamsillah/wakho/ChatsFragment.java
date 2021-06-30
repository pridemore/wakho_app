package com.pamsillah.wakho;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.pamsillah.wakho.Adapters.ChatAdapter;
import com.pamsillah.wakho.Models.Chat;
import com.pamsillah.wakho.Models.ReadChats;

import java.util.ArrayList;

/**
 * Created by psillah on 3/24/2017.
 */
public class ChatsFragment extends Fragment {
    public RecyclerView chatRecyclerView;
    private TextView emptyState6;
    public RecyclerView.Adapter chatAdapter;
    public RecyclerView.LayoutManager chatLayoutManager;
    private ArrayList<Chat> chats = new ArrayList<>();
    FloatingActionButton fab;
    ReadChats rc = new ReadChats();

    final String chat = MyApplication.getinstance().getChatName();
    final Firebase mRef = new Firebase("https://wakhoapp.firebaseio.com//Conversations/");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chats_layouts, container, false);

//fab=(FloatingActionButton)v.findViewById(R.id.fabchats);
//
        emptyState6 = v.findViewById(R.id.txtEmptyState);
        fab = (FloatingActionButton) v.findViewById(R.id.fabchats);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button send;
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View convertView = inflater.inflate(R.layout.supportconf, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("WAKHO SUPPORT SERVICE");
                alertDialog.setIcon(R.drawable.logo);

                send = convertView.findViewById(R.id.send);

                alertDialog.show();
//

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), Support.class));
                    }
                });
            }
        });


        chatRecyclerView = v.findViewById(R.id.chats_recycler);
        chatRecyclerView.setHasFixedSize(true);
        chatLayoutManager = new LinearLayoutManager(getContext());
        chatRecyclerView.setLayoutManager(chatLayoutManager);
        chatAdapter = new ChatAdapter(rc.chatList);
        chatRecyclerView.setAdapter(chatAdapter);
        mRef.addChildEventListener(new ChildEventListener() {
            String mess;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Chat chat = dataSnapshot.getValue(Chat.class);

showEmptyState(chat==null);
                chat.setId(dataSnapshot.getKey());
                String phone = null;
                if (MyApplication.getinstance().getSession().getSubscriber() != null) {
                    phone = MyApplication.getinstance().getSession().getSubscriber().getPhone();

                }
                chat.setLast(chat.message.get(chat.message.size() - 1).getMessage());

                if (phone == null) {
                    phone = "077777777";
                }

                if (phone.equals(chat.getRecipient()) || phone.equals(chat.getCreatorID())) {

                    rc.chatList.clear();
                    chats.add(chat);

                    if (MyApplication.getinstance().getSubscribers() != null) {
                        chatAdapter = new ChatAdapter(chats);
                        chatRecyclerView.setAdapter(chatAdapter);
                        chatAdapter.notifyDataSetChanged();
                    }
                } else {


                }

                showEmptyState(chat==null);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Chat chat = dataSnapshot.getValue(Chat.class);


                chat.setId(dataSnapshot.getKey());

                chat.setLast(chat.message.get(chat.message.size() - 1).getMessage());


                for (Chat ch : chats) {
                    if (ch.getId().equals(chat.getId())) {

                        chats.remove(ch);
                        break;
                    }


                }

                chats.add(0, chat);
                chatAdapter = new ChatAdapter(chats);
                chatRecyclerView.setAdapter(chatAdapter);
                chatAdapter.notifyDataSetChanged();


            }

            private void showEmptyState(boolean b) {
                if(b){
                    emptyState6.setVisibility(View.VISIBLE);
                    chatRecyclerView.setVisibility(View.GONE);
                }else{
                    emptyState6.setVisibility(View.GONE);
                    chatRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


        return v;
    }


}
