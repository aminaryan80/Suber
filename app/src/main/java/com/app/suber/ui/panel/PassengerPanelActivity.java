package com.app.suber.ui.panel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.suber.R;

public class PassengerPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_panel);

        Button searchButton = findViewById(R.id.searchButton);
        Button profileButton = findViewById(R.id.profileButton);
        TextView sourceTextView = findViewById(R.id.sourceTextView);
        TextView destinationTextView = findViewById(R.id.destinationTextView);

        searchButton.setOnClickListener(view -> {
            searchForCar(sourceTextView.getText().toString(), destinationTextView.getText().toString());
        });

        profileButton.setOnClickListener(view -> {
            Toast.makeText(PassengerPanelActivity.this, "Profile", Toast.LENGTH_SHORT).show();
        });
    }

    private void searchForCar(String source, String destination) {

    }
}