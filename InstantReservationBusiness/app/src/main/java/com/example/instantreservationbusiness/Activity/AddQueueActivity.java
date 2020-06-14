package com.example.instantreservationbusiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class AddQueueActivity extends AppCompatActivity {

    private SharedPreferences userInfo;
    public static final int PICK_IMAGE = 2;

    EditText add_queue_name, add_queue_desc, add_queue_maxReserv;
    DatabaseReference referenceQueue;
    DatabaseReference referenceBusiness;

    StorageReference storageRefImages;
    StorageReference storageRefQR;

    String businessID, businessName, businessCity;

    Button btn_cancel;
    View btn_create;
    ImageButton add_queue_image;

    Uri imageUri;

    //generate queueID
    Integer queueNum = new Random().nextInt();

    ProgressButton progressButton;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if(data!=null) {
                imageUri = data.getData();
                add_queue_image.setImageURI(imageUri);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_queue);

        storageRefImages = FirebaseStorage.getInstance().getReference().child("images/queue_images");
        storageRefQR = FirebaseStorage.getInstance().getReference().child("images/queue_qr_codes");

        userInfo = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);
        businessID = userInfo.getString("businessUID", "null");
        businessName = userInfo.getString("businessName", "null");
        businessCity = userInfo.getString("businessCity", "null");

        btn_create = findViewById(R.id.btn_create);
        btn_cancel = findViewById(R.id.btn_cancel);
        progressButton = new ProgressButton(AddQueueActivity.this, btn_create, "Create Now");

        add_queue_desc = findViewById(R.id.add_queue_desc);
        add_queue_name = findViewById(R.id.add_queue_name);
        add_queue_maxReserv = findViewById(R.id.add_queue_maxReserv);
        add_queue_image = findViewById(R.id.add_queue_image);

        //add queue image, open gallery
        add_queue_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        //CREATE A NEW QUEUE
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressButton.buttonActivated();

                final String queue_id = "queue" + queueNum;

                final Queue queue = new Queue(add_queue_name.getText().toString(),
                        add_queue_desc.getText().toString(),
                        businessName,
                        businessID,
                        "",
                        "",
                        businessCity,
                        Integer.parseInt(add_queue_maxReserv.getText().toString()),
                        0);


                referenceQueue = FirebaseDatabase.getInstance().getReference().child("queues").child(queue_id);

                // SAVE QUEUE ON FIREBASE
                referenceQueue.setValue(queue).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //UPDATE N. QUEUES IN BUSINESS
                            referenceBusiness = FirebaseDatabase.getInstance().getReference().child("business").child(businessID);
                            referenceBusiness.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int nQueues = dataSnapshot.child("business_nQueues").getValue(int.class) + 1;
                                    System.out.println(nQueues);
                                    final Task<Void> business_nQueues = dataSnapshot.getRef().child("business_nQueues").setValue(nQueues);
                                    business_nQueues.addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //generate and add QR
                                                Bitmap qrImage = qrGenerator(queue_id);
                                                if (qrImage != null) {
                                                    StorageReference imageRef = storageRefQR.child(queue_id);
                                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                    qrImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                    byte[] data = baos.toByteArray();
                                                    UploadTask uploadTask = imageRef.putBytes(data);
                                                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                        //UPDATE QUEUE QR URI IN FIREBASE
                                                        @Override
                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                            referenceQueue.child("queue_QRCodeImage").setValue("images/queue_qr_codes/"+queue_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (imageUri != null) {
                                                                        //ADD IMAGE IN STORAGE
                                                                        StorageReference imageRef = storageRefImages.child(imageUri.getLastPathSegment());
                                                                        UploadTask uploadTask = imageRef.putFile(imageUri);
                                                                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                            //UPDATE URI QUEUE IMAGE IN FIREBASE
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                referenceQueue.child("queue_image").setValue("images/queue_images/"+imageUri.getLastPathSegment()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    //ADD QR CODE IN FIREBASE
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        progressButton.buttonFinished("Queue added!");
                                                                                        finish();
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                    else {
                                                                        progressButton.buttonFinished("Queue added!");
                                                                        finish();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                }

                                            }
                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

            }
        });

        //CANCEL
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private Bitmap qrGenerator (String queue_id) {
        JSONObject json = new JSONObject();
        try {
            json.put("id",queue_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        QRGEncoder qrgEncoder = new QRGEncoder(json.toString(), null, QRGContents.Type.TEXT, 350);
        try {
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            return bitmap;
            //ImageView qrImage = (ImageView) returnView.findViewById(R.id.qrCodeImage);
            // Setting Bitmap to ImageView
            //qrImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            System.out.println("------------------------- generation QRcode Error: ");
            return null;
        }

    };
}
