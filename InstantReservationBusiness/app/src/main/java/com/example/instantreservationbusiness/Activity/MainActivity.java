package com.example.instantreservationbusiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.instantreservationbusiness.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    private SharedPreferences userInfo;

    private FirebaseAuth mAuth;

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

    Toolbar toolbar;

    String id;

    @Override
    public void onStart() {
        super.onStart();

        business_layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        
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

        mAuth = FirebaseAuth.getInstance();

        userInfo = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);
        id = userInfo.getString("businessUID", "null");
        reference = FirebaseDatabase.getInstance().getReference().child("business");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
