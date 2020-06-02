package com.example.instantreservation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.instantreservation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QueueActivity extends AppCompatActivity {

    final String queue_id;
    final String queue_name;
    final String queue_description;
    final String queue_business;
    final int queue_nMaxReservation;
    final int queue_nReservation;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public QueueActivity(String queue_id, String queue_name, String queue_description, String queue_business, int queue_nMaxReservation, int queue_nReservation) {
        this.queue_id = queue_id;
        this.queue_name = queue_name;
        this.queue_description = queue_description;
        this.queue_business = queue_business;
        this.queue_nMaxReservation = queue_nMaxReservation;
        this.queue_nReservation = queue_nReservation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

       // queue_id = findViewById(R.id.queue_id);
        Intent intent = getIntent();
        String id = intent.getStringExtra("payload");
        //queue_id.setText(id);

        DatabaseReference ref = database.getReference("instant-reservation-6c2a2/codeattivita/"+id);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                QueueActivity queue = dataSnapshot.getValue(QueueActivity.class);
                System.out.println(queue.queue_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }




}
