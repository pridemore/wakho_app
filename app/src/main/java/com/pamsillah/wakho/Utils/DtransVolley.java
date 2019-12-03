package com.pamsillah.wakho.Utils;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class DtransVolley extends Application {

    private RequestQueue mRequestque;
    private static DtransVolley instance;

    public static synchronized DtransVolley getinstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public RequestQueue getmRequestque() {
        if (mRequestque == null) {
            mRequestque = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestque;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestque().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getmRequestque().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestque != null) {
            mRequestque.cancelAll(tag);
        }
    }
}
