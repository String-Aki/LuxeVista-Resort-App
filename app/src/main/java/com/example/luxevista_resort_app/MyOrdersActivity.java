package com.example.luxevista_resort_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.luxevista_resort_app.databinding.ActivityMyOrdersBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyOrdersActivity extends AppCompatActivity implements OrderAdapter.OnCancelButtonClickListener {

    private ActivityMyOrdersBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupClickListeners();
        fetchOrdersAndReservations();
    }

    private void fetchOrdersAndReservations() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to see your orders.", Toast.LENGTH_SHORT).show();
            updateEmptyState(true);
            return;
        }
        String userId = currentUser.getUid();

        Task<QuerySnapshot> bookingsTask = db.collection("bookings").whereEqualTo("userId", userId).get();
        Task<QuerySnapshot> reservationsTask = db.collection("reservations").whereEqualTo("userId", userId).get();

        Tasks.whenAllSuccess(bookingsTask, reservationsTask).addOnSuccessListener(results -> {
            // 1. Create separate lists for active and history items
            List<Order> activeSuiteBookings = new ArrayList<>();
            List<Order> activeInhouseReservations = new ArrayList<>();
            List<Order> historyItems = new ArrayList<>();

            // Process bookings result
            QuerySnapshot bookingsSnapshot = (QuerySnapshot) results.get(0);
            for (DocumentSnapshot document : bookingsSnapshot.getDocuments()) {
                Booking booking = document.toObject(Booking.class);
                if (booking != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());
                    String dateRange = sdf.format(booking.getCheckInDate()) + " - " + sdf.format(booking.getCheckOutDate());
                    Order order = new Order(booking.getSuiteName(), dateRange, "", booking.getImageResId(), booking.getStatus());
                    order.setDocumentId(document.getId());
                    order.setOrderType("bookings");

                    // 2. Sort into the correct list based on status
                    if ("Completed".equalsIgnoreCase(booking.getStatus()) || "Cancelled".equalsIgnoreCase(booking.getStatus())) {
                        historyItems.add(order);
                    } else {
                        activeSuiteBookings.add(order);
                    }
                }
            }

            // Process reservations result
            QuerySnapshot reservationsSnapshot = (QuerySnapshot) results.get(1);
            for (DocumentSnapshot document : reservationsSnapshot.getDocuments()) {
                Reservation reservation = document.toObject(Reservation.class);
                if (reservation != null) {
                    Order order = new Order(reservation.getServiceName(), "Today", "", reservation.getImageResId(), reservation.getStatus());
                    order.setDocumentId(document.getId());
                    order.setOrderType("reservations");

                    // 2. Sort into the correct list based on status
                    if ("Completed".equalsIgnoreCase(reservation.getStatus()) || "Cancelled".equalsIgnoreCase(reservation.getStatus())) {
                        historyItems.add(order);
                    } else {
                        activeInhouseReservations.add(order);
                    }
                }
            }

            // 3. Build the final combined list for the adapter
            List<Object> combinedList = new ArrayList<>();
            if (!activeSuiteBookings.isEmpty()) {
                combinedList.add("Active Suite Bookings");
                combinedList.addAll(activeSuiteBookings);
            }
            if (!activeInhouseReservations.isEmpty()) {
                combinedList.add("Active In-house Reservations");
                combinedList.addAll(activeInhouseReservations);
            }
            if (!historyItems.isEmpty()) {
                combinedList.add("History");
                combinedList.addAll(historyItems);
            }

            updateEmptyState(combinedList.isEmpty());
            setupRecyclerView(combinedList);
        });
    }

    private void setupRecyclerView(List<Object> combinedList) {
        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderAdapter adapter = new OrderAdapter(combinedList, this);
        binding.ordersRecyclerView.setAdapter(adapter);
    }

    private void updateEmptyState(boolean show) {
        if (show) {
            binding.emptyStateText.setVisibility(View.VISIBLE);
            binding.ordersRecyclerView.setVisibility(View.GONE);
        } else {
            binding.emptyStateText.setVisibility(View.GONE);
            binding.ordersRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCancelClick(Order order) {
        if (order.getDocumentId() == null || order.getOrderType() == null) {
            Toast.makeText(this, "Cannot cancel this order.", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Cancel Order")
                .setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> {
                    db.collection(order.getOrderType()).document(order.getDocumentId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Order cancelled successfully.", Toast.LENGTH_SHORT).show();
                                fetchOrdersAndReservations();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to cancel order.", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void setupClickListeners() {
        binding.backButton.setOnClickListener(v -> finish());
    }
}