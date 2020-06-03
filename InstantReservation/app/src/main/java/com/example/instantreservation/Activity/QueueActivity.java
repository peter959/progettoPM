package com.example.instantreservation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.instantreservation.Queue;
import com.example.instantreservation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class QueueActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        TextView queue_id = findViewById(R.id.queue_id);
        Intent intent = getIntent();
        String id = intent.getStringExtra("payload");
        queue_id.setText(id);

        DatabaseReference ref = database.getReference("instant-reservation-6c2a2/codeattivita/"+id);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Queue queue = dataSnapshot.getValue(Queue.class);

                System.out.println("AaaaaAAAAAAAAAAAAAAAAAAAAAAAAA" + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }




}
