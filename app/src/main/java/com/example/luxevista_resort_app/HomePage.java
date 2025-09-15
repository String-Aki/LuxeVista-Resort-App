package com.example.luxevista_resort_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.luxevista_resort_app.databinding.ActivityHomePageBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends AppCompatActivity implements ServiceAdapter.OnServiceClickListener, SuiteAdapter.OnSuiteClickListener, FilterBottomSheetFragment.FilterListener {

    private ActivityHomePageBinding binding;
    private SuiteAdapter suiteAdapter;
    private final List<Suite> allSuites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupServicesRecyclerView();
        setupSuitesViewPager();
        setupClickListeners();
    }

// Inhouse Services Displaying and Managing Methods
    private void setupServicesRecyclerView() {

        List<Service> services = new ArrayList<>();
        services.add(new Service("Fine Dining", R.drawable.img_dining_inhouse, Arrays.asList(
                new MenuSection("Breakfast", Arrays.asList(
                        "Enjoy a gourmet breakfast with fresh, locally-sourced ingredients, from classic pastries to signature egg dishes.",
                        "Served daily from 7:00 AM to 11:00 AM."
                )),
                new MenuSection("Lunch", Arrays.asList(
                        "A delightful lunch menu featuring light and flavorful options, including fresh salads, artisanal sandwiches, and grilled seafood.",
                        "Served daily from 12:00 PM to 3:00 PM."
                )),
                new MenuSection("Dinner", Arrays.asList(
                        "An exquisite fine dining experience with a multi-course tasting menu and an extensive wine list.",
                        "Reservations are highly recommended. Served nightly from 6:00 PM to 10:00 PM."
                ))
        ),
                "dining"
        ));
        services.add(new Service("Spa & Wellness", R.drawable.img_spa_inhouse, List.of(
                "Indulge in a wide range of rejuvenating treatments, from massages to facials, designed to relax and revitalize.",
                "Our serene spa environment includes a sauna, steam room, and relaxation lounge for your complete well-being.")
        ));
        services.add(new Service("Beach Tour", R.drawable.img_beach_tour,List.of(
                "Explore the pristine coastline with our guided beach tours, available daily.",
                "Discover hidden coves, vibrant marine life, and breathtaking sunset views.")
        ));
        services.add(new Service("Poolside Cabanas", R.drawable.img_poolside_inhouse, List.of(
                "Reserve your all day private cabana and enjoy plush lounge seating, complimentary chilled water, and fresh towels.",
                "Your private cabana includes comfortable seating for up to four guests, sheer privacy curtains, and a dedicated attendant.")
        ));


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.servicesRecyclerView.setLayoutManager(layoutManager);

        ServiceAdapter serviceAdapter = new ServiceAdapter(this, services, this);

        binding.servicesRecyclerView.setAdapter(serviceAdapter);
    }

    @Override
    public void onServiceClick(Service service) {

        if ("dining".equals(service.getServiceType())) {
            Intent intent = new Intent(this, FineDiningActivity.class);
            intent.putExtra(FineDiningActivity.EXTRA_DINING_DETAILS, service);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ServiceDetailActivity.class);
            intent.putExtra(ServiceDetailActivity.EXTRA_SERVICE_DETAILS, service);
            startActivity(intent);
        }
    }

    //Suite Displaying and Managing Methods
    private void setupSuitesViewPager() {
        allSuites.clear();
        allSuites.add(new Suite("Oceanfront Suite",
                R.drawable.img_oceanview_suite,
                950,
                List.of(
                        "Experience breathtaking, panoramic vistas of the sparkling ocean from your expansive private terrace.",
                        "Designed for ultimate relaxation, this suite blurs the lines between its luxurious interior and the natural beauty outside.",
                        "Features plush, oversized modular seating perfect for lounging, entertaining, or simply soaking in the serene coastal atmosphere.",
                        "Adorned with natural materials, a soothing neutral palette, and thoughtful touches like a woven pendant light, creating a sophisticated yet relaxed beach house feel."),
                "Suite",
                false
        ));
        allSuites.add(new Suite("Executive Suite",
                R.drawable.img_dulexe_suite_2,
                450,
                List.of(
                        "Experience modern luxury in this meticulously designed suite, featuring a minimalist aesthetic with clean lines and premium materials.",
                        "Unwind on a plush, low-profile king bed dressed in high-thread-count linens for an exceptional night's sleep.",
                        "The room boasts a perfectly balanced layout, with designer pendant lighting and matching bedside tables creating a harmonious and calming atmosphere.",
                        "Luxury en-suite bathroom featuring a rainfall shower, designer toiletries, and soft bathrobes."),
                "Suite",
                false
        ));
        allSuites.add(new Suite("Deluxe Suite",
                R.drawable.img_dulexe_suite,
                375,
                List.of(
                        "This suite offers a classic and refined decor, featuring rich wood furnishings and a soothing, neutral color palette.",
                        "Sink into our signature king bed with a stately upholstered headboard and crisp, white linens.",
                        "Large windows, dressed with both sheer and blackout curtains, allow for beautiful natural light or complete privacy.",
                        "This room is perfect for travelers who appreciate traditional luxury and a cozy, inviting atmosphere."),
                "Suite",
                false
        ));
        allSuites.add(new Suite("Sunlit Double Room",
                R.drawable.img_double_br,
                310,
                List.of(
                        "Bright, airy, and filled with natural elements, this room is designed for relaxation and rejuvenation.",
                        "The room is centered around a beautifully crafted, light-wood bed frame with soft, neutral-toned bedding.",
                        "Expansive windows allow sunlight to pour in, creating a warm and uplifting environment throughout the day."),
                "Room",
                false
        ));
        allSuites.add(new Suite("Superior Queen Room",
                R.drawable.img_standard_suite,
                250,
                List.of(
                        "This room combines minimalist design with classic European architectural details",
                        "Features a comfortable, low-profile upholstered queen bed with a relaxed, layered throw blanket.",
                        "The intimate and thoughtfully curated space feels more like a private Parisian apartment than a hotel room."),
                "Room",
                false
        ));

        suiteAdapter = new SuiteAdapter(new ArrayList<>(allSuites), this);
        binding.suitesViewPager.setAdapter(suiteAdapter);

        binding.suitesViewPager.setClipToPadding(false);
        binding.suitesViewPager.setClipChildren(false);
        binding.suitesViewPager.setOffscreenPageLimit(3);

        CompositePageTransformer compositeTransformer = new CompositePageTransformer();

        compositeTransformer.addTransformer(new MarginPageTransformer(20));
        compositeTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        binding.suitesViewPager.setPageTransformer(compositeTransformer);

        binding.textViewSuitesTitle.setText(allSuites.get(0).getName());

        binding.suitesViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position >= 0 && position < suiteAdapter.getSuiteList().size()) {
                    Suite currentSuite = suiteAdapter.getSuiteList().get(position);
                    binding.textViewSuitesTitle.setText(currentSuite.getName());
                }
            }
        });
        if (!allSuites.isEmpty()) {
            binding.textViewSuitesTitle.setText(allSuites.get(0).getName());
        }
    }

    @Override
    public void onFiltersApplied(String sortBy, boolean showAvailableOnly, List<String> selectedTypes) {
        List<Suite> filteredList = new ArrayList<>(allSuites);

        if (showAvailableOnly) {
            filteredList = filteredList.stream()
                    .filter(Suite::isAvailable)
                    .collect(Collectors.toList());
        }
        if (!selectedTypes.isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(suite -> selectedTypes.contains(suite.getType()))
                    .collect(Collectors.toList());
        }

        if ("price_asc".equals(sortBy)) {
            Collections.sort(filteredList, (s1, s2) -> Integer.compare(s1.getPrice(), s2.getPrice()));
        } else if ("price_desc".equals(sortBy)) {
            Collections.sort(filteredList, (s1, s2) -> Integer.compare(s2.getPrice(), s1.getPrice()));
        }


        suiteAdapter.updateSuites(filteredList);
        if (!filteredList.isEmpty()) {
            binding.textViewSuitesTitle.setText(filteredList.get(0).getName());
            binding.suitesViewPager.setCurrentItem(0, false);
        } else {
            binding.textViewSuitesTitle.setText("No rooms match your criteria");
        }
    }

    public void onSuiteClick(Suite suite) {

        Intent intent = new Intent(this, SuiteDetailActivity.class);
        intent.putExtra(SuiteDetailActivity.EXTRA_SUITE_DETAILS, suite);
        startActivity(intent);
    }

    private void setupClickListeners() {

        binding.navBookingsIcon.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyOrdersActivity.class);
            startActivity(intent);
        });

        binding.navSettingsIcon.setOnClickListener(v -> {
            FilterBottomSheetFragment filterSheet = new FilterBottomSheetFragment();
            filterSheet.show(getSupportFragmentManager(), filterSheet.getTag());
        });

        binding.imageViewProfile.setOnClickListener(v -> {
            ProfileBottomSheetFragment profileSheet = new ProfileBottomSheetFragment();
            profileSheet.show(getSupportFragmentManager(), profileSheet.getTag());
        });

        binding.imageViewNotification.setOnClickListener(v -> {
            NotificationsDialogFragment notificationsDialog = new NotificationsDialogFragment();
            notificationsDialog.show(getSupportFragmentManager(), "notifications_dialog");
        });
    }
}


