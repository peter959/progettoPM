package com.example.instantreservationbusiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.instantreservationbusiness.Business;
import com.example.instantreservationbusiness.ProgressButton;
import com.example.instantreservationbusiness.Queue;
import com.example.instantreservationbusiness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class EditBusinessActivity extends AppCompatActivity {

    private SharedPreferences businessInfo;
    public static final int PICK_IMAGE = 1;

    EditText add_business_name, add_business_desc, add_business_city;

    StorageReference storageRef;

    DatabaseReference referenceBusiness;

    Button btn_cancel;
    View btn_edit;
    ImageButton add_business_image;
    ImageView business_image;
    String current_imageUri;
    Uri imageUri;

    String businessID;


    ProgressButton progressButton;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
           imageUri = data.getData();
           add_business_image.setImageURI(imageUri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_business);

        storageRef = FirebaseStorage.getInstance().getReference().child("images/business_images");

        businessInfo = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);
        businessID = businessInfo.getString("businessUID", "error");


        btn_edit = findViewById(R.id.btn_edit);
        btn_cancel = findViewById(R.id.btn_cancel);
        progressButton = new ProgressButton(EditBusinessActivity.this, btn_edit, "Save changes");

        add_business_name = findViewById(R.id.add_business_name);
        add_business_desc = findViewById(R.id.add_business_desc);
        add_business_city = findViewById(R.id.add_business_city);
        add_business_image = findViewById(R.id.add_business_image);

        Intent intent = getIntent();
        add_business_city.setText(intent.getStringExtra("city"));
        add_business_name.setText(intent.getStringExtra("name"));
        add_business_desc.setText(intent.getStringExtra("description"));



        add_business_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressButton.buttonActivated();

                referenceBusiness = FirebaseDatabase.getInstance().getReference().child("business").child(businessID);
                referenceBusiness.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().child("business_name").setValue(add_business_name.getText().toString());
                        dataSnapshot.getRef().child("business_description").setValue(add_business_desc.getText().toString());
                        dataSnapshot.getRef().child("business_city").setValue(add_business_city.getText().toString());
                        if (imageUri != null) {
                            // TODO: remove old image
                            dataSnapshot.getRef().child("business_image").setValue("images/business_images/"+imageUri.getLastPathSegment());
                            StorageReference imageRef = storageRef.child(imageUri.getLastPathSegment());
                            UploadTask uploadTask = imageRef.putFile(imageUri);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressButton.buttonFinishedUnsuccessully("Error upload image");
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressButton.buttonFinished("Saved");
                                    finish();
                                }
                            });
                        }

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
                finish();
            }
        });

    }


}


