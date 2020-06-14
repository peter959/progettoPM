package com.example.instantreservation;

public class Reservation {
    String id_queue;
    String id_business;
    String id_user;
    int ticket;
    String note;
    String business_name;
    String queue_name;

    public Reservation(String id_user, int ticket, String note) {
        this.id_user = id_user;
        this.ticket = ticket;
        this.note = note;
    }

    public Reservation(int ticket, String note) {
        this.ticket = ticket;
        this.note = note;
    }

    public String getId_business() {
        return id_business;
    }

    public void setId_business(String id_business) {
        this.id_business = id_business;
    }


    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }


    public String getQueue_name() {
        return queue_name;
    }

    public void setQueue_name(String queue_name) {
        this.queue_name = queue_name;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId_queue() {
        return id_queue;
    }

    public void setId_queue(String id_queue) {
        this.id_queue = id_queue;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
