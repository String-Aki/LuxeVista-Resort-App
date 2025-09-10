package com.example.luxevista_resort_app;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista_resort_app.databinding.ActivityServiceDetailBinding;

import java.util.List;

public class ServiceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SERVICE_DETAILS = "EXTRA_SERVICE_DETAILS";
    private ActivityServiceDetailBinding binding;
    private Service service;

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
            Toast.makeText(this, "Reservation feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }
}