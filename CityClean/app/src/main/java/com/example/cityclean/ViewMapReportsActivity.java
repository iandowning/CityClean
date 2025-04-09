package com.example.cityclean;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;

public class ViewMapReportsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DatabaseReference reportsRef;
    private String username;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_map_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backBtn = findViewById(R.id.VMR_back);
        username = MainActivity.UsernameSetter.getUsername(this);
        reportsRef = FirebaseDatabase.getInstance().getReference("users").child("reports");
        backBtn.setOnClickListener(v -> finish());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /** Called when the Google Map is ready. We can now add markers for uncleaned reports. */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        reportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    Toast.makeText(ViewMapReportsActivity.this, "No reports found to display.",
                            Toast.LENGTH_SHORT).show();
                    LatLng defaultCenter = new LatLng(49.8801, -119.4436);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultCenter, 10));
                    return;
                }

                boolean addedMarker = false;
                LatLng firstMarkerPos = null;
                for (DataSnapshot reportSnap : dataSnapshot.getChildren()) {
                    Boolean isCleanFlag = reportSnap.child("isClean").getValue(Boolean.class);
                    if (isCleanFlag == null) continue;  // skip if no flag (data incomplete)
                    if (!isCleanFlag) {
                        LatLng position = null;
                        Double lat = reportSnap.child("lat").getValue(Double.class);
                        Double lng = reportSnap.child("lng").getValue(Double.class);
                        if (lat != null && lng != null) {
                            position = new LatLng(lat, lng);
                        } else {
                            String address = reportSnap.child("address").getValue(String.class);
                            if (address != null && !address.isEmpty()) {
                                try {
                                    Geocoder geocoder = new Geocoder(ViewMapReportsActivity.this);
                                    List<Address> results = geocoder.getFromLocationName(address, 1);
                                    if (results != null && !results.isEmpty()) {
                                        Address addrObj = results.get(0);
                                        position = new LatLng(addrObj.getLatitude(), addrObj.getLongitude());
                                    } else {
                                        Toast.makeText(ViewMapReportsActivity.this,
                                                "Unable to locate address: " + address,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (position != null) {
                            String title = "Litter Report";
                            String addr = reportSnap.child("address").getValue(String.class);
                            if (addr != null && !addr.isEmpty()) {
                                title = addr;
                            }
                            mMap.addMarker(new MarkerOptions().position(position).title(title));
                            if (!addedMarker) {
                                firstMarkerPos = position;
                            }
                            addedMarker = true;
                        }
                    }
                }

                if (addedMarker && firstMarkerPos != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstMarkerPos, 12));
                } else {
                    Toast.makeText(ViewMapReportsActivity.this,
                            "No uncleaned litter reports to display.",
                            Toast.LENGTH_LONG).show();
                    LatLng defaultCenter = new LatLng(49.8801, -119.4436);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultCenter, 10));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewMapReportsActivity.this,
                        "Failed to load reports: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
