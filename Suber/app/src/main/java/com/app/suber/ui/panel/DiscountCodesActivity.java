package com.app.suber.ui.panel;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class DiscountCodesActivity extends AppCompatActivity {
    private DiscountAdapter adapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_codes);

        username = getIntent().getStringExtra("username");

        handleRecyclerView();
        updateData();
    }

    private void updateData() {
        String url = "http://192.168.42.98:8000/discount/get-discount-codes/";
        StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        adapter.populateDailyData(new JSONArray(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(DiscountCodesActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(myRequest);
    }

    private void addDivider(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        DiscountCodesActivity.this,
                        LinearLayoutManager.VERTICAL
                )
        );
    }

    private void handleRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.discountCodeRecyclerView);
        addDivider(recyclerView);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(
                DiscountCodesActivity.this,
                LinearLayoutManager.VERTICAL,
                false
        );
        adapter = new DiscountAdapter(new String[][]{}, DiscountCodesActivity.this);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}