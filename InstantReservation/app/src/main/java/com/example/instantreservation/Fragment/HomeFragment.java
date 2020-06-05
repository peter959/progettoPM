package com.example.instantreservation.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

        referenceForReservedQueue = FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("reservedQueue");
        referenceForQueueInfo = FirebaseDatabase.getInstance().getReference().child("codeattivita");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View returnView = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = returnView.findViewById(R.id.progressBarReservations);
        viewPager = returnView.findViewById(R.id.viewPager);
        

        TextView hello_name = (TextView) returnView.findViewById(R.id.hello_name);
        hello_name.setText("Hello " + name + "!");

        models = new ArrayList<>();

        viewPager.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        referenceForReservedQueue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String queueID =  dataSnapshot1.getKey();
                    System.out.println(dataSnapshot1.getKey());
                    if (queueID!=null) {
                        referenceForQueueInfo.child(queueID);
                        referenceForQueueInfo.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                Queue queue = dataSnapshot2.child(queueID).getValue(Queue.class);
                                queue.setQueue_id(queueID);
                                System.out.println(queue.getQueue_name());
                                models.add(queue);

                                queueAdapter = new QueueAdapter(models, getContext());

                                viewPager.setAdapter(queueAdapter);
                                queueAdapter.notifyDataSetChanged();
                                viewPager.setPadding(0,0,80,0);
                                viewPager.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //models.add(new Queue("name", "description", "business", "businessID", "QRcode", "image", "city", 5,2));
       // models.add(new Queue("name", "description", "business", "businessID", "QRcode", "image", "city", 5,2));
       // models.add(new Queue("name", "description", "business", "businessID", "QRcode", "image", "city", 5,2));
        //models.add(new Queue("name", "description", "business", "businessID", "QRcode", "image", "city", 5,2));

        return returnView;
    }

}
