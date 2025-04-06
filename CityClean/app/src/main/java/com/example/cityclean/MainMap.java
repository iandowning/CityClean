package com.example.cityclean;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.cityclean.databinding.ActivityMainMapBinding;

public class MainMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMainMapBinding binding;
    //Set target locations
    private LatLng Kelowna = new LatLng(49.8801, -119.4436);
    private LatLng UBCO = new LatLng(49.9394, -119.3948);
    private LatLng LakeCountry = new LatLng(50.0537, -119.4106);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
    }
}