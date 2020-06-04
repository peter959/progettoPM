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
import androidx.viewpager.widget.PagerAdapter;

public class Adapter extends PagerAdapter {

    private List<Model> models;
    private LayoutInflater layoutInflater;
    private HomeFragment context;

    public Adapter(List<Model> models, HomeFragment context) {
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
        View view = layoutInflater.inflate(R.layout.item, container, false);
        ImageView imageView;
        TextView title, descr;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        descr = view.findViewById(R.id.description);

        imageView.setImageResource(models.get(position).getImage());
        title.setText(models.get(position).getTitle());
        descr.setText(models.get(position).getDescr());

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}

