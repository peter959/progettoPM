package com.example.instantreservationbusiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.instantreservationbusiness.Business;
import com.example.instantreservationbusiness.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    private SharedPreferences userInfo;

    DatabaseReference reference;
    TextView business_name;
    TextView business_description;
    TextView business_city;
    TextView business_nQueues;
    ImageView business_image;

    ImageButton editBtn;
    Button addBtn;

    ProgressBar progressBar;
    LinearLayout business_layout;

    String id;

    @Override
    public void onStart() {
        super.onStart();

        business_layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        System.out.println("aaaaaaaaaaaaaaaaaaaaaaa" + id);

        // Check if user is signed in (non-null) and update UI accordingly.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Business business = dataSnapshot.child(id).getValue(Business.class);
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

        userInfo = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);
        id = userInfo.getString("businessUID", "null");
        reference = FirebaseDatabase.getInstance().getReference().child("business");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("");


        business_name = findViewById(R.id.business_name);
        business_city = findViewById(R.id.business_city);
        business_nQueues = findViewById(R.id.business_nQueues);
        business_description = findViewById(R.id.business_description);
        business_image = findViewById(R.id.business_image);

        progressBar = findViewById(R.id.progressBarBusiness);
        business_layout = findViewById(R.id.business_layout);

        editBtn = findViewById(R.id.edit_button);
        addBtn = findViewById(R.id.add_queue_btn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddQueueActivity.class);
                startActivity(intent);
            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("business");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       /* switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

}
