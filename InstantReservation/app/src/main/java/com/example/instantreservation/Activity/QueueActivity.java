package com.example.instantreservation.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.instantreservation.MyDoes;
import com.example.instantreservation.Queue;
import com.example.instantreservation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;



public class QueueActivity extends AppCompatActivity {

    DatabaseReference reference;
    TextView queue_name;
    TextView queue_business;
    TextView queue_city;
    TextView queue_nReservation;
    TextView queue_description ;
    ImageView queue_QRCodeImage;
    ImageView queue_image;

    ProgressBar progressBarQueue;
    LinearLayout queue_layout;

    String id;

    @Override
    public void onStart() {
        super.onStart();

        queue_layout.setVisibility(View.GONE);
        progressBarQueue.setVisibility(View.VISIBLE);

        // Check if user is signed in (non-null) and update UI accordingly.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Queue queue = dataSnapshot.child(id).getValue(Queue.class);
                queue_business.setText(queue.getQueue_business());
                queue_city.setText(queue.getQueue_city());
                queue_description.setText(queue.getQueue_description());
                queue_name.setText(queue.getQueue_name());
                queue_nReservation.setText(queue.getQueue_nReservationString());
                //queue_image.setImageURI(queue.getQueue_image());
                //queue_QRCodeImage.setImageURI();

                queue_layout.setVisibility(View.VISIBLE);
                progressBarQueue.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        Toolbar toolbar = findViewById(R.id.toolbar_queue);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("");


        queue_business = findViewById(R.id.queue_business);
        queue_name = findViewById(R.id.queue_name);
        queue_city = findViewById(R.id.queue_city);
        queue_nReservation = findViewById(R.id.queue_nReservation);
        queue_description = findViewById(R.id.queue_description);
        queue_QRCodeImage = findViewById(R.id.queue_QRCodeImage);
        queue_image = findViewById(R.id.queue_image);

        progressBarQueue = findViewById(R.id.progressBarQueue);
        queue_layout = findViewById(R.id.queue_layout);

        Intent intent = getIntent();
        id = intent.getStringExtra("payload");

        reference = FirebaseDatabase.getInstance().getReference().child("codeattivita");



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
