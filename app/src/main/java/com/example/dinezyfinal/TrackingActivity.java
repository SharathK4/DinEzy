package com.example.dinezyfinal;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageButton refreshButton, helpButton, locationButton;
    private TextView orderStatusText, arrivalTimeText, restaurantNameText, viewDetailsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        initViews();
        setupMapFragment();
        setupListeners();
    }

    private void initViews() {
        refreshButton = findViewById(R.id.refreshButton);
        helpButton = findViewById(R.id.helpButton);
        locationButton = findViewById(R.id.locationButton);
        orderStatusText = findViewById(R.id.orderStatusText);
        arrivalTimeText = findViewById(R.id.arrivalTimeText);
        restaurantNameText = findViewById(R.id.restaurantNameText);
        viewDetailsText = findViewById(R.id.viewDetailsText);
    }

    private void setupMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setupListeners() {
        refreshButton.setOnClickListener(v -> {
            // Refresh map or order status
        });

        helpButton.setOnClickListener(v -> {
            // Show help or support options
        });

        locationButton.setOnClickListener(v -> {
            // Center map on user's location
            if (mMap != null) {
                // In a real app, you would get the user's actual location
                LatLng userLocation = new LatLng(28.4595, 77.0266);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }
        });

        viewDetailsText.setOnClickListener(v -> {
            // Show order details
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // In a real app, you would get these locations from the API
        LatLng restaurantLocation = new LatLng(28.4595, 77.0266);
        LatLng deliveryLocation = new LatLng(28.4615, 77.0208);
        LatLng userLocation = new LatLng(28.4645, 77.0180);

        // Add markers
        mMap.addMarker(new MarkerOptions().position(restaurantLocation).title("Restaurant"));
        mMap.addMarker(new MarkerOptions().position(deliveryLocation).title("Delivery Person"));
        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));

        // Draw route (simplified)
        mMap.addPolyline(new PolylineOptions()
                .add(restaurantLocation, deliveryLocation, userLocation)
                .width(5)
                .color(getResources().getColor(android.R.color.holo_orange_dark)));

        // Move camera to show the route
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deliveryLocation, 14));
    }
} 