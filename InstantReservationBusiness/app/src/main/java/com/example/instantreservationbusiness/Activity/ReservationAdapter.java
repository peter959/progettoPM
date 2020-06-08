package com.example.instantreservationbusiness.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instantreservationbusiness.BuildConfig;
import com.example.instantreservationbusiness.R;
import com.example.instantreservationbusiness.Reservation;

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
        final String user_name = reservations.get(i).getUserName();
        final String user_phone = reservations.get(i).getUserPhone();
        final String reservation_description = reservations.get(i).getDescription();

        myViewHolder.user_name.setText(user_name);
        myViewHolder.user_phone.setText(user_phone);
        myViewHolder.reservation_description.setText(reservation_description);


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
            item_next = itemView.findViewById(R.id.item_next);

        }
    }
}
