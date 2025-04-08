package com.example.cityclean;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class UserPage extends AppCompatActivity {

    //views be used
    private ImageView userImage;
    private TextView userName;
    private TextView litterReported;
    private TextView litterCleaned;
    private TextView points_totalGet;
    private TextView points_current;
    private Button manageReports;
    private Button back;
    // Firebase Database
    private DatabaseReference userDatabase;
    //user name
    private String userName_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //get views
        userImage = findViewById(R.id.User_userImage);
        userName = findViewById(R.id.User_userName);
        litterReported = findViewById(R.id.User_reportLitter_count);
        litterCleaned = findViewById(R.id.User_cleanLitterCount);
        points_totalGet  = findViewById(R.id.User_getPointsCount);
        points_current = findViewById(R.id.User_currentPointsCount);
        manageReports = findViewById(R.id.User_manageReports);
        back = findViewById(R.id.User_back);

        //get user name
        userName_str = MainActivity.UsernameSetter.getUsername(this);

        //set DB root to user (based on userName)
        userDatabase = FirebaseDatabase.getInstance().getReference("users").child(userName_str);

        //bound click listener to buttons
        manageReports.setOnClickListener(v -> {
            goManageReports();
        });
        back.setOnClickListener(v -> {
            finish();
        });
        //read DB and set page
        readDB();
    }
    private void goManageReports(){
        Intent intent = new Intent(this, ManageReports.class);
        startActivity(intent);
    }

    private void setPage(ArrayList<String> lines){
        //update the page
        if (lines.size()<5){
            Toast.makeText(this, "This line of data is damaged!", Toast.LENGTH_SHORT).show();
        }
        else{
            userName.setText(lines.get(0));
            litterReported.setText(lines.get(1));
            litterCleaned.setText(lines.get(2));
            points_totalGet.setText(lines.get(3));
            points_current.setText(lines.get(4));
        }
    }

    private void readDB(){
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<String> linesDB = new ArrayList<>();
                    String userName_str1, litterReported_str,litterCleaned_str,points_totalGet_str,points_current_str;
                    //check the user db, and get data
                    if (dataSnapshot.hasChild("username")){
                        userName_str1 = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                    }else {
                        userName_str1 = "N/A";
                    }
                    if (dataSnapshot.hasChild("litterReported")){
                        litterReported_str = Objects.requireNonNull(dataSnapshot.child("litterReported").getValue()).toString();
                    }else {
                        litterReported_str = "N/A";
                    }
                    if (dataSnapshot.hasChild("litterCleaned")){
                        litterCleaned_str = Objects.requireNonNull(dataSnapshot.child("litterCleaned").getValue()).toString();
                    }else {
                        litterCleaned_str = "N/A";
                    }
                    if (dataSnapshot.hasChild("points_totalGet")){
                        points_totalGet_str = Objects.requireNonNull(dataSnapshot.child("points_totalGet").getValue()).toString();
                    }else {
                        points_totalGet_str = "N/A";
                    }
                    if (dataSnapshot.hasChild("points_current")){
                        points_current_str = Objects.requireNonNull(dataSnapshot.child("points_current").getValue()).toString();
                    }else {
                        points_current_str = "N/A";
                    }

                    //add datas into list
                    linesDB.add(userName_str1);
                    linesDB.add(litterReported_str);
                    linesDB.add(litterCleaned_str);
                    linesDB.add(points_totalGet_str);
                    linesDB.add(points_current_str);
                    //use data to update the screen
                    setPage(linesDB);
                } else {
                    Log.d("Firebase", "User not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }
}