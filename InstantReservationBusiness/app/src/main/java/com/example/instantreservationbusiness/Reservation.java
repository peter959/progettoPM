package com.example.instantreservationbusiness;

public class Reservation {
    String userUID;
    String queueID;

    public Reservation(String userUID, String queueID) {
        this.userUID = userUID;
        this.queueID = queueID;
    }

    public Reservation() {
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
