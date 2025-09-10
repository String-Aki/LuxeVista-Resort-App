package com.example.luxevista_resort_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.luxevista_resort_app.databinding.ActivityHomePageBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePage extends AppCompatActivity implements ServiceAdapter.OnServiceClickListener {

    private ActivityHomePageBinding binding;

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
    }

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
}