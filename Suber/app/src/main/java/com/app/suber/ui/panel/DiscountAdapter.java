package com.app.suber.ui.panel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.suber.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {
    protected final Context context;
    protected String[][] discountData;

    public DiscountAdapter(String[][] discountData, Context context) {
        this.discountData = discountData;
        this.context = context;
    }

    public static class DiscountViewHolder extends RecyclerView.ViewHolder {
        TextView codeTextView;
        TextView percentTextView;

        public DiscountViewHolder(View itemView) {
            super(itemView);
            codeTextView = itemView.findViewById(R.id.codeTextView);
            percentTextView = itemView.findViewById(R.id.percentTextView);
        }
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tripView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.discount_item, parent, false
        );

        return new DiscountViewHolder(tripView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        String[] data = discountData[position];
        holder.codeTextView.setText(data[0]);
        holder.percentTextView.setText(data[1] + "%");
    }

    @Override
    public int getItemCount() {
        return discountData.length;
    }

    public void clear() {
        int size = discountData.length;
        if (size > 0) {
            discountData = new String[0][0];
            notifyItemRangeRemoved(0, size);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void populateDailyData(JSONArray data) throws JSONException {
        // code, percent
        String[][] allData = new String[data.length()][2];
        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonObj = data.getJSONObject(i);
            allData[i][0] = jsonObj.getString("code");
            allData[i][1] = jsonObj.getString("percent");
        }
        discountData = allData;
        notifyDataSetChanged();
    }
}