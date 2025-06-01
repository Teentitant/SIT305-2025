package com.example.lostandfoundapp.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider; // Import ViewModelProvider

import com.example.lostandfoundapp.R; // Import R
import com.example.lostandfoundapp.databinding.ActivityCreateAdvertBinding;
import com.example.lostandfoundapp.model.Advert;
import com.example.lostandfoundapp.viewmodel.AdvertViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {
    private ActivityCreateAdvertBinding binding;
    private AdvertViewModel advertViewModel;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAdvertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        advertViewModel = new ViewModelProvider(this).get(AdvertViewModel.class);


        binding.editTextDate.setOnClickListener(v -> showDatePicker());
        binding.buttonSave.setOnClickListener(v -> saveAdvert());
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateInView();
        };
        new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateInView() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.editTextDate.setText(sdf.format(calendar.getTime()));
    }

    private void saveAdvert() {
        String postType;
        int selectedId = binding.radioGroupPostType.getCheckedRadioButtonId();
        if (selectedId == binding.radioButtonLost.getId()) { // Use binding for IDs
            postType = "Lost";
        } else if (selectedId == binding.radioButtonFound.getId()) {
            postType = "Found";
        } else {
            Toast.makeText(this, "Please select a post type", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = binding.editTextName.getText().toString().trim();
        String phone = binding.editTextPhone.getText().toString().trim();
        String description = binding.editTextDescription.getText().toString().trim();
        String date = binding.editTextDate.getText().toString().trim();
        String location = binding.editTextLocation.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Advert advert = new Advert(postType, name, phone, description, date, location);
        advertViewModel.insert(advert);
        Toast.makeText(this, "Advert saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}