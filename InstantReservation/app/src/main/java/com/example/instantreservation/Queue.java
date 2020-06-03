package com.example.instantreservation;

public class Queue {
    String queue_name;
    String queue_description;
    String queue_business;
    String queue_businessID;
    String queue_QRCodeImage;
    String queue_image;
    String queue_city;
    String queue_businessID;

    int queue_nMaxReservation;
    int queue_nReservation;

    public Queue(){

    }

    public Queue(String queue_name, String queue_description, String queue_business, String queue_businessID, String queue_QRCodeImage, String queue_image, String queue_city, int queue_nMaxReservation, int queue_nReservation) {
        this.queue_name = queue_name;
        this.queue_description = queue_description;
        this.queue_business = queue_business;
        this.queue_businessID = queue_businessID;
        this.queue_QRCodeImage = queue_QRCodeImage;
        this.queue_image = queue_image;
        this.queue_city = queue_city;
        this.queue_businessID = queue_businessID;
        this.queue_nMaxReservation = queue_nMaxReservation;
        this.queue_nReservation = queue_nReservation;
    }

    public String getQueue_businessID() {
        return queue_businessID;
    }

    public void setQueue_businessID(String queue_businessID) {
        this.queue_businessID = queue_businessID;
    }

    public String getQueue_name() {
        return queue_name;
    }

    public void setQueue_name(String queue_name) {
        this.queue_name = queue_name;
    }

    public String getQueue_description() {
        return queue_description;
    }

    public void setQueue_description(String queue_description) {
        this.queue_description = queue_description;
    }

    public String getQueue_business() {
        return queue_business;
    }

    public void setQueue_business(String queue_business) {
        this.queue_business = queue_business;
    }

    public String getQueue_QRCodeImage() {
        return queue_QRCodeImage;
    }

    public void setQueue_QRCodeImage(String queue_QRCodeImage) {
        this.queue_QRCodeImage = queue_QRCodeImage;
    }

    public String getQueue_image() {
        return queue_image;
    }

    public void setQueue_image(String queue_image) {
        this.queue_image = queue_image;
    }

    public String getQueue_city() {
        return queue_city;
    }

    public void setQueue_city(String queue_city) {
        this.queue_city = queue_city;
    }

    public String getQueue_nMaxReservationString() {
        return Integer.toString(queue_nMaxReservation);
    }

    public int getQueue_nMaxReservation() {
        return queue_nMaxReservation;
    }

    public void setQueue_nMaxReservation(int queue_nMaxReservation) {
        this.queue_nMaxReservation = queue_nMaxReservation;
    }

    public String getQueue_nReservationString() {
        return Integer.toString(queue_nReservation);
    }

    public int getQueue_nReservation() {
        return queue_nReservation;
    }

    public void setQueue_nReservation(int queue_nReservation) {
        this.queue_nReservation = queue_nReservation;
    }
}
