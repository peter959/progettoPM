package com.example.instantreservation.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instantreservation.ProgressButton;
import com.example.instantreservation.Queue;
import com.example.instantreservation.R;
import com.example.instantreservation.Reservation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


@RequiresApi(api = Build.VERSION_CODES.N)
public class QueueActivity extends AppCompatActivity {

    DatabaseReference referenceForQueueInfo;
    DatabaseReference referenceForAddingReservation;
    DatabaseReference referenceForAddingCheckingIfReservationExist;

    TextView queue_name;
    TextView queue_business;
    TextView queue_city;
    TextView queue_nReservationString;
    int queue_nMaxReservation;
    int queue_nReservation;
    TextView queue_description ;
    ImageView queue_QRCodeImage;
    ImageView queue_image;

    ProgressBar progressBarQueue;
    LinearLayout queue_layout;

    View btnReserve;
    View btnRemoveReservation;
    ProgressButton reserveButton;
    ProgressButton removeReservationButton;

    String queueID;
    String userUID;
    String queueBusinessID;
    SharedPreferences sharedPreferences;

    Boolean reserved;


    @Override
    public void onStart() {
        super.onStart();

        queue_layout.setVisibility(View.GONE);
        progressBarQueue.setVisibility(View.VISIBLE);

        // Check if user is signed in (non-null) and update UI accordingly.
        referenceForQueueInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Queue queue = dataSnapshot.child(queueID).getValue(Queue.class);
                queueBusinessID = queue.getQueue_businessID();
                queue_business.setText(queue.getQueue_business());
                queue_city.setText(queue.getQueue_city());
                queue_description.setText(queue.getQueue_description());
                queue_name.setText(queue.getQueue_name());
                queue_nMaxReservation = queue.getQueue_nMaxReservation();
                queue_nReservation = queue.getQueue_nReservation();
                queue_nReservationString.setText(queue.getQueue_nReservationString() + "/" + queue.getQueue_nMaxReservation());
                //queue_image.setImageURI(queue.getQueue_image());
                //queue_QRCodeImage.setImageURI();

                referenceForAddingCheckingIfReservationExist = FirebaseDatabase.getInstance().getReference()
                        .child("reservations")
                        .child(queueBusinessID)
                        .child(queueID);

                referenceForAddingCheckingIfReservationExist.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                        if (dataSnapshot2.hasChild(userUID)) {
                            reserveButton.buttonFinished("Reserved");
                            reserved = true;
                            btnRemoveReservation.setVisibility(View.VISIBLE);
                        }else{
                            reserveButton = new ProgressButton(QueueActivity.this, btnReserve, "Pick up a ticket");
                            reserved=false;
                            btnRemoveReservation.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                queue_layout.setVisibility(View.VISIBLE);
                progressBarQueue.setVisibility(View.GONE);



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!reserved) {
                    reserveButton.buttonActivated();
                    writeNewReservation(userUID, queueID);
                    //btnRemoveReservation.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(QueueActivity.this, "You've already took a ticket!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        btnRemoveReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reserved) {
                    btnRemoveReservation.setVisibility(View.GONE);
                    removeReservation(userUID, queueID);

                    //btnRemoveReservation.setVisibility(View.VISIBLE);
                }else{

                }
            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        btnReserve = findViewById(R.id.btnReserve);
        reserveButton = new ProgressButton(QueueActivity.this, btnReserve, "Pick up a ticket");
        reserved = false;

        btnRemoveReservation = findViewById(R.id.btnRemoveReservation);
        removeReservationButton = new ProgressButton(QueueActivity.this, btnRemoveReservation, "Remove");
        removeReservationButton.buttonRemove("Remove reservation");


        Toolbar toolbar = findViewById(R.id.toolbar_queue);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("");


        queue_business = findViewById(R.id.queue_business);
        queue_name = findViewById(R.id.queue_name);
        queue_city = findViewById(R.id.queue_city);
        queue_nReservationString = findViewById(R.id.queue_nReservation);
        queue_description = findViewById(R.id.queue_description);
        queue_QRCodeImage = findViewById(R.id.queue_QRCodeImage);
        queue_image = findViewById(R.id.queue_image);

        progressBarQueue = findViewById(R.id.progressBarQueue);
        queue_layout = findViewById(R.id.queue_layout);

        Intent intent = getIntent();
        queueID = intent.getStringExtra("payload");
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userUID = sharedPreferences.getString("userUID", "null");

        referenceForQueueInfo = FirebaseDatabase.getInstance().getReference().child("codeattivita");
        referenceForAddingReservation= FirebaseDatabase.getInstance().getReference().child("reservations");


    }

    private void writeNewReservation(String userUID, final String queueID){
        //Reservation reservation = new Reservation(userUID, queueID);
       // String currentHourIn24Format = Integer.toString(rightNow.get(Calendar.HOUR_OF_DAY));
        //Task<Void> task = referenceForAddingReservation.child(queueBusinessID).child(queueID).setValue(userUID);
        if(queue_nReservation < queue_nMaxReservation) {
            DatabaseReference ref = referenceForAddingReservation.child(queueBusinessID).child(queueID).child(userUID);
            ref.child("state").setValue("pending").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        referenceForQueueInfo.child(queueID)
                                .child("queue_nReservation")
                                .setValue(queue_nReservation+1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task2) {
                                           if (task2.isSuccessful()) {
                                               reserveButton.buttonFinished("Reserved");
                                               reserved = true;
                                               btnRemoveReservation.setVisibility(View.VISIBLE);
                                           }else
                                               reserveButton.buttonFinishedUnsuccessully("Something went wrong :(");
                                       }
                                   }

                                );
                    } else reserveButton.buttonFinishedUnsuccessully("Something went wrong :(");
                }
            });
        }else {
            Toast.makeText(QueueActivity.this, "The queue is full! Retry when there is availability.", Toast.LENGTH_SHORT).show();
            reserveButton.buttonFinishedUnsuccessully("Retry");
        }
    }

    private void removeReservation(String userUID, final String queueID){
            DatabaseReference ref = referenceForAddingReservation.child(queueBusinessID).child(queueID).child(userUID);
            ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        referenceForQueueInfo.child(queueID)
                                .child("queue_nReservation")
                                .setValue(queue_nReservation-1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task2) {
                                                               if (task2.isSuccessful()) {
                                                                   reserveButton = new ProgressButton(QueueActivity.this, btnReserve, "Pick up a ticket");
                                                                   reserved = false;
                                                                   btnRemoveReservation.setVisibility(View.GONE);
                                                               }else
                                                                   removeReservationButton.buttonFinishedUnsuccessully("Something went wrong :(");
                                                           }
                                                       }

                                );
                    } else removeReservationButton.buttonFinishedUnsuccessully("Something went wrong :(");
                }
            });
    }

    //

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
