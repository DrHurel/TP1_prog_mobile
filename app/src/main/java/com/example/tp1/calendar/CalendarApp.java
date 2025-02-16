package com.example.tp1.calendar;

import com.example.tp1.MainActivity;
import com.example.tp1.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class CalendarApp extends AppCompatActivity {
    private EventAdapter eventAdapter;
    private final List<Event> eventList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private static final String EVENTS_LIST = "events_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences("calendar_events", MODE_PRIVATE);
        loadEvents();

        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        FloatingActionButton addEventButton = findViewById(R.id.fab_add_event);
        addEventButton.setOnClickListener(v -> showAddEventDialog());
    }

    private void showAddEventDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_event, null);
        EditText eventEditText = dialogView.findViewById(R.id.editEvent);
        DatePicker eventDatePicker = dialogView.findViewById(R.id.datePicker);
        TimePicker eventTimePicker = dialogView.findViewById(R.id.timePicker);
        Spinner eventTypeSpinner = dialogView.findViewById(R.id.eventTypeSpinner);

        eventTimePicker.setIs24HourView(true);

        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Sport", "Party", "Cultural", "Professional"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(spinnerAdapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_next_events)
                .setView(dialogView)
                .setPositiveButton(R.string.validate, null)
                .setNegativeButton(R.string.btn_home, null)
                .create();

        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String eventText = eventEditText.getText().toString().trim();
            if (eventText.isEmpty()) {
                eventEditText.setError("Please enter a name for the event");
                return;
            }

            String eventDate = String.format(Locale.getDefault(), "%02d/%02d/%d",
                    eventDatePicker.getDayOfMonth(), eventDatePicker.getMonth() + 1, eventDatePicker.getYear());

            String eventTime = String.format(Locale.getDefault(), "%02d:%02d",
                    eventTimePicker.getHour(), eventTimePicker.getMinute());

            String eventType = eventTypeSpinner.getSelectedItem().toString();

            eventList.add(new Event(eventDate, eventTime, eventText, eventType));
            sortEventsByDate();
            eventAdapter.notifyDataSetChanged();
            saveEvents();
            dialog.dismiss();
        }));

        dialog.show();
    }

    private void saveEvents() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();

        for (Event event : eventList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("date", event.getDate());
                jsonObject.put("time", event.getTime());
                jsonObject.put("description", event.getDescription());
                jsonObject.put("type", event.getType());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                Logger.getAnonymousLogger().severe("Error saving events to SharedPreferences : \n" + e.getMessage());
            }
        }

        editor.putString(EVENTS_LIST, jsonArray.toString());
        editor.apply();
    }

    private void loadEvents() {
        String json = sharedPreferences.getString(EVENTS_LIST, null);
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                eventList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    eventList.add(new Event(
                            jsonObject.getString("date"),
                            jsonObject.getString("time"),
                            jsonObject.getString("description"),
                            jsonObject.getString("type")));
                }
                sortEventsByDate();
            } catch (JSONException e) {
                Logger.getAnonymousLogger().severe("Error loading events from SharedPreferences : \n"+ e.getMessage());

            }
        }
    }

    private void sortEventsByDate() {
        eventList.sort((event1, event2) -> {
            try {
                String dateTime1 = event1.getDate() + " " + event1.getTime();
                String dateTime2 = event2.getDate() + " " + event2.getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date date1 = format.parse(dateTime1);
                Date date2 = format.parse(dateTime2);
                assert date1 != null;
                return date1.compareTo(date2);
            } catch (ParseException e) {
                Logger.getAnonymousLogger().severe("Error parsing date : \n" + e.getMessage());
                return 0;
            }
        });
    }

    public void go_back_home(View view) {
        Intent intent = new Intent(CalendarApp.this, MainActivity.class);
        startActivity(intent);
    }
}
