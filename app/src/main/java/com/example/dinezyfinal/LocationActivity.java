package com.example.dinezyfinal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    
    private GoogleMap mMap;
    private EditText searchEditText;
    private Button confirmButton;
    private ImageButton currentLocationButton, backButton;
    private TextView addressTextView;
    private View confirmPanel;
    
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LatLng selectedLocation;
    private Marker locationMarker;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initViews();
        setupMapFragment();
        setupLocationServices();
        setupListeners();
        
        geocoder = new Geocoder(this, Locale.getDefault());
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
        searchEditText = findViewById(R.id.searchEditText);
        confirmButton = findViewById(R.id.confirmButton);
        currentLocationButton = findViewById(R.id.currentLocationButton);
        backButton = findViewById(R.id.backButton);
        addressTextView = findViewById(R.id.addressTextView);
        confirmPanel = findViewById(R.id.confirmPanel);
        
        // Initially hide the confirm panel until a location is selected
        confirmPanel.setVisibility(View.GONE);
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
                if (location != null && selectedLocation == null) {
                    // Only use this if user hasn't selected a location yet
                    selectedLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    updateSelectedLocationMarker();
                    getAddressFromLocation(selectedLocation);
                }
            }
        };
    }

    private void setupListeners() {
        confirmButton.setOnClickListener(v -> {
            if (selectedLocation != null) {
                // Save selected location (e.g., to SharedPreferences)
                String address = addressTextView.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", selectedLocation.latitude);
                resultIntent.putExtra("longitude", selectedLocation.longitude);
                resultIntent.putExtra("address", address);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        currentLocationButton.setOnClickListener(v -> {
            if (mMap != null) {
                getLastKnownLocation();
            }
        });
        
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            String searchText = searchEditText.getText().toString().trim();
            if (!searchText.isEmpty()) {
                searchLocation(searchText);
            }
        });
    }
    
    private void searchLocation(String locationName) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                selectedLocation = new LatLng(address.getLatitude(), address.getLongitude());
                
                updateSelectedLocationMarker();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15));
                
                // Show address
                getAddressFromLocation(selectedLocation);
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Geocoding service not available", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void getAddressFromLocation(LatLng latLng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude, 1);
            
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i));
                    if (i < address.getMaxAddressLineIndex()) {
                        sb.append(", ");
                    }
                }
                
                addressTextView.setText(sb.toString());
                confirmPanel.setVisibility(View.VISIBLE);
            } else {
                addressTextView.setText("Address not found");
            }
        } catch (IOException e) {
            addressTextView.setText("Unable to get address");
        }
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
            requestLocationPermission();
            return;
        }
        
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                selectedLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (mMap != null) {
                    updateSelectedLocationMarker();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15));
                    getAddressFromLocation(selectedLocation);
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
    }
    
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
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
        mMap.getUiSettings().setMyLocationButtonEnabled(false); // We use our custom button
        
        // Set up map click listener to select location
        mMap.setOnMapClickListener(latLng -> {
            selectedLocation = latLng;
            updateSelectedLocationMarker();
            getAddressFromLocation(latLng);
        });
        
        // Get initial location
        getLastKnownLocation();
    }
    
    private void updateSelectedLocationMarker() {
        if (mMap == null || selectedLocation == null) {
            return;
        }
        
        if (locationMarker != null) {
            locationMarker.remove();
        }
        
        MarkerOptions markerOptions = new MarkerOptions()
                .position(selectedLocation)
                .title("Selected Location")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                
        locationMarker = mMap.addMarker(markerOptions);
        
        // Set up drag listener to update address when marker is dragged
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}
            
            @Override
            public void onMarkerDrag(Marker marker) {}
            
            @Override
            public void onMarkerDragEnd(Marker marker) {
                selectedLocation = marker.getPosition();
                getAddressFromLocation(selectedLocation);
            }
        });
    }
} 