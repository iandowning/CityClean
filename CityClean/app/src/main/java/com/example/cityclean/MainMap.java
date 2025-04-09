package com.example.cityclean;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.cityclean.databinding.ActivityMainMapBinding;

public class MainMap extends FragmentActivity implements OnMapReadyCallback {
    private Marker selectedMarker;
    private GoogleMap mMap;
    private ActivityMainMapBinding binding;
    //Set target locations
    private LatLng Kelowna = new LatLng(49.8801, -119.4436);
    private LatLng UBCO = new LatLng(49.9394, -119.3948);
    private LatLng LakeCountry = new LatLng(50.0537, -119.4106);

    //used views
    private Button goUser;
    private Button goRecordClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        goRecordClean = findViewById(R.id.Map_goRecordClean);
        goRecordClean.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecordCleanLitterActivity.class);
            intent.putExtra("lat", 49.8801);
            intent.putExtra("lng", -119.4436);
            startActivity(intent);
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get views
        goUser = findViewById(R.id.Map_goUser);
        //bound functions on button

        goUser.setOnClickListener(v -> {
            setGoUser();
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //enable zoom control
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Marker for Kelowna, UBCO, LakeCountry
        Marker marker_Kelowna = mMap.addMarker(
                new MarkerOptions().position(Kelowna).title("Kelowna")
        );
        Marker marker_UBCO = mMap.addMarker(
                new MarkerOptions().position(UBCO).title("UBCO")
        );
        Marker marker_LakeCountry = mMap.addMarker(
                new MarkerOptions().position(LakeCountry).title("Lake Country")
        );
        //move to Kelowna and zoom 10
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Kelowna,10));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng));

                Intent intent = new Intent(MainMap.this, ReportLitterActivity.class);
                intent.putExtra("lat", latLng.latitude);
                intent.putExtra("lng", latLng.longitude);
                Toast.makeText(MainMap.this, "Pin dropped at Lat: " + latLng.latitude + " Lng: " + latLng.longitude, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    private void setGoUser(){
        Intent intent = new Intent(this, UserPage.class);
        startActivity(intent);
    }
}