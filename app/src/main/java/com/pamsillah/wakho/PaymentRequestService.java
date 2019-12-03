package com.pamsillah.wakho;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pamsillah.wakho.app_settings.AuthHeader;
import com.pamsillah.wakho.app_settings.ConnectionConfig;

public class PaymentRequestService {
    static String message = null;

    public static String requestPayment(int agentId) {


        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                ConnectionConfig.BASE_URL + "/api/PaymentRequests?agent_id=" + agentId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {

                    message = "Success";
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message = error.toString();

            }
        }) {

            @Override
            public java.util.Map<String, String> getHeaders() {
                return AuthHeader.getHeaders();
            }


        };
        return message;
    }
}
