package com.pamsillah.wakho.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by .Net Developer on 8/31/2017.
 */

public class SingleNegAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    Context context;
    List<String> negs;

    public SingleNegAdapter(List<String> negs, Context context) {
        this.negs = negs;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
