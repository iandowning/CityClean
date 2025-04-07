package com.example.cityclean;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManageReports extends AppCompatActivity {
    private Button back;
    private RecyclerView reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //get button and set button
        back = findViewById(R.id.MR_back);
        back.setOnClickListener(v -> {
            finish();
        });
        //get reports view
        reports = findViewById(R.id.MR_reports);
        reports.setLayoutManager(new LinearLayoutManager(this));
        // these code is for test
        List<Report> itemList = new ArrayList<>();
        itemList.add(new Report(1, "Address 1", "2023-04-05", "Comment 1", "image1", true));
        itemList.add(new Report(2, "Address 2", "2023-04-06", "Comment 2", "image2", false));
        MyAdapter adapter = new MyAdapter(this, itemList);
        reports.setAdapter(adapter);
    }
}