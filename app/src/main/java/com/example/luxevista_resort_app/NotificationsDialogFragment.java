package com.example.luxevista_resort_app;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.luxevista_resort_app.databinding.DialogNotificationsBinding;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDialogFragment extends DialogFragment {

    private DialogNotificationsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply our custom style for the animations
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.TopSlideDialogAnimation);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogNotificationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // This logic makes the dialog appear at the top of the screen
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.TOP);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.closeButton.setOnClickListener(v -> {
            dismiss(); // dismiss() is the standard way to close a DialogFragment
        });

        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Booking Confirmed!", "Your reservation for the Oceanfront Suite is confirmed.", "2 hours ago"));
        notifications.add(new Notification("Spa Appointment Reminder", "Your massage appointment is tomorrow at 10:00 AM.", "1 day ago"));
        notifications.add(new Notification("Special Offer", "Enjoy 20% off at our Fine Dining restaurant this weekend.", "3 days ago"));

        NotificationAdapter adapter = new NotificationAdapter(notifications);
        binding.notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.notificationsRecyclerView.setAdapter(adapter);
    }
}