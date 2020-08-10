package com.pamsillah.wakho.Utils;

import com.pamsillah.wakho.app_settings.ConnectionConfig;

/**
 * Created3 by .Net Developer on 24/2/2017.
 */

public class DTransUrls {


    public static final String Subscribers = ConnectionConfig.BASE_URL + "/api/Subscribers";
    public static final String UploadParcel = ConnectionConfig.BASE_URL + "/api/upload";
    public static final String PostSubscribers = ConnectionConfig.BASE_URL + "/api/Subscribers";
    public static final String PostPost = ConnectionConfig.BASE_URL + "/api/Posts/";
    public static final String PostJourney = ConnectionConfig.BASE_URL + "/api/PostsByAgents";
    public static final String usertransactions = ConnectionConfig.BASE_URL + "/api/UserTransactions?ID=";
    public static final String agentTransactions = ConnectionConfig.BASE_URL + "/api/AgentTransactions?ID=";
    public static final String transactions = ConnectionConfig.BASE_URL + "/api/TransactionFees";
    public static final String wallet = ConnectionConfig.BASE_URL + "/api/AgentWallet?id=";
    public static final String wallets = ConnectionConfig.BASE_URL + "/api/Wallets";
    public static final String HireReqs = ConnectionConfig.BASE_URL + "/api/HireRequests/";
    public static final String login = ConnectionConfig.BASE_URL + "/api/login";
    public static String agents=ConnectionConfig.BASE_URL + "/api/agents";
}
