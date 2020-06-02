package com.example.instantreservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class QueueActivity extends AppCompatActivity {

    TextView queue_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        queue_id = findViewById(R.id.queue_id);
        Intent intent = getIntent();
        String id = intent.getStringExtra("payload");
        queue_id.setText(id);
    }
}
