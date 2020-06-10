package com.example.instantreservationbusiness.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.instantreservationbusiness.ProgressButton;
import com.example.instantreservationbusiness.Queue;
import com.example.instantreservationbusiness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


@RequiresApi(api = Build.VERSION_CODES.N)
public class QueueActivity extends AppCompatActivity {

    DatabaseReference referenceForQueueInfo;
    DatabaseReference referenceBusiness;

    private FirebaseUser firebaseUser;

    TextView queue_name;
    TextView queue_business;
    TextView queue_city;
    TextView queue_nReservationString;
    int queue_nMaxReservation;
    int queue_nReservation;
    TextView queue_description ;
    //ImageView queue_QRCodeImage;
    ImageView queue_image;

    ProgressBar progressBarQueue;
    LinearLayout queue_layout;

    Button btn_menage_reservation;
    Button btn_remove_queue;

    String queueID;
    String queueBusinessID;
    String imageUri;
    SharedPreferences sharedPreferences;


    @Override
    public void onStart() {
        super.onStart();




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_test);

        Toolbar toolbar = findViewById(R.id.toolbar_queue);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("");

        btn_menage_reservation = findViewById(R.id.btn_menage_reservation);
        btn_remove_queue = findViewById(R.id.btn_remove_queue);

        queue_business = findViewById(R.id.queue_business);
        queue_name = findViewById(R.id.queue_name);
        queue_city = findViewById(R.id.queue_city);
        queue_nReservationString = findViewById(R.id.queue_nReservation);
        queue_description = findViewById(R.id.queue_description);
        queue_image = findViewById(R.id.queue_image);

        progressBarQueue = findViewById(R.id.progressBarQueue);
        queue_layout = findViewById(R.id.queue_layout);

        Intent intent = getIntent();
        queueID = intent.getStringExtra("payload");
        sharedPreferences = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);

        referenceForQueueInfo = FirebaseDatabase.getInstance().getReference().child("queues");
        referenceBusiness = FirebaseDatabase.getInstance().getReference().child("business").child(sharedPreferences.getString("businessUID", null));

        queue_layout.setVisibility(View.GONE);
        progressBarQueue.setVisibility(View.VISIBLE);

        // Check if user is signed in (non-null) and update UI accordingly.
        referenceForQueueInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Queue queue = dataSnapshot.child(queueID).getValue(Queue.class);
                //queueBusinessID = queue.getQueue_businessID();
                queue_business.setText(queue.getQueue_business());
                queue_city.setText(queue.getQueue_city());
                queue_description.setText(queue.getQueue_description());
                queue_name.setText(queue.getQueue_name());
                //queue_nMaxReservation = queue.getQueue_nMaxReservation();
                //queue_nReservation = queue.getQueue_nReservation();
                queue_nReservationString.setText(String.format("%s/%d", queue.getQueue_nReservationString(), queue.getQueue_nMaxReservation()));
                imageUri = queue.getQueue_image();
                //queue_QRCodeImage.setImageURI();

                queue_layout.setVisibility(View.VISIBLE);
                progressBarQueue.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        btn_menage_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReservationMenager.class);
                intent.putExtra("payload", queueID);
                startActivity(intent);
            }
        });


        btn_remove_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeQueue(queueID);
                finish();
            }
        });

    }

    private void removeQueue(final String queueID){

        final StorageReference storageRefQR = FirebaseStorage.getInstance().getReference().child("images/queue_qr_codes/"+queueID);
        final StorageReference storageRefImage = FirebaseStorage.getInstance().getReference().child(imageUri);

        DatabaseReference ref = referenceForQueueInfo.child(queueID);
        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    referenceBusiness = FirebaseDatabase.getInstance().getReference().child("business").child(sharedPreferences.getString("businessUID", "null"));
                    referenceBusiness.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int nQueues = dataSnapshot.child("business_nQueues").getValue(int.class) -1;
                            dataSnapshot.getRef().child("business_nQueues").setValue(nQueues).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    storageRefQR.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            storageRefImage.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
