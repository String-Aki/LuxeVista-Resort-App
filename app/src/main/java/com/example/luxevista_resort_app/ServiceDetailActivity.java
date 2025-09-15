package com.example.luxevista_resort_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista_resort_app.databinding.ActivityServiceDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ServiceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SERVICE_DETAILS = "EXTRA_SERVICE_DETAILS";
    private ActivityServiceDetailBinding binding;
    private Service service;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServiceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        if (getIntent().hasExtra(EXTRA_SERVICE_DETAILS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                service = getIntent().getSerializableExtra(EXTRA_SERVICE_DETAILS, Service.class);
            } else {
                service = (Service) getIntent().getSerializableExtra(EXTRA_SERVICE_DETAILS);
            }
        }

        if (service != null) {
            populateUi();
        }

        setupClickListeners();
    }

    private void populateUi() {

        binding.serviceDetailImage.setImageResource(service.getImageResId());
        binding.serviceNameText.setText(service.getName());

        StringBuilder descriptionBuilder = new StringBuilder();
        List<String> descriptions = service.getDescriptions();
        for (int i = 0; i < descriptions.size(); i++) {
            descriptionBuilder.append("• ");
            descriptionBuilder.append(descriptions.get(i));
            if (i < descriptions.size() - 1) {
                descriptionBuilder.append("\n\n");
            }
        }
        binding.serviceDescriptionText.setText(descriptionBuilder.toString());
    }

    private void setupClickListeners() {
        binding.backButton.setOnClickListener(v -> {
            finish();
        });

        binding.reserveButton.setOnClickListener(v -> {
            makeReservation();
        });
    }

    private void makeReservation() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String serviceName = service.getName();

            Reservation reservation = new Reservation(userId, serviceName, "Pending",service.getImageResId());

            db.collection("reservations")
                    .add(reservation)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Reservation confirmed for " + serviceName, Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to make reservation. Please try again.", Toast.LENGTH_SHORT).show();
                    });

        } else {
            Toast.makeText(this, "You must be logged in to make a reservation.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }
    }
}