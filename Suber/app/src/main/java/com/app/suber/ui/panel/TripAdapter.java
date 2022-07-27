package com.app.suber.ui.panel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.suber.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;


public class TripAdapter extends RecyclerView.Adapter<TripAdapter.WeatherViewHolder> {
    protected final Context context;
    protected String[][] tripData;
    protected Button acceptButton;
    protected String username;

    public TripAdapter(String[][] tripData, Context context, Button acceptButton, String username) {
        this.username = username;
        this.tripData = tripData;
        this.context = context;
        this.acceptButton = acceptButton;
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView sourceTextView;
        TextView destinationTextView;
        TextView priceTextView;
        ConstraintLayout tripLayout;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            tripLayout = itemView.findViewById(R.id.tripLayout);
        }
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tripView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.trip_item, parent, false
        );

        acceptButton.setOnClickListener(view -> {
            acceptTrip(tripData[viewType]);
        });

        tripView.setOnClickListener(view -> {
            tripView.setBackgroundResource(R.color.teal_900);
            acceptButton.setEnabled(true);
        });

        return new WeatherViewHolder(tripView);
    }

    private void acceptTrip(String[] trip) {
        String tripId = trip[3];
        try {
            String url = "http://192.168.42.98:8000/trip/accept-trip/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("trip_id", tripId);
            final String requestBody = jsonBody.toString();
            StringRequest myRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Trip accepted");
                        alertDialog.setMessage("source: " + trip[0] + "\n" +
                                "destination: " + trip[1] + "\n" +
                                "balance: " + trip[2] + "\n");

                        alertDialog.setButton("OK", (dialog, which) -> {
                            Intent intent = new Intent(context, DriverPanelActivity.class);
                            intent.putExtra("username", username);
                            context.startActivity(intent);
                        });

                        alertDialog.show();
                    },
                    error -> {
                        Toast.makeText(context, error.networkResponse.toString(), Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(myRequest);
        } catch (JSONException ignored) {
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        String[] data = tripData[position];
        holder.sourceTextView.setText("s: " + data[0]);
        holder.destinationTextView.setText("d: " + data[1]);
        holder.priceTextView.setText(data[2] + "$");
    }

    @Override
    public int getItemCount() {
        return tripData.length;
    }

    public void clear() {
        int size = tripData.length;
        if (size > 0) {
            tripData = new String[0][0];
            notifyItemRangeRemoved(0, size);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void populateDailyData(JSONArray data) throws JSONException {
        // source, destination, price, id
        String[][] allData = new String[data.length()][4];
        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonObj = data.getJSONObject(i);
            allData[i][0] = jsonObj.getString("source");
            allData[i][1] = jsonObj.getString("destination");
            allData[i][2] = jsonObj.getString("price");
            allData[i][3] = jsonObj.getString("id");
        }
        tripData = allData;
        notifyDataSetChanged();
    }
}