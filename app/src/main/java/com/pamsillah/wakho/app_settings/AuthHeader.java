package com.pamsillah.wakho.app_settings;

import java.util.HashMap;
import java.util.Map;

public final class AuthHeader {
    private static final String authToken = "XX123456789#@";

    public static Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", authToken);
        headers.put("Content-Type", "application/json");

        return headers;
    }
}
