package com.example.lostandfoundapp.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lostandfoundapp.databinding.ActivityItemDetailBinding;
import com.example.lostandfoundapp.model.Advert;
import com.example.lostandfoundapp.viewmodel.AdvertViewModel;

public class ItemDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ADVERT_ID = "com.example.lostandfoundapp.EXTRA_ADVERT_ID";
    private ActivityItemDetailBinding binding;
    private AdvertViewModel advertViewModel;
    private int currentAdvertId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        advertViewModel = new ViewModelProvider(this).get(AdvertViewModel.class);
        currentAdvertId = getIntent().getIntExtra(EXTRA_ADVERT_ID, -1);

        if (currentAdvertId != -1) {
            advertViewModel.getAdvertById(currentAdvertId).observe(this, advert -> {
                if (advert != null) {
                    binding.textViewDetailName.setText(advert.name);
                    binding.textViewDetailPostType.setText("Type: " + advert.postType);
                    binding.textViewDetailPhone.setText("Phone: " + advert.phone);
                    binding.textViewDetailDescription.setText("Description: " + advert.description);
                    binding.textViewDetailDate.setText("Date: " + advert.date);
                    binding.textViewDetailLocation.setText("Location: " + advert.location);
                }
            });
        } else {
            Toast.makeText(this, "Error: Advert ID not found.", Toast.LENGTH_LONG).show();
            finish();
        }

        binding.buttonRemove.setOnClickListener(v -> {
            if (currentAdvertId != -1) {
                advertViewModel.deleteAdvertById(currentAdvertId);
                Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}