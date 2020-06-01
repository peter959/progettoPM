package com.example.instantreservation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
//import androidx.support.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    String email, name;

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private User user;

    final Fragment profileFragment = new ProfileFragment();
    final Fragment homeFragment = new HomeFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.





        //updateUI(currentUser);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:

                    fm.beginTransaction().hide(active).show(profileFragment).commit();
                    active = profileFragment;
                    return true;
                case R.id.navigationFavorites:
                    return true;
                case R.id.navigationHome:
                    fm.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;
                    return true;
                case R.id.navigationSearch:

                    return true;
                case R.id.navigationCamera:
                    //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    // drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //user = dataSnapshot.getValue(User.class);
                   // email =  dataSnapshot.child("email").getValue().toString();
                  //  name =  dataSnapshot.child("fullname").getValue().toString();
                  //  System.out.println("AAAAAAAAAAAAQAAAAAAAAAAAAAAAA" + email + " " + name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //Bundle args = new Bundle();
            //args.putString("name", "name");
            //args.putString("email", "email");
            //profileFragment.setArguments(args);
            //homeFragment.setArguments(args);

        }
        else {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        }


        /*
        Bundle args = new Bundle();
        args.putString("name", "adkjshkjdhf");
        args.putString("email", "user.getEmail()");
        profileFragment.setArguments(args);
        homeFragment.setArguments(args);*/


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm.beginTransaction().add(R.id.main_container,profileFragment,"2").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.main_container,homeFragment, "1").commit();


        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

       /* CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());*/

        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

    }

   /* @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    /*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_dark_mode) {
            //code for setting dark mode
            //true for dark mode, false for day mode, currently toggling on each click
           //DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
          //  darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
          //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

}
