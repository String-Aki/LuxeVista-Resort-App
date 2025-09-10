package com.example.luxevista_resort_app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista_resort_app.databinding.ActivityFineDiningBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FineDiningActivity extends AppCompatActivity {

    public static final String EXTRA_DINING_DETAILS = "EXTRA_DINING_DETAILS";

    private ActivityFineDiningBinding binding;

    private Service service;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFineDiningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (getIntent().hasExtra(EXTRA_DINING_DETAILS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                service = getIntent().getSerializableExtra(EXTRA_DINING_DETAILS, Service.class);
            } else {
                service = (Service) getIntent().getSerializableExtra(EXTRA_DINING_DETAILS);
            }
        }

        if (service != null) {
            populateInitialUi();
            setupTabs();
        }

        setupClickListeners();
    }

    private void populateInitialUi() {
        binding.diningDetailImage.setImageResource(service.getImageResId());

        if (service.getMenuSections() != null && !service.getMenuSections().isEmpty()) {
            updateUiForSection(service.getMenuSections().get(0));
        }
    }

    private void setupTabs() {
        binding.breakfastTab.setOnClickListener(v -> updateUiForSection(service.getMenuSections().get(0)));
        binding.lunchTab.setOnClickListener(v -> updateUiForSection(service.getMenuSections().get(1)));
        binding.dinnerTab.setOnClickListener(v -> updateUiForSection(service.getMenuSections().get(2)));
    }

    private void updateUiForSection(MenuSection section) {
        binding.mealTitleText.setText(section.getTitle());
        binding.reserveButton.setText("Reserve For " + section.getTitle());

        StringBuilder descriptionBuilder = new StringBuilder();
        List<String> items = section.getItems();
        for (int i = 0; i < items.size(); i++) {
            descriptionBuilder.append("• ");
            descriptionBuilder.append(items.get(i));
            if (i < items.size() - 1) {
                descriptionBuilder.append("\n\n");
            }
        }
        binding.mealDescriptionText.setText(descriptionBuilder.toString());

        updateTabStyles(section.getTitle());
    }

    private void updateTabStyles(String selectedTitle) {

        resetTabStyle(binding.breakfastTab);
        resetTabStyle(binding.lunchTab);
        resetTabStyle(binding.dinnerTab);

        if (selectedTitle.equals("Breakfast")) {
            setSelectedTabStyle(binding.breakfastTab);
        } else if (selectedTitle.equals("Lunch")) {
            setSelectedTabStyle(binding.lunchTab);
        } else if (selectedTitle.equals("Dinner")) {
            setSelectedTabStyle(binding.dinnerTab);
        }
    }

    private void setSelectedTabStyle(TextView textView) {
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        textView.setTypeface(null, Typeface.BOLD);
    }

    private void resetTabStyle(TextView textView) {
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        textView.setTypeface(null, Typeface.NORMAL);
    }

    private void setupClickListeners() {
        binding.backButton.setOnClickListener(v -> finish());
        binding.reserveButton.setOnClickListener(v -> {
            makeDiningReservation();
        });
    }

    private void makeDiningReservation() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String reservationDetails = binding.reserveButton.getText().toString();

            Reservation reservation = new Reservation(userId, reservationDetails, "Confirmed");

            db.collection("reservations")
                    .add(reservation)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Reservation confirmed!", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to make reservation.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "You must be logged in to make a reservation.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }
    }
}