package com.app.suber.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.app.suber.ui.panel.DriverPanelActivity;
import com.app.suber.ui.panel.PassengerPanelActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView usernameTextView = findViewById(R.id.username);
        TextView passwordTextView = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);
        RadioGroup radio_group = findViewById(R.id.radio_group);


        Intent intent = getIntent();
        if (intent.hasExtra("username")) {
            usernameTextView.setText(intent.getStringExtra("username"));
        }
        if (intent.hasExtra("password")) {
            passwordTextView.setText(intent.getStringExtra("password"));
        }

        loginButton.setOnClickListener(view -> {
            String username = usernameTextView.getText().toString();
            String password = passwordTextView.getText().toString();
            int selectedId = radio_group.getCheckedRadioButtonId();
            RadioButton radioButton = radio_group.findViewById(selectedId);
            boolean isPassenger = radioButton.getText().toString().equals("Passenger");

            login(username, password, isPassenger);
        });

        registerButton.setOnClickListener(view -> {
            Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            myIntent.putExtra("username", usernameTextView.getText().toString());
            myIntent.putExtra("password", passwordTextView.getText().toString());
            startActivity(myIntent);
        });
    }

    private void login(String username, String password, boolean isPassenger) {
        try {
            String url = "http://192.168.42.98:8000/account/login/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("user_type", isPassenger ? "P" : "D");
            final String requestBody = jsonBody.toString();
            StringRequest myRequest = new StringRequest(Request.Method.POST, url,
                    response -> openPanel(username, isPassenger),
                    error -> Toast.makeText(LoginActivity.this, "Wrong username or password!", Toast.LENGTH_LONG).show()
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
            Toast.makeText(LoginActivity.this, "INTERNAL ERROR!", Toast.LENGTH_LONG).show();
        }
    }

    private void openPanel(String username, boolean isPassenger) {
        Intent myIntent;
        if (isPassenger) {
            myIntent = new Intent(LoginActivity.this, PassengerPanelActivity.class);
        } else {
            myIntent = new Intent(LoginActivity.this, DriverPanelActivity.class);
        }

        myIntent.putExtra("username", username);
        startActivity(myIntent);
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}