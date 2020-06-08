package com.example.instantreservationbusiness.Activity;

import androidx.annotation.NonNull;
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

import com.example.instantreservationbusiness.Business;
import com.example.instantreservationbusiness.R;
import com.example.instantreservationbusiness.Reservation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReservationMenager extends AppCompatActivity {

    TextView queue_title, n_reservation;
    Button btn_next;

    DatabaseReference referenceReservation;
    DatabaseReference referenceUserInfo;

    ArrayList<Reservation> list;
    RecyclerView reservations;
    ReservationAdapter reservationAdapter;

    String businessID;
    String queueID;

    SharedPreferences sharedPreferences;


    Reservation reservation = new Reservation("alkhslda", "asldalskj");

    @Override
    public void onStart() {
        super.onStart();

        //get data from database
        /*referenceReservation = FirebaseDatabase.getInstance().getReference().child("reservations").child(businessID).child(queueID);
        referenceReservation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // set code to retrive data
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    final Reservation p = dataSnapshot1.getValue(Reservation.class);

                    referenceUserInfo = FirebaseDatabase.getInstance().getReference().child("users").child(p.getUserUID());
                    referenceUserInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            p.setUserPhone(dataSnapshot.child("phone").getValue(String.class));
                            p.setUserName(dataSnapshot.child("fullname").getValue(String.class));
                            list.add(p);

                            reservationAdapter = new ReservationAdapter(ReservationMenager.this, list);
                            reservations.setAdapter(reservationAdapter);
                            reservationAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //set code to show an error
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

*/

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
        n_reservation = findViewById(R.id.n_reservation);
        reservations = findViewById(R.id.reservations);
        btn_next = findViewById(R.id.btn_next);


        reservations.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        reservation.setDescription("alskda");
        reservation.setUserName("PIPPO");
        reservation.setUserPhone("342342345");
        list.add(reservation);

        reservationAdapter = new ReservationAdapter(ReservationMenager.this, list);
        reservations.setAdapter(reservationAdapter);
        reservationAdapter.notifyDataSetChanged();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "next!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
