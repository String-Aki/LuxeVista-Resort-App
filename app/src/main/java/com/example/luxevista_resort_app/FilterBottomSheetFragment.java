package com.example.luxevista_resort_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.example.luxevista_resort_app.databinding.BottomSheetFilterBinding;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {

    public interface FilterListener {
        void onFiltersApplied(String sortBy, boolean showAvailableOnly, List<String> selectedTypes);
    }
    private BottomSheetFilterBinding binding;
    private FilterListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FilterListener) {
            listener = (FilterListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FilterListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.applyFiltersButton.setOnClickListener(v -> {
            String sortBy = "none";
            int selectedSortId = binding.sortRadioGroup.getCheckedRadioButtonId();
            if (selectedSortId == R.id.radio_low_to_high) {
                sortBy = "price_asc";
            } else if (selectedSortId == R.id.radio_high_to_low) {
                sortBy = "price_desc";
            }
            boolean showAvailableOnly = binding.availabilitySwitch.isChecked();

            List<String> selectedTypes = new ArrayList<>();
            for (int i = 0; i < binding.typeChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) binding.typeChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selectedTypes.add(chip.getText().toString());
                }
            }

            listener.onFiltersApplied(sortBy, showAvailableOnly, selectedTypes);
            dismiss();
        });
    }

}