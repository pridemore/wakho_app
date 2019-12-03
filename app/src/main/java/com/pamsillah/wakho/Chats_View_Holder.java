package com.pamsillah.wakho;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by psillah on 3/27/2017.
 */
public class Chats_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView user_name, content, time;
    ImageView userpic;
    View view;

    public Chats_View_Holder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        user_name = (TextView) itemView.findViewById(R.id.user_name);
        content = (TextView) itemView.findViewById(R.id.content);
        time = (TextView) itemView.findViewById(R.id.time);
        userpic = (ImageView) itemView.findViewById(R.id.picture);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ChatSingle.class);
        v.getContext().startActivity(intent);
    }

}
