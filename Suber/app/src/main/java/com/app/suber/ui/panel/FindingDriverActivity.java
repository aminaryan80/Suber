package com.app.suber.ui.panel;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.suber.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class FindingDriverActivity extends AppCompatActivity {
    private final Handler handler = new Handler();
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_driver);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        runDelayed(this::checkTripStatus, 5000);

        Button hurryUpButton = findViewById(R.id.hurryUpButton);

        hurryUpButton.setOnClickListener(view -> {
            hurryUpTrip(username);
            hurryUpButton.setEnabled(false);
        });
    }

    private void checkTripStatus() {

        String url = "http://192.168.1.11:8000/trip/get-trip-status/?username=" + username;
        StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        if (!response.equals("searching")) {
                            JSONObject jsonResponse = new JSONObject(response);

                            AlertDialog alertDialog = new AlertDialog.Builder(FindingDriverActivity.this).create();
                            alertDialog.setTitle("Trip accepted");
                            alertDialog.setMessage("name: " + jsonResponse.getString("name") + "\n" +
                                    "car: " + jsonResponse.getString("car"));

                            alertDialog.setButton("OK", (dialog, which) -> {
                                handler.removeCallbacksAndMessages(null);
                                Intent intent = new Intent(FindingDriverActivity.this, PassengerPanelActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                finish();
                            });

                            alertDialog.show();
                        }
                    } catch (JSONException ignored) {
                    }
                    runDelayed(this::checkTripStatus, 5000);
                },
                error -> Toast.makeText(FindingDriverActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(myRequest);
    }

    private void hurryUpTrip(String username) {
        try {
            String url = "http://192.168.1.11:8000/trip/hurry-up/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            final String requestBody = jsonBody.toString();
            StringRequest myRequest = new StringRequest(Request.Method.PUT, url,
                    response -> {
                        Toast.makeText(FindingDriverActivity.this, "Trip price has increased 20%!", Toast.LENGTH_LONG).show();
                    },
                    error -> {
                        Toast.makeText(FindingDriverActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return requestBody.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    assert response != null;
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(myRequest);
        } catch (JSONException ignored) {
        }
    }

    private void runDelayed(Runnable runnable, int delay) {
        handler.postDelayed(runnable, delay);
    }
}