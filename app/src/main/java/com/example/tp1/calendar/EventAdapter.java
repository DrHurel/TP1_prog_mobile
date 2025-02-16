package com.example.tp1.calendar;
import com.example.tp1.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.textDate.setText(event.getDate());
        holder.textTime.setText(event.getTime());
        holder.textDescription.setText(event.getDescription());
        holder.textType.setText(event.getType());

        int backgroundColor;
        switch (event.getType()) {
            case "Sport":
                backgroundColor = Color.parseColor("#6669ff");
                break;
            case "Fête":
                backgroundColor = Color.parseColor("#4fe896");
                break;
            case "Culturel":
                backgroundColor = Color.parseColor("#f4366f");
                break;
            case "Professionnel":
                backgroundColor = Color.parseColor("#edea24");
                break;
            default:
                backgroundColor = Color.GRAY;
                break;
        }
        holder.background.setBackgroundColor(backgroundColor);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textDate;
        TextView textTime;
        TextView textDescription;
        TextView textType;
        View background;

        EventViewHolder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textTime = itemView.findViewById(R.id.textTime);
            textDescription = itemView.findViewById(R.id.textDescription);
            textType = itemView.findViewById(R.id.textType);
            background = itemView.findViewById(R.id.eventBackground);
        }
    }
}