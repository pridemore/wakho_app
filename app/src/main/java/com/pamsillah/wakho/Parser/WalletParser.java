package com.pamsillah.wakho.Parser;


import com.pamsillah.wakho.Models.Wallet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psillah on 4/10/2017.
 */
public class WalletParser {


    public static List<Wallet> parseFeed(JSONArray array) {
        List<Wallet> wallets = new ArrayList<Wallet>();
        try {

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Wallet sub = new Wallet();
                sub.setAgentID(obj.getString("AgentID"));
                sub.setOveral(obj.getString("Overal"));
                sub.setDrawable(obj.getString("Drawable"));
                sub.setPending(obj.getString("Pending"));
                sub.setTotal(obj.getString("Total"));
                sub.setID(obj.getString("ID"));


                sub.setAvailable(obj.getString("Available"));

                wallets.add(sub);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return wallets;
    }

    public static Wallet parseWallet(String reg) {
        Wallet sub = new Wallet();
        try {
            JSONObject obj = new JSONObject(reg);


            sub.setAgentID(obj.getString("AgentID"));
            sub.setOveral(obj.getString("Overal"));
            sub.setDrawable(obj.getString("Drawable"));
            sub.setPending(obj.getString("Pending"));
            sub.setTotal(obj.getString("Total"));
            sub.setID(obj.getString("ID"));


            sub.setAvailable(obj.getString("Available"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sub;
    }

}
