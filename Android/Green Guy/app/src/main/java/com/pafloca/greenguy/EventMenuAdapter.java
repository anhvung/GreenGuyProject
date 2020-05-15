package com.pafloca.greenguy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
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
    String id;
    final static String EXTRA_INFOID= "1sdgftrh2346";
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
        View view = inflater.inflate(R.layout.item_menu_event, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if(events.get(position).getSoustitre()!=null){
            holder.sous_titre.setText(events.get(position).getSoustitre());
            holder.titre.setText(events.get(position).getTitre());
        }
        else{
            holder.titre.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            holder.sous_titre.setVisibility(View.GONE);
            holder.titre.setText(events.get(position).getTitre());
            if(events.get(position).getId()!=null){
                holder.titre.setTag(R.id.INFOIDTAG,events.get(position).getId());
            }
            else {
                holder.titre.setTag(R.id.INFOIDTAG,"pas de tag");

            }


        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titre;
        private TextView sous_titre;
        public View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mView = itemView;
            titre = itemView.findViewById(R.id.titre_item_menu_event);
            sous_titre = itemView.findViewById(R.id.sous_titre_item_menu_event);
            mView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("greend", "tag : "+(String) titre.getTag(R.id.INFOIDTAG));
                    String id = (String) titre.getTag(R.id.INFOIDTAG);
                    Intent intent=new Intent(mView.getContext(),DisplayInfo.class);
                    intent.putExtra(EXTRA_INFOID, id);
                    mView.getContext().startActivity(intent);
                }
            }
            );



        }
    }
}
