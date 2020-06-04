package com.example.instantreservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instantreservation.Fragment.HomeFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

public class QueueAdapter extends PagerAdapter {

    private List<Queue> models;
    private LayoutInflater layoutInflater;
    private Fragment context;

    public QueueAdapter(List<Queue> models, HomeFragment context) {
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
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = context.getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.queue_card, container, false);
        ImageView queue_image;
        TextView queue_name, queue_business;

        //queue_image = view.findViewById(R.id.imageID);
        queue_name = view.findViewById(R.id.queue_name);
        //queue_business = view.findViewById(R.id.queue_business);

        //queue_image.setImageResource(R.drawable.resturant_example);
        queue_name.setText(models.get(position).getQueue_name());
        //queue_business.setText(models.get(position).getQueue_business());

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}

