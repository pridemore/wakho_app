package com.pamsillah.wakho.Parser;

import com.pamsillah.wakho.Models.TransactionFee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by .Net Developer on 9/9/2017.
 */

public class TransactionsParser {
    public static List<TransactionFee> parseFeed(JSONArray array) {
        List<TransactionFee> fees = new ArrayList<>();
        try {

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                TransactionFee sub = new TransactionFee();
                sub.setTransID(Integer.parseInt(obj.getString("transID")));
                sub.setSubmitted(obj.getString("Submitted"));
                sub.setAmount(Double.parseDouble(obj.getString("amount")));
                sub.setAgentID(obj.getString("AgentID"));
                sub.setDate(obj.getString("Date"));
                sub.setTime(obj.getString("Time"));
                sub.setPaymentReff(obj.getString("paymentReff"));
                sub.setStatus(obj.getString("Status"));
                sub.setPostID(Integer.parseInt(obj.getString("postID")));
                sub.setReff_Number(obj.getString("reff_Number"));
                sub.setPollUrl(obj.getString("PollURL"));
                sub.setHash(obj.getString("Hash"));

                fees.add(sub);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return fees;
    }
}
