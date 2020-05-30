package com.example.instantreservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {
    //private FirebaseAuth mAuth;

    MaterialRippleLayout btnRegister;
    MaterialRippleLayout btnSignIn;
    TextView title;

   /* @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        //updateUI(currentUser);
    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        title = (TextView) findViewById(R.id.txt_title);

        TextPaint paint = title.getPaint();
        float width = paint.measureText("Instant Reservation");

        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#C55BEA"),
                        Color.parseColor("#E75375"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);


        //mAuth = FirebaseAuth.getInstance();
        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

