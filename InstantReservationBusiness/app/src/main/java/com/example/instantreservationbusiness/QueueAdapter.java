package com.example.instantreservationbusiness;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.instantreservationbusiness.Activity.QueueActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

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
        boolean queue_is_favorite;

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
        queue_is_favorite = models.get(position).getQueue_is_favorite();

        container.addView(view, 0);

        /*
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QueueActivity.class);
                intent.putExtra("payload", queue_id);
                context.startActivity(intent);
            }
        });*/
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

}

