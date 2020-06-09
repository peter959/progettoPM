package com.example.instantreservationbusiness;

public class Reservation {
    String id_queue;
    String id_user;
    int ticket;
    String note;
    String user_name;
    String user_phone;

    /*public Reservation(String id_queue, String id_user, String note, int ticket) {
        this.id_queue = id_queue;
        this.id_user = id_user;
        this.note = note;
        this.ticket = ticket;
    } */

    public Reservation(String note, int ticket) {
        this.note = note;
        this.ticket = ticket;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
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

    public Reservation() {
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
