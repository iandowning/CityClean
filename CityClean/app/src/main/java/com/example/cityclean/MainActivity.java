package com.example.cityclean;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button goMap;
    EditText username;
    EditText password;

    // Firebase Database
    private DatabaseReference mDatabase;

    //developer
    Button dev_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        goMap = findViewById(R.id.GoMap);
        username = findViewById(R.id.editTextText);
        password = findViewById(R.id.editTextText1);

        goMap.setOnClickListener(view -> {
            String userStr = username.getText().toString().trim();
            String passStr = password.getText().toString().trim();

            if (userStr.isEmpty() || passStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter username and password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            checkUserCredentials(userStr, passStr);

            dev_user = findViewById(R.id.Dev_user);
            dev_user.setOnClickListener(v -> {
                Intent intent = new Intent(this, UserPage.class);
                startActivity(intent);
            });
        });
    }

    private void checkUserCredentials(String username, String password) {
        mDatabase.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);

                    if (password.equals(storedPassword)) {
                        loginSuccessful(username);
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect password",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    createNewUser(username, password);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createNewUser(String username, String password) {
        DatabaseReference userRef = mDatabase.child(username);
        userRef.child("username").setValue(username);
        userRef.child("password").setValue(password);

        Toast.makeText(MainActivity.this, "New user created", Toast.LENGTH_SHORT).show();
        loginSuccessful(username);
    }

    private void loginSuccessful(String username) {
        UsernameSetter.setUsername(MainActivity.this, username);

        Intent intent = new Intent(MainActivity.this, MainMap.class);
        startActivity(intent);
    }

    // This allows us to store and retrieve the username, all activites can access the username with this command:
    // MainActivity.UsernameSetter.getUsername(this);
    // Or to set the username:
    // MainActivity.UsernameSetter.setUsername(this, "user1");
    public static class UsernameSetter {
        private static final String PREF_NAME = "UserNames";
        private static final String KEY_USERNAME = "username";

        public static void setUsername(Context context, String username) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_USERNAME, username);
            editor.apply();
        }

        public static String getUsername(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            return prefs.getString(KEY_USERNAME, "");
        }
    }
}