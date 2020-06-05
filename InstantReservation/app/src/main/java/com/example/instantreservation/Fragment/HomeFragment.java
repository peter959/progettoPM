package com.example.instantreservation.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instantreservation.QueueAdapter;
import com.example.instantreservation.Queue;
import com.example.instantreservation.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private SharedPreferences userInfo;
    String name;

    ViewPager viewPager;
    QueueAdapter queueAdapter;
    List<Queue> models;

    private TextView hello_name;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        name = userInfo.getString("userName" , "null");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_home, container, false);




        // Inflate the layout for this fragment
        TextView hello_name = (TextView) returnView.findViewById(R.id.hello_name);
        hello_name.setText("Hello " + name + "!");

        models = new ArrayList<>();
        models.add(new Queue("name", "description", "business", "businessID", "QRcode", "image", "city", 5,2));
        models.add(new Queue("name", "description", "business", "businessID", "QRcode", "image", "city", 5,2));
        models.add(new Queue("name", "description", "business", "businessID", "QRcode", "image", "city", 5,2));
        models.add(new Queue("name", "description", "business", "businessID", "QRcode", "image", "city", 5,2));


        queueAdapter = new QueueAdapter(models, this);

        viewPager = returnView.findViewById(R.id.viewPager);
        viewPager.setAdapter(queueAdapter);
        viewPager.setPadding(0,0,80,0);





        return returnView;
    }

}
