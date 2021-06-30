package com.pamsillah.wakho;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Chat;
import com.pamsillah.wakho.Models.Message;
import com.pamsillah.wakho.Models.Notifications;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.PostsByAgent;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Utils.Session;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

//import com.firebase.client.Firebase;

/**
 * Created by user on 01/27/2017.
 */

public class MyApplication extends Application {
    Session session;
    private RequestQueue mRequestque;
    private static MyApplication instance;
    private Post post;
    private Agent agent;
    private PostsByAgent postsByAgent;

    public List<Post> getPending() {
        return pending;
    }

    public void setPending(List<Post> pending) {
        this.pending = pending;
    }

    List<Post> pending = new ArrayList<>();


    public static List<com.pamsillah.wakho.Models.Notification> lstNots = new ArrayList<>();

    public List<com.pamsillah.wakho.Models.Notification> getLstNots() {
        return lstNots;
    }

    public void setLstNots(List<com.pamsillah.wakho.Models.Notification> lstNots) {
        this.lstNots = lstNots;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    List<String> contacts;

    public com.pamsillah.wakho.Models.Notification getNotification() {
        return notification;
    }

    public void setNotification(com.pamsillah.wakho.Models.Notification notification) {
        this.notification = notification;
    }

    private com.pamsillah.wakho.Models.Notification notification;


    public List<Agent> getLstAgent() {
        return lstAgent;
    }

    public void setLstAgent(List<Agent> lstAgent) {
        this.lstAgent = lstAgent;
    }

    private List<Agent> lstAgent;

    public List<Post> getListPost() {
        return listPost;
    }

    public void setListPost(List<Post> listPost) {
        this.listPost = listPost;
    }

    public List<Post> listPost = new ArrayList<>();

    public PostsByAgent getPostsByAgent() {
        return postsByAgent;
    }

    public void setPostsByAgent(PostsByAgent postsByAgent) {
        this.postsByAgent = postsByAgent;
    }

    public static synchronized MyApplication getinstance() {

        return instance;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    private Notifications notifications;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    private Chat chat;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    private List<Subscriber> subscribers;
    Firebase rootref, mRef, userRef, conversation;
    private String email, rec;
    int uid;
    private List<Message> messages = new ArrayList<>();
    private String ChatName;

    public String getChatName() {
        return ChatName;
    }

    public void setChatName(String chatName) {
        ChatName = chatName;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

        // new FirebaseNotificationServices();
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/times.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        session = new Session(this);
        Firebase.setAndroidContext(this);
        instance = this;
        rootref = new Firebase("https://wakhoapp.firebaseio.com/");
        userRef = new Firebase("https://wakhoapp.firebaseio.com/Negotiations/");

    }


    public Session getSession() {

        if (session == null) {
            session = new Session(this);
        }

        return session;
    }


    public RequestQueue getmRequestque() {
        if (mRequestque == null) {
            mRequestque = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestque;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestque().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getmRequestque().add(req);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestque != null) {
            mRequestque.cancelAll(tag);
        }
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}