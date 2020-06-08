package com.example.instantreservationbusiness;

public class Reservation {
    String userUID;
    String queueID;
    String userName;
    String userPhone;
    String description;

    public Reservation(String userUID, String queueID) {
        this.userUID = userUID;
        this.queueID = queueID;
    }

    public Reservation() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getQueueID() {
        return queueID;
    }

    public void setQueueID(String queueID) {
        this.queueID = queueID;
    }
}
