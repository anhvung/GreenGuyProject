package com.pafloca.greenguy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventMenuAdapter extends RecyclerView.Adapter<EventMenuAdapter.MyViewHolder> {

    private ArrayList<ModelEvent> events;
    private Context context;

    public EventMenuAdapter(Context context, ArrayList<ModelEvent> events){
        this.context=context;
        this.events=events;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.actu_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nom;
        private TextView comment;
        public View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mView = itemView;
            //nom = itemView.findViewById(R.id.commentaire_item_nom);
            //comment = itemView.findViewById(R.id.commentaire_item_commentaire);
        }
    }
}
