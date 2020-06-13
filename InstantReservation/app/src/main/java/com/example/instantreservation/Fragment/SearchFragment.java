package com.example.instantreservation.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.example.instantreservation.Queue;
import com.example.instantreservation.QueueAdapterRecycler;
import com.example.instantreservation.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    Client client;
    Index index;

    RecyclerView recyclerView;
    QueueAdapterRecycler queueAdapter;
    List<Queue> models;
    ProgressBar progressBar;

    DatabaseReference referenceForQueueInfo;
    CompletionHandler completionHandler;

    public SearchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new Client("2HRU3Q2XH0", "ae7b1b26182a207e18ba383449e604b7");
        index = client.getIndex("queues");

        referenceForQueueInfo = FirebaseDatabase.getInstance().getReference().child("queues");

        models = new ArrayList<>();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View returnView = inflater.inflate(R.layout.fragment_search, container, false);

        progressBar = returnView.findViewById(R.id.progressBarSearch);
        final EditText et_keyword = returnView.findViewById(R.id.keyword);

        recyclerView = returnView.findViewById(R.id.match_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        completionHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                try {
                    JSONArray hits = (JSONArray)content.get("hits");
                    for(int i=0; i<hits.length(); i++){
                        JSONObject hitsChild = hits.getJSONObject(i);
                        final String queue_id = hitsChild.getString("objectID");
                        System.out.println("MATCHED Queues ID for search: " + " ???? " + queue_id);
                        models = new ArrayList<>();
                        referenceForQueueInfo.child(queue_id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                if(dataSnapshot1.exists()){
                                    Queue queue = dataSnapshot1.getValue(Queue.class);

                                    queue.setQueue_id(dataSnapshot1.getKey());
                                    models.add(queue);
                                    System.out.println("ADDED on Matched LIST: " + queue_id);

                                    queueAdapter = new QueueAdapterRecycler(getContext(), models);
                                    recyclerView.setAdapter(queueAdapter);
                                    queueAdapter.notifyDataSetChanged();
                                    recyclerView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }else {
                                    models.clear();
                                    Toast.makeText(getContext(), "not found", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Button button = returnView.findViewById(R.id.goSearch);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String keyword = et_keyword.getText().toString();
                models = new ArrayList<>();
                index.searchAsync(new Query(keyword), completionHandler);
            }
        });


        // Inflate the layout for this fragment
        return returnView;
    }


    @Override
    public void onResume() {
        super.onResume();


    }
}
