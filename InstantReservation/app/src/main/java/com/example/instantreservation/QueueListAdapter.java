package com.example.instantreservation;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instantreservation.Activity.BusinessActivity;
import com.example.instantreservation.Queue;
import com.example.instantreservation.R;

import java.util.ArrayList;

public class QueueListAdapter extends RecyclerView.Adapter<QueueListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Queue> QueueList;

    public QueueListAdapter(Context c, ArrayList<Queue> p) {
        context = c;
        QueueList = p;
    }

    @NonNull
    @Override
    //specifico quali oggetti "iniettare" nell'adapter (itemdoes)
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.queue_card, viewGroup, false));
    }

    //
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        /*final String getQueue_image = QueueList.get(i).getQueue_image();
        final String getDescDoes = QueueList.get(i).getQueue_city();
        final String getDateDoes = QueueList.get(i).getQueue_business();
        final String getDateDoes = QueueList.get(i).getQueue_name();
        final String getDateDoes = QueueList.get(i).get();


        myViewHolder.titledoes.setText(getTitleDoes);
        myViewHolder.datedoes.setText(getDescDoes);
        myViewHolder.descdoes.setText(getDateDoes);*/

        //Quando il does viene toccato, vengono passati ad EditTaskDesk le informazioni già presenti
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(context, BusinessActivity.class);
                context.startActivity(edit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return QueueList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView queue_image, queue_name, queue_business, queue_city, queue_nReservation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            queue_image = itemView.findViewById(R.id.queue_image);
            queue_name = itemView.findViewById(R.id.queue_name);
            queue_business = itemView.findViewById(R.id.queue_business);
            queue_city = itemView.findViewById(R.id.queue_city);
            queue_nReservation = itemView.findViewById(R.id.queue_nReservation);

        }
    }
}
