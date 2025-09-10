package com.example.luxevista_resort_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.luxevista_resort_app.databinding.BottomSheetBookingBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookingBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String ARG_SUITE_NAME = "suite_name";
    private static final String ARG_SUITE_IMAGE = "suite_image";
    private BottomSheetBookingBinding binding;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    private String suiteName;
    private int suiteImageResId;

    private long checkInDate = 0;
    private long checkOutDate = 0;
    private int adultsCount = 1;
    private int childrenCount = 0;

    public static BookingBottomSheetFragment newInstance(String suiteName,int suiteImageResId) {
        BookingBottomSheetFragment fragment = new BookingBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUITE_NAME, suiteName);
        args.putInt(ARG_SUITE_IMAGE, suiteImageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetBookingBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            this.suiteName = getArguments().getString(ARG_SUITE_NAME);
            this.suiteImageResId = getArguments().getInt(ARG_SUITE_IMAGE);
            binding.suiteNameTitle.setText(this.suiteName);
        }

        updateGuestCounts();
        binding.calendarView.setMinDate(System.currentTimeMillis());

        setupDateSelection();
        setupGuestCounters();
        setupActionButtons();
    }

    private void setupDateSelection() {
        binding.calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            long selectedDate = calendar.getTimeInMillis();

            if (checkInDate == 0) {
                checkInDate = selectedDate;
                binding.selectedDateText.setText("Selected Check-in: " + formatDate(checkInDate));
                binding.calendarView.setMinDate(checkInDate + 86400000);
                //binding.suiteNameTitle.setText("Select Check-out Date");
            } else if (checkOutDate == 0) {
                if (selectedDate > checkInDate) {
                    checkOutDate = selectedDate;
                    binding.suiteNameTitle.setText("Dates Selected");
                    binding.selectedDateText.setText("Booking: " + formatDate(checkInDate) + " to " + formatDate(checkOutDate));
                    binding.calendarView.setEnabled(false);
                }
                else {
                    Toast.makeText(getContext(), "Check-out date must be after check-in date.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupGuestCounters() {
        binding.adultsPlusButton.setOnClickListener(v -> {
            if (adultsCount < 3) {
                adultsCount++;
                updateGuestCounts();
            }
        });

        binding.adultsMinusButton.setOnClickListener(v -> {
            if (adultsCount > 1) {
                adultsCount--;
                updateGuestCounts();
            }
        });

        binding.childrenPlusButton.setOnClickListener(v -> {
            if (childrenCount < 2) {
                childrenCount++;
                updateGuestCounts();
            }
        });

        binding.childrenMinusButton.setOnClickListener(v -> {
            if (childrenCount > 0) {
                childrenCount--;
                updateGuestCounts();
            }
        });
    }

    private void setupActionButtons() {
        binding.clearDatesButton.setOnClickListener(v -> {
            resetDateSelection();
        });

        binding.confirmBookingButton.setOnClickListener(v -> {
            saveBookingToFirestore();
        });
    }

    private void updateGuestCounts() {
        binding.adultsCountText.setText(String.valueOf(adultsCount));
        binding.childrenCountText.setText(String.valueOf(childrenCount));
    }

    private void resetDateSelection() {
        checkInDate = 0;
        checkOutDate = 0;
        binding.calendarView.setEnabled(true);
        binding.calendarView.setMinDate(System.currentTimeMillis());
        binding.suiteNameTitle.setText("Select Check-in Date");
        binding.selectedDateText.setText("Select your dates");
    }

    private String formatDate(long dateInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(new Date(dateInMillis));
    }

    private void saveBookingToFirestore() {
        if (checkInDate == 0 || checkOutDate == 0) {
            Toast.makeText(getContext(), "Please select a check-in and check-out date.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "You must be logged in to make a booking.", Toast.LENGTH_LONG).show();
            return;
        }

        String userId = currentUser.getUid();
        String suiteName = binding.suiteNameTitle.getText().toString();
        Date checkIn = new Date(checkInDate);
        Date checkOut = new Date(checkOutDate);

        Booking booking = new Booking(userId, this.suiteName, checkIn, checkOut, adultsCount, childrenCount, "Pending", this.suiteImageResId);

        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Booking Confirmed!", Toast.LENGTH_LONG).show();
                    dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }
}