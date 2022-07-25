package com.app.suber.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String username = "";
        String password = "";

        Intent intent = getIntent();
        if (intent.hasExtra("username")) {
            username = intent.getStringExtra("username");
        }
        if (intent.hasExtra("password")) {
            password = intent.getStringExtra("password");
        }

        TextView usernameTextView = findViewById(R.id.username);
        TextView passwordTextView = findViewById(R.id.password);
        TextView firstnameTextView = findViewById(R.id.firstname);
        TextView lastnameTextView = findViewById(R.id.lastname);
        TextView extraTextView = findViewById(R.id.extra);
        RadioGroup radio_group = findViewById(R.id.radio_group);

        Button login_button = findViewById(R.id.login_button);
        Button register_button = findViewById(R.id.register_button);

        usernameTextView.setText(username);
        passwordTextView.setText(password);

        radio_group.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            String string = radioButton.getText().toString();
            if (string.equals("Passenger")) {
                extraTextView.setText(null);
                extraTextView.setVisibility(View.GONE);
            } else {
                extraTextView.setText(null);
                extraTextView.setVisibility(View.VISIBLE);
            }
        });

        register_button.setOnClickListener(v -> {
            int selectedId = radio_group.getCheckedRadioButtonId();
            RadioButton radioButton = radio_group.findViewById(selectedId);
            boolean isPassenger = radioButton.getText().toString().equals("Passenger");
            if (usernameTextView.getText().toString().isEmpty() || passwordTextView.getText().toString().isEmpty()) {
                handleEmptyInput();
                return;
            }
            register(
                    usernameTextView.getText().toString(),
                    passwordTextView.getText().toString(),
                    firstnameTextView.getText().toString(),
                    lastnameTextView.getText().toString(),
                    extraTextView.getText().toString(),
                    isPassenger
            );
        });

        login_button.setOnClickListener(view -> goToLogin(
                usernameTextView.getText().toString(),
                passwordTextView.getText().toString()
        ));
    }

    private void register(String username, String password, String firstname, String lastname, String extra, boolean isPassenger) {
        try {
            String url = "http://192.168.1.11:8000/account/register/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("first_name", firstname);
            jsonBody.put("last_name", lastname);
            jsonBody.put("extra", extra);
            jsonBody.put("user_type", isPassenger ? "P" : "D");
            final String requestBody = jsonBody.toString();
            StringRequest myRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        goToLogin(username, password);
                    },
                    error -> {
                        Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(RegisterActivity.this, "INTERNAL ERROR!", Toast.LENGTH_LONG).show();
        }
    }

    private void handleEmptyInput() {
        Toast.makeText(
                RegisterActivity.this,
                "Username or password can't be empty!",
                Toast.LENGTH_SHORT).show();
    }

    private void goToLogin(String username, String password) {
        Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        myIntent.putExtra("username", username);
        myIntent.putExtra("password", password);
        RegisterActivity.this.startActivity(myIntent);
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}