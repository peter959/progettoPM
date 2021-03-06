package com.example.instantreservationbusiness;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.instantreservationbusiness.Activity.QueueActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.List;

public class QueueAdapterRecycler extends RecyclerView.Adapter<QueueAdapterRecycler.MyViewHolder> {

    Context context;
    List<Queue> queues;

    public QueueAdapterRecycler(Context c, List<Queue> p) {
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
        final String queue_imageUri = queues.get(i).getQueue_image();
        final String queue_id = queues.get(i).getQueue_id();
        final Boolean queue_is_favorite = queues.get(i).getQueue_is_favorite();

        myViewHolder.queue_name.setText(queue_name);
        myViewHolder.queue_business.setText(queue_business);
        myViewHolder.queue_city.setText(queue_city);
        myViewHolder.queue_nReservation.setText(queue_nReservation);
        myViewHolder.queue_image.setImageResource(R.drawable.resturant_example);
        if (!queue_imageUri.equals("")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(queue_imageUri);
            Glide.with(context).load(storageReference).into(myViewHolder.queue_image);
        }

        View v = myViewHolder.itemView;

        //when the queue get touched, navigate to queue activity passing, queue_id
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
