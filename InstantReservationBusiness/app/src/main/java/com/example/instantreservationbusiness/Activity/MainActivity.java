package com.example.instantreservationbusiness.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instantreservationbusiness.Business;
import com.example.instantreservationbusiness.Queue;
import com.example.instantreservationbusiness.QueueAdapterRecycler;
import com.example.instantreservationbusiness.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences userInfo;

    private FirebaseAuth mAuth;

    DatabaseReference referenceForBusinessInfo;
    DatabaseReference referenceForBusinessQueuesInfo;

    TextView business_name;
    TextView business_description;
    TextView business_city;
    TextView business_nQueues;
    ImageView business_image;

    ImageButton editBtn;
    Button addBtn;

    ProgressBar progressBar;
    LinearLayout business_layout;

    RecyclerView recyclerView;
    QueueAdapterRecycler queueAdapter;
    List<Queue> models;

    Toolbar toolbar;

    String business_ID;

    @Override
    public void onStart() {
        super.onStart();

        business_layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // Check if user is signed in (non-null) and update UI accordingly.
        referenceForBusinessInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Business business = dataSnapshot.child(business_ID).getValue(Business.class);
                business_city.setText(business.getBusiness_city());
                business_description.setText(business.getBusiness_description());
                business_name.setText(business.getBusiness_name());
                business_nQueues.setText(business.getBusiness_nQueuesString());
                //business_image

                business_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        userInfo = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);
        business_ID = userInfo.getString("businessUID", "null");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");

        models = new ArrayList<>();

        referenceForBusinessInfo = FirebaseDatabase.getInstance().getReference().child("business");
        referenceForBusinessQueuesInfo = FirebaseDatabase.getInstance().getReference().child("queues");

        recyclerView = findViewById(R.id.business_queues);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setNestedScrollingEnabled(false);


        business_name = findViewById(R.id.business_name);
        business_city = findViewById(R.id.business_city);
        business_nQueues = findViewById(R.id.business_nQueues);
        business_description = findViewById(R.id.business_description);
        business_image = findViewById(R.id.business_image);

        progressBar = findViewById(R.id.progressBarBusiness);
        business_layout = findViewById(R.id.business_layout);

        editBtn = findViewById(R.id.edit_button);
        addBtn = findViewById(R.id.add_queue_btn);

        referenceForBusinessQueuesInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Queue queue = dataSnapshot.getValue(Queue.class);
                queue.setQueue_id(dataSnapshot.getKey());
                if(queue.getQueue_businessID().equals(business_ID)){
                    //System.out.println("Adding queue in business list: " + queue.getQueue_businessID());
                    models.add(models.size(), queue);
                    System.out.println("ADDED on Queue LIST: " + dataSnapshot.getKey());

                    queueAdapter = new QueueAdapterRecycler(MainActivity.this, (ArrayList<Queue>) models);
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

                    queueAdapter = new QueueAdapterRecycler(MainActivity.this, (ArrayList<Queue>) models);
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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddQueueActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_profile:
                new AlertDialog.Builder(this)
                        .setTitle("Log out")
                        .setMessage("you are actually connected with " + userInfo.getString("businessEmail", null) + " do you want to quit?")
                        .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
