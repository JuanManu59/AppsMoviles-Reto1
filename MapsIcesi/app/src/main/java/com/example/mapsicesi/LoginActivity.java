package com.example.mapsicesi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mapsicesi.modelo.User;
import com.example.mapsicesi.util.Constants;
import com.example.mapsicesi.util.HTTPSWebUtilDomi;
import com.google.gson.Gson;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    private EditText userET;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userET = findViewById(R.id.userET);
        loginBtn = findViewById(R.id.loginBtn);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        loginBtn.setOnClickListener(
                (v) -> {
                    Intent i = new Intent(this, MapsActivity.class);
                    i.putExtra("username",userET.getText().toString());
                    startActivity(i);
                }
        );
    }
}