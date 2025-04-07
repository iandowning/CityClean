package com.example.cityclean;

import android.content.Intent;
import android.media.Image;
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

public class UserPage extends AppCompatActivity {

    //views be used
    private ImageView userImage;
    private TextView userName;
    private TextView litterReported;
    private TextView litterCleaned;
    private TextView points_totalGet;
    private TextView points_current;
    private Button manageReports;
    private String userName_str;
    // Firebase Database
    private DatabaseReference mDatabase;
    // possible need to get user inf form preview page to connect db to get other data

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
        //get user name
        userName_str = MainActivity.UsernameSetter.getUsername(this);

        //Unknow the DB structure now, so no develop the data show part now.

        //bound click listener to manage reports button
        manageReports.setOnClickListener(v -> {
            goManageReports();
        });
        //set page
        setPage();
    }
    private void goManageReports(){
        Intent intent = new Intent(this, ManageReports.class);
        startActivity(intent);
    }

    private void setPage(){
        userName.setText(userName_str);
    }
}