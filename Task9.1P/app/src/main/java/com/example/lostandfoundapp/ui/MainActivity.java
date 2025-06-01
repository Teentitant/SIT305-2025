package com.example.lostandfoundapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostandfoundapp.databinding.ActivityMainBinding;
import com.example.lostandfoundapp.ui.SelectItemActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonCreateAdvert.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CreateAdvertActivity.class));
        });

        binding.buttonShowOnMap.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SelectItemActivity.class));
        });
    }
}