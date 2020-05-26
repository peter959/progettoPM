package com.example.instantreservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView titlepage, endpage, subtitlepage;
    Button btnAddNew;

    //dichiaro gli oggetti corrispondenti ai componenti grafici
    DatabaseReference reference;
    RecyclerView ourdoes;
    ArrayList<MyDoes> list;
    DoesAdapter doesAdapter;
    Toolbar toolbar;

    MyDoes test = new MyDoes("bla bla", "aksjhf", "ldjash", "1213404");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // inizializzo gli oggetti corrispondenti ai componenti grafici
        titlepage = findViewById(R.id.titlepage);
        endpage = findViewById(R.id.endpage);
        subtitlepage = findViewById(R.id.subtitlepage);
        btnAddNew = findViewById(R.id.btnAddNew);
        ourdoes = findViewById(R.id.ourdoes);
        //btnAddNew.setTypeface();

        //cliccanso sul bottone + si apre l'activity per generare un nuovo does
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent che consente di navifare alla activity NewTaskAct
                Intent a = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(a);
            }
        });

        //working with data
        ourdoes.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyDoes>();

        //get data from database
        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // set code to retrive data
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    //preleva dati da firebase e li inserisce in un oggetto MyDoes
                    MyDoes p = dataSnapshot1.getValue(MyDoes.class);
                    list.add(p);
                }
                doesAdapter = new DoesAdapter(MainActivity.this, list);
                ourdoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //set code to show an error
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.add_item:
                Toast.makeText(getApplicationContext(), "item aggiunto", Toast.LENGTH_SHORT).show();
                break;

            case R.id.settings_item:
                Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
