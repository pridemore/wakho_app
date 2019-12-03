package com.pamsillah.wakho.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.Post;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Models.TransactionFee;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.R;

import java.util.List;

/**
 * Created by .Net Developer on 9/9/2017.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    TransactionAdapter.OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private List<TransactionFee> mylist;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View textView;
        public TextView to, date, ttile, status, pawnoyref, item, paystat, amount;
        CardView cardView;

        public ViewHolder(View v) {

            super(v);
            textView = v;
            amount = (TextView) v.findViewById(R.id.amount);
            to = (TextView) v.findViewById(R.id.to);
            date = (TextView) v.findViewById(R.id.date);
            ttile = (TextView) v.findViewById(R.id.ttile);
            status = (TextView) v.findViewById(R.id.status);
            pawnoyref = (TextView) v.findViewById(R.id.paynowreff);
            paystat = (TextView) v.findViewById(R.id.paystat);
            item = (TextView) v.findViewById(R.id.item);


        }
    }

    public TransactionAdapter(List<TransactionFee> ds) {

        mylist = ds;
    }

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.his_money_card, parent, false);

        TransactionAdapter.ViewHolder viewHolder = new TransactionAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TransactionAdapter.ViewHolder holder, int position) {
        final TransactionFee bfo = mylist.get(position);
        Post pp = null;
        for (Post post : MyApplication.getinstance().getListPost()
                ) {
            if (String.valueOf(post.getPostId()).equals(bfo.getPostID())) {
                pp = post;
                break;
            }
        }


        Agent agent = MyApplication.getinstance().getSession().getAgent();
        Subscriber s = MyApplication.getinstance().getSession().getSubscriber();


        if (agent != null) {
            if (String.valueOf(agent.getAgentId()).equals(bfo.getAgentID())) {
                holder.to.setText("To        :         You");
            }

        } else {
            holder.to.setText("From        :         You");

        }
        holder.date.setText("Transacted On   :   " + bfo.getDate());
        if (pp != null) {
            holder.ttile.setText("Transaction History for " + pp.getTitle());

            holder.item.setText("For   :   " + pp.getTitle());
        } else {
            holder.ttile.setText("Transaction History for  Post " + bfo.getPostID());
            holder.item.setText("For   :   " + bfo.getPostID());
        }
        holder.status.setText("Status   :   " + bfo.getStatus());
        holder.pawnoyref.setText("Paynow Refference   :   " + bfo.getPaymentReff());


        holder.amount.setText("Amount   :   " + bfo.getAmount());


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


    public void setLoadMoreListener(TransactionAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
 

