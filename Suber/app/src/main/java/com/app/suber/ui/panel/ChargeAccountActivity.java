package com.app.suber.ui.panel;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class ChargeAccountActivity extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_account);

        username = getIntent().getStringExtra("username");

        EditText amountEditText = findViewById(R.id.amountEditText);
        Button chargeButton = findViewById(R.id.chargeButton);

        chargeButton.setOnClickListener(view -> {
            chargeAccount(amountEditText.getText().toString());
        });
    }

    private void chargeAccount(String amount) {
        try {
            String url = "http://192.168.42.98:8000/account/charge-account/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("amount", amount);
            final String requestBody = jsonBody.toString();
            StringRequest myRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Intent intent = new Intent(ChargeAccountActivity.this, PassengerProfileActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    },
                    error -> {
                        Toast.makeText(ChargeAccountActivity.this, error.networkResponse.toString(), Toast.LENGTH_LONG).show();
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
            };
            RequestQueue requestQueue = Volley.newRequestQueue(ChargeAccountActivity.this);
            requestQueue.add(myRequest);
        } catch (JSONException ignored) {
        }
    }
}