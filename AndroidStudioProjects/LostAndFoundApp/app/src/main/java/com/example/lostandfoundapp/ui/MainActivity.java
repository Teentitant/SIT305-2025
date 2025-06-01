package com.example.lostandfoundapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostandfoundapp.databinding.ActivityMainBinding;
import com.example.lostandfoundapp.adapter.ShowAllActivity;

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

        binding.buttonShowAllItems.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ShowAllActivity.class));
        });
    }
}