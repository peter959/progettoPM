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

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;

    /*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
        }
        //updateUI(currentUser);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("business");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // check if the user is a business
                    if (!dataSnapshot.hasChild(currentUser.getUid())) {
                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                       // Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        System.out.println("AAAAAAAAAAAASJDAKSJDNAKSBDAKSFBAHFBA");
                       // startActivity(intent);
                        finish();
                    }
                    else {
                        String name = dataSnapshot.child("business_name").getValue().toString();
                        String phone = dataSnapshot.child("business_phone").getValue().toString();
                        String email = dataSnapshot.child("business_email").getValue().toString();
                        //SHARED PREFERENCES
                        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("business_UID", currentUser.getUid());
                        editor.putString("business_Name", name);
                        editor.putString("business_Email", email);
                        editor.putString("business_Phone", phone);
                        editor.commit();
                        //SHARED PREFERENCES
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
            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
        }

    }

}
