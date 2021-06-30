package com.pamsillah.wakho;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.FirebaseApp;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.FirebaseNotificationServices;
import com.pamsillah.wakho.Models.Notification;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Parser.SubscribersParser;
import com.pamsillah.wakho.Parsers.PostsParser;
import com.pamsillah.wakho.Utils.DTransUrls;
import com.pamsillah.wakho.app_settings.AuthHeader;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by psillah on 3/24/2017.
 */
public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView imageView;
    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    TextView tvAddress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //START CHAT ALL NOTIFICATIONS SERVICES
        FirebaseApp.initializeApp(this);
        startService(new Intent(this.getApplicationContext(), FirebaseNotificationServices.class));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary));


        if (MyApplication.getinstance().getSession().getSubscriber().getStatus().contains("Pending")) {
            searchSub(MyApplication.getinstance().getSession().getSubscriber().getSubscriberId());
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }//OnCreate end

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PostsFragment(), "Posts");
        adapter.addFragment(new AgentsFragment(), "Agents");
        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new ShopsFragment(), "Ongoing");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(listener);


        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                MyApplication.getinstance().getSession().setSubscriber(null);
                MyApplication.getinstance().getSession().setAgent(null);
                MyApplication.getinstance().getSession().setNeg(null);
                MyApplication.getinstance().getSession().setWallet(null);
                MyApplication.getinstance().setLstNots(Collections.<Notification>emptyList());
                //STOP ALL NOTIFICATIONS SERVICES
                stopService(new Intent(this, FirebaseNotificationServices.class));

                MyApplication.getinstance().getSession().setVerification(null);
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, ProfileScreen.class));
                return true;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.agent_reg:
                if (MyApplication.getinstance().getSession().getAgent() != null) {
                    Toast.makeText(this, "Already registered as an Agent", Toast.LENGTH_SHORT).show();

                } else {
                    startActivity(new Intent(this, FinishAccSetup.class));
                }
                return true;
            case R.id.refresh:
                startActivity(new Intent(this, SplashScreen.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {

            // newText is text entered by user to SearchView
            Log.d("", newText);
            return false;
        }
    };
    public List<Subscriber> subsList;

    public void searchSub(int id) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                DTransUrls.Subscribers + "/" + id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                subsList = SubscribersParser.parseFeed(response);
                //LOAD PENDING
                filWithPending();
                MyApplication.getinstance().getSession().setSubscriber(subsList.get(0));
                Toast.makeText(MainActivity.this, "Subscriber Updated", Toast.LENGTH_SHORT).show();
                if (MyApplication.getinstance().getSession().getSubscriber().getStatus().contains("Active")) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());
            }
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }
        };


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }

    List<Post> datta = new ArrayList<>();
    List<Post> data = new ArrayList<>();

    public void filWithPending() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                com.pamsillah.wakho.app_settings.ConnectionConfig.BASE_URL + "/api/pendingPost", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                datta = PostsParser.parseFeed(response);
                Agent aget = MyApplication.getinstance().getSession().getAgent();
                Subscriber sub = MyApplication.getinstance().getSession().getSubscriber();

                // callback.onSuccess(publications);
                if (datta.size() > 0) {
                    for (Post p : datta
                            ) {


                        if ((p.getSubscriberId().equals(String.valueOf(sub.getSubscriberId())) && (p.getStatus().contains("Waiting for Delivery") | p.getStatus().contains("Pay Initiated") | p.getStatus().contains("Created")))) {
                            for (Agent a : MyApplication.getinstance().getLstAgent()) {
                                if (String.valueOf(a.getAgentId()).equals(p.getAgentID())) {

                                    p.setStatus(p.getStatus() + "#" + a.getCompanyName() + "#" + a.getCompanyLogo() + "#" + sub.getName() + " " + sub.getSurname());
                                    data.add(p);
                                    break;
                                }
                            }


                        } else if ((aget != null) && (String.valueOf(aget.getAgentId()).equals(p.getAgentID())) && (p.getStatus().contains("Waiting for Delivery") | p.getStatus().contains("Pay Initiated") | p.getStatus().contains("Pay Initiated"))) {
                            for (Subscriber a : MyApplication.getinstance().getSubscribers()
                                    ) {
                                if (String.valueOf(a.getSubscriberId()).equals(p.getSubscriberId())) {
                                    p.setStatus(p.getStatus() + "#" + aget.getCompanyName() + "#" + aget.getCompanyLogo() + "#" + sub.getName() + " " + sub.getSurname());

                                    data.add(p);
                                    break;
                                }
                            }

                        } else {

                            if (p.getDescription().contains(sub.getPhone().replace("+263", "0").replace(" ", ""))) {

                                for (Agent a : MyApplication.getinstance().getLstAgent()) {
                                    if (String.valueOf(a.getAgentId()).equals(p.getAgentID())) {

                                        p.setStatus(p.getStatus() + "#" + a.getCompanyName() + "#" + a.getCompanyLogo());

                                        break;
                                    }
                                }

                                for (Subscriber a : MyApplication.getinstance().getSubscribers()
                                        ) {
                                    if (String.valueOf(a.getSubscriberId()).equals(p.getSubscriberId())) {
                                        p.setStatus(p.getStatus() + "#" + sub.getName() + " " + sub.getSurname());


                                        break;
                                    }
                                }

                                data.add(p);
                            }


                        }

                    }

                    MyApplication.getinstance().listPost.addAll(data);


                } else {


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());
            }
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                return AuthHeader.getHeaders();
            }
        };


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }
}

