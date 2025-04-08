package com.example.cityclean;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        userDatabase = FirebaseDatabase.getInstance().getReference("users").child(userName_str);
        //get report
        report  = (Report) getIntent().getSerializableExtra("Report");

        setPage(report);
    }

    public void setPage(Report report){
        if (report != null) {
            // show things
            date.setText(report.getDate());
            address.setText(report.getAddress());
            comment.setText(report.getComment());
        }
    }
}