package com.app.suber.ui.panel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.suber.R;
import com.app.suber.ui.account.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class PassengerProfileActivity extends AppCompatActivity {

    private String username;
    private EditText balanceEditText;
    private EditText usernameEditText;
    private EditText firstnameEditText;
    private EditText lastnameEditText;

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);

        username = getIntent().getStringExtra("username");

        balanceEditText = findViewById(R.id.balanceEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        firstnameEditText = findViewById(R.id.firstnameEditText);
        lastnameEditText = findViewById(R.id.lastnameEditText);
        Button updateProfileButton = findViewById(R.id.updateProfileButton);
        Button discountButton = findViewById(R.id.discountCodesButton);
        Button chargeAccountButton = findViewById(R.id.chargeAccountButton);

        loadProfileData();

        updateProfileButton.setOnClickListener(view -> {
            updateProfile(
                    usernameEditText.getText().toString(),
                    firstnameEditText.getText().toString(),
                    lastnameEditText.getText().toString()
            );
        });

        discountButton.setOnClickListener(view -> {
            Intent intent = new Intent(PassengerProfileActivity.this, DiscountCodesActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        chargeAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(PassengerProfileActivity.this, ChargeAccountActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }

    private void loadProfileData() {
        String url = "http://192.168.42.98:8000/account/get-profile/?user_type=P&username=" + username;
        @SuppressLint("SetTextI18n") StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        usernameEditText.setText(jsonResponse.getString("username"));
                        firstnameEditText.setText(jsonResponse.getString("first_name"));
                        lastnameEditText.setText(jsonResponse.getString("last_name"));
                        balanceEditText.setText(jsonResponse.get("balance") + "$");
                    } catch (JSONException ignored) {
                    }
                },
                error -> Toast.makeText(PassengerProfileActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(myRequest);
    }

    private void updateProfile(String new_username, String firstname, String lastname) {
        try {
            String url = "http://192.168.42.98:8000/account/update-profile/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("new_username", new_username);
            jsonBody.put("firstname", firstname);
            jsonBody.put("lastname", lastname);
            jsonBody.put("user_type", "P");
            final String requestBody = jsonBody.toString();
            StringRequest myRequest = new StringRequest(Request.Method.PUT, url,
                    response -> {
                        Toast.makeText(PassengerProfileActivity.this, "Profile Updated!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PassengerProfileActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    },
                    error -> Toast.makeText(PassengerProfileActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return requestBody.getBytes(StandardCharsets.UTF_8);
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(myRequest);
        } catch (JSONException ignored) {
        }
    }
}