package com.example.instantreservation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.instantreservation.Activity.MainActivity;
import com.example.instantreservation.Activity.QueueActivity;
import com.example.instantreservation.Fragment.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

public class QueueAdapter extends PagerAdapter {

    private List<Queue> models;
    private LayoutInflater layoutInflater;
    private Context context;

    private FirebaseUser firebaseUser;
    DatabaseReference referenceForAddingReservationInUserFavorites;

    public QueueAdapter(List<Queue> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {


        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.queue_card_mini, container, false);
        TextView queue_ID;
        ImageView queue_image;
        TextView queue_name;
        TextView queue_business;
        TextView queue_city;
        TextView queue_nReservation;
        final String queue_id;
        final String userUID;

        queue_ID = view.findViewById(R.id.queue_ID);
        queue_image = view.findViewById(R.id.queue_image);
        queue_name = view.findViewById(R.id.queue_name);
        queue_business = view.findViewById(R.id.queue_business);
        queue_city = view.findViewById(R.id.queue_city);
        queue_nReservation = view.findViewById(R.id.queue_nReservation);

        queue_image.setImageResource(R.drawable.resturant_example);
        queue_name.setText(models.get(position).getQueue_name());
        queue_business.setText(models.get(position).getQueue_business());
        queue_city.setText(models.get(position).getQueue_city());
        queue_nReservation.setText(models.get(position).getQueue_nReservationString());
        queue_nReservation.setText(models.get(position).getQueue_nReservationString());
        queue_ID.setText(models.get(position).getQueue_id());
        queue_id = models.get(position).getQueue_id();

        container.addView(view, 0);

        ToggleButton tb = view.findViewById(R.id.toggleFavorite);
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                referenceForAddingReservationInUserFavorites = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
                if (checked) {
                   referenceForAddingReservationInUserFavorites.child("favoritesQueue").child(models.get(position).getQueue_id()).setValue("favorite");
                } else {
                    referenceForAddingReservationInUserFavorites.child("favoritesQueue").child(models.get(position).getQueue_id()).removeValue();
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QueueActivity.class);
                intent.putExtra("payload", queue_id);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

}

