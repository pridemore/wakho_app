package com.pamsillah.wakho;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.pamsillah.wakho.Adapters.ChatAdapter;
import com.pamsillah.wakho.Adapters.SingleChatAdapter;
import com.pamsillah.wakho.Models.Chat;
import com.pamsillah.wakho.Models.Message;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Utils.Notifications.NotificationsSender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by psillah on 6/12/2017.
 */
public class ChatSingle extends AppCompatActivity {
    Toolbar toolbar;
    EditText txtMessage;
    RecyclerView messag;

    private ImageView mSendMsg;
    SingleChatAdapter arrayAdapter;
    String Recipient;
    ChatAdapter chatAdapter;
    List<Message> messagesses = new ArrayList<>();
    List<Message> mess = new ArrayList<>();
    LinearLayoutManager layoutManager;
    Subscriber s = MyApplication.getinstance().getSession().getSubscriber();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlechat_layout);
        Firebase mm = new Firebase(
                "https://wakhoapp.firebaseio.com//Status/" +
                        MyApplication.getinstance().session.getSubscriber().getSubscriberId());
        mm.child("Status").setValue("Online");
        Firebase mRe = new Firebase("https://wakhoapp.firebaseio.com//Status/"
                + MyApplication.getinstance().session.getSubscriber().getPhone());
        mRe.child("Status").setValue("Online");
        if (MyApplication.getinstance().session.getAgent() != null) {
            Firebase mR = new Firebase("https://wakhoapp.firebaseio.com//Status/"
                    + MyApplication.getinstance().session.getAgent().getAgentId());
            mR.child("Status").setValue("Online");

        }


        toolbar = (Toolbar) findViewById(R.id.barSingle);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_keyboard));

        if (MyApplication.getinstance().getChat() != null) {
            Chat chats = MyApplication.getinstance().getChat();
            if (chats.getRecipient() != s.getPhone() || chats.getRecipient() != s.getName()) {
                toolbar.setTitle(chats.getRecipName());
            } else {

                toolbar.setTitle(chats.getRecipient());

            }

        } else {
            toolbar.setTitle("");
        }
        Recipient = MyApplication.getinstance().getChat().getRecipient();

        if (Recipient.equals(MyApplication.getinstance().getSession().getSubscriber().getPhone())) {
            Recipient = MyApplication.getinstance().getChat().getCreatorID().trim();

        }

        Firebase m = new Firebase("https://wakhoapp.firebaseio.com//Status/" + Recipient);
        m.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                toolbar.setSubtitle(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                toolbar.setSubtitle(dataSnapshot.getValue().toString());
                //status=dataSnapshot.getValue().toString();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                toolbar.setSubtitle(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                toolbar.setSubtitle(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final Chat chat = MyApplication.getinstance().getChat();

        final Firebase mRef = new Firebase("https://wakhoapp.firebaseio.com//Conversations/" + chat.getId() + "/message");
        messag = (RecyclerView) findViewById(R.id.list_msg);
        messag.setHasFixedSize(true);

        messag.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messag.setLayoutManager(layoutManager);

        txtMessage = (EditText) findViewById(R.id.txtMessage);
        mSendMsg = (ImageView) findViewById(R.id.btn_chat_send);

        mSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy @ h:mm");

                String date = sdf.format(new Date());

                final String name = MyApplication.getinstance().getSession().getSubscriber().getPhone();
                Message m = new Message();
                m.setMessage(txtMessage.getText().toString());
                m.setSender(name);
                m.setRecipient(Recipient);
                m.setMessageFormat("Text");
                m.setDateSend(date);
                m.setStatus("post_chat");
                m.setTimeSend(date);
                mess.clear();
                mess.addAll(messagesses);
                mess.add(m);

                mRef.setValue(mess);
                    NotificationsSender.sendNotification(
                            Recipient.replace("+", ""), chat.getId(),/*who the notification is meant for*/
                            m.getMessage(),/*Message to be displayed on the notification*/
                            MyApplication.getinstance().getSession().getSubscriber().getName(), /*Message title*/
                            "out_of_chat" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

                    );
                txtMessage.setText("");


            }
        });

        mRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message mn = dataSnapshot.getValue(Message.class);
                messagesses.add(mn);

                layoutManager.setStackFromEnd(true);
                arrayAdapter = new SingleChatAdapter(messagesses, getApplicationContext());

                messag.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataChanged();


                //Chats Update
                //chatAdapter=new ChatAdapter(new ReadChats().chatList);


                //chatRecyclerView.setAdapter(chatAdapter);
                //chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

        txtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Firebase mRef = new Firebase("https://wakhoapp.firebaseio.com//Status/"
                        + MyApplication.getinstance().session.getSubscriber().getSubscriberId());
                mRef.child("Status").setValue("typing...");
                Firebase mRe = new Firebase("https://wakhoapp.firebaseio.com//Status/"
                        + MyApplication.getinstance().session.getSubscriber().getPhone());
                mRe.child("Status").setValue("typing...");
                if (MyApplication.getinstance().session.getAgent() != null) {
                    Firebase mR = new Firebase("https://wakhoapp.firebaseio.com//Status/"
                            + MyApplication.getinstance().session.getAgent().getAgentId());
                    mR.child("Status").setValue("typing...");

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Firebase mRef = new Firebase("https://wakhoapp.firebaseio.com//Status/"
                        + MyApplication.getinstance().session.getSubscriber().getSubscriberId());
                mRef.child("Status").setValue("Online");
                Firebase mRe = new Firebase("https://wakhoapp.firebaseio.com//Status/"
                        + MyApplication.getinstance().session.getSubscriber().getPhone());
                mRe.child("Status").setValue("Online");
                if (MyApplication.getinstance().session.getAgent() != null) {
                    Firebase mR = new Firebase("https://wakhoapp.firebaseio.com//Status/"
                            + MyApplication.getinstance().session.getAgent().getAgentId());
                    mR.child("Status").setValue("Online");

                }
            }
        });

    }


}
