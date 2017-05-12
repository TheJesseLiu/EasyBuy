package com.example.ee4901.project2;

/**
 * Created by Jesse on 4/28/17.
 */

public class MyData {
    private String price;
    private String userId, contact, itemName, link, longitude, latitude;



    public MyData(String price, String userId, String contact, String itemName, String link, String longitude, String latitude) {
        this.price = price;
        this.userId = userId;
        this.contact = contact;
        this.itemName = itemName;
        this.link = link;
        this.longitude = longitude;
        this.latitude = latitude;

    }
    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
    public String getPrice() {
        return price;
    }

    public String getUserId() {
        return userId;
    }

    public String getContact() {
        return contact;
    }

    public String getItemName() {
        return itemName;
    }

    public String getLink() {
        return link;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
