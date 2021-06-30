package com.pamsillah.wakho;

import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by psillah on 3/24/2017.
 */
public class View_Holder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView title, destination, departTime, depart;
    //TextView description;
    ImageView imageView;
    View view;
    Button btn;

    View_Holder(View itemView) {
        super(itemView);
        view = itemView;
        cv = (CardView) itemView.findViewById(R.id.cv);
        title = (TextView) itemView.findViewById(R.id.agents_name);
        destination = (TextView) itemView.findViewById(R.id.destination);
        depart = (TextView) itemView.findViewById(R.id.depart);
        departTime = (TextView) itemView.findViewById(R.id.departDate);

        imageView = (ImageView) itemView.findViewById(R.id.postImage);
        imageView = (ImageView) itemView.findViewById(R.id.agent_img);
        btn = (Button) itemView.findViewById(R.id.send);

        view.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ItemSingle.class);
                view.getContext().startActivity(intent);

            }
        });


    }
}
