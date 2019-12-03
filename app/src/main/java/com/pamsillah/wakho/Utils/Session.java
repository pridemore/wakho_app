package com.pamsillah.wakho.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.pamsillah.wakho.Models.Agent;
import com.pamsillah.wakho.Models.NegotiationsRoot;
import com.pamsillah.wakho.Models.Subscriber;
import com.pamsillah.wakho.Models.Wallet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class Session {
    //WALLET
    public Wallet getWallet() {
        Wallet wallet;
        if (sp.contains("Walle")) {

            String json = sp.getString("Walle", null);
            Gson gson = new Gson();
            wallet = gson.fromJson(json, Wallet.class);
        } else {

            return null;
        }
        return wallet;
    }

    public void setWallet(Wallet neg) {
        spEditor = sp.edit();
        Gson gson = new Gson();
        spEditor.putString("Walle", gson.toJson(neg));

        spEditor.commit();
    }


    public NegotiationsRoot getNeg() {
        NegotiationsRoot negotiationsRoot = new NegotiationsRoot();
        if (sp.contains("Neg")) {

            String json = sp.getString("Neg", null);
            Gson gson = new Gson();
            negotiationsRoot = gson.fromJson(json, NegotiationsRoot.class);
        } else {

            return null;
        }
        return negotiationsRoot;
    }

    public void setNeg(NegotiationsRoot neg) {
        spEditor = sp.edit();
        Gson gson = new Gson();
        spEditor.putString("Neg", gson.toJson(neg));

        spEditor.commit();
    }

    public Agent getAgent() {
        Agent agen = new Agent();
        if (sp.contains("Agent")) {

            String json = sp.getString("Agent", null);
            Gson gson = new Gson();
            agen = gson.fromJson(json, Agent.class);
        } else {

            return null;
        }
        return agen;
    }

    public void setAgent(Agent agent) {
        spEditor = sp.edit();
        Gson gson = new Gson();
        spEditor.putString("Agent", gson.toJson(agent));

        spEditor.commit();
    }

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public Session(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void setSubscriber(Subscriber subscriber) {
        spEditor = sp.edit();
        Gson gson = new Gson();
        spEditor.putString("Bonasi", gson.toJson(subscriber));
        spEditor.putString("isAgent", "false");
        spEditor.commit();


    }

    public Subscriber getSubscriber() {
        Subscriber subscriber = new Subscriber();
        if (sp.contains("Bonasi")) {

            String json = sp.getString("Bonasi", null);
            Gson gson = new Gson();
            subscriber = gson.fromJson(json, Subscriber.class);
        } else {

            return null;
        }
        return subscriber;
    }

    public void setVerification(String verify) {
        spEditor = sp.edit();

        spEditor.putString("Verification", verify);
        spEditor.commit();


    }

    public String getVerification() {
        String verification = new String();
        if (sp.contains("Verification")) {

            verification = sp.getString("Verification", null);


        } else {

            return null;
        }
        return verification;
    }


    public List<Subscriber> getSubs() {
        List<Subscriber> negotiationsRoot = new ArrayList<>();
        if (sp.contains("Subs")) {

            String json = sp.getString("Subs", null);
            Gson gson = new Gson();
            negotiationsRoot = gson.fromJson(json, new ArrayList<Subscriber>().getClass());
        } else {

            return null;
        }
        return negotiationsRoot;
    }

    public void setSubs(List<Subscriber> neg) {
        spEditor = sp.edit();
        Gson gson = new Gson();
        spEditor.putString("Subs", gson.toJson(neg));

        spEditor.commit();
    }


}
