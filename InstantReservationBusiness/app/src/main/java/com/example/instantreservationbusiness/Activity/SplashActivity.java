package com.example.instantreservationbusiness.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // check if some user is currently connected
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("business");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // check if the user is a business
                    if (!dataSnapshot.hasChild(currentUser.getUid())) {
                        // if it is not a business user, show wronAccountActivity
                        Intent intent = new Intent(getApplicationContext(), WrongAccountActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        //save user data in shared preferences
                        dataSnapshot = dataSnapshot.child(currentUser.getUid());
                        String name =  dataSnapshot.child("business_name").getValue().toString();
                        String phone =  dataSnapshot.child("business_phone").getValue().toString();
                        String city =  dataSnapshot.child("business_city").getValue().toString();
                        String email =  dataSnapshot.child("business_email").getValue().toString();

                        //SHARED PREFERENCES
                        SharedPreferences sharedPreferences = getSharedPreferences("BusinessInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("businessUID", currentUser.getUid());
                        editor.putString("businessName", name);
                        editor.putString("businessCity", city);
                        editor.putString("businessEmail", email);
                        editor.putString("businessPhone", phone);
                        editor.commit();
                        System.out.println("-------Logged: email: " + email + ", name: " + name + ", phone:" + phone + ", UID: " + currentUser.getUid());


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            // if there's no user, show welcome activity
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
