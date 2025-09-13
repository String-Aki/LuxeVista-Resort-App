package com.example.luxevista_resort_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.luxevista_resort_app.databinding.ActivityAdminDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity implements AdminOptionAdapter.OnAdminOptionClickListener {

    private ActivityAdminDashboardBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        setupRecyclerView();
        setupClickListeners();
    }
    private void setupClickListeners() {
        binding.signOutButton.setOnClickListener(v -> {
            // Sign the user out of Firebase
            mAuth.signOut();
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to the login screen and clear all previous activities
            Intent intent = new Intent(this, login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
        private void setupRecyclerView() {
            // 2. Create the list of admin options.
            // NOTE: You will need to add these icons to your res/drawable folder.
            List<AdminOption> options = new ArrayList<>();
            options.add(new AdminOption("Create In-house Services", R.drawable.ico_cube, R.color.admin_card_color_1, "Create a new in-house service")); // Placeholder icon
            options.add(new AdminOption("Approve Reservations", R.drawable.ico_checks, R.color.admin_card_color_2, "Approve or reject reservations")); // Placeholder icon
            options.add(new AdminOption("Send Notifications", R.drawable.ico_send, R.color.admin_card_color_3, "Send notifications to all users")); // Placeholder icon
            options.add(new AdminOption("Manage Suites", R.drawable.ico_carousel, R.color.admin_card_color_4, "Update suite details and photos")); // Placeholder icon

            // 3. Set up the RecyclerView.
            // The GridLayoutManager is what creates the two-column grid.
            binding.adminOptionsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            AdminOptionAdapter adapter = new AdminOptionAdapter(options, this);
            binding.adminOptionsRecyclerView.setAdapter(adapter);
        }

        @Override
        public void onAdminOptionClick(AdminOption option) {
            // Use a Toast for now. Later, we can navigate to different screens.
            Toast.makeText(this, option.getTitle() + " clicked", Toast.LENGTH_SHORT).show();

            if ("Approve Reservations".equals(option.getTitle())) {
                Intent intent = new Intent(this, ApproveReservationsActivity.class);
                startActivity(intent);
            } else if ("Send Notifications".equals(option.getTitle())) {
                Intent intent = new Intent(this, SendNotificationActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, option.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
            }
        }
    }