package com.example.instantreservation.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instantreservation.Activity.MainActivity;
import com.example.instantreservation.Activity.WelcomeActivity;
import com.example.instantreservation.QueueAdapter;
import com.example.instantreservation.Queue;
import com.example.instantreservation.R;
import com.google.firebase.auth.FirebaseAuth;
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

    FirebaseAuth mAuth;

    ViewPager viewPager;
    QueueAdapter queueAdapter;
    List<Queue> models;
    ProgressBar progressBar;
    ImageButton profileButton;

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
        mAuth = FirebaseAuth.getInstance();
        userInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        name = userInfo.getString("userName" , "null");
        userUID = userInfo.getString("userUID", "null");

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

        profileButton = returnView.findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Log out")
                        .setMessage("you are actually connected with " + userInfo.getString("userEmail", null) + " do you want to quit?")
                        .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        referenceForReservedQueue.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                viewPager.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                final String queueID =  dataSnapshot.getKey();
                referenceForQueueInfo.child(queueID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        if(dataSnapshot1.exists()) {
                            Queue queue = dataSnapshot1.getValue(Queue.class);
                            queue.setQueue_id(dataSnapshot1.getKey());
                            models.add(models.size(), queue);
                            System.out.println("ADDED on Reserved LIST: " + queueID);

                            queueAdapter = new QueueAdapter(models, getContext());
                            viewPager.setAdapter(queueAdapter);
                            queueAdapter.notifyDataSetChanged();
                            viewPager.setPadding(0, 0, 80, 0);
                            viewPager.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }else {
                            for (int i = 0; i<models.size(); i++){
                                if(models.get(i).getQueue_id().equals(queueID)){
                                    models.remove(i);
                                    System.out.println("REMOVED on Reservated LIST: " + queueID);
                                }
                                referenceForReservedQueue.child(queueID).removeValue().isComplete();
                                queueAdapter = new QueueAdapter(models, getContext());
                                viewPager.setAdapter(queueAdapter);
                                queueAdapter.notifyDataSetChanged();
                                viewPager.setPadding(0, 0, 80, 0);
                                viewPager.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }

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
                for (int i = 0; i<models.size(); i++){
                    if(models.get(i).getQueue_id().equals(queueID)){
                        models.remove(i);
                        System.out.println("!!!REMOVED on Reserved LIST: " + queueID);
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
