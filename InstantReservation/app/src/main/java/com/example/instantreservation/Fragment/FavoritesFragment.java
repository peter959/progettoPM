package com.example.instantreservation.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.instantreservation.Queue;

import com.example.instantreservation.QueueAdapter;
import com.example.instantreservation.QueueAdapterRecycler;
import com.example.instantreservation.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;


import org.json.JSONException;
import org.json.JSONObject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    private SharedPreferences userInfo;
    String name;
    String userUID;

    RecyclerView recyclerView;
    QueueAdapterRecycler queueAdapter;
    List<Queue> models;
    //ProgressBar progressBar;

    DatabaseReference referenceForFavoritesQueues;
    DatabaseReference referenceForQueueInfo;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        name = userInfo.getString("userName" , "null");
        userUID = userInfo.getString("userUID", "null");

        referenceForFavoritesQueues = FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("favoritesQueue");
        referenceForQueueInfo = FirebaseDatabase.getInstance().getReference().child("queues");

        models = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = returnView.findViewById(R.id.my_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);


        referenceForFavoritesQueues.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                final String queueID =  dataSnapshot.getKey();
                referenceForQueueInfo.child(queueID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        if(dataSnapshot1.exists()){
                            Queue queue = dataSnapshot1.getValue(Queue.class);
                            queue.setQueue_id(dataSnapshot1.getKey());
                            queue.setQueue_is_favorite(true);
                            models.add(queue);
                            System.out.println("ADDED on Favorites LIST: " + queueID);

                            queueAdapter = new QueueAdapterRecycler(getContext(), models);
                            recyclerView.setAdapter(queueAdapter);
                            queueAdapter.notifyDataSetChanged();
                        }else {
                            for (int i = 0; i<models.size(); i++){
                                if(models.get(i).getQueue_id().equals(queueID)){
                                    models.remove(i);
                                    System.out.println("REMOVED on Favorites LIST: " + queueID);

                                };
                            }
                            referenceForFavoritesQueues.child(queueID).removeValue().isComplete();
                            queueAdapter = new QueueAdapterRecycler(getContext(), models);
                            recyclerView.setAdapter(queueAdapter);
                            queueAdapter.notifyDataSetChanged();
                            System.out.println("NON ESISTE PIU il favorite queue");
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
                referenceForQueueInfo.child(queueID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        for (int i = 0; i<models.size(); i++){
                            if(models.get(i).getQueue_id().equals(queueID)){
                                models.remove(i);
                                System.out.println("REMOVED on Favorites LIST: " + queueID);
                            };
                        }
                        queueAdapter = new QueueAdapterRecycler(getContext(), models);
                        recyclerView.setAdapter(queueAdapter);
                        queueAdapter.notifyDataSetChanged();
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

        return returnView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
