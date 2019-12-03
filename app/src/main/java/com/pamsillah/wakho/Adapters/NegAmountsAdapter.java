package com.pamsillah.wakho.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pamsillah.wakho.MainActivity;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.NegModel;
import com.pamsillah.wakho.Models.NegotiationsRoot;
import com.pamsillah.wakho.Models.Notification;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.PayInit;
import com.pamsillah.wakho.R;
import com.pamsillah.wakho.Utils.Notifications.NotificationsSender;

import java.util.List;

/**
 * Created by psillah on 7/18/2017.
 */

public class NegAmountsAdapter extends RecyclerView.Adapter<NegAmountsAdapter.AmountsViewholder> {
    List<NegModel> negItems;
    Context context;

    public NegAmountsAdapter(List<NegModel> negItems, Context context) {
        this.negItems = negItems;
        this.context = context;
    }

    @Override
    public AmountsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.neg_card_view, parent, false);

        return new AmountsViewholder(v);
    }

    @Override
    public void onBindViewHolder(AmountsViewholder holder, final int position) {

        final boolean isAgent = false;
        final NegModel nm = negItems.get(position);
        final Agent agent = MyApplication.getinstance().getSession().getAgent();
        final NegotiationsRoot negotiationsRoot = MyApplication.getinstance().getSession().getNeg();

        final Subscriber subscriber = MyApplication.getinstance().getSession().getSubscriber();

        holder.negAmt.setText("$ " + nm.getAmount());
        holder.negUser.setText(nm.getSender());
        holder.negTime.setText(nm.getMessagetime());
        if (agent != null) {
            if (agent.getCompanyName().equals(nm.getSender())) {
                holder.imageView.setVisibility(View.GONE);
                holder.ac.setText("");

            } else if (subscriber.getName().contains(nm.getSender())) {
                holder.imageView.setVisibility(View.GONE);
                holder.ac.setText("");
            } else {

                holder.imageView.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View view) {

                        if ((negotiationsRoot != null) && negotiationsRoot.getID().split("_")[1].equals(String.valueOf(agent.getAgentId()))) {

                            final Dialog dialog = new Dialog(view.getContext());
                            dialog.setContentView(R.layout.layout_accept_dialog);
                            dialog.setTitle("Sent");

                            TextView dialogtxt = (TextView) dialog.findViewById(R.id.textView);
                            dialogtxt.setText("The user has been notified about your interest in their post. We will let you know once they give a response");

                            ImageView image = (ImageView) dialog.findViewById(R.id.image);
                            image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_green_check));

                            //adding button click event
                            Button dismissButton = (Button) dialog.findViewById(R.id.button);
                            dismissButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    NotificationsSender.sendNotification(negotiationsRoot.getID().split("_")[2], negotiationsRoot.getID() + "_" + subscriber.getPhone(), "Congrats Agent " + agent.getCompanyName() + " accepted Your offer of $" + nm.getAmount() + "  .Please proceed to payment or cancel the deal.",/*Message to be displayed on the notification*/
                                            " Negotiations", /*Message title*/
                                            "accept" /*Notification type, You can use this to determine what activities to stack when the receiver clicks on the notification item*/

                                    );


                                    //dialog.dismiss();
                                    Intent i = new Intent(context, MainActivity.class);
                                    context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    dialog.dismiss();
                                }
                            });


                            dialog.show();
                        } else {
                            if (negotiationsRoot.getID().split("_")[2].equals(String.valueOf(subscriber.getSubscriberId()))) {
                                Notification n = new Notification();
                                n.setConverid(negotiationsRoot.getID() + "_" + nm.getAgentNum());
                                n.setMessage("Congrats You have accepted Agent's offer of $" + negItems.get(position).getAmount() + " . Please proceed to pay or cancel the deal");
                                n.setType("accept");
                                n.setDescription("Whako notifcs");
                                MyApplication.getinstance().setNotification(n);


                                context.startActivity(new Intent(context, PayInit.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            } else {

                                Notification n = new Notification();
                                n.setConverid(negotiationsRoot.getID() + "_" + nm.getAgentNum());
                                n.setMessage("Congrats You have accepted Agent's offer of $" + negItems.get(position).getAmount() + " . Please proceed to pay or cancel the deal");
                                n.setType("accept");
                                n.setDescription("Whako notifcs");
                                MyApplication.getinstance().setNotification(n);


                                context.startActivity(new Intent(context, PayInit.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        }


                    }
                });
            }


        } else {

            if (subscriber.getName().contains(nm.getSender())) {
                holder.imageView.setVisibility(View.GONE);
                holder.ac.setText("");
            } else {

                holder.imageView.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View view) {

                        if (negotiationsRoot.getID().split("_")[2].equals(String.valueOf(subscriber.getSubscriberId()))) {
                            Notification n = new Notification();
                            n.setConverid(negotiationsRoot.getID() + "_" + nm.getAgentNum());
                            n.setMessage("Congrats You have accepted Agent's offer of $" + negItems.get(position).getAmount() + " . Please proceed to pay or cancel the deal");
                            n.setType("accept");
                            n.setDescription("Whako notifcs");
                            MyApplication.getinstance().setNotification(n);


                            context.startActivity(new Intent(context, PayInit.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                    }
                });
            }


        }
    }

    @Override
    public int getItemCount() {
        return negItems.size();
    }


    public class AmountsViewholder extends RecyclerView.ViewHolder {
        TextView negAmt, ac;
        TextView negUser;
        TextView negTime;
        ImageView imageView;


        public AmountsViewholder(View itemView) {
            super(itemView);
            ac = (TextView) itemView.findViewById(R.id.ac);
            negAmt = (TextView) itemView.findViewById(R.id.txtNegamt);
            negUser = (TextView) itemView.findViewById(R.id.txtNegUser);
            negTime = (TextView) itemView.findViewById(R.id.txtNegTym);
            imageView = (ImageView) itemView.findViewById(R.id.addToCart);

        }
    }
}
