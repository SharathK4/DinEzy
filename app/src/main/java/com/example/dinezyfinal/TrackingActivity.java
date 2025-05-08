package com.example.dinezyfinal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private GoogleMap mMap;
    private ImageButton refreshButton, helpButton, locationButton;
    private TextView orderStatusText, arrivalTimeText, restaurantNameText, viewDetailsText;
    
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LatLng restaurantLocation;
    private LatLng deliveryLocation;
    private LatLng userLocation;
    private Marker deliveryMarker;
    
    // Simulated delivery coordinates for demo purposes
    private double[] simulatedDeliveryLat = {28.4595, 28.4605, 28.4615, 28.4625, 28.4635, 28.4645};
    private double[] simulatedDeliveryLng = {77.0266, 77.0250, 77.0235, 77.0220, 77.0200, 77.0180};
    private int simulationStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        initViews();
        setupMapFragment();
        setupLocationServices();
        setupListeners();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
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
    
    private void setupLocationServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    updateMap();
                }
            }
        };
    }

    private void setupListeners() {
        refreshButton.setOnClickListener(v -> {
            // Refresh map and order status
            if (mMap != null) {
                updateMap();
                simulateDeliveryMovement();
            }
        });

        helpButton.setOnClickListener(v -> {
            // Show help or support options
            Toast.makeText(this, "Customer support will be with you shortly", Toast.LENGTH_SHORT).show();
        });

        locationButton.setOnClickListener(v -> {
            // Center map on user's location
            if (mMap != null && userLocation != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            } else {
                requestLocationPermission();
            }
        });

        viewDetailsText.setOnClickListener(v -> {
            // Show order details
            Toast.makeText(this, "Order details coming soon", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != 
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastKnownLocation();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
            } else {
                Toast.makeText(this, "Location permission denied. Some features may be limited.", 
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != 
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (mMap != null) {
                    updateMap();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                }
            }
        });
    }
    
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != 
                PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5000)
                .setMaxUpdateDelayMillis(15000)
                .build();
        
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());
        
        // Start simulated delivery updates for demo purposes
        simulateDeliveryMovement();
    }
    
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    
    private void simulateDeliveryMovement() {
        // For demonstration purposes: simulate delivery person movement every few seconds
        simulationStep = (simulationStep + 1) % simulatedDeliveryLat.length;
        deliveryLocation = new LatLng(
                simulatedDeliveryLat[simulationStep], 
                simulatedDeliveryLng[simulationStep]);
        
        updateDeliveryMarker();
        
        // Update order status text based on simulation step
        switch (simulationStep) {
            case 0:
                orderStatusText.setText("Preparing your order");
                break;
            case 1:
                orderStatusText.setText("Order picked up");
                break;
            case 2:
            case 3:
            case 4:
                orderStatusText.setText("Order on the way");
                break;
            case 5:
                orderStatusText.setText("Order arrived");
                break;
        }
    }
    
    private void updateDeliveryMarker() {
        if (mMap != null && deliveryLocation != null) {
            if (deliveryMarker != null) {
                deliveryMarker.setPosition(deliveryLocation);
            } else {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(deliveryLocation)
                        .title("Delivery Person")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                deliveryMarker = mMap.addMarker(markerOptions);
            }
            
            // Update the route
            updateMap();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != 
                PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false); // We use our own button
        
        // Initialize locations
        restaurantLocation = new LatLng(28.4595, 77.0266);
        deliveryLocation = new LatLng(28.4595, 77.0266); // Start at restaurant
        
        // Get user's current location
        getLastKnownLocation();
        
        // Initial setup of map
        if (userLocation == null) {
            // Default user location if actual location not available yet
            userLocation = new LatLng(28.4645, 77.0180);
        }
        
        updateMap();
        
        // Move camera to show the delivery person
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deliveryLocation, 14));
    }
    
    private void updateMap() {
        if (mMap == null || userLocation == null || restaurantLocation == null || deliveryLocation == null) {
            return;
        }
        
        mMap.clear();
        
        // Add markers
        mMap.addMarker(new MarkerOptions()
                .position(restaurantLocation)
                .title("Restaurant")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        
        deliveryMarker = mMap.addMarker(new MarkerOptions()
                .position(deliveryLocation)
                .title("Delivery Person")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        
        mMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        // Draw route (restaurant to delivery person to user)
        mMap.addPolyline(new PolylineOptions()
                .add(restaurantLocation, deliveryLocation)
                .width(5)
                .color(getResources().getColor(android.R.color.holo_blue_dark)));
        
        mMap.addPolyline(new PolylineOptions()
                .add(deliveryLocation, userLocation)
                .width(5)
                .color(getResources().getColor(android.R.color.holo_orange_dark)));
    }
} 