package com.example.instantreservation.Activity;

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
import com.example.instantreservation.R;

public class WelcomeActivity extends AppCompatActivity {
    MaterialRippleLayout btnRegister;
    MaterialRippleLayout btnSignIn;
    TextView title;


    //Da la possibilità all'utente di registrarsi o effetturare login
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

