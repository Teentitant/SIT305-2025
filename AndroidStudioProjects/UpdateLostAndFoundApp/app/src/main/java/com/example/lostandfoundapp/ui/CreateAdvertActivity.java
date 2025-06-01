package com.example.lostandfoundapp.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.example.lostandfoundapp.R;
import com.example.lostandfoundapp.databinding.ActivityCreateAdvertBinding;
import com.example.lostandfoundapp.model.Advert;
import com.example.lostandfoundapp.viewmodel.AdvertViewModel;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {
    private static final String TAG = "CreateAdvertActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 2;

    private ActivityCreateAdvertBinding binding;
    private AdvertViewModel advertViewModel;
    private Calendar calendar = Calendar.getInstance();
    private FusedLocationProviderClient fusedLocationClient;
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;
    private String selectedAddress = "";

    private final ActivityResultLauncher<Intent> startAutocomplete =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            if (intent != null) {
                                Place place = Autocomplete.getPlaceFromIntent(intent);
                                binding.editTextLocation.setText(place.getAddress());
                                selectedAddress = place.getAddress();
                                if (place.getLatLng() != null) {
                                    selectedLatitude = place.getLatLng().latitude;
                                    selectedLongitude = place.getLatLng().longitude;
                                }
                                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress() + ", " + place.getLatLng());
                            }
                        } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                            // TODO: Handle the error.
                            Log.e(TAG, "Autocomplete error: " + Autocomplete.getStatusFromIntent(result.getData()));
                        } else if (result.getResultCode() == RESULT_CANCELED) {
                            // The user canceled the operation.
                            Log.i(TAG, "Autocomplete canceled by user.");
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAdvertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        advertViewModel = new ViewModelProvider(this).get(AdvertViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAMv7diZo6V9K1OtLEfEckb7WPvLjOmXiQ");
        }


        binding.editTextDate.setOnClickListener(v -> showDatePicker());
        binding.buttonGetCurrentLocation.setOnClickListener(v -> getCurrentLocation());
        binding.editTextLocation.setOnClickListener(v -> startAutocompleteActivity());
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

    private void startAutocompleteActivity() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startAutocomplete.launch(intent);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        selectedLatitude = location.getLatitude();
                        selectedLongitude = location.getLongitude();
                        // Get address from LatLng (Geocoding)
                        Geocoder geocoder = new Geocoder(CreateAdvertActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(selectedLatitude, selectedLongitude, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                selectedAddress = addresses.get(0).getAddressLine(0);
                                binding.editTextLocation.setText(selectedAddress);
                            } else {
                                binding.editTextLocation.setText("Lat: " + selectedLatitude + ", Lng: " + selectedLongitude);
                                selectedAddress = "Lat: " + selectedLatitude + ", Lng: " + selectedLongitude;
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Geocoding error", e);
                            binding.editTextLocation.setText("Lat: " + selectedLatitude + ", Lng: " + selectedLongitude);
                            selectedAddress = "Lat: " + selectedLatitude + ", Lng: " + selectedLongitude;
                        }
                    } else {
                        Toast.makeText(CreateAdvertActivity.this, "Could not get current location. Please ensure location is enabled.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(CreateAdvertActivity.this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveAdvert() {
        String postType;
        int selectedId = binding.radioGroupPostType.getCheckedRadioButtonId();
        if (selectedId == binding.radioButtonLost.getId()) {
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
        String locationString = selectedAddress;

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || locationString.isEmpty() || selectedLatitude == 0.0 || selectedLongitude == 0.0){
            Toast.makeText(this, "Please fill all fields and set a valid location.", Toast.LENGTH_SHORT).show();
            return;
        }


        int selectedRadioId = binding.radioGroupPostType.getCheckedRadioButtonId();
        if (selectedRadioId == -1) {
            Toast.makeText(this, "Please select a post type", Toast.LENGTH_SHORT).show();
            return;
        }
        postType = ((RadioButton) findViewById(selectedRadioId)).getText().toString();


        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || locationString.isEmpty()) {
            Toast.makeText(this, "Please fill all fields including location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedLatitude == 0.0 && selectedLongitude == 0.0 && !locationString.contains("Lat:")) {
            Toast.makeText(this, "Please select a location using the map or get current location.", Toast.LENGTH_LONG).show();
            return;
        }


        Advert advert = new Advert(postType, name, phone, description, date, locationString, selectedLatitude, selectedLongitude);
        advertViewModel.insert(advert);
        Toast.makeText(this, "Advert saved successfully!", Toast.LENGTH_SHORT).show();
        finish();

    }
}