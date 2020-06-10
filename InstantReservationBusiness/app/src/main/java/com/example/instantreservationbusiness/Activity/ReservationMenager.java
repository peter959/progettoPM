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
                FirebaseDatabase.getInstance().getReference().child("reservations").child(queueID).child( list.get(0).getId_user()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "next!", Toast.LENGTH_LONG).show();
                            reservationAdapter = new ReservationAdapter(ReservationMenager.this, (ArrayList<Reservation>) list);
                            reservations.setAdapter(reservationAdapter);
                            reservationAdapter.notifyDataSetChanged();
                        }
                        else Toast.makeText(getApplicationContext(), "There is an error", Toast.LENGTH_LONG).show();

                    }
                });


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();


    }
}
