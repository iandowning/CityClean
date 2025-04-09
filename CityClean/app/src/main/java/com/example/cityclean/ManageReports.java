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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageReports extends AppCompatActivity {
    private Button back;
    private RecyclerView reports;

    // Firebase Database
    private DatabaseReference userDatabase;
    //user name
    private String userName_str;
    //list to store reports
    private List<Report> itemList;
    //adapter for list
    MyAdapter adapter;

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
        //get views
        back = findViewById(R.id.MR_back);
        reports = findViewById(R.id.MR_reports);
        //get user name
        userName_str = MainActivity.UsernameSetter.getUsername(this);
        //set DB root to user (based on userName)
        userDatabase = FirebaseDatabase.getInstance().getReference("users");
        //set back button
        back.setOnClickListener(v -> {
            finish();
        });
        //set manager for reports
        reports.setLayoutManager(new LinearLayoutManager(this));
        //set list and adapter
        itemList = new ArrayList<>();
        adapter = new MyAdapter(this, itemList);
        //set adapter for reports, the list will auto
        reports.setAdapter(adapter);

//        //add data to Firebase for test function
//        addReports();

        //read data and set reports
        readData();
    }

    private void readData(){
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                //list to store reports
                ArrayList<Report> reportList = new ArrayList<>();
                DataSnapshot dbReports;
                //check weather "reports" exist
                if(!dataSnapshot.hasChild("reports")){
                    //if nor exist, return and notice
                    Toast.makeText(ManageReports.this, "No reports found in data base!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //exist, continue
                    dbReports = dataSnapshot.child("reports");
                    // for loop to read data
                    for (DataSnapshot snapshot : dbReports.getChildren()) {
                        Report report = snapshot.getValue(Report.class);
                        if (report != null) {
                            itemList.add(report);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //this function will add data into firebase for test the reading function
//    private void addReports(){
//        DatabaseReference reports = userDatabase.child("reports");
//        reports.push().setValue(new Report(1, "Address 1", "2025-04-06", "Sample Comment", "image1", true));
//        reports.push().setValue(new Report(2, "Address 2", "2025-04-07", "Sample Comment", "image2", false));
//    }
}