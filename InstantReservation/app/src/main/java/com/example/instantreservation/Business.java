package com.example.instantreservation;

public class Business {

    String business_city;
    String business_description;
    String business_image;
    int business_nQueues;
    String business_name;
    String business_ID;

    public Business() {

    }

    public Business(String business_ID, String business_city, String business_description, String business_image, int business_nQueues, String business_name) {
        this.business_ID = business_ID;
        this.business_city = business_city;
        this.business_description = business_description;
        this.business_image = business_image;
        this.business_nQueues = business_nQueues;
        this.business_name = business_name;
    }

    public String getBusiness_ID() {
        return business_ID;
    }

    public void setBusiness_ID(String business_ID) {
        this.business_ID = business_ID;
    }

    public String getBusiness_city() {
        return business_city;
    }

    public void setBusiness_city(String business_city) {
        this.business_city = business_city;
    }

    public String getBusiness_description() {
        return business_description;
    }

    public void setBusiness_description(String business_description) {
        this.business_description = business_description;
    }

    public String getBusiness_image() {
        return business_image;
    }

    public void setBusiness_image(String business_image) {
        this.business_image = business_image;
    }

    public int getBusiness_nQueues() {
        return business_nQueues;
    }

    public String getBusiness_nQueuesString() {
        return Integer.toString(business_nQueues);
    }

    public void setBusiness_nQueues(int business_nQueues) {
        this.business_nQueues = business_nQueues;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }
}
