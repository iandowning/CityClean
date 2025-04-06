package com.example.cityclean;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {
    private Button goMap;
    EditText username;
    EditText password;

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

        goMap = findViewById(R.id.GoMap);
        goMap.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainMap.class);
            startActivity(intent);
        });
        username = findViewById(R.id.editTextText);
        password = findViewById(R.id.editTextText1);

        FileInputStream reader;
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