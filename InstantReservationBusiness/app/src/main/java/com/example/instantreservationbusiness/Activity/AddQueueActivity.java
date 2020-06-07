package com.example.instantreservationbusiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.instantreservationbusiness.ProgressButton;
import com.example.instantreservationbusiness.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class AddQueueActivity extends AppCompatActivity {

    private SharedPreferences userInfo;

    EditText add_queue_name, add_queue_desc, add_queue_maxReserv;
    DatabaseReference reference;

    Button btn_cancel;
    View btn_create;
    ImageButton add_queue_image;

    //generate queueID
    Integer queueNum = new Random().nextInt();


    ProgressButton progressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_queue);


        userInfo = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);

        btn_create = findViewById(R.id.btn_create);
        btn_cancel = findViewById(R.id.btn_cancel);
        progressButton = new ProgressButton(AddQueueActivity.this, btn_create, "Create Now");

        add_queue_desc = findViewById(R.id.add_queue_desc);
        add_queue_name = findViewById(R.id.add_queue_name);
        add_queue_maxReserv = findViewById(R.id.add_queue_maxReserv);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressButton.buttonActivated();
                reference = FirebaseDatabase.getInstance().getReference().child("queues").child("queue" + queueNum);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("queue_name").setValue(add_queue_name.getText().toString());
                        dataSnapshot.getRef().child("queue_description").setValue(add_queue_desc.getText().toString());
                        dataSnapshot.getRef().child("queue_nMaxReservation").setValue(add_queue_maxReserv.getText().toString());
                        dataSnapshot.getRef().child("queue_businessID").setValue(userInfo.getString("businessUID", "null"));
                        dataSnapshot.getRef().child("queue_business").setValue(userInfo.getString("businessName", "null"));
                        dataSnapshot.getRef().child("queue_city").setValue(userInfo.getString("businessCity", "null"));
                        dataSnapshot.getRef().child("queue_image").setValue("image path");
                        dataSnapshot.getRef().child("queue_QRCodeImage").setValue("QR image path");
                        dataSnapshot.getRef().child("queue_nReservation").setValue(0);

                        progressButton.buttonFinished("Queue added!");
                        Intent a = new Intent(AddQueueActivity.this, MainActivity.class);
                        startActivity(a);
                        finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(AddQueueActivity.this, MainActivity.class);
                startActivity(a);
                finish();
            }
        });

    }
}
