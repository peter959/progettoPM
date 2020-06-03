package com.example.instantreservation;

public class Queue {
    final String queue_name;
    final String queue_description;
    final String queue_business;
    final int queue_nMaxReservation;
    final int queue_nReservation;



    public Queue(String queue_name, String queue_description, String queue_business, int queue_nMaxReservation, int queue_nReservation) {
        this.queue_name = queue_name;
        this.queue_description = queue_description;
        this.queue_business = queue_business;
        this.queue_nMaxReservation = queue_nMaxReservation;
        this.queue_nReservation = queue_nReservation;
    }

    public String getQueue_name() {
        return queue_name;
    }

    public String getQueue_description() {
        return queue_description;
    }

    public String getQueue_business() {
        return queue_business;
    }

    public int getQueue_nMaxReservation() {
        return queue_nMaxReservation;
    }

    public int getQueue_nReservation() {
        return queue_nReservation;
    }
}
