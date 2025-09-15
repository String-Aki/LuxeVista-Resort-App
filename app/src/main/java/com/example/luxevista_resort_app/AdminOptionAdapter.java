package com.example.luxevista_resort_app;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxevista_resort_app.databinding.ItemAdminOptionBinding;
import java.util.List;

public class AdminOptionAdapter extends RecyclerView.Adapter<AdminOptionAdapter.AdminOptionViewHolder> {

    public interface OnAdminOptionClickListener {
        void onAdminOptionClick(AdminOption option);
    }

    private final List<AdminOption> optionList;
    private final OnAdminOptionClickListener listener;

    public AdminOptionAdapter(List<AdminOption> optionList, OnAdminOptionClickListener listener) {
        this.optionList = optionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminOptionBinding binding = ItemAdminOptionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminOptionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOptionViewHolder holder, int position) {
        AdminOption currentOption = optionList.get(position);
        holder.bind(currentOption, listener);
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    static class AdminOptionViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminOptionBinding binding;

        public AdminOptionViewHolder(ItemAdminOptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final AdminOption option, final OnAdminOptionClickListener listener) {
            binding.optionIcon.setImageResource(option.getIconResId());
            binding.optionTitle.setText(option.getTitle());

            binding.optionDescription.setText(option.getDescription());

            binding.getRoot().setCardBackgroundColor(itemView.getContext().getResources().getColor(option.getColorResId()));

            itemView.setOnClickListener(v -> listener.onAdminOptionClick(option));
        }
    }
}