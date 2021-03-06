package com.example.instantreservationbusiness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.MyViewHolder> {

    Context context;
    ArrayList<Reservation> reservations;


    public ReservationAdapter(Context c, ArrayList<Reservation> reservations) {
        context = c;
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reservation, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        final String user_name = reservations.get(i).getUser_name();
        final String user_phone = reservations.get(i).getUser_phone();
        final String reservation_description = reservations.get(i).getNote();

        myViewHolder.user_name.setText(user_name);
        myViewHolder.user_phone.setText(user_phone);
        myViewHolder.reservation_description.setText(reservation_description);


        //when the next button of the card is touched, the corresponding reservation get removed and the notification sent
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
                                    final String queueName = dataSnapshot.child("queue_name").getValue(String.class);
                                    final String queueBusiness = dataSnapshot.child("queue_business").getValue(String.class);
                                    //decrement nReservation
                                    referenceQueueinfo.child("queue_nReservation").setValue(nReservation-1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if(task1.isSuccessful()) {
                                                FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("reservedQueue").child(queueID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task2) {
                                                        if(task2.isSuccessful()){
                                                            //send notification to reserved user
                                                            MyNotificationManager myNotificationManager = new MyNotificationManager(context, userID, queueName, queueBusiness);
                                                            myNotificationManager.sendNotification();
                                                            Toast.makeText(context, "next!", Toast.LENGTH_LONG).show();
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
