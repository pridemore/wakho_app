package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class ShopItems {
    public int ItemID;
    public String Name;

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBar_Code() {
        return Bar_Code;
    }

    public void setBar_Code(String bar_Code) {
        Bar_Code = bar_Code;
    }

    public String getUnique_ID() {
        return Unique_ID;
    }

    public void setUnique_ID(String unique_ID) {
        Unique_ID = unique_ID;
    }

    public String getDate_Updated() {
        return Date_Updated;
    }

    public void setDate_Updated(String date_Updated) {
        Date_Updated = date_Updated;
    }

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int shopID) {
        ShopID = shopID;
    }

    public Double Price;
    public String Bar_Code;
    public String Unique_ID;
    public String Date_Updated;
    public int ShopID;

}
