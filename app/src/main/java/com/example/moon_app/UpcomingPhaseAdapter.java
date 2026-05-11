package com.example.moon_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moon_app.model.UpcomingPhase;

import java.util.List;

public class UpcomingPhaseAdapter extends RecyclerView.Adapter<UpcomingPhaseAdapter.ViewHolder> {

    private List<UpcomingPhase> phases;

    public UpcomingPhaseAdapter(List<UpcomingPhase> phases) {
        this.phases = phases;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upcoming_phase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UpcomingPhase phase = phases.get(position);
        holder.tvName.setText(phase.getName());
        holder.tvDate.setText(phase.getDate());
        holder.tvDaysLeft.setText(phase.getDaysLeft());
    }

    @Override
    public int getItemCount() {
        return phases.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvDaysLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_phase_name);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            tvDaysLeft = itemView.findViewById(R.id.tv_item_days_left);
        }
    }
}