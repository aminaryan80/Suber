package com.app.suber.ui.panel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class PassengerPanelActivity extends AppCompatActivity {
    private String username;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_panel);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        Button searchButton = findViewById(R.id.searchButton);
        Button profileButton = findViewById(R.id.profileButton);
        Button submitLocationButton = findViewById(R.id.submitLocationButton);
        TextView sourceTextView = findViewById(R.id.sourceTextView);
        TextView destinationTextView = findViewById(R.id.destinationTextView);
        TextView balanceTextView = findViewById(R.id.balanceTextView);
        EditText sourceLongitudeEditText = findViewById(R.id.sourceLongitudeEditText);
        EditText sourceLatitudeEditText = findViewById(R.id.sourceLatitudeEditText);
        EditText destinationLongitudeEditText = findViewById(R.id.destinationLongitudeEditText);
        EditText destinationLatitudeEditText = findViewById(R.id.destinationLatitudeEditText);
        EditText discountEditText = findViewById(R.id.discountEditText);

        updatePage(balanceTextView);

        searchButton.setOnClickListener(view -> {
            searchForCar(
                    sourceTextView.getText().toString(),
                    destinationTextView.getText().toString(),
                    discountEditText.getText().toString()
            );
        });

        profileButton.setOnClickListener(view -> {
            Toast.makeText(PassengerPanelActivity.this, "Profile", Toast.LENGTH_SHORT).show();
        });

        submitLocationButton.setOnClickListener(view -> {
            sourceTextView.setText(sourceLatitudeEditText.getText().toString() + "|" + sourceLongitudeEditText.getText().toString());
            destinationTextView.setText(destinationLatitudeEditText.getText().toString() + "|" + destinationLongitudeEditText.getText().toString());
        });
    }

    private void updatePage(TextView balanceTextView) {
        String url = "http://192.168.1.11:8000/account/get-profile/?user_type=P&username=" + username;
        @SuppressLint("SetTextI18n") StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        balanceTextView.setText("balance: " + jsonResponse.get("balance") + "$");
                    } catch (JSONException ignored) {
                    }
                },
                error -> Toast.makeText(PassengerPanelActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(myRequest);
    }

    private void searchForCar(String source, String destination, String discount_code) {
        try {
            String url = "http://192.168.1.11:8000/trip/search-for-driver/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("source", source);
            jsonBody.put("destination", destination);
            jsonBody.put("discount_code", discount_code);
            final String requestBody = jsonBody.toString();
            StringRequest myRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Toast.makeText(PassengerPanelActivity.this, "Searching for driver...", Toast.LENGTH_LONG).show();
                        openFindingCarPage();
                    },
                    error -> {
                        if (error.networkResponse.statusCode == 404)
                            Toast.makeText(PassengerPanelActivity.this, "Discount code not found!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(PassengerPanelActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

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

    private void openFindingCarPage() {
        Intent myIntent = new Intent(PassengerPanelActivity.this, FindingDriverActivity.class);
        myIntent.putExtra("username", username);
        startActivity(myIntent);
    }
}
