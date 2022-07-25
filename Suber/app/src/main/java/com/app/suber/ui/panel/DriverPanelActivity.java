package com.app.suber.ui.panel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.suber.R;

import org.json.JSONArray;
import org.json.JSONException;

public class DriverPanelActivity extends AppCompatActivity {
    private TripAdapter adapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_panel);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        Button reloadButton = findViewById(R.id.reloadButton);
        Button acceptButton = findViewById(R.id.acceptButton);
        ConstraintLayout driverPanelView = findViewById(R.id.driverPanelView);

        driverPanelView.setOnClickListener(view -> acceptButton.setEnabled(false));

        reloadButton.setOnClickListener(view -> updateData());

        handleRecyclerView();
        updateData();

    }

    private void updateData() {
        String url = "http://192.168.1.11:8000/trip/get-ongoing-trips/";
        StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        adapter.populateDailyData(new JSONArray(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(DriverPanelActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(myRequest);
    }

    private void addDivider(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        DriverPanelActivity.this,
                        LinearLayoutManager.VERTICAL
                )
        );
    }

    private void handleRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.tripRecyclerView);
        Button acceptButton = findViewById(R.id.acceptButton);
        addDivider(recyclerView);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(
                DriverPanelActivity.this,
                LinearLayoutManager.VERTICAL,
                false
        );
        adapter = new TripAdapter(new String[][]{}, DriverPanelActivity.this, acceptButton, username);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}