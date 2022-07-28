package com.app.suber.ui.panel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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
import org.json.JSONObject;

public class DriverPanelActivity extends AppCompatActivity {
    private TripAdapter adapter;
    private String username;
    private TextView balanceTextView;

    @Override
    protected void onResume() {
        super.onResume();
        updatePage();
        updateData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_panel);

        username = getIntent().getStringExtra("username");

        Button reloadButton = findViewById(R.id.reloadButton);
        Button profileButton = findViewById(R.id.profileButton);
        Button acceptButton = findViewById(R.id.acceptButton);
        ConstraintLayout driverPanelView = findViewById(R.id.driverPanelView);
        balanceTextView = findViewById(R.id.balanceTextView);

        driverPanelView.setOnClickListener(view -> acceptButton.setEnabled(false));

        reloadButton.setOnClickListener(view -> updateData());

        handleRecyclerView();
        updateData();
        updatePage();

        profileButton.setOnClickListener(view -> {
            Intent intent = new Intent(DriverPanelActivity.this, DriverProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

    }

    private void updatePage() {
        String url = "http://192.168.42.98:8000/account/get-profile/?user_type=D&username=" + username;
        @SuppressLint("SetTextI18n") StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        balanceTextView.setText("balance: " + jsonResponse.get("balance") + "$");
                    } catch (JSONException ignored) {
                    }
                },
                error -> Toast.makeText(DriverPanelActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(myRequest);
    }

    private void updateData() {
        String url = "http://192.168.42.98:8000/trip/get-ongoing-trips/";
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