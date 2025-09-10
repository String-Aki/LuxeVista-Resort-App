package com.example.luxevista_resort_app;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    public interface OnServiceClickListener {
        void onServiceClick(Service service);
    }

    private final List<Service> serviceList;
    private final int itemWidth;
    private final OnServiceClickListener listener;

    public ServiceAdapter(Context context, List<Service> serviceList, OnServiceClickListener listener) {
        this.serviceList = serviceList;
        this.listener = listener;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int padding = (int) (48 * displayMetrics.density);
        this.itemWidth = (displayMetrics.widthPixels - padding) / 2;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = itemWidth;
        view.setLayoutParams(layoutParams);

        return new ServiceViewHolder(view, serviceList, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {

        Service currentService = serviceList.get(position);
        holder.bind(currentService);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceImage;
        TextView serviceName;

        public ServiceViewHolder(@NonNull View itemView, List<Service> serviceList, OnServiceClickListener listener) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.serviceImageView);
            serviceName = itemView.findViewById(R.id.serviceNameTextView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onServiceClick(serviceList.get(position));
                }
            });
        }

        void bind(Service service) {
            serviceName.setText(service.getName());
            serviceImage.setImageResource(service.getImageResId());
        }
    }
}