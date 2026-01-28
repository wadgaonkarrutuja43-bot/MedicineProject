package com.example.medicineexpiryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<History> historyList;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.qrContentText.setText(history.getQrContent());

        // Format the timestamp to a readable date format
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        holder.timestampText.setText(sdf.format(history.getTimestamp()));

        holder.typeText.setText(history.getType());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView qrContentText;
        TextView timestampText;
        TextView typeText;

        public ViewHolder(View itemView) {
            super(itemView);
            qrContentText = itemView.findViewById(R.id.qr_content);
            timestampText = itemView.findViewById(R.id.timestamp);
            typeText = itemView.findViewById(R.id.type);
        }
    }
}
