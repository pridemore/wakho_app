package com.pamsillah.wakho;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pamsillah.wakho.Adapters.NotificationsAdapter;
import com.pamsillah.wakho.Models.Notifications;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psillah on 3/24/2017.
 */
public class ShopsFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Notifications> notifsList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shops_activity, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.notif_rv);

        adapter = new NotificationsAdapter(notifsList, this.getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        notifications();


        return v;
    }

    private void notifications() {
        Notifications n = new Notifications("Negotiations", "A list of all the negotiations that you are currently engaged in", R.drawable.negotiate);
        notifsList.add(n);
        Notifications n1 = new Notifications("Pending", "A List of all pickups and deliveries currently awaiting transportation", R.drawable.pending);
        notifsList.add(n1);
        Notifications n2 = new Notifications("History", "A List of all transactions carried out", R.drawable.history);
        notifsList.add(n2);
        Notifications n4 = new Notifications("Hire Request", "A List of all hire requests sent to you", R.drawable.hire);
        Notifications n5 = new Notifications("Notifications", "A List of all notifications sent to you", R.drawable.notifications);
        notifsList.add(n4);
        notifsList.add(n5);
        adapter.notifyDataSetChanged();
    }


}
