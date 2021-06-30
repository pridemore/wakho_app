package com.pamsillah.wakho.Parsers.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.R;

import java.util.List;

/**
 * Created by psillah on 7/10/2017.
 */

public class AgentsAdapter extends RecyclerView.Adapter<AgentsAdapter.AgentsViewHolder> {
    private List<Agent> agents;
    private Context context;

    public AgentsAdapter(List<Agent> agents, Context context) {
        this.agents = agents;
        this.context = context;
    }

    @Override
    public AgentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agents_cards_layout, parent, false);
        return new AgentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AgentsViewHolder holder, int position) {
        Agent agentList = agents.get(position);
        holder.mAgentName.setText(agentList.getCompanyName());
    }

    @Override
    public int getItemCount() {
        return agents.size();
    }

    public class AgentsViewHolder extends RecyclerView.ViewHolder {
        public ImageView mAgentImg;
        public TextView mAgentName;
        public TextView mdestination;

        public TextView mdepatTime;
        public TextView mDatePosted;

        public AgentsViewHolder(View itemView) {
            super(itemView);

            mAgentImg = (ImageView) itemView.findViewById(R.id.agent_img);
            mAgentName = (TextView) itemView.findViewById(R.id.agents_name);
            mdestination = (TextView) itemView.findViewById(R.id.destination);
            mdepatTime = (TextView) itemView.findViewById(R.id.depart);
            mDatePosted = (TextView) itemView.findViewById(R.id.postTime);
        }
    }
}
