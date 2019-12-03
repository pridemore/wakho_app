package com.pamsillah.wakho;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by psillah on 3/24/2017.
 */
public class Posts_View_Holder extends RecyclerView.ViewHolder {
    TextView title;
    TextView weight;
    TextView postTo;
    TextView postFrom;
    TextView arrDate;
    TextView price;

    ImageView imageView;
    ;
    View view;


    Posts_View_Holder(View itemView) {
        super(itemView);
        // view = itemView;
        title = (TextView) itemView.findViewById(R.id.post_title);
        postTo = (TextView) itemView.findViewById(R.id.postTo);
        postFrom = (TextView) itemView.findViewById(R.id.postFrom);
        arrDate = (TextView) itemView.findViewById(R.id.postArrDate);
        price = (TextView) itemView.findViewById(R.id.proposedFee);
        weight = (TextView) itemView.findViewById(R.id.postWeight);
        imageView = (ImageView) itemView.findViewById(R.id.postImage);


        view.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ProfileScreen.class);
                view.getContext().startActivity(intent);

            }
        });

    }
}
