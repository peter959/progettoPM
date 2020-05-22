package com.example.todolistyoutube;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyDoes> myDoes;

    public DoesAdapter(Context c, ArrayList<MyDoes> p) {
        context = c;
        myDoes = p;
    }

    @NonNull
    @Override
    //specifico quali oggetti "iniettare" nell'adapter (itemdoes)
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does, viewGroup, false));
    }

    //
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final String getTitleDoes = myDoes.get(i).getTitledoes();
        final String getDescDoes = myDoes.get(i).getDescdoes();
        final String getDateDoes = myDoes.get(i).getDatedoes();
        final String getKeyDoes = myDoes.get(i).getKeydoes();

        myViewHolder.titledoes.setText(getTitleDoes);
        myViewHolder.datedoes.setText(getDescDoes);
        myViewHolder.descdoes.setText(getDateDoes);


        //Quando il does viene toccato, vengono passati ad EditTaskDesk le informazioni già presenti
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(context, EditTaskDesk.class);
                edit.putExtra("titledoes", getTitleDoes);
                edit.putExtra("descdoes", getDescDoes);
                edit.putExtra("datedoes", getDateDoes);
                edit.putExtra("keydoes", getKeyDoes);
                context.startActivity(edit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titledoes, descdoes, datedoes, keydoes;

         public MyViewHolder(@NonNull View itemView) {
             super(itemView);
             titledoes = (TextView) itemView.findViewById(R.id.titledoes);
             descdoes = (TextView) itemView.findViewById(R.id.descdoes);
             datedoes = (TextView) itemView.findViewById(R.id.datedoes);

         }
     }
}
