package com.example.instantreservationbusiness.Activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.instantreservationbusiness.ProgressButton;
import com.example.instantreservationbusiness.Queue;
import com.example.instantreservationbusiness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


@RequiresApi(api = Build.VERSION_CODES.N)
public class QueueActivity extends AppCompatActivity {

    DatabaseReference referenceForQueueInfo;
    DatabaseReference referenceBusiness;


    TextView queue_name;
    TextView queue_business;
    TextView queue_city;
    TextView queue_nReservationString;
    int queue_nMaxReservation;
    int queue_nReservation;
    TextView queue_description ;

    ImageView queue_qr;
    ImageView queue_image;

    ProgressBar progressBarQueue;
    LinearLayout queue_layout;

    Button btn_menage_reservation;
    Button btn_remove_queue;

    String queueID;
    String imageUri;
    String qrUri;
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
        queue_qr = findViewById(R.id.queue_qr);

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
                if (!imageUri.equals("")) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imageUri);
                    Glide.with(QueueActivity.this).load(storageReference).into(queue_image);
                }
                qrUri = queue.getQueue_QRCodeImage();
                if (!qrUri.equals("")) {
                    StorageReference storageReferenceQR = FirebaseStorage.getInstance().getReference().child(qrUri);
                    Glide.with(QueueActivity.this).load(storageReferenceQR).into(queue_qr);
                }

                queue_layout.setVisibility(View.VISIBLE);
                progressBarQueue.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        queue_qr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                    Toast.makeText(getApplicationContext(), "hey", Toast.LENGTH_SHORT).show();
                    StorageReference storageReferenceQR = FirebaseStorage.getInstance().getReference().child(qrUri);

                    storageReferenceQR.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            System.out.println("Download url success");
                            String url = uri.toString();
                            downloadFile(QueueActivity.this, queueID, ".jpg", DIRECTORY_DOWNLOADS, url);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                return false;
            }
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

        DatabaseReference resRef = FirebaseDatabase.getInstance().getReference().child("reservations").child(queueID);
        resRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
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
                                            StorageReference storageRefQR = FirebaseStorage.getInstance().getReference().child(qrUri);
                                            storageRefQR.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (!imageUri.equals("")) {
                                                        StorageReference storageRefImage = FirebaseStorage.getInstance().getReference().child(imageUri);
                                                        storageRefImage.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                finish();
                                                            }
                                                        });
                                                    }else {
                                                        finish();
                                                    }

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

    private void checkPermission() {
    }


    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        } else {

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            System.out.println("Download request created");

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName+fileExtension);
            System.out.println("request set");

            downloadManager.enqueue(request);
            System.out.println("request enqueued");

        }


    }


}
