package com.example.luxevista_resort_app;

import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista_resort_app.databinding.ActivitySuiteDetailBinding;

import java.util.List;

public class SuiteDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SUITE_DETAILS = "EXTRA_SUITE_DETAILS";
    private ActivitySuiteDetailBinding binding;
    private Suite suite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuiteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent().hasExtra(EXTRA_SUITE_DETAILS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suite = getIntent().getSerializableExtra(EXTRA_SUITE_DETAILS, Suite.class);
            } else {
                suite = (Suite) getIntent().getSerializableExtra(EXTRA_SUITE_DETAILS);
            }
        }

        if (suite != null) {
            populateUi();
        }

        setupClickListeners();
    }

    private void populateUi() {
        binding.suiteDetailImage.setImageResource(suite.getImageResId());
        binding.suiteNameText.setText(suite.getName());
        binding.suitePriceText.setText(suite.getPrice());

        StringBuilder featuresBuilder = new StringBuilder();
        List<String> features = suite.getFeatures();
        for (int i = 0; i < features.size(); i++) {
            featuresBuilder.append("• ");
            featuresBuilder.append(features.get(i));
            if (i < features.size() - 1) {
                featuresBuilder.append("\n\n");
            }
        }
        binding.featuresText.setText(featuresBuilder.toString());
    }

    private void setupClickListeners() {
        binding.backButton.setOnClickListener(v -> {
            finish();
        });

        binding.bookNowButton.setOnClickListener(v -> {
            //BookingBottomSheetFragment bookingSheet = BookingBottomSheetFragment.newInstance(suite.getName());
            //bookingSheet.show(getSupportFragmentManager(), bookingSheet.getTag());
        });
    }
}