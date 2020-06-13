package com.example.instantreservation.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
//import androidx.support.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.instantreservation.BottomNavigationBehavior;
import com.example.instantreservation.Fragment.CameraFragment;
import com.example.instantreservation.Fragment.FavoritesFragment;
import com.example.instantreservation.Fragment.HomeFragment;
import com.example.instantreservation.Fragment.ProfileFragment;
import com.example.instantreservation.Fragment.SearchFragment;
import com.example.instantreservation.Queue;
import com.example.instantreservation.R;
import com.example.instantreservation.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    final Fragment profileFragment = new ProfileFragment();
    final Fragment homeFragment = new HomeFragment();
    final Fragment searchFragment = new SearchFragment();
    final Fragment cameraFragment = new CameraFragment();
    final Fragment favoritesFragment = new FavoritesFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;

    FusedLocationProviderClient fusedLocationProviderClient;
    String city;

    double latitude, longitude;
    String locality;
    String address;


    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize Location
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //initialize geoCoder
                        Geocoder geocoder = new Geocoder (MainActivity.this, Locale.getDefault());
                        //Initialize address List
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                        locality = addresses.get(0).getLocality();
                        address = addresses.get(0).getAddressLine(0);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            getLocation();
            //Toast.makeText(this, locality + " " + address, Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

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




}
