package com.pamsillah.wakho;

import com.pamsillah.wakho.Models.Subscriber;

import java.util.List;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public interface VolleyCallback {
    void onSuccess(List<Subscriber> subscribers);
    void onSuccess(Subscriber subscriber);
    void onObjectSuccess(String object);
}
