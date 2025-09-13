package com.example.luxevista_resort_app;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista_resort_app.databinding.ActivitySendNotificationBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class SendNotificationActivity extends AppCompatActivity {

    private ActivitySendNotificationBinding binding;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        binding.sendNotificationButton.setOnClickListener(v -> sendNotification());
        binding.backButton.setOnClickListener(v -> {
            finish(); // This will close the current activity
        });
    }
    private void sendNotification() {
        String title = binding.notificationTitleEditText.getText().toString().trim();
        String message = binding.notificationMessageEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Notification object. The timestamp will be added by the server.
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);

        db.collection("notifications").add(notification)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity and return to the dashboard
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error sending notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}