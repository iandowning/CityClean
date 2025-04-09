package com.example.cityclean;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;


import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Locale;

public class ReportLitterActivity extends AppCompatActivity {
    Button uploadPhoto;
    Button submitReport;
    // temp username
    String username = "user1";
    FileInputStream fileInputStream;
    private DatabaseReference mDatabase;
    int  currentReportId;
    Integer lastReportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_litter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageURI(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double lng = intent.getDoubleExtra("lng", 0);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        uploadPhoto = findViewById(R.id.uploadPhoto);
        uploadPhoto.setOnClickListener(view -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        username = MainActivity.UsernameSetter.getUsername(this);
        submitReport = findViewById(R.id.submitReport);
        submitReport.setOnClickListener(view -> {
            Toast.makeText(this, "Report submitted", Toast.LENGTH_SHORT).show();


            EditText editText = findViewById(R.id.editTextTextMultiLine);
            String comment = editText.getText().toString();

            // storing by seperated tabs (3 spaces). Not ideal but it will work for this purpose
            // temp username right now
            String fileContents = username + "   " + comment + "   " + lat + "   " + lng;
            String filename = "reports.txt";
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = openFileOutput(filename, MODE_APPEND);
                fileOutputStream.write(fileContents.getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Saving to firebase:
            mDatabase = FirebaseDatabase.getInstance().getReference("users");
            DatabaseReference reportsRef = mDatabase.child("reports");

            reportsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()){
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        lastReportId = null;
                        DataSnapshot receivedValue = task.getResult();

                        for(DataSnapshot node: receivedValue.getChildren()) {
                            Integer reportId = node.child("id").getValue(Integer.class);
                            if (reportId != null) {
                                lastReportId = reportId;
                            }
                        }
                        if (lastReportId == null) {
                            lastReportId = 0;
                        }
                    }
                    currentReportId = lastReportId + 1;
                    Report report = new Report(currentReportId, lat, lng, getCurrentDateTime(), comment, "Path_NA", false);
                    reportsRef.push().setValue(report);
                }
                });
            Intent intent1 = new Intent(this, MainMap.class);
            startActivity(intent1);
        });
    }

    public String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}