package com.example.instantreservation.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
//import androidx.support.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.instantreservation.BottomNavigationBehavior;
import com.example.instantreservation.Fragment.CameraFragment;
import com.example.instantreservation.Fragment.FavoritesFragment;
import com.example.instantreservation.Fragment.HomeFragment;
import com.example.instantreservation.Fragment.ProfileFragment;
import com.example.instantreservation.Fragment.SearchFragment;
import com.example.instantreservation.R;
import com.example.instantreservation.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

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
    final Fragment searchFragment = new SearchFragment();
    final Fragment cameraFragment = new CameraFragment();
    final Fragment favoritesFragment = new FavoritesFragment();
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
                    fm.beginTransaction().hide(active).show(favoritesFragment).commit();
                    active = favoritesFragment;
                    return true;
                case R.id.navigationHome:
                    fm.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;
                    return true;
                case R.id.navigationSearch:
                    fm.beginTransaction().hide(active).show(searchFragment).commit();
                    active = searchFragment;
                    return true;
                case R.id.navigationCamera:
                    fm.beginTransaction().hide(active).show(cameraFragment).commit();
                    active = cameraFragment;
                    //Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                    //startActivity(intent);
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

        /*mAuth = FirebaseAuth.getInstance();
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

        }
        else {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        }*/


        /*
        Bundle args = new Bundle();
        args.putString("name", "adkjshkjdhf");
        args.putString("email", "user.getEmail()");
        profileFragment.setArguments(args);
        homeFragment.setArguments(args);*/


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm.beginTransaction().add(R.id.main_container,cameraFragment,"5").hide(cameraFragment).commit();
        fm.beginTransaction().add(R.id.main_container,searchFragment,"3").hide(searchFragment).commit();
        fm.beginTransaction().add(R.id.main_container,favoritesFragment,"4").hide(favoritesFragment).commit();
        fm.beginTransaction().add(R.id.main_container,profileFragment,"2").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.main_container,homeFragment, "1").commit();


        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    //textViewName.setText(obj.getString("name"));
                    //textViewAddress.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

   /* @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }*/

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
