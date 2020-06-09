package com.example.instantreservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
    //specifico quali oggetti "iniettare" nell'adapter (itemdoes)
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reservation, viewGroup, false));
    }

    //
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final String business_name = reservations.get(i).getBusiness_name();
        final String queue_name = reservations.get(i).getQueue_name();
        final String note = reservations.get(i).getNote();
        final String ticket = Integer.toString(reservations.get(i).getTicket());

        myViewHolder.business_name.setText(business_name);
        myViewHolder.queue_name.setText(queue_name);
        myViewHolder.note.setText(note);
        myViewHolder.ticket.setText(ticket);

    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView business_name, queue_name, note, ticket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            business_name = (TextView) itemView.findViewById(R.id.business_name);
            queue_name = (TextView) itemView.findViewById(R.id.queue_name);
            note = (TextView) itemView.findViewById(R.id.note);
            ticket = (TextView) itemView.findViewById(R.id.ticket);

        }
    }
}
