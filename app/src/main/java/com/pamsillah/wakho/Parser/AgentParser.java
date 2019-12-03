package com.pamsillah.wakho.Parser;

import com.pamsillah.wakho.Models.Agent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psillah on 4/10/2017.
 */
public class AgentParser {


    public static List<Agent> parseFeed(JSONArray array) {
        List<Agent> Agents = new ArrayList<Agent>();
        try {

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Agent sub = new Agent();
                sub.setAgentId(Integer.parseInt(obj.getString("AgentId")));
                sub.setCompanyAdress(obj.getString("CompanyAdress"));
                sub.setCompanyLogo(obj.getString("CompanyLogo"));
                sub.setCompanyName(obj.getString("CompanyName"));
                sub.setCompanyTel(obj.getString("CompanyTel"));
                sub.setIDpic(obj.getString("IDpic"));
                sub.setProofRes(obj.getString("ProofRes"));
                sub.setBankName(obj.getString("BankName"));
                sub.setAccNumber(obj.getString("Account_Number"));
                sub.setProofRes(obj.getString("ProofRes"));
                sub.setCompanyRegNumber(obj.getString("Bank_Adress"));
                sub.setSubscriberId(obj.getString("SubscriberId"));
                Agents.add(sub);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Agents;
    }
}
