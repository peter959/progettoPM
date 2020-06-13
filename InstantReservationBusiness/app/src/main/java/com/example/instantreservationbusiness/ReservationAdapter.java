package com.example.instantreservationbusiness;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.instantreservationbusiness.Activity.ReservationManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.MyViewHolder> {

    Context context;
    ArrayList<Reservation> reservations;

    RequestQueue mRequestQueue;
    String URL = "https://fcm.googleapis.com/fcm/send";

    public ReservationAdapter(Context c, ArrayList<Reservation> reservations) {
        context = c;
        this.reservations = reservations;
    }

    @NonNull
    @Override
    //specifico quali oggetti "iniettare" nell'adapter (itemdoes)
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reservation, viewGroup, false));
    }

    //
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        final String user_name = reservations.get(i).getUser_name();
        final String user_phone = reservations.get(i).getUser_phone();
        final String reservation_description = reservations.get(i).getNote();

        myViewHolder.user_name.setText(user_name);
        myViewHolder.user_phone.setText(user_phone);
        myViewHolder.reservation_description.setText(reservation_description);

        mRequestQueue = Volley.newRequestQueue(context);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        myViewHolder.item_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = reservations.get(i).getId_user();
                final String queueID = reservations.get(i).getId_queue();
                final DatabaseReference referenceQueueinfo = FirebaseDatabase.getInstance().getReference().child("queues").child(queueID);
                FirebaseDatabase.getInstance().getReference().child("reservations").child(queueID).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            referenceQueueinfo.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int nReservation = dataSnapshot.child("queue_nReservation").getValue(Integer.class);
                                    referenceQueueinfo.child("queue_nReservation").setValue(nReservation-1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if(task1.isSuccessful()) {
                                                FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("reservedQueue").child(queueID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task2) {
                                                        if(task2.isSuccessful()){
                                                            sendNotification();
                                                            Toast.makeText(context, "next!", Toast.LENGTH_LONG).show();
                                                            //reservationAdapter = new ReservationAdapter(ReservationAdapter.this, (ArrayList<Reservation>) list);
                                                           // reservations.setAdapter(reservationAdapter);
                                                           // reservationAdapter.notifyDataSetChanged();
                                                        }  else Toast.makeText(context, "There is an error", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }  else Toast.makeText(context, "There is an error", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else Toast.makeText(context, "There is an error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    private void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","any title");
            notificationObj.put("body","any body");

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAJ9QVTnE:APA91bFTVoDSyFqeVqG70atokpsG4s24FRKFSazw-VYlNnopatehRWRv3sNy5mXUxlwz7FWOiW2iFNbLmdXaSpACi9tVS-PeyO2BkOcbWgw82vNhjIrT6i4BngE5XflhCtj6YJB_Lqh-");
                    return header;
                }
            };
            mRequestQueue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView user_name, user_phone, reservation_description;
        Button item_next;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_phone = (TextView) itemView.findViewById(R.id.user_phone);
            reservation_description = (TextView) itemView.findViewById(R.id.reservation_description);
            item_next = itemView.findViewById(R.id.btn_item_next);

        }
    }
}
