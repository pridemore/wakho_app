package com.pamsillah.wakho.Models;

/**
 * Created by psillah on 8/24/2017.
 */

public class Notifications {
    public String nTitle;
    public String nDesc;
    public int nImage;

    public Notifications(String nTitle, String nDesc, int nImage) {
        this.nTitle = nTitle;
        this.nDesc = nDesc;
        this.nImage = nImage;
    }

    public String getnTitle() {
        return nTitle;
    }

    public void setnTitle(String nTitle) {
        this.nTitle = nTitle;
    }

    public String getnDesc() {
        return nDesc;
    }

    public void setnDesc(String nDesc) {
        this.nDesc = nDesc;
    }

    public int getnImage() {
        return nImage;
    }

    public void setnImage(int nImage) {
        this.nImage = nImage;
    }
}
