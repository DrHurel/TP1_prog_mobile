package com.example.tp1.sncf_info;
import com.example.tp1.MainActivity;
import com.example.tp1.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SncfApp extends AppCompatActivity {
    public static final String SNCF_API = "SNCF_API";
    private TextInputEditText departureInput;
    private TextInputEditText arrivalInput;
    private ProgressBar progressBar;
    private ScheduleAdapter adapter;
    private RequestQueue requestQueue;
        private static final String API_KEY = "f72b05e2-6c16-428a-9d38-e5be4d4f24ba";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sncf_info);

        departureInput = findViewById(R.id.departureInput);
        arrivalInput = findViewById(R.id.arrivalInput);
        RecyclerView schedulesList = findViewById(R.id.schedulesList);
        progressBar = findViewById(R.id.progressBar);
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setEnabled(false);
        schedulesList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter();
        schedulesList.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isReady = !Objects.requireNonNull(departureInput.getText()).toString().trim().isEmpty() &&
                        !Objects.requireNonNull(arrivalInput.getText()).toString().trim().isEmpty();
                searchButton.setEnabled(isReady);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        departureInput.addTextChangedListener(textWatcher);
        arrivalInput.addTextChangedListener(textWatcher);

        searchButton.setOnClickListener(v -> searchSchedules());
    }


    private void searchSchedules() {
        String departure = Objects.requireNonNull(departureInput.getText()).toString();
        String arrival = Objects.requireNonNull(arrivalInput.getText()).toString();

        String stationSearchUrl = String.format(
                "https://api.sncf.com/v1/coverage/sncf/places?q=%s",
                departure
        );

        JsonObjectRequest stationRequest = new JsonObjectRequest(
                Request.Method.GET, stationSearchUrl, null,
                response -> {
                    try {
                        JSONArray places = response.getJSONArray("places");
                        if (places.length() == 0) {
                            Toast.makeText(SncfApp.this,
                                    "The city \"" + departure + "\" does not exist",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        String departureId = places.getJSONObject(0).getString("id");
                        searchArrivalStation(departureId, arrival);
                    } catch (JSONException e) {
                        Toast.makeText(SncfApp.this,
                                "The city \"" + departure + "\" does not exist",
                                Toast.LENGTH_LONG).show();
                    }
                },
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " +
                        Base64.encodeToString(API_KEY.getBytes(), Base64.NO_WRAP));
                return headers;
            }
        };

        requestQueue.add(stationRequest);
    }

    private void searchArrivalStation(String departureId, String arrival) {
        String stationSearchUrl = String.format(
                "https://api.sncf.com/v1/coverage/sncf/places?q=%s",
                arrival
        );

        JsonObjectRequest stationRequest = new JsonObjectRequest(
                Request.Method.GET, stationSearchUrl, null,
                response -> {
                    try {
                        JSONArray places = response.getJSONArray("places");
                        if (places.length() == 0) {
                            Toast.makeText(SncfApp.this,
                                    "The city \"" + arrival + "\" does not exist",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        String arrivalId = places.getJSONObject(0).getString("id");
                        searchJourneys(departureId, arrivalId);
                    } catch (JSONException e) {
                        Toast.makeText(SncfApp.this,
                                "The city \"" + arrival + "\" does not exist",
                                Toast.LENGTH_LONG).show();
                    }
                },
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " +
                        Base64.encodeToString(API_KEY.getBytes(), Base64.NO_WRAP));
                return headers;
            }
        };

        requestQueue.add(stationRequest);
    }

    private void searchJourneys(String departureId, String arrivalId) {
        String datetime = new SimpleDateFormat("yyyyMMdd'T'HHmmss")
                .format(new Date());

        String url = String.format(
                "https://api.sncf.com/v1/coverage/sncf/journeys" +
                        "?from=%s" +
                        "&to=%s" +
                        "&datetime=%s" +
                        "&count=50" +
                        "&max_nb_transfers=2" +
                        "&data_freshness=base_schedule" +
                        "&datetime_represents=departure" +
                        "&disable_geojson=true",
                departureId, arrivalId, datetime
        );

        Log.e("REQUEST", url);

        JsonObjectRequest request = getJourneys(url);

        requestQueue.add(request);
    }

    @NonNull
    private JsonObjectRequest getJourneys(String url) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    parseAndDisplayResponse(response);
                },
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " +
                        Base64.encodeToString(API_KEY.getBytes(), Base64.NO_WRAP));
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        return request;
    }

    private void handleError(VolleyError error) {
        progressBar.setVisibility(View.GONE);
       String errorMessage = "Error during the search\n";
        if (error.networkResponse != null) {
            errorMessage += "Code : " + error.networkResponse.statusCode + "\n";
            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
            errorMessage += "Response : " + responseBody;
        }
        Log.e(SNCF_API, errorMessage, error);
        Toast.makeText(SncfApp.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void parseAndDisplayResponse(JSONObject response) {
        try {
            JSONArray journeys = response.getJSONArray("journeys");
            Set<Schedule> uniqueSchedules = new LinkedHashSet<>();

            for (int i = 0; i < journeys.length(); i++) {
                JSONObject journey = journeys.getJSONObject(i);
                JSONArray sections = journey.getJSONArray("sections");

                String departureTime = "";
                String arrivalTime = "";
                String duration;
                StringBuilder details = new StringBuilder();
                boolean hasPublicTransport = false;

                for (int j = 0; j < sections.length(); j++) {
                    JSONObject section = sections.getJSONObject(j);
                    String type = section.getString("type");

                    if (type.equals("public_transport")) {
                        hasPublicTransport = true;
                        JSONObject display = section.getJSONObject("display_informations");
                        String trainNumber = display.optString("headsign", "");
                        String trainType = display.optString("commercial_mode", "");

                        details.append(trainType)
                                .append(" n°")
                                .append(trainNumber)
                                .append("\n");

                        if (departureTime.isEmpty()) {
                            departureTime = formatDateTime(section.getString("departure_date_time"));
                        }
                        arrivalTime = formatDateTime(section.getString("arrival_date_time"));
                    }
                }

                if (hasPublicTransport) {
                    duration = formatDuration(journey.getInt("duration"));
                    Schedule schedule = new Schedule(
                            departureTime,
                            arrivalTime,
                            duration,
                            details.toString().trim()
                    );
                    uniqueSchedules.add(schedule);
                }
            }

            List<Schedule> schedules = new ArrayList<>(uniqueSchedules);

            schedules.sort((s1, s2) ->
                    s1.getDepartureTime().compareTo(s2.getDepartureTime()));

            adapter.setSchedules(schedules);

            Toast.makeText(this,
                    String.format("%d unique journeys found", schedules.size()),
                    Toast.LENGTH_SHORT).show();
            KeyboardUtils.hideKeyboard(this);

        } catch (JSONException e) {
            Toast.makeText(this,
                    "Error while handling responses" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            Log.e(SNCF_API, "Error parsing response", e);
        }
    }

    private String formatDateTime(String apiDateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
            Date date = inputFormat.parse(apiDateTime);
            assert date != null;
            return outputFormat.format(date);
        } catch (ParseException e) {
            return apiDateTime;
        }
    }

    private String formatDuration(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        return String.format("%dh%02d", hours, minutes);
    }

    public void go_back_home(View view){
        Intent intention= new Intent(SncfApp.this, MainActivity.class);
        startActivity(intention);
    }
}