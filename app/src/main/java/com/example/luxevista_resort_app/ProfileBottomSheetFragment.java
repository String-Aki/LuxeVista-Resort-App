package com.example.luxevista_resort_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.luxevista_resort_app.databinding.BottomSheetProfileBinding;
import java.util.HashMap;
import java.util.Map;

public class ProfileBottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private boolean isEditMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetProfileBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchAndDisplayUserData();
        setupClickListeners();
    }

    private void fetchAndDisplayUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            binding.emailValueText.setText(currentUser.getEmail());

            db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String firstName = documentSnapshot.getString("firstName");
                    String lastName = documentSnapshot.getString("lastName");
                    String fullName = firstName + " " + lastName;
                    binding.userNameText.setText(fullName);
                    binding.fullNameValueText.setText(fullName);
                }
            });
        }
    }

    private void setupClickListeners() {
        binding.backButton.setOnClickListener(v -> dismiss());

        binding.updateButton.setOnClickListener(v -> {
            toggleEditMode();
        });

        binding.logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dismiss();
        });
    }

    private void toggleEditMode() {
        isEditMode = !isEditMode;

        if (isEditMode) {

            binding.fullNameValueText.setVisibility(View.GONE);
            binding.emailValueText.setVisibility(View.GONE);
            binding.passwordValueText.setVisibility(View.GONE);

            binding.fullNameEditText.setVisibility(View.VISIBLE);
            binding.emailEditText.setVisibility(View.VISIBLE);
            binding.passwordEditText.setVisibility(View.VISIBLE);


            binding.fullNameEditText.setText(binding.fullNameValueText.getText());
            binding.emailEditText.setText(binding.emailValueText.getText());

            binding.updateButton.setText("Save");
        } else {

            saveAllUserData();
        }
    }

    private void saveAllUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        String newFullName = binding.fullNameEditText.getText().toString().trim();
        String newEmail = binding.emailEditText.getText().toString().trim();
        String newPassword = binding.passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(newFullName) || TextUtils.isEmpty(newEmail)) {
            Toast.makeText(getContext(), "Name and Email cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        updateNameInFirestore(newFullName, newEmail, newPassword);
    }

    private void updateNameInFirestore(String fullName, String email, String password) {
        String firstName = fullName;
        String lastName = "";
        if (fullName.contains(" ")) {
            firstName = fullName.substring(0, fullName.lastIndexOf(" "));
            lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
        }
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("firstName", firstName);
        updatedData.put("lastName", lastName);
        updatedData.put("email", email);

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {

                    sendVerificationAndupdatePassword(email, password);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update name.", Toast.LENGTH_SHORT).show();
                    stayInEditMode();
                });
    }

    private void sendVerificationAndupdatePassword(String newEmail, String newPassword) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (!newEmail.equals(currentUser.getEmail())) {
            currentUser.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Verification email sent to " + newEmail + ". Please verify to update.", Toast.LENGTH_LONG).show();
                    updatePasswordInAuth(newPassword);
                } else {
                    Toast.makeText(getContext(), "Failed to send verification email: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    stayInEditMode();
                }
            });
        } else {
            updatePasswordInAuth(newPassword);
        }
    }

    private void updatePasswordInAuth(String newPassword) {
        if (!newPassword.isEmpty()) {
            mAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    finishUpdateSuccessfully();
                } else {
                    Toast.makeText(getContext(), "Failed to update password. It must be at least 6 characters. Please log in again and try.", Toast.LENGTH_LONG).show();
                    stayInEditMode();
                }
            });
        } else {
            finishUpdateSuccessfully();
        }
    }

    private void finishUpdateSuccessfully() {
        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        fetchAndDisplayUserData();

        isEditMode = false;
        binding.fullNameValueText.setVisibility(View.VISIBLE);
        binding.emailValueText.setVisibility(View.VISIBLE);
        binding.passwordValueText.setVisibility(View.VISIBLE);

        binding.fullNameEditText.setVisibility(View.GONE);
        binding.emailEditText.setVisibility(View.GONE);
        binding.passwordEditText.setVisibility(View.GONE);

        binding.updateButton.setText("Update");
    }

    private void stayInEditMode() {
        isEditMode = true;
        binding.updateButton.setText("Save");
    }
}