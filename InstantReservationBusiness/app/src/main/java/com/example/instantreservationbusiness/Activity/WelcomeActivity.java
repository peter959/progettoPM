package com.example.instantreservationbusiness.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.instantreservationbusiness.R;

public class WelcomeActivity extends AppCompatActivity {

    MaterialRippleLayout btnRegister;
    MaterialRippleLayout btnSignIn;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //initialize views
        title = (TextView) findViewById(R.id.txt_title);
        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn = findViewById(R.id.btnSignIn);

        //Apply gradient to title in welcome screen
        TextPaint paint = title.getPaint();
        float width = paint.measureText("Instant Reservation");

        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#C55BEA"),
                        Color.parseColor("#E75375"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);


        //navigate to Register activity
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        //navigate to Login activity
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

