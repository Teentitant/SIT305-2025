package com.example.lostandfoundapp.adapter;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.lostandfoundapp.databinding.ActivityShowAllBinding;
import com.example.lostandfoundapp.ui.ItemDetailActivity;
import com.example.lostandfoundapp.viewmodel.AdvertViewModel;

public class ShowAllActivity extends AppCompatActivity {
    private ActivityShowAllBinding binding;
    private AdvertViewModel advertViewModel;
    private AdvertAdapter advertAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowAllBinding.inflate(getLayoutInflater());
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
        advertAdapter = new AdvertAdapter();
        binding.recyclerViewAdverts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewAdverts.setAdapter(advertAdapter);

        advertAdapter.setOnItemClickListener(advert -> {
            Intent intent = new Intent(ShowAllActivity.this, ItemDetailActivity.class);
            intent.putExtra(ItemDetailActivity.EXTRA_ADVERT_ID, advert.id);
            startActivity(intent);
        });
    }
}