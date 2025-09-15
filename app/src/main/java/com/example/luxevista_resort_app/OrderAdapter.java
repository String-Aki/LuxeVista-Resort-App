package com.example.luxevista_resort_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxevista_resort_app.databinding.ItemOrderBinding;
import com.example.luxevista_resort_app.databinding.ItemOrderHeadingBinding;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADING = 0;
    private static final int TYPE_ORDER = 1;

    public interface OnCancelButtonClickListener {
        void onCancelClick(Order order);
    }

    private final List<Object> items;
    private final OnCancelButtonClickListener listener;

    public OrderAdapter(List<Object> items, OnCancelButtonClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return TYPE_HEADING;
        } else if (items.get(position) instanceof Order) {
            return TYPE_ORDER;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADING) {
            ItemOrderHeadingBinding binding = ItemOrderHeadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new HeadingViewHolder(binding);
        } else {
            ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new OrderViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADING) {
            String title = (String) items.get(position);
            ((HeadingViewHolder) holder).bind(title);
        } else {
            Order order = (Order) items.get(position);
            ((OrderViewHolder) holder).bind(order, listener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding binding;

        public OrderViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Order order, final OnCancelButtonClickListener listener) {
            binding.orderImage.setImageResource(order.getImageResId());
            binding.orderNameText.setText(order.getName());
            binding.orderPriceText.setText(order.getPrice());
            binding.orderInfoText.setText(order.getDetails());

            if ("Pending".equalsIgnoreCase(order.getStatus())) {
                binding.orderStatusText.setVisibility(View.GONE);
                binding.cancelOrderButton.setVisibility(View.VISIBLE);
                binding.cancelOrderButton.setOnClickListener(v -> listener.onCancelClick(order));
            } else {
                binding.orderStatusText.setVisibility(View.VISIBLE);
                binding.cancelOrderButton.setVisibility(View.GONE);
                binding.orderStatusText.setText(order.getStatus());
            }
        }
    }

    static class HeadingViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderHeadingBinding binding;

        public HeadingViewHolder(ItemOrderHeadingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String title) {
            binding.headingText.setText(title);
        }
    }
}