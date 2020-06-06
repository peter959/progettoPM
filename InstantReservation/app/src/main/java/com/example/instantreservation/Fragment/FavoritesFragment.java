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
    ArrayList<Queue> models;
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
        referenceForQueueInfo = FirebaseDatabase.getInstance().getReference().child("codeattivita");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = returnView.findViewById(R.id.my_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        models = new ArrayList<>();
        //models.add(new Queue("name", "description", "business", "businessID", "QRcode", "image", "city", 5,2));
        //models.add(new Queue("name", "description", "business", "businessID", "QRcode", "image", "city", 5,2));


        //recyclerView.setVisibility(View.GONE);
        //progressBar.setVisibility(View.VISIBLE);

        referenceForFavoritesQueues.addValueEventListener(new ValueEventListener() {
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
                                queue.setQueue_is_favorite(true);
                                System.out.println(queue.getQueue_name());
                                models.add(queue);

                                queueAdapter = new QueueAdapterRecycler(getContext(), models);
                                recyclerView.setAdapter(queueAdapter);
                                queueAdapter.notifyDataSetChanged();

                                //recyclerView.setVisibility(View.VISIBLE);
                                //progressBar.setVisibility(View.GONE);
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

        return returnView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
