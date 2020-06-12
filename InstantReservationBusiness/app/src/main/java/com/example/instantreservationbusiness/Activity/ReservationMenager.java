package com.example.instantreservationbusiness.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instantreservationbusiness.Queue;
import com.example.instantreservationbusiness.QueueAdapterRecycler;
import com.example.instantreservationbusiness.R;
import com.example.instantreservationbusiness.Reservation;
import com.example.instantreservationbusiness.ReservationAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

public class ReservationMenager extends AppCompatActivity {

    TextView queue_title, n_reservation;
    Button btn_next;

    DatabaseReference referenceReservation;
    DatabaseReference referenceQueueinfo;
    DatabaseReference referenceUserInfo;

    List<Reservation> list;
    RecyclerView reservations;
    ReservationAdapter reservationAdapter;

    String businessID;
    String queueID;

    SharedPreferences sharedPreferences;


//    Reservation reservation = new Reservation("alkhslda", "asldalskj", "tavolo per 4", 4);

    @Override
    public void onStart() {
        super.onStart();

        list = new ArrayList<Reservation>();

        referenceQueueinfo = FirebaseDatabase.getInstance().getReference().child("queues").child(queueID);

        referenceQueueinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getKey());
                queue_title.setText(dataSnapshot.child("queue_name").getValue(String.class));
                n_reservation.setText(Integer.toString(dataSnapshot.child("queue_nReservation").getValue(Integer.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get data from database
        referenceReservation = FirebaseDatabase.getInstance().getReference().child("reservations").child(queueID);
        Query query = referenceReservation.orderByChild("ticket");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Reservation r = dataSnapshot.getValue(Reservation.class);
                r.setId_queue(queueID);
                r.setId_user(dataSnapshot.getKey());
                referenceUserInfo = FirebaseDatabase.getInstance().getReference().child("users").child(r.getId_user());
                referenceUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        r.setUser_name(dataSnapshot.child("fullname").getValue(String.class));
                        r.setUser_phone(dataSnapshot.child("phone").getValue(String.class));

                        list.add(list.size(), r);
                        System.out.println("Adding in queue list user: " + dataSnapshot.child("fullname").getValue(String.class));
                        reservationAdapter = new ReservationAdapter(ReservationMenager.this, (ArrayList<Reservation>) list);
                        reservations.setAdapter(reservationAdapter);
                        reservationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                final String userID =  dataSnapshot.getKey();
                //Reservation r = dataSnapshot.getValue(Reservation.class);
                System.out.println("Removing from queue reservation from user: " + userID);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId_user().equals(userID)) {
                        list.remove(i);
                    }
                }

                reservationAdapter = new ReservationAdapter(ReservationMenager.this, (ArrayList<Reservation>) list);
                reservations.setAdapter(reservationAdapter);
                reservationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_menager);

        Intent intent = getIntent();
        queueID = intent.getStringExtra("payload");
        sharedPreferences = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);
        businessID = sharedPreferences.getString("businessID", null);

        queue_title = findViewById(R.id.queue_title);
        n_reservation = findViewById(R.id.nReservation);
        reservations = findViewById(R.id.reservations);
        btn_next = findViewById(R.id.btn_next);

        reservations.setLayoutManager(new LinearLayoutManager(this));

        //list.add(reservation);

        //reservationAdapter = new ReservationAdapter(ReservationMenager.this, (ArrayList<Reservation>) list);
        //reservations.setAdapter(reservationAdapter);
        //reservationAdapter.notifyDataSetChanged();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = list.get(0).getId_user();
                FirebaseDatabase.getInstance().getReference().child("reservations").child(queueID).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            referenceQueueinfo.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int nReservation = dataSnapshot.child("queue_nReservation").getValue(Integer.class);
                                    referenceQueueinfo.child("queue_nReservation").setValue(nReservation-1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if(task1.isSuccessful()) {
                                                FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("reservedQueue").child(queueID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task2) {
                                                        if(task2.isSuccessful()){
                                                            sendToToken();
                                                            Toast.makeText(getApplicationContext(), "next!", Toast.LENGTH_LONG).show();
                                                            reservationAdapter = new ReservationAdapter(ReservationMenager.this, (ArrayList<Reservation>) list);
                                                            reservations.setAdapter(reservationAdapter);
                                                            reservationAdapter.notifyDataSetChanged();
                                                        }  else Toast.makeText(getApplicationContext(), "There is an error", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }  else Toast.makeText(getApplicationContext(), "There is an error", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else Toast.makeText(getApplicationContext(), "There is an error", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }

    public void sendToToken(){
        // [START send_to_token]
        // This registration token comes from the client FCM SDKs.
        String registrationToken = "csNG12jgma4:APA91bEXgM9WT0j6LkE05t9y3QfUzneCzX_isZzIwgCIpd8tBU1LKUw-AuIZppKaUlMCODCosqQGvdrGeWqToOtBHJRfY78swOUJnIGXspVbI0lbSYVkqE4eUy4V_57nbeiehJevaO4Y";

        // See documentation on defining a message payload.
        RemoteMessage message = new RemoteMessage.Builder(registrationToken).addData("title","jko").build();

        // Send a message to the device corresponding to the provided
        // registration token.
        FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + message.getData().toString());
        // [END send_to_token]
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();


    }
}
