package com.example.luxevista_resort_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.luxevista_resort_app.databinding.ActivityApproveReservationsBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ApproveReservationsActivity extends AppCompatActivity implements ReservationAdminAdapter.ReservationActionListener {

    private ActivityApproveReservationsBinding binding;
    private FirebaseFirestore db;
    private ReservationAdminAdapter adapter;
    private List<AdminReservationItem> reservationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApproveReservationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        setupRecyclerView();
        setupClickListeners();
        fetchAllReservationsAndUsers();
    }
    private void setupRecyclerView() {
        binding.reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservationAdminAdapter(reservationList, this);
        binding.reservationsRecyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        binding.backButton.setOnClickListener(v -> {
            // The finish() method closes the current activity and returns
            // to the screen that opened it (the Admin Dashboard).
            finish();
        });
    }

    private void fetchAllReservationsAndUsers() {
        // Create tasks for all three collections
        Task<QuerySnapshot> bookingsTask = db.collection("bookings").get();
        Task<QuerySnapshot> reservationsTask = db.collection("reservations").get();
        Task<QuerySnapshot> usersTask = db.collection("users").get();

        // Wait for all three tasks to complete
        Tasks.whenAllSuccess(bookingsTask, reservationsTask, usersTask).addOnSuccessListener(results -> {
            reservationList.clear();

            // 1. Create a map of user IDs to names for easy lookup
            Map<String, String> userNames = new HashMap<>();
            QuerySnapshot usersSnapshot = (QuerySnapshot) results.get(2);
            for (DocumentSnapshot userDoc : usersSnapshot.getDocuments()) {
                String fullName = userDoc.getString("firstName") + " " + userDoc.getString("lastName");
                userNames.put(userDoc.getId(), fullName);
            }

            // 2. Process bookings
            QuerySnapshot bookingsSnapshot = (QuerySnapshot) results.get(0);
            for (DocumentSnapshot doc : bookingsSnapshot.getDocuments()) {
                Booking booking = doc.toObject(Booking.class);
                if (booking != null) {
                    String userName = userNames.getOrDefault(booking.getUserId(), "Unknown User");
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());
                    String dateDetails = sdf.format(booking.getCheckInDate()) + " - " + sdf.format(booking.getCheckOutDate());
                    reservationList.add(new AdminReservationItem(doc.getId(), "bookings", booking.getSuiteName(), userName, dateDetails, booking.getStatus()));
                }
            }

            // 3. Process in-house reservations
            QuerySnapshot reservationsSnapshot = (QuerySnapshot) results.get(1);
            for (DocumentSnapshot doc : reservationsSnapshot.getDocuments()) {
                Reservation reservation = doc.toObject(Reservation.class);
                if (reservation != null) {
                    String userName = userNames.getOrDefault(reservation.getUserId(), "Unknown User");
                    String dateDetails = "Today";
                    reservationList.add(new AdminReservationItem(doc.getId(), "reservations", reservation.getServiceName(), userName, dateDetails, reservation.getStatus()));
                }
            }

            // 4. Notify the adapter ONE time, after all data is processed.
            adapter.notifyDataSetChanged();

        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load reservations.", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public void onApprove(AdminReservationItem item) {
        updateStatus(item, "Reserved");
    }

    @Override
    public void onDecline(AdminReservationItem item) {
        updateStatus(item, "Declined");
    }

    private void updateStatus(AdminReservationItem item, String newStatus) {
        db.collection(item.getCollectionName()).document(item.getDocumentId())
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                    item.setStatus(newStatus);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update status.", Toast.LENGTH_SHORT).show();
                });
    }
}