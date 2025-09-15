package com.example.luxevista_resort_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxevista_resort_app.databinding.ItemReservationAdminBinding;
import java.util.List;

public class ReservationAdminAdapter extends RecyclerView.Adapter<ReservationAdminAdapter.ReservationViewHolder> {

    public interface ReservationActionListener {
        void onApprove(AdminReservationItem item);
        void onDecline(AdminReservationItem item);
    }

    private final List<AdminReservationItem> itemList;
    private final ReservationActionListener listener;

    public ReservationAdminAdapter(List<AdminReservationItem> itemList, ReservationActionListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReservationAdminBinding binding = ItemReservationAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReservationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        holder.bind(itemList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        private final ItemReservationAdminBinding binding;

        public ReservationViewHolder(ItemReservationAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final AdminReservationItem item, final ReservationActionListener listener) {
            binding.itemNameText.setText(item.getItemName());
            binding.userDetailsText.setText("Booked by: " + item.getUserFullName());
            binding.dateDetailsText.setText(item.getDateDetails());
            binding.statusText.setText(item.getStatus());

            binding.approveButton.setOnClickListener(v -> listener.onApprove(item));
            binding.declineButton.setOnClickListener(v -> listener.onDecline(item));

            if (!"Pending".equalsIgnoreCase(item.getStatus())) {
                binding.approveButton.setVisibility(View.GONE);
                binding.declineButton.setVisibility(View.GONE);
            } else {
                binding.approveButton.setVisibility(View.VISIBLE);
                binding.declineButton.setVisibility(View.VISIBLE);
            }
        }
    }
}