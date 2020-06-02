package com.example.instantreservation.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             String name = dataSnapshot.child("fullname").getValue().toString();
                             String phone = dataSnapshot.child("phone").getValue().toString();
                             String email = dataSnapshot.child("email").getValue().toString();
                             //SHARED PREFERENCES
                             SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                             SharedPreferences.Editor editor = sharedPreferences.edit();
                             editor.putString("userUID", currentUser.getUid());
                             editor.putString("userName", name);
                             editor.putString("userEmail", email);
                             editor.putString("userPhone", phone);
                             editor.commit();
                             //SHARED PREFERENCES
                             System.out.println("-------Logged: email: " + email + ", name: " + name + ", phone:" + phone + ", UID: " + currentUser.getUid());
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
