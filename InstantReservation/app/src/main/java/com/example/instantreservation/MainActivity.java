package com.example.instantreservation;

import android.os.Build;
import android.os.Bundle;
//import androidx.support.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    final Fragment profileFragment = new ProfileFragment();
    final Fragment homeFragment = new HomeFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    //fragment =
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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Hi ");

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
