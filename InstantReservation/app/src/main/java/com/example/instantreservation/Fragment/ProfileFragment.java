package com.example.instantreservation.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.instantreservation.Queue;
import com.example.instantreservation.R;
import com.example.instantreservation.Activity.WelcomeActivity;
import com.example.instantreservation.Reservation;
import com.example.instantreservation.ReservationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private SharedPreferences userInfo;


    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;

    DatabaseReference referenceReservation;
    DatabaseReference referenceQueueInfo;

    List<Reservation> list;
    RecyclerView reservations;
    ReservationAdapter reservationAdapter;

    String businessID;
    String queueID;

    String userID;
    String name;
    String email;


    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userID = userInfo.getString("userUID" , "id");
        name = userInfo.getString("userName" , "name");
        email = userInfo.getString("userEmail" , "email");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        TextView tv_userName = (TextView) returnView.findViewById(R.id.userName);
        TextView tv_userEmail = (TextView) returnView.findViewById(R.id.userEmail);

        tv_userName.setText(name);
        tv_userEmail.setText(email);


        final TextView logout = (TextView) returnView.findViewById(R.id.txt_log_out);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //SHARED PREFERENCES
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userUID", null);
                editor.putString("userName", null);
                editor.putString("userEmail", null);
                editor.putString("userPhone", null);
                editor.commit();
                //SHARED PREFERENCES
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });



        reservations = returnView.findViewById(R.id.reservations);
        reservations.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<Reservation>();

        referenceReservation = FirebaseDatabase.getInstance().getReference().child("reservations");
        referenceReservation.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                    if (dataSnapshot1.getKey().equals(userID)) {
                        System.out.println("user      " + dataSnapshot1.getKey());
                        System.out.println("queue     " + dataSnapshot.getKey());

                        final Reservation r = dataSnapshot1.getValue(Reservation.class);
                        r.setId_user(dataSnapshot1.getKey());
                        r.setId_queue(dataSnapshot.getKey());

                        referenceQueueInfo = FirebaseDatabase.getInstance().getReference().child("queues").child(r.getId_queue());
                        referenceQueueInfo.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshotQueue) {
                                System.out.println("queueSnaphotkey     " + dataSnapshotQueue.getKey());
                                System.out.println("business ID     " + dataSnapshotQueue.child("queue_businessID").getValue(String.class));
                                r.setId_business(dataSnapshotQueue.child("queue_businessID").getValue(String.class));
                                r.setBusiness_name(dataSnapshotQueue.child("queue_business").getValue(String.class));
                                r.setQueue_name(dataSnapshotQueue.child("queue_name").getValue(String.class));

                                list.add(list.size(), r);

                                reservationAdapter = new ReservationAdapter(getActivity(), (ArrayList<Reservation>) list);
                                reservations.setAdapter(reservationAdapter);
                                reservationAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Reservation r = dataSnapshot.getValue(Reservation.class);
                //System.out.println("Adding queue in business list: " + queue.getQueue_businessID());
                for (int i = 0; i<list.size(); i++){
                    if(list.get(i).getId_user().equals(r.getId_user())){
                        list.remove(i);
                    }
                }

                reservationAdapter = new ReservationAdapter(getActivity(), (ArrayList<Reservation>) list);
                reservations.setAdapter(reservationAdapter);
                reservationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return returnView;
    }
}
