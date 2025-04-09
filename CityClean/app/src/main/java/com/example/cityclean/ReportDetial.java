package com.example.cityclean;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportDetial extends AppCompatActivity {
    //views
    private Button back;
    private Button save;
    private Button delete;
    private TextView date;
    private TextView address;
    private TextView comment;
    private ImageView image;
    // Firebase Database
    private DatabaseReference userDatabase;
    //user name
    private String userName_str;
    //Object
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_detial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //get views
        back = findViewById(R.id.RD_back);
        save = findViewById(R.id.RD_save);
        delete = findViewById(R.id.RD_delete);
        date = findViewById(R.id.RD_dateContent);
        address = findViewById(R.id.RD_addressContent);
        comment = findViewById(R.id.RD_commentContent);
        image = findViewById(R.id.RD_image);
        //get user name
        userName_str = MainActivity.UsernameSetter.getUsername(this);
        //set DB root to user (based on userName)
        userDatabase = FirebaseDatabase.getInstance().getReference("users").child("reports");
        //get report
        report  = (Report) getIntent().getSerializableExtra("Report");
        //bound buttons
        back.setOnClickListener(v -> {
            finish();
        });
        save.setOnClickListener(v -> {
            updateData();
        });
        delete.setOnClickListener(v -> {
            deleteData();
        });
        setPage(report);

    }
    private void updateData() {
        // get edit comment
        String newComment = comment.getText().toString();

        // set report
        report.setComment(newComment);

        // Update to Firebase
        userDatabase.orderByChild("id").equalTo(report.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().hasChildren()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    snapshot.getRef().setValue(report).addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Update successful!", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e ->
                            Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(this, "Object not found in database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteData() {
        if (report == null) return;
        //delete report from db
        userDatabase.orderByChild("id").equalTo(report.getId())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().hasChildren()) {
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            snapshot.getRef().removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Object deleted successfully.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Deletion failed.", Toast.LENGTH_SHORT).show()
                                    );
                        }
                    } else {
                        Toast.makeText(this, "Object not found.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void setPage(Report report){
        if (report != null) {
            // show things
            date.setText(report.getDate());
            address.setText(report.getLocation());
            comment.setText(report.getComment());
        }
    }
}