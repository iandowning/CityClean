package com.example.cityclean;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;

public class RecordCleanLitterActivity extends AppCompatActivity {
    Button uploadPhotoBefore;
    Button uploadPhotoAfter;
    Button submitClean;
    String username = "user1";
    Uri beforePhotoUri;
    Uri afterPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record_clean_litter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickLitterPhoto =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI (before): " + uri);
                        ImageView imageViewBefore = findViewById(R.id.imageViewBefore);
                        imageViewBefore.setImageURI(uri);
                        beforePhotoUri = uri;
                    } else {
                        Log.d("PhotoPicker", "No media selected for before photo");
                    }
                });
        ActivityResultLauncher<PickVisualMediaRequest> pickTrashPhoto =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI (after): " + uri);
                        ImageView imageViewAfter = findViewById(R.id.imageViewAfter);
                        imageViewAfter.setImageURI(uri);
                        afterPhotoUri = uri;
                    } else {
                        Log.d("PhotoPicker", "No media selected for after photo");
                    }
                });
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double lng = intent.getDoubleExtra("lng", 0);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView locationText = findViewById(R.id.textViewLocation);
        locationText.setText(String.format("Location: %.5f, %.5f", lat, lng));
        username = MainActivity.UsernameSetter.getUsername(this);
        uploadPhotoBefore = findViewById(R.id.uploadPhotoBefore);
        uploadPhotoAfter = findViewById(R.id.uploadPhotoAfter);
        submitClean = findViewById(R.id.submitClean);
        uploadPhotoBefore.setOnClickListener(view -> {
            pickLitterPhoto.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        uploadPhotoAfter.setOnClickListener(view -> {
            pickTrashPhoto.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        submitClean.setOnClickListener(view -> {
            if (beforePhotoUri == null || afterPhotoUri == null) {
                Toast.makeText(this, "Please select both photos before submitting", Toast.LENGTH_SHORT).show();
                return;
            }
            EditText commentField = findViewById(R.id.editTextTextMultiLine);
            String comment = commentField.getText().toString().trim();

            String fileContents = username + "   " + comment + "   " + lat + "   " + lng + "   "
                    + beforePhotoUri.toString() + "   " + afterPhotoUri.toString();
            String filename = "reports.txt";
            try {
                FileOutputStream fos = openFileOutput(filename, MODE_APPEND);
                fos.write(fileContents.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Clean litter record submitted", Toast.LENGTH_SHORT).show();
            Intent backIntent = new Intent(this, MainActivity.class);
            startActivity(backIntent);
        });
    }
}
