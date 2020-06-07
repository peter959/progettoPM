package com.example.instantreservationbusiness;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instantreservationbusiness.Activity.QueueActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QueueAdapterRecycler extends RecyclerView.Adapter<QueueAdapterRecycler.MyViewHolder> {

    Context context;
    ArrayList<Queue> queues;
    private FirebaseUser firebaseUser;
    DatabaseReference referenceForAddingReservationInUserFavorites;

    public QueueAdapterRecycler(Context c, ArrayList<Queue> p) {
        context = c;
        queues = p;
    }

    @NonNull
    @Override
    //specifico quali oggetti "iniettare" nell'adapter (itemdoes)
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.queue_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final String queue_name = queues.get(i).getQueue_name();
        final String queue_business = queues.get(i).getQueue_business();
        final String queue_city = queues.get(i).getQueue_city();
        final String queue_nReservation = queues.get(i).getQueue_nReservationString();
        final String queue_image = queues.get(i).getQueue_image();
        final String queue_id = queues.get(i).getQueue_id();
        final Boolean queue_is_favorite = queues.get(i).getQueue_is_favorite();

        myViewHolder.queue_name.setText(queue_name);
        myViewHolder.queue_business.setText(queue_business);
        myViewHolder.queue_city.setText(queue_city);
        myViewHolder.queue_nReservation.setText(queue_nReservation);
        myViewHolder.queue_image.setImageResource(R.drawable.resturant_example);

        View v = myViewHolder.itemView;

        //Quando il does viene toccato, vengono passati ad EditTaskDesk le informazioni già presenti
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QueueActivity.class);
                intent.putExtra("payload", queue_id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return queues.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView queue_name, queue_business, queue_nReservation, queue_city;
        ImageView queue_image;
        ToggleButton tb;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            queue_image = itemView.findViewById(R.id.queue_image);
            queue_name = (TextView) itemView.findViewById(R.id.queue_name);
            queue_business = (TextView) itemView.findViewById(R.id.queue_business);
            queue_city = (TextView) itemView.findViewById(R.id.queue_city);
            queue_nReservation = (TextView) itemView.findViewById(R.id.queue_nReservation);
        }
    }
}
