package com.pafloca.greenguy;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListOfEventsActivity extends AppCompatActivity {


    private RecyclerView list_of_events_recyclerview;
    private EventMenuAdapter adapter;
    private ArrayList<ModelEvent> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_events);

        eventList = new ArrayList<>();
        ModelEvent modelEvent3 = new ModelEvent( "Cop 29", "Rassemblement pour défendre l'écologie", "blablabla","Marie Thérèse", "1 avril", "Cracovie");
        ModelEvent modelEvent4 = new ModelEvent( "Conférence", "Les économistes enfument ils la planère","blablabla", "Charles", "3 avril", "Berlin");
        ModelEvent modelEvent5 = new ModelEvent( "Débat politico écologique", "Occasion de retrouver les figures principales du mouvement écologique", "blablabla", "Thomas", "25 décembre", "Marseille");
        eventList.add(modelEvent3);
        eventList.add(modelEvent4);
        eventList.add(modelEvent5);


        list_of_events_recyclerview = findViewById(R.id.list_of_events_recyclerview);
        list_of_events_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventMenuAdapter(this, eventList);
        list_of_events_recyclerview.setAdapter(adapter);

        chargementEvents();
    }

    private void chargementEvents() {

        //ajouter à eventList les évènements récupérés de la base de donnée

        adapter.notifyDataSetChanged();
    }

}
