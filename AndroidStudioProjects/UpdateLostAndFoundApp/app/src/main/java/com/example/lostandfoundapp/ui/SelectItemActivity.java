package com.example.lostandfoundapp.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.lostandfoundapp.adapter.AdvertAdapter; // Your existing adapter
import com.example.lostandfoundapp.databinding.ActivitySelectItemBinding; // ViewBinding
import com.example.lostandfoundapp.model.Advert;
import com.example.lostandfoundapp.viewmodel.AdvertViewModel;

public class SelectItemActivity extends AppCompatActivity {

    private ActivitySelectItemBinding binding;
    private AdvertViewModel advertViewModel;
    private AdvertAdapter advertAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        advertViewModel = new ViewModelProvider(this).get(AdvertViewModel.class);

        setupRecyclerView();

        advertViewModel.getAllAdverts().observe(this, adverts -> {
            if (adverts != null) {
                advertAdapter.submitList(adverts);
            }
        });
    }

    private void setupRecyclerView() {
        advertAdapter = new AdvertAdapter(); // Initialize your existing adapter
        binding.recyclerViewSelectAdvert.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewSelectAdvert.setAdapter(advertAdapter);

        advertAdapter.setOnItemClickListener(new AdvertAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Advert advert) {
                // When an item is clicked, launch MapActivity with this item's details
                Intent intent = new Intent(SelectItemActivity.this, MapActivity.class);
                // Pass latitude and longitude for the selected item
                // Or, you could pass advert.id and have MapActivity fetch it.
                // Passing LatLng directly might be simpler if MapActivity is just for display.
                if (advert.latitude != 0.0 || advert.longitude != 0.0) { // Ensure location exists
                    intent.putExtra(MapActivity.EXTRA_LATITUDE, advert.latitude);
                    intent.putExtra(MapActivity.EXTRA_LONGITUDE, advert.longitude);
                    intent.putExtra(MapActivity.EXTRA_ADVERT_TITLE, advert.postType + ": " + advert.name);
                    intent.putExtra(MapActivity.EXTRA_ADVERT_SNIPPET, advert.location);
                    startActivity(intent);
                } else {
                    // Handle case where selected item has no location data
                    android.widget.Toast.makeText(SelectItemActivity.this, "This item does not have location data.", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}