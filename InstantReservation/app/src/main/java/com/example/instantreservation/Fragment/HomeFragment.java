package com.example.instantreservation.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instantreservation.QueueAdapter;
import com.example.instantreservation.Queue;
import com.example.instantreservation.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    String userUID;

    ViewPager viewPager;
    QueueAdapter queueAdapter;
    List<Queue> models;
    ProgressBar progressBar;

    DatabaseReference referenceForReservedQueue;
    DatabaseReference referenceForQueueInfo;
    DatabaseReference referenceForFavoritesQueues;

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
        userUID = userInfo.getString("userUID", "null");

        referenceForFavoritesQueues = FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("favoritesQueue");
        referenceForReservedQueue = FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("reservedQueue");
        referenceForQueueInfo = FirebaseDatabase.getInstance().getReference().child("queues");

        models = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View returnView = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = returnView.findViewById(R.id.progressBarReservations);
        viewPager = returnView.findViewById(R.id.viewPager);


        TextView hello_name = returnView.findViewById(R.id.hello_name);
        hello_name.setText("Hello " + name + "!");

        referenceForReservedQueue.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                viewPager.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                final String queueID =  dataSnapshot.getKey();
                referenceForQueueInfo.child(queueID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        Queue queue = dataSnapshot1.getValue(Queue.class);

                        queue.setQueue_id(dataSnapshot1.getKey());
                        models.add(models.size(), queue);
                        System.out.println("ADDED on Reserved LIST: " + queueID);

                        queueAdapter = new QueueAdapter(models, getContext());
                        viewPager.setAdapter(queueAdapter);
                        queueAdapter.notifyDataSetChanged();
                        viewPager.setPadding(0,0,80,0);
                        viewPager.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                final String queueID =  dataSnapshot.getKey();
                referenceForQueueInfo.child(queueID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        Queue queue = dataSnapshot1.getValue(Queue.class);

                        for (int i = 0; i<models.size(); i++){
                            if(models.get(i).getQueue_id().equals(queueID)){
                                models.remove(i);
                                System.out.println("REMOVED on Reserved LIST: " + queueID);
                            };
                        }

                        queueAdapter = new QueueAdapter(models, getContext());
                        viewPager.setAdapter(queueAdapter);
                        queueAdapter.notifyDataSetChanged();
                        viewPager.setPadding(0,0,80,0);
                        viewPager.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        queueAdapter = new QueueAdapter(models, getContext());
        viewPager.setAdapter(queueAdapter);
        queueAdapter.notifyDataSetChanged();

        return returnView;
    }

}
