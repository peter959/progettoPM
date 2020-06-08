package com.example.instantreservation.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.instantreservation.Business;
import com.example.instantreservation.Queue;
import com.example.instantreservation.QueueAdapterRecycler;
import com.example.instantreservation.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BusinessActivity extends AppCompatActivity {

    DatabaseReference referenceForBusinessInfo;
    DatabaseReference referenceForBusinessQueuesInfo;
    DatabaseReference referenceForQueueInfo;

    TextView business_name;
    TextView business_description;
    TextView business_city;
    TextView business_nQueues;
    ImageView business_image;
    String business_ID;
    String queue_ID;
    Business business;

    RecyclerView recyclerView;
    QueueAdapterRecycler queueAdapter;
    List<Queue> models;

    ProgressBar progressBarBusiness;
    LinearLayout business_layout;


    @Override
    public void onStart() {
        super.onStart();

        business_layout.setVisibility(View.GONE);
        progressBarBusiness.setVisibility(View.VISIBLE);

        // Check if user is signed in (non-null) and update UI accordingly.
        referenceForBusinessInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                business = dataSnapshot.child(business_ID).getValue(Business.class);
                business_city.setText(business.getBusiness_city());
                business_description.setText(business.getBusiness_description());
                business_name.setText(business.getBusiness_name());
                business_nQueues.setText(business.getBusiness_nQueuesString());
                //business_image

                business_layout.setVisibility(View.VISIBLE);
                progressBarBusiness.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceForBusinessQueuesInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Queue queue = dataSnapshot.getValue(Queue.class);
                if(queue.getQueue_businessID().equals(business_ID)){
                    //System.out.println("Adding queue in business list: " + queue.getQueue_businessID());

                    models.add(models.size(), queue);
                    System.out.println("ADDED on Favorites LIST: " + queue.getQueue_id());

                    queueAdapter = new QueueAdapterRecycler(BusinessActivity.this, models);
                    recyclerView.setAdapter(queueAdapter);
                    queueAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Queue queue = dataSnapshot.getValue(Queue.class);
                if(queue.getQueue_businessID().equals(business_ID)){
                    //System.out.println("Adding queue in business list: " + queue.getQueue_businessID());
                    for (int i = 0; i<models.size(); i++){
                        if(models.get(i).getQueue_id().equals(queue.getQueue_id())){
                            models.remove(i);
                            System.out.println("REMOVED on Business LIST: " + queue.getQueue_id());
                        }
                    }

                    queueAdapter = new QueueAdapterRecycler(BusinessActivity.this, models);
                    recyclerView.setAdapter(queueAdapter);
                    queueAdapter.notifyDataSetChanged();
                }
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
        setContentView(R.layout.activity_business);

        Toolbar toolbar = findViewById(R.id.toolbar_business);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("");


        business_name = findViewById(R.id.business_name);
        business_city = findViewById(R.id.business_city);
        business_nQueues = findViewById(R.id.business_nQueues);
        business_description = findViewById(R.id.business_description);
        business_image = findViewById(R.id.business_image);

        progressBarBusiness = findViewById(R.id.progressBarBusiness);
        business_layout = findViewById(R.id.business_layout);

        Intent intent = getIntent();
        business_ID = intent.getStringExtra("business_id");

        referenceForBusinessInfo = FirebaseDatabase.getInstance().getReference().child("business");
        referenceForBusinessQueuesInfo = FirebaseDatabase.getInstance().getReference().child("codeattivita");
        referenceForQueueInfo = FirebaseDatabase.getInstance().getReference().child("codeattivita");

        recyclerView = findViewById(R.id.busines_queues);
        recyclerView.setLayoutManager(new LinearLayoutManager(BusinessActivity.this));
        recyclerView.setNestedScrollingEnabled(false);

        models = new ArrayList<>();

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
